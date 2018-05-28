package com.cnc.encryption.aes;


public class KeyEncryUtil {


public static final int Nb=4;   //列数
	
	public static int Nk;  //密钥行数
	public static int Nr;  //加密进行 Nr 轮
	public static  int[]  w; //扩展密钥
	
	static{
			switch (EncryKey.key.length) {
			default:
			case 16: Nk = 4; Nr = 10; break;
			case 24: Nk = 6; Nr = 12; break;
			case 32: Nk = 8; Nr = 14; break;
			}	
			
			w=new int[Nb*(Nr + 1) * 4]; //为扩展密钥分配数组
			key_expansion(EncryKey.key, w);//生成扩展密钥
	}
	
	/*
	 * Generates the round constant Rcon[i]
	 */
	public static final int  R[] = {0x02, 0x00, 0x00, 0x00};
	
	public static int[] Rcon(int i) {
		int [] RR=new int [4];
		
		if (i == 1) {
			RR[0] = 0x01; // x^(1-1) = x^0 = 1
		} else if (i > 1) {
			RR[0] = 0x02;
			i--;
			while (i-1 > 0) {
				RR[0] = gmult(RR[0], 0x02);
				i--;
			}
		}
		
		return RR;
	}
	
	

	/*
	 * Addition in GF(2^8)
	 * 位异或运算
	 */
	public static int gadd(int a,int b){
		return a^b;
	}
	
	public static int gsub(int a,int b)
	{
		return a^b;
	}
	
	/*
	 * Multiplication in GF(2^8)
	 * http://en.wikipedia.org/wiki/Finite_field_arithmetic
	 * Irreducible polynomial m(x) = x8 + x4 + x3 + x + 1
	 */
	public static int gmult(  int a,  int b){
		int p=0,i=0,hbs=0;
		for(i=0;i<8;i++){
			if((b&1)==1){
				p^=a;
			}
			hbs=a & 0x80;
			a<<=1;
			if(hbs !=0)   a^=0x1b;  //0000 0001 0001 1011
			b>>=1;
		}
		return p&0xff;
	}
	
	/*
	 * Addition of 4 byte words
	 * m(x) = x4+1
	 */
	public static void coef_add(int a[], int b[], int d[]) {

		d[0] = a[0]^b[0];
		d[1] = a[1]^b[1];
		d[2] = a[2]^b[2];
		d[3] = a[3]^b[3];
	}
	
	/*
	 * Multiplication of 4 byte words
	 * m(x) = x4+1
	 * 不用返回值，直接将结果写入数组d
	 * 只能修改类实例,基本数据类型不能改
	 */
	public static void coef_mult(int []a,int []b,int []d) {

		d[0] = gmult(a[0],b[0])^gmult(a[3],b[1])^gmult(a[2],b[2])^gmult(a[1],b[3]);
		d[1] = gmult(a[1],b[0])^gmult(a[0],b[1])^gmult(a[3],b[2])^gmult(a[2],b[3]);
		d[2] = gmult(a[2],b[0])^gmult(a[1],b[1])^gmult(a[0],b[2])^gmult(a[3],b[3]);
		d[3] = gmult(a[3],b[0])^gmult(a[2],b[1])^gmult(a[1],b[2])^gmult(a[0],b[3]);
	}
	
	
	/*
	 * Transformation in the Cipher and Inverse Cipher in which a Round 
	 * Key is added to the State using an XOR operation. The length of a 
	 * Round Key equals the size of the State (i.e., for Nb = 4, the Round 
	 * Key length equals 128 bits/16 bytes).
	 */
	public static void add_round_key(int[] state, int []w, int r) {
		
		int c;
		for (c = 0; c < Nb; c++) {
			state[Nb*0+c] = state[Nb*0+c]^w[4*Nb*r+4*c+0];   //debug, so it works for Nb !=4 
			state[Nb*1+c] = state[Nb*1+c]^w[4*Nb*r+4*c+1];
			state[Nb*2+c] = state[Nb*2+c]^w[4*Nb*r+4*c+2];
			state[Nb*3+c] = state[Nb*3+c]^w[4*Nb*r+4*c+3];	
		}
	}
	
	/*
	 * Transformation in the Cipher that takes all of the columns of the 
	 * State and mixes their data (independently of one another) to 
	 * produce new columns.
	 */
	public static void mix_columns(int[] state) {

		int a[] = {0x02, 0x01, 0x01, 0x03}; // a(x) = {02} + {01}x + {01}x2 + {03}x3
		int i, j ;
		int  col[]=new int [4];
		int  res[]=new int [4];
		
		for (j = 0; j < Nb; j++) {
			for (i = 0; i < 4; i++) {
				col[i] = state[Nb*i+j];
			}

			coef_mult(a, col, res);

			for (i = 0; i < 4; i++) {
				state[Nb*i+j] = res[i];
			}
		}
	}
	
	/*
	 * Transformation in the Inverse Cipher that is the inverse of 
	 * MixColumns().
	 */
	public static void inv_mix_columns(int[] state) {

		int a[] = {0x0e, 0x09, 0x0d, 0x0b}; // a(x) = {0e} + {09}x + {0d}x2 + {0b}x3
		int i, j;

		int  col[]=new int [4];
		int  res[]=new int [4];
		
		for (j = 0; j < Nb; j++) {
			for (i = 0; i < 4; i++) {
				col[i] = state[Nb*i+j];
			}

			coef_mult(a, col, res);

			for (i = 0; i < 4; i++) {
				state[Nb*i+j] = res[i];
			}
		}
	}
	
	/*
	 * Transformation in the Cipher that processes the State by cyclically 
	 * shifting the last three rows of the State by different offsets. 
	 */
	public static void shift_rows(int[] state) {

		int i, k, s, tmp;

		for (i = 1; i < 4; i++) {
			// shift(1,4)=1; shift(2,4)=2; shift(3,4)=3
			// shift(r, 4) = r;
			s = 0;
			while (s < i) {
				tmp = state[Nb*i+0];
				
				for (k = 1; k < Nb; k++) {
					state[Nb*i+k-1] = state[Nb*i+k];
				}

				state[Nb*i+Nb-1] = tmp;
				s++;
			}
		}
	}
	
	/*
	 * Transformation in the Inverse Cipher that is the inverse of 
	 * ShiftRows().
	 */
	public static void inv_shift_rows(int[] state) {

		int i, k, s, tmp;

		for (i = 1; i < 4; i++) {
			s = 0;
			while (s < i) {
				tmp = state[Nb*i+Nb-1];
				
				for (k = Nb-1; k > 0; k--) {
					state[Nb*i+k] = state[Nb*i+k-1];
				}

				state[Nb*i+0] = tmp;
				s++;
			}
		}
	}
	
	/*
	 * Transformation in the Cipher that processes the State using a non­
	 * linear byte substitution table (S-box) that operates on each of the 
	 * State bytes independently. 
	 */
	public static void sub_bytes(int []state) {

		int i, j;
		int row, col;

		for (i = 0; i < 4; i++) {
			for (j = 0; j < Nb; j++) {
				row = (state[Nb*i+j] & 0xf0) >> 4;
				col = state[Nb*i+j] & 0x0f;
				state[Nb*i+j] = S_box.s_box[16*row+col];
			}
		}
	}
	
	/*
	 * Transformation in the Inverse Cipher that is the inverse of 
	 * SubBytes().
	 */
	public static void inv_sub_bytes(int[] state) {

		int i, j;
		int row, col;

		for (i = 0; i < 4; i++) {
			for (j = 0; j < Nb; j++) {
				row = (state[Nb*i+j] & 0xf0) >> 4;
				col = state[Nb*i+j] & 0x0f;
				state[Nb*i+j] = S_box.inv_s_box[16*row+col];
			}
		}
	}
	
	/*
	 * Function used in the Key Expansion routine that takes a four-byte 
	 * input word and applies an S-box to each of the four bytes to 
	 * produce an output word.
	 */
	public static void sub_word(int[] w) {

		int i;

		for (i = 0; i < 4; i++) {
			w[i] = S_box.s_box[16*((w[i] & 0xf0) >> 4) + (w[i] & 0x0f)];
		}
	}
	
	/*
	 * Function used in the Key Expansion routine that takes a four-byte 
	 * word and performs a cyclic permutation. 
	 */
	public static void rot_word(int[] w) {

		int tmp;
		int i;

		tmp = w[0];

		for (i = 0; i < 3; i++) {
			w[i] = w[i+1];
		}
		w[3] = tmp;
	}
	
	
	/*
	 * Key Expansion，获取扩展密钥
	 * Nk密钥行数； Nb列数 ；Nr循环次数
	 */
	public static void key_expansion(int[] key, int[] w) {

		int tmp[]=new int[4];
		int i, j;
		int len = Nb*(Nr+1);

		for (i = 0; i < Nk; i++) {
			w[4*i+0] = key[4*i+0];
			w[4*i+1] = key[4*i+1];
			w[4*i+2] = key[4*i+2];
			w[4*i+3] = key[4*i+3];
		}

		for (i = Nk; i < len; i++) {
			tmp[0] = w[4*(i-1)+0];
			tmp[1] = w[4*(i-1)+1];
			tmp[2] = w[4*(i-1)+2];
			tmp[3] = w[4*(i-1)+3];

			if (i%Nk == 0) {

				rot_word(tmp);
				sub_word(tmp);
				coef_add(tmp, Rcon(i/Nk), tmp);

			} else if (Nk > 6 && i%Nk == 4) {
				sub_word(tmp);
			}

			w[4*i+0] = w[4*(i-Nk)+0]^tmp[0];
			w[4*i+1] = w[4*(i-Nk)+1]^tmp[1];
			w[4*i+2] = w[4*(i-Nk)+2]^tmp[2];
			w[4*i+3] = w[4*(i-Nk)+3]^tmp[3];
		}
	}
	
	/*
	 * 加密函数，输入长度为16的数组
	 * @param in:需要加密的数据块，长度为16字节
	 * @return 加密后的数据字节数组，长度依然为16字节
	 */
	public static int[] cipher(int[] in) {
		
		if(in.length !=16 ){
			System.out.println("加密的数据块长度不为16！！！");
		}
		
		int[] out=new int[4*4];
		
		int[] state=new int[4*Nb];   //一次给128bit块加密
		int  r, i, j;

		for (i = 0; i < 4; i++) {
			for (j = 0; j < Nb; j++) {
				state[Nb*i+j] = in[i+4*j]; //矩阵转置
			}
		}

		add_round_key(state, w, 0);

		for (r = 1; r < Nr; r++) {
			sub_bytes(state);
			shift_rows(state);
			mix_columns(state);
			add_round_key(state, w, r);
		}

		sub_bytes(state);
		shift_rows(state);
		add_round_key(state, w, Nr);

		for (i = 0; i < 4; i++) {
			for (j = 0; j < Nb; j++) {
				out[i+4*j] = state[Nb*i+j];
			}
		}
		return out;
	}
	

	/*
	 * 解密函数，输入长度为16的数组
	 * @param in:需要加密的数据块，长度为16字节
	 * @return 加密后的数据字节数组，长度依然为16字节
	 */
	public  static int[]  inv_cipher(int[] in) {

		if(in.length !=16 ){
			System.out.println("解密的数据块长度不为16！！！");
		}
		
		int [] out=new int[4*4];
		int[] state=new int[4*Nb];
		int r, i, j;

		for (i = 0; i < 4; i++) {
			for (j = 0; j < Nb; j++) {
				state[Nb*i+j] = in[i+4*j];
			}
		}

		add_round_key(state, w, Nr);

		for (r = Nr-1; r >= 1; r--) {
			inv_shift_rows(state);
			inv_sub_bytes(state);
			add_round_key(state, w, r);
			inv_mix_columns(state);
		}

		inv_shift_rows(state);
		inv_sub_bytes(state);
		add_round_key(state, w, 0);

		for (i = 0; i < 4; i++) {
			for (j = 0; j < Nb; j++) {
				out[i+4*j] = state[Nb*i+j];
			}
		}	
		return out;
	}
	
		
}

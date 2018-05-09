package com.cnc.encryption.aes;

import java.util.ArrayList;
import java.util.List;

/*
 * �ֽڴ�������
 */
public class ByteDealUtil {
	
	/*
	 * ��byteת��Ϊ�޷���������Int���ͷ���
	 * @param  byte��
	 * @return  byte��ʾ���޷���ֵ
	 */
	 public static int getUnsignedByte (byte data){      //��data�ֽ�������ת��Ϊ int�������ݣ�0~255 (0xFF ��BYTE)��
         return data&0x0FF;
     }
	 
	 /*
	  * ��ȡ����Ϊn��������
	  * @param src:ԭ����
	  * @param offset:��ʼ��ַ
	  * @param n:�����鳤��
	  */
	static int[] getbyteblock(int [] src, int offset , int n){
		int  [] tmpb=new int [n];		
		for(int i=0;i<n;i++){
			tmpb[i]=src[i+offset];
		}
		return tmpb;
	} 
	
	/*
	 * ��������out �� dec���� ,����Ϊout�ĳ���
	 * @param 
	 */
	static void  setbyteblock(int [] dec,int [] out, int offset ){

		int n=out.length; //Ҫ���������ĳ���
		if(dec.length < (offset+n)){
			System.out.println("Ŀ������ĳ��Ȳ�������������ʧ�ܣ�");			
		}else{		//��������
			for(int i=0;i<n;i++){
				dec[i+offset]=out[i];
			}
		}	
	}
	
	/*
	  * ��ȡ����Ϊn�������� ,byte����
	  * @param src:ԭ����
	  * @param offset:��ʼ��ַ
	  * @param n:�����鳤��
	 */
	public static byte[] getbytearray(byte[] src, int offset,int n){
		byte  [] tmpb=new byte [n];		
		for(int i=0;i<n;i++){
			tmpb[i]=src[i+offset];
		}
		return tmpb;
	}

	/*
	 * ��������out �� dec���� ,����Ϊout�ĳ���
	 * @param 
	 */
	static void  setbytearray(byte [] dec,byte [] out, int offset ){

		int n=out.length; //Ҫ���������ĳ���
		if(dec.length < (offset+n)){
			System.out.println("Ŀ������ĳ��Ȳ�������������ʧ�ܣ�");			
		}else{		//��������
			for(int i=0;i<n;i++){
				dec[i+offset]=out[i];
			}
		}	
	}
	

	/*
	 * byte[]  to  int[]  ,���Ȳ�Ϊ16�������Ĳ���
	 * @param 
	 */
	public static int[]  ByteArraytoIntArray(byte[] srcbyte){
		int len=srcbyte.length;		
		int count=len/16; //�����
		if(len%16 != 0){
			count++ ;  //��16��������count��1
		}		
		int[] decintarray= new int [count*16];		
		for(int i=0;i<len;i++){
			decintarray[i]= (int)(srcbyte[i] & 0xff);
		}
		return decintarray;
	}
	
	
	/*
	 * int[]  to  byte[]  ,���Ȳ�Ϊ16�������Ĳ���
	 * @param
	 */
	public static byte[]  IntArraytoByteArray(int[] srcint){
		int len=srcint.length;		
		int count=len/16; //�����
		if(len%16 != 0){
			count++ ;  //��16��������count��1
		}		
		byte[] decbytearray= new byte [count*16];		
		for(int i=0;i<len;i++){
			decbytearray[i]=(byte)(srcint[i]& 0xff );
		}
		return decbytearray;
	}
	
	/*
	 * ��ӡint����
	 * @param "s":��ʾ��Ϣ
	 * @param tmp:��Ҫ��ʾ������
	 */
	public  static void  printIntArray(String s,int [] tmp){
		
		int len=tmp.length;
		System.out.print(s);
		for(int i=0;i<len;i++){
			
			if(i%32 == 0 ){
				System.out.println("");
			}			
			System.out.print(tmp[i]+" ");		
		}		
		System.out.println("");
	}

	/*
	 * ��int����ת��Ϊint�ַ���,��','�ָ�
	 * 
	 */
	public static String IntArraytoIntArrayString(int[] a){
		
		String restring=null;
//		List<String> names=new ArrayList<String>(); 
		int len=a.length; //���鳤��
		
		restring=a[0]+",";
		for(int i=1;i<len-1;i++){
			restring+=a[i]+",";
		}
		restring+=a[len-1];

		return restring;	
	}
	
	
	
	/*
	 * String����ת��Ϊ  int���麯��
	 */
	static int[]  StringArraytoInnArray(String[] stmp){
		int len=stmp.length; //���鳤��
		int [] sint=new int[len]; //�����ڴ�ռ�
		
		for(int i=0;i<len;i++){
			sint[i]= Integer.valueOf(stmp[i]);
		}		
		return sint;	
	}

}

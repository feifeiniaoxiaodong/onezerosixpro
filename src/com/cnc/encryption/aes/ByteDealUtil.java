package com.cnc.encryption.aes;

import java.util.ArrayList;
import java.util.List;

/*
 * 字节处理工具类
 */
public class ByteDealUtil {
	
	/*
	 * 将byte转化为无符号数，以Int类型返回
	 * @param  byte数
	 * @return  byte表示的无符号值
	 */
	 public static int getUnsignedByte (byte data){      //将data字节型数据转换为 int类型数据，0~255 (0xFF 即BYTE)。
         return data&0x0FF;
     }
	 
	 /*
	  * 获取长度为n的子数组
	  * @param src:原数组
	  * @param offset:起始地址
	  * @param n:子数组长度
	  */
	static int[] getbyteblock(int [] src, int offset , int n){
		int  [] tmpb=new int [n];		
		for(int i=0;i<n;i++){
			tmpb[i]=src[i+offset];
		}
		return tmpb;
	} 
	
	/*
	 * 保存数组out 到 dec数组 ,长度为out的长度
	 * @param 
	 */
	static void  setbyteblock(int [] dec,int [] out, int offset ){

		int n=out.length; //要保存的数组的长度
		if(dec.length < (offset+n)){
			System.out.println("目标数组的长度不够，保存数组失败！");			
		}else{		//保存数组
			for(int i=0;i<n;i++){
				dec[i+offset]=out[i];
			}
		}	
	}
	
	/*
	  * 获取长度为n的子数组 ,byte类型
	  * @param src:原数组
	  * @param offset:起始地址
	  * @param n:子数组长度
	 */
	public static byte[] getbytearray(byte[] src, int offset,int n){
		byte  [] tmpb=new byte [n];		
		for(int i=0;i<n;i++){
			tmpb[i]=src[i+offset];
		}
		return tmpb;
	}

	/*
	 * 保存数组out 到 dec数组 ,长度为out的长度
	 * @param 
	 */
	static void  setbytearray(byte [] dec,byte [] out, int offset ){

		int n=out.length; //要保存的数组的长度
		if(dec.length < (offset+n)){
			System.out.println("目标数组的长度不够，保存数组失败！");			
		}else{		//保存数组
			for(int i=0;i<n;i++){
				dec[i+offset]=out[i];
			}
		}	
	}
	

	/*
	 * byte[]  to  int[]  ,长度不为16整数倍的补齐
	 * @param 
	 */
	public static int[]  ByteArraytoIntArray(byte[] srcbyte){
		int len=srcbyte.length;		
		int count=len/16; //块个数
		if(len%16 != 0){
			count++ ;  //非16整数倍，count加1
		}		
		int[] decintarray= new int [count*16];		
		for(int i=0;i<len;i++){
			decintarray[i]= (int)(srcbyte[i] & 0xff);
		}
		return decintarray;
	}
	
	
	/*
	 * int[]  to  byte[]  ,长度不为16整数倍的补齐
	 * @param
	 */
	public static byte[]  IntArraytoByteArray(int[] srcint){
		int len=srcint.length;		
		int count=len/16; //块个数
		if(len%16 != 0){
			count++ ;  //非16整数倍，count加1
		}		
		byte[] decbytearray= new byte [count*16];		
		for(int i=0;i<len;i++){
			decbytearray[i]=(byte)(srcint[i]& 0xff );
		}
		return decbytearray;
	}
	
	/*
	 * 打印int数组
	 * @param "s":提示信息
	 * @param tmp:需要显示的数组
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
	 * 将int数组转化为int字符串,用','分隔
	 * 
	 */
	public static String IntArraytoIntArrayString(int[] a){
		
		String restring=null;
//		List<String> names=new ArrayList<String>(); 
		int len=a.length; //数组长度
		
		restring=a[0]+",";
		for(int i=1;i<len-1;i++){
			restring+=a[i]+",";
		}
		restring+=a[len-1];

		return restring;	
	}
	
	
	
	/*
	 * String数组转化为  int数组函数
	 */
	static int[]  StringArraytoInnArray(String[] stmp){
		int len=stmp.length; //数组长度
		int [] sint=new int[len]; //分配内存空间
		
		for(int i=0;i<len;i++){
			sint[i]= Integer.valueOf(stmp[i]);
		}		
		return sint;	
	}

}

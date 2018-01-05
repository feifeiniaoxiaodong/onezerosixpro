package com.cnc.gsk.datautils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * 广数数据处理工具类
 * Created by wei on 2017/4/17.
 */

public class BytedealUtil {

    //得到短整形数据的字节数组2bytes
    public static byte[] getBytes(short data){
        byte[] bytes=new byte[2];
        bytes[0]= (byte) (data & 0xff);
        bytes[1]=(byte) ((data>>8)&0xff);
        return bytes;
    }
    //得到整形数据的字节数组4bytes
    public static byte[] getBytes(int data){
        byte[] bytes=new byte[4];
        bytes[0]=(byte)(data&0xff);
        bytes[1]=(byte)((data>>8)&0xff);
        bytes[2]=(byte)((data>>16)&0xff);
        bytes[3]=(byte)((data>>24)&0xff);
        return bytes;
    }

    public static byte[] getBytes(char data){
        byte[] bytes=new byte[2];
        bytes[0]= (byte) (data & 0xff);
        bytes[1]=(byte) ((data>>8)&0xff);
        return bytes;
    }
    public static byte[] getBytes(long data){
        byte[] bytes=new byte[8];
        for(int i=0,j=0;i<8;i++) {
            j = i * 8;
            bytes[i] = (byte) ((data >> j) & 0xff);
        }
        return  bytes;
    }
    //得到浮点数的字节
    public static byte[] getBytes(float data){
        int  tmp=Float.floatToIntBits(data);
        return  getBytes(tmp);
    }

    public static byte[] getBytes(double data){
        long tmp=Double.doubleToLongBits(data);
        return getBytes(tmp);
    }
    public static byte[] getBytes(String str,String charsetName){
        Charset charset=Charset.forName(charsetName);
        return str.getBytes(charset);
    }

    public static byte[] getBytes(String str){
        return getBytes(str,"GBK");
    }
    /**
     * 将字符数组根据字符集转化为字节数组
     * @param encode 字符集的名称
     * @param chars 字符数组
     * @return	字节数组
     */
    public static byte[] getBytes(char[] chars,String encode){
        Charset cs=Charset.forName(encode);
        CharBuffer cb=CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb=cs.encode(cb);
        return  bb.array();
    }

    public static short getShort(byte[] bytes){
        int len=0;
        short tmp=0;
        if((len=bytes.length)>2){   len=2;  }

        for(int i=0,j=0;i<len;i++){
            //tmp=(short) (tmp |(bytes[i] <<(8*i)));
            j=8*i;
            tmp|=(  (bytes[i] <<j) & (0xff<<j)  );
        }
        return  tmp;
    }
    public static char getChar(byte[] bytes){
        int len=0;
        char tmp=0;
        if((len=bytes.length)>2){   len=2;  }

        for(int i=0,j=0;i<len;i++){
            //tmp=(short) (tmp |(bytes[i] <<(8*i)));
            j=8*i;
            tmp|=(  ( (char)bytes[i] <<j) & (0xff<<j)  );
        }
        return  tmp;
    }
    public static char getAsciiChar(byte b){
        return (char) (b&0xff);
    }
    //根据2个字节得到整形数据
    public static int getUnsignedShort(byte[] bytes){
        if(bytes.length != 2)
            throw new IllegalArgumentException("输入的字节数组长度不为2！");
        return (int) (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8));
    }
    //根据4个字节得到整形数据
    public static int getInt(byte [] bytes){
        int   tmpint=0;
        int   len=bytes.length;
        if(len>4)  throw new IllegalArgumentException("输入的字节数组长度大于4！");

        for (int i = 0,j=0; i < len; i++) {
            j=8*i;
            tmpint |=( ((int) bytes[i] <<j)&(0xff<<j));
        }
        return tmpint;
    }

    //根据8个字节得到长整形数据
    public  static long  getLong(byte [] bytes){
        long   tmplong=0;
        int len=bytes.length;
        if(len>8)  throw new IllegalArgumentException("输入的字节数组长度大于8！");

        for (int i = 0,j=0; i < len; i++) {
            j=8*i;
            tmplong |= ( ((long)bytes[i] << j)&(0xffL<<j));
        }

//        return (0xffL & (long)bytes[0]) | (0xff00L & ((long)bytes[1] << 8)) | (0xff0000L & ((long)bytes[2] << 16)) | (0xff000000L & ((long)bytes[3] << 24))
//                | (0xff00000000L & ((long)bytes[4] << 32)) | (0xff0000000000L & ((long)bytes[5] << 40)) | (0xff000000000000L & ((long)bytes[6] << 48)) | (0xff00000000000000L & ((long)bytes[7] << 56));

        return tmplong;
    }
    //获取无符号整形
    public static long getUnsignedInt(byte[] bytes){
        int len=bytes.length;
        if(len!=4) throw new IllegalArgumentException("输入的字节数组长度不为4！");
        if(len>4)  len=4;
        long  tmp=0;
        for(int i=0,j=0;i<len;i++){
            j=8*i;
            tmp|= (((long) bytes[i]<<j)&(0xff<<j));
        }
        return tmp;
    }
    //根据字节得到浮点型数据
    public static float getFloat(byte[] bytes){
        return Float.intBitsToFloat(getInt(bytes));
    }
    //根据字节得到双精度浮点型数据
    public static double getDouble(byte[] bytes){
        long  tmp=getLong(bytes);
        return Double.longBitsToDouble(tmp);
    }

    //根据字符集得到字节数组的字符串
    public  static String getString(byte[] bytes,String charsetName){
        String tmp=new String(bytes,Charset.forName(charsetName));
        int pos =tmp.indexOf('\0');
        tmp=tmp.substring(0,pos);
        return tmp;
    }
    //根据默认字符集GBK得到字节数组的字符串
    public static String getString(byte[] bytes){
        return getString(bytes,"GBK");
    }

    /*
    从指定的字节数组中取出一个子数组并返回
     */
    public static  byte[] copyOfRange(byte[] src,int from,int len){
        if(src.length<(from+len)){
            throw new IllegalArgumentException("要复制的长度超过了原数组的长度！");
        }
        byte[] copy=new byte[len];
        System.arraycopy(src,from,copy,0,len);
        return copy;
    }
    /*
    从字节数组的某一个位置取出一个字节
     */
    public static byte getOneByte(byte[] src,int pos){
        if((pos>=src.length) || (pos<0)){
            throw new IllegalArgumentException("输入的pos参数为负数或者超过了字节数组的长度!");
        }
        return src[pos];
    }
    /*
    在指定的数组后添加一个字节并返回
     */
    public static byte[] append(byte[] src ,byte to){
        byte[] tmp=new byte[src.length+1];
        System.arraycopy(src,0,tmp,0,src.length);
        tmp[src.length]=to;
        return tmp;
    }
    /*
    合并两个字节数组
     */
    public static  byte[] merge(byte[] first,byte[] second){
        byte[] allbyte=new byte[first.length+second.length];
        System.arraycopy(first,0,allbyte,0,first.length);
        System.arraycopy(second,0,allbyte,first.length,second.length);
        return  allbyte;
    }
    /*
    将字符串转化为字符数组
     */
    public static char[] string2chars(String str,int charlenght){
        char[] tmp=new char[charlenght];
        str.getChars(0,str.length(),tmp,0);
        return  tmp;
    }

    public static String chars2string(char [] chars){
        byte[] tmpbyte=getBytes(chars,"US-ASCII");
        String  tmp=getString(tmpbyte,"US-ASCII");
        return tmp;
    }
    /*
    将输入的字节数组转化为double数组
     */
    public static double[] getDoubleArray(byte[] bytes){
        int len=bytes.length;
        if(len%8!=0){ return  null; }

        double [] tmp=new double[len/8];
        for(int i=0;i< len/8;i++){
            tmp[i]=getDouble(copyOfRange(bytes,i*8,8));
        }
        return tmp;
    }
    /*
  将输入的字节数组转化为float数组
   */
    public static float[] getFloatArray(byte[] bytes){
        int len=bytes.length;
        if(len%4!=0){ return  null; }

        float [] tmp=new float[len/4];
        for(int i=0;i< len/4;i++){
            tmp[i]=getFloat(copyOfRange(bytes,i*4,4));
        }
        return tmp;
    }
}

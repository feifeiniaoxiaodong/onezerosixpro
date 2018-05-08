package com.cnc.utils;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;


public class ByteUtil
{
	//得到短整型数据的字节数组2bytes
    public static byte[] getBytes(short data)
    {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    //得到Java字符的字节数据，2bytes
    public static byte[] getBytes(char data)
    {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    /*public static byte[] getBytes(String encode, char c) {
		Charset cs = Charset.forName(encode);
		CharBuffer cb = CharBuffer.allocate(1);
		cb.put(c);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();
	}
*/
    //得到整形数据的字节，4bytes
    public static byte[] getBytes(int data)
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    //得到长整形数据的字节，8bytes
    public static byte[] getBytes(long data)
    {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    //得到浮点数的字节
    public static byte[] getBytes(float data)
    {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    //得到双精度浮点数的字节
    public static byte[] getBytes(double data)
    {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    //根据字符集得到字符串的字节
    public static byte[] getBytes(String data, String charsetName)
    {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    //根据默认的GBK编码得到数据的字节
    public static byte[] getBytes(String data)
    {
        return getBytes(data, "GBK");
    }

    /**
	 * 将字符数组根据字符集转化为字节数组
	 * @param encode 字符集的名称
	 * @param chars 字符数组
	 * @return	字节数组
	 */
	public static byte[] getBytes(char[] chars, String encode) {
		Charset cs = Charset.forName(encode);
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();
	}
    
	//根据两个字节得到短整型数据
    public static short getShort(byte[] bytes)
    {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    //根据两个字节得到字符数据
    public static char getChar(byte[] bytes)
    {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }
    
  //根据一个字节得到ASCII码字符数据
    public static char getAsciiChar(byte b)
    {
        return (char) (b & 0xff);
    }
    
    public static int getUnsignedShort(byte[] bytes)    
    {
    	if(bytes.length != 2)
    		throw new IllegalArgumentException("输入的字节数组长度不为2！");
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8));
    }

    //根据四个字节得到整形数据
    public static int getInt(byte[] bytes)    
    {
    	if(bytes.length != 4)
    		throw new IllegalArgumentException("输入的字节数组长度不为4！");
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }
    
  //根据四个字节得到整形数据
    public static long getUnsignedInt(byte[] bytes)    
    {
    	if(bytes.length != 4)
    		throw new IllegalArgumentException("输入的字节数组长度不为4！");
    	return (0xffL & (long)bytes[0]) | (0xff00L & ((long)bytes[1] << 8)) | (0xff0000L & ((long)bytes[2] << 16)) | (0xff000000L & ((long)bytes[3] << 24));         
    }
   
    //根据八个字节得到长整型数据
    public static long getLong(byte[] bytes)
    {
        return(0xffL & (long)bytes[0]) | (0xff00L & ((long)bytes[1] << 8)) | (0xff0000L & ((long)bytes[2] << 16)) | (0xff000000L & ((long)bytes[3] << 24))
         | (0xff00000000L & ((long)bytes[4] << 32)) | (0xff0000000000L & ((long)bytes[5] << 40)) | (0xff000000000000L & ((long)bytes[6] << 48)) | (0xff00000000000000L & ((long)bytes[7] << 56));
    }

    //根据字节得到浮点型数据
    public static float getFloat(byte[] bytes)
    {
        return Float.intBitsToFloat(getInt(bytes));
    }

    //根据字节得到双精度浮点型数据
    public static double getDouble(byte[] bytes)
    {
        long l = getLong(bytes);
        return Double.longBitsToDouble(l);
    }

    //根据字符集得到字节数组的字符串
    public static String getString(byte[] bytes, String charsetName)
    {
//        return new String(bytes, Charset.forName(charsetName));
    	String tmp = new String(bytes, Charset.forName(charsetName));
    	int pos = tmp.indexOf('\0');
//        System.out.println(pos);
        tmp = tmp.substring(0, pos);
        return tmp;
    }

  //根据默认字符集GBK得到字节数组的字符串
    public static String getString(byte[] bytes)
    {
    	return getString(bytes, "GBK");         
    }

    /**
	 * 从指定的字节数组中提取一个子数组并返回
	 *
	 * @param original
	 *            of type byte[] 原数组
	 * @param from
	 *            起始点
	 * @param length
	 *            长度
	 * @return 返回截取的数组
	 */
	public static byte[] copyOfRange(byte[] original, int from, int length) {		
		if (original.length < (from + length))
			throw new IllegalArgumentException("要复制的长度超过了原数组的长度");
		byte[] copy = new byte[length];
		System.arraycopy(original, from, copy, 0, length);
		return copy;
	}
	
	/**
	 * 从字节数组的某一个位置获取一个字节
	 * @param bytes 字节数组
	 * @param pos 位置
	 * @return 获取的字节
	 */
	public static byte getOneByte(byte[] bytes, int pos) 
	{
		if((pos >= bytes.length) || (pos < 0))
		{
			throw new IllegalArgumentException("输入的pos参数为负数或者超过了字节数组的长度");
		}
		byte b = bytes[pos];
		return b;
	}
    
	/**
	 * 从指定字节数组后添加一个字节并返回
	 *
	 * @param org
	 *            of type byte[] 原数组
	 * @param to
	 *            待被添加的一个byte
	 * @return 合并的数据
	 */
	public static byte[] append(byte[] org, byte to) {
		byte[] newByte = new byte[org.length + 1];
		System.arraycopy(org, 0, newByte, 0, org.length);
		newByte[org.length] = to;
		return newByte;
	}
	
	/**
	 * 将两个字节数组合并成一个字节数组
	 *
	 * @param first
	 *            前一个数组数组
	 * @param second
	 *            后一个byte[]
	 * @return 合并的字节数组
	 */
	public static byte[] merge(byte[] first, byte[] second) {
		byte[] newByte = new byte[first.length + second.length];
		System.arraycopy(first, 0, newByte, 0, first.length);
		System.arraycopy(second, 0, newByte, first.length, second.length);
		return newByte;
	}
	
	//将字符串转化字符数组
	public static char[] string2Chars(String str, int charLength)
	{
		char[] tmp = new char[charLength];
		str.getChars(0, str.length(), tmp, 0);
		return tmp;
	}
	
	//将字符数组转化为字符串
	public static String chars2String(char chars[])
	{
		byte[] bytearr = getBytes(chars, "US-ASCII");
		String tmp = getString(bytearr, "US-ASCII");
		return tmp;
	}
	
	/**
	 * 将输入的字节数组转化为double类型的数组
	 * @param bytes 输入的字节数 
	 * @return double类型的数组
	 */
	public static double[] getDoubleArray(byte[] bytes) {
		int Length = bytes.length;
		if(Length % 8 != 0)//如果无法整除8，就返回空。8是Java中double数据占的字节数
			return null;
		double[] temp = new double[Length/8];
		
		for(int i = 0; i < Length/8; i++)
		{
			temp[i] = getDouble(copyOfRange(bytes, i*8, 8));
		}
		return temp;
	}
}


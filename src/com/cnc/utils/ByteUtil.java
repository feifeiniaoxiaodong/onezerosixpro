package com.cnc.utils;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;


public class ByteUtil
{
	//�õ����������ݵ��ֽ�����2bytes
    public static byte[] getBytes(short data)
    {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    //�õ�Java�ַ����ֽ����ݣ�2bytes
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
    //�õ��������ݵ��ֽڣ�4bytes
    public static byte[] getBytes(int data)
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    //�õ����������ݵ��ֽڣ�8bytes
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

    //�õ����������ֽ�
    public static byte[] getBytes(float data)
    {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    //�õ�˫���ȸ��������ֽ�
    public static byte[] getBytes(double data)
    {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    //�����ַ����õ��ַ������ֽ�
    public static byte[] getBytes(String data, String charsetName)
    {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    //����Ĭ�ϵ�GBK����õ����ݵ��ֽ�
    public static byte[] getBytes(String data)
    {
        return getBytes(data, "GBK");
    }

    /**
	 * ���ַ���������ַ���ת��Ϊ�ֽ�����
	 * @param encode �ַ���������
	 * @param chars �ַ�����
	 * @return	�ֽ�����
	 */
	public static byte[] getBytes(char[] chars, String encode) {
		Charset cs = Charset.forName(encode);
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();
	}
    
	//���������ֽڵõ�����������
    public static short getShort(byte[] bytes)
    {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    //���������ֽڵõ��ַ�����
    public static char getChar(byte[] bytes)
    {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }
    
  //����һ���ֽڵõ�ASCII���ַ�����
    public static char getAsciiChar(byte b)
    {
        return (char) (b & 0xff);
    }
    
    public static int getUnsignedShort(byte[] bytes)    
    {
    	if(bytes.length != 2)
    		throw new IllegalArgumentException("������ֽ����鳤�Ȳ�Ϊ2��");
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8));
    }

    //�����ĸ��ֽڵõ���������
    public static int getInt(byte[] bytes)    
    {
    	if(bytes.length != 4)
    		throw new IllegalArgumentException("������ֽ����鳤�Ȳ�Ϊ4��");
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }
    
  //�����ĸ��ֽڵõ���������
    public static long getUnsignedInt(byte[] bytes)    
    {
    	if(bytes.length != 4)
    		throw new IllegalArgumentException("������ֽ����鳤�Ȳ�Ϊ4��");
    	return (0xffL & (long)bytes[0]) | (0xff00L & ((long)bytes[1] << 8)) | (0xff0000L & ((long)bytes[2] << 16)) | (0xff000000L & ((long)bytes[3] << 24));         
    }
   
    //���ݰ˸��ֽڵõ�����������
    public static long getLong(byte[] bytes)
    {
        return(0xffL & (long)bytes[0]) | (0xff00L & ((long)bytes[1] << 8)) | (0xff0000L & ((long)bytes[2] << 16)) | (0xff000000L & ((long)bytes[3] << 24))
         | (0xff00000000L & ((long)bytes[4] << 32)) | (0xff0000000000L & ((long)bytes[5] << 40)) | (0xff000000000000L & ((long)bytes[6] << 48)) | (0xff00000000000000L & ((long)bytes[7] << 56));
    }

    //�����ֽڵõ�����������
    public static float getFloat(byte[] bytes)
    {
        return Float.intBitsToFloat(getInt(bytes));
    }

    //�����ֽڵõ�˫���ȸ���������
    public static double getDouble(byte[] bytes)
    {
        long l = getLong(bytes);
        return Double.longBitsToDouble(l);
    }

    //�����ַ����õ��ֽ�������ַ���
    public static String getString(byte[] bytes, String charsetName)
    {
//        return new String(bytes, Charset.forName(charsetName));
    	String tmp = new String(bytes, Charset.forName(charsetName));
    	int pos = tmp.indexOf('\0');
//        System.out.println(pos);
        tmp = tmp.substring(0, pos);
        return tmp;
    }

  //����Ĭ���ַ���GBK�õ��ֽ�������ַ���
    public static String getString(byte[] bytes)
    {
    	return getString(bytes, "GBK");         
    }

    /**
	 * ��ָ�����ֽ���������ȡһ�������鲢����
	 *
	 * @param original
	 *            of type byte[] ԭ����
	 * @param from
	 *            ��ʼ��
	 * @param length
	 *            ����
	 * @return ���ؽ�ȡ������
	 */
	public static byte[] copyOfRange(byte[] original, int from, int length) {		
		if (original.length < (from + length))
			throw new IllegalArgumentException("Ҫ���Ƶĳ��ȳ�����ԭ����ĳ���");
		byte[] copy = new byte[length];
		System.arraycopy(original, from, copy, 0, length);
		return copy;
	}
	
	/**
	 * ���ֽ������ĳһ��λ�û�ȡһ���ֽ�
	 * @param bytes �ֽ�����
	 * @param pos λ��
	 * @return ��ȡ���ֽ�
	 */
	public static byte getOneByte(byte[] bytes, int pos) 
	{
		if((pos >= bytes.length) || (pos < 0))
		{
			throw new IllegalArgumentException("�����pos����Ϊ�������߳������ֽ�����ĳ���");
		}
		byte b = bytes[pos];
		return b;
	}
    
	/**
	 * ��ָ���ֽ���������һ���ֽڲ�����
	 *
	 * @param org
	 *            of type byte[] ԭ����
	 * @param to
	 *            ������ӵ�һ��byte
	 * @return �ϲ�������
	 */
	public static byte[] append(byte[] org, byte to) {
		byte[] newByte = new byte[org.length + 1];
		System.arraycopy(org, 0, newByte, 0, org.length);
		newByte[org.length] = to;
		return newByte;
	}
	
	/**
	 * �������ֽ�����ϲ���һ���ֽ�����
	 *
	 * @param first
	 *            ǰһ����������
	 * @param second
	 *            ��һ��byte[]
	 * @return �ϲ����ֽ�����
	 */
	public static byte[] merge(byte[] first, byte[] second) {
		byte[] newByte = new byte[first.length + second.length];
		System.arraycopy(first, 0, newByte, 0, first.length);
		System.arraycopy(second, 0, newByte, first.length, second.length);
		return newByte;
	}
	
	//���ַ���ת���ַ�����
	public static char[] string2Chars(String str, int charLength)
	{
		char[] tmp = new char[charLength];
		str.getChars(0, str.length(), tmp, 0);
		return tmp;
	}
	
	//���ַ�����ת��Ϊ�ַ���
	public static String chars2String(char chars[])
	{
		byte[] bytearr = getBytes(chars, "US-ASCII");
		String tmp = getString(bytearr, "US-ASCII");
		return tmp;
	}
	
	/**
	 * ��������ֽ�����ת��Ϊdouble���͵�����
	 * @param bytes ������ֽ��� 
	 * @return double���͵�����
	 */
	public static double[] getDoubleArray(byte[] bytes) {
		int Length = bytes.length;
		if(Length % 8 != 0)//����޷�����8���ͷ��ؿա�8��Java��double����ռ���ֽ���
			return null;
		double[] temp = new double[Length/8];
		
		for(int i = 0; i < Length/8; i++)
		{
			temp[i] = getDouble(copyOfRange(bytes, i*8, 8));
		}
		return temp;
	}
}


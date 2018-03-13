package com.cnc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ʱ�乤����
 * @author wei
 *
 */
public class TimeUtil {
	
	//ϵͳ��ʱʱ���ʽ
	private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");//ʱ�����ʽ
	//�ɼ���Ϣʱ�����ʽ
	private static SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

	/**
	 * ��ȡ��ǰ��ʱ���
	 * @return
	 */
	public static String getTimestamp(){
//		String strTime = formatter2.format(new Date());//��ʼ�ɼ���Ϣ�ĸ����¼���ʱ���
		return formatter2.format(Calendar.getInstance().getTime());		
		
	}
	
	public static String getSimpleTime(){
		return formatter.format(Calendar.getInstance().getTime());
	}
}

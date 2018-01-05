package com.cnc.netService;

import com.cnc.huazhong.nctime;

import android.text.format.Time;

//������
public class Tools {	
	
	//���һ����ĸ��N�ģ���ʾʱ����û�а������ڼ���һ��
	public static String getStrTimeN(nctime time)
	{
		String rtnStr = time.year + "/" + time.month + "/" + time.day + ";"
				+ time.hour + "_" + time.minute + "_" + time.second + "_" + time.hsecond;//����������
		return rtnStr;
	}
	/*
	 * ����������ںͻ�������ϵͳ�еı���������ƥ��
	 */
	public static String getStrTimeF(nctime time)
	{
		String rtnStr = time.year + "/" + time.month + "/" + time.day + ";" + time.wday + ";"//��������
				+ time.hour + "_" + time.minute + "_" + time.second + "_" + time.hsecond;
		return rtnStr;
	}
	
	//��ȡnctime�ṹ��ʱ��
	public static nctime getNcTimeN(String timeStr)
	{
		nctime time = new nctime();
		String[] timeSplit = timeStr.split(";");
		String[] YMD = timeSplit[0].split("/");
		time.wday = 0;
		String[] HMS = timeSplit[1].split("_");
		
		time.year = Integer.parseInt(YMD[0]);
		time.month = Integer.parseInt(YMD[1]);
		time.day = Integer.parseInt(YMD[2]);
		
		time.hour = Integer.parseInt(HMS[0]);
		time.minute = Integer.parseInt(HMS[1]);
		time.second = Integer.parseInt(HMS[2]);
		time.hsecond = Integer.parseInt(HMS[3]);
		return time;
	}
	
	/*
	 * ����������ںͻ�������ϵͳ�еı���������ƥ��
	 */
	public static nctime getNcTimeF(String timeStr) //�ַ�����ʽ��ʱ��ת��Ϊ nctime�ṹ��ʱ��
	{
		nctime time = new nctime();
		String[] timeSplit = timeStr.split(";");
		String[] YMD = timeSplit[0].split("/");   //������
		time.wday = Integer.parseInt(timeSplit[1]); //����
		String[] HMS = timeSplit[2].split("_");  //ʱ����
		
		time.year = Integer.parseInt(YMD[0]);
		time.month = Integer.parseInt(YMD[1]);
		time.day = Integer.parseInt(YMD[2]);
		
		time.hour = Integer.parseInt(HMS[0]);
		time.minute = Integer.parseInt(HMS[1]);
		time.second = Integer.parseInt(HMS[2]);
		time.hsecond = Integer.parseInt(HMS[3]);
		return time;
	}
	
	/*
	 * ����������ںͻ�������ϵͳ�еı���������ƥ��
	 */
	public static String getTimeNow(Time t)  //��ȡ���ڵ�ʱ�䣬�ַ�������
	{		
		t.setToNow(); // ȡ��ϵͳʱ�䡣
		int year = t.year;
		int month = t.month + 1;//0-11
		int date = t.monthDay;
		int weekday = t.weekDay;//0-6
		
		int hour = t.hour;    // 0-23
		int minute = t.minute;
		int second = t.second;
		nctime tmptime = new nctime(year,month,date,weekday,hour,minute,second);
		String strtime = Tools.getStrTimeF(tmptime); 
		return strtime;
	}
}






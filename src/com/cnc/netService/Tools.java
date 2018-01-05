package com.cnc.netService;

import com.cnc.huazhong.nctime;

import android.text.format.Time;

//工具类
public class Tools {	
	
	//最后一个字母是N的，表示时间中没有包含星期几这一项
	public static String getStrTimeN(nctime time)
	{
		String rtnStr = time.year + "/" + time.month + "/" + time.day + ";"
				+ time.hour + "_" + time.minute + "_" + time.second + "_" + time.hsecond;//不包含星期
		return rtnStr;
	}
	/*
	 * 这个函数用于和华中数控系统中的本地数据相匹配
	 */
	public static String getStrTimeF(nctime time)
	{
		String rtnStr = time.year + "/" + time.month + "/" + time.day + ";" + time.wday + ";"//包含星期
				+ time.hour + "_" + time.minute + "_" + time.second + "_" + time.hsecond;
		return rtnStr;
	}
	
	//获取nctime结构的时间
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
	 * 这个函数用于和华中数控系统中的本地数据相匹配
	 */
	public static nctime getNcTimeF(String timeStr) //字符串格式的时间转化为 nctime结构的时间
	{
		nctime time = new nctime();
		String[] timeSplit = timeStr.split(";");
		String[] YMD = timeSplit[0].split("/");   //年月日
		time.wday = Integer.parseInt(timeSplit[1]); //星期
		String[] HMS = timeSplit[2].split("_");  //时分秒
		
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
	 * 这个函数用于和华中数控系统中的本地数据相匹配
	 */
	public static String getTimeNow(Time t)  //获取现在的时间，字符串类型
	{		
		t.setToNow(); // 取得系统时间。
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






package com.cnc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * @author wei
 *
 */
public class TimeUtil {
	
	//系统定时时间格式
	private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");//时间戳格式
	//采集信息时间戳格式
	private static SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

	/**
	 * 获取当前的时间戳
	 * @return
	 */
	public static String getTimestamp(){
//		String strTime = formatter2.format(new Date());//开始采集信息的各种事件，时间戳
		return formatter2.format(Calendar.getInstance().getTime());		
		
	}
	
	public static String getSimpleTime(){
		return formatter.format(Calendar.getInstance().getTime());
	}
}

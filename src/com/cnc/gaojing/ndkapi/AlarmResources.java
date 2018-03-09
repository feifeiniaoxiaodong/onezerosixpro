package com.cnc.gaojing.ndkapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 提供了两个工具函数根据报警ID号，从properties文件中获取报警信息
 * @author wei
 *
 */
public class AlarmResources {
	//机床报警信息文件路径
	private static final String strMotion  ="/com/gaojing/resources/alarms/mechinealarms.properties";  
	//PLC报警信息文件路径
	private static final String strPLC="/com/gaojing/resources/alarms/plcalarms.properties";
	
	/**
	 * 根据报警号在本地查找对应的报警信息
	 * @param alramid:报警号
	 * @return
	 */
	public static  String  getAlarmInfoById(String alarmType,String alramId){

		String alarmInfo=null,
				propertiesPath=strMotion;
		InputStream in=null;
		Properties  pro=null;

		if("PLC".equals(alarmType)){
			propertiesPath=strPLC;
		}		
		try {
			in=AlarmResources.class.getResourceAsStream(propertiesPath);
			pro=new Properties();
			pro.load(new InputStreamReader(in, "utf-8"));
			alarmInfo =pro.getProperty(alramId);
			
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}	
		}
		return alarmInfo;
	}
	

}

package com.cnc.gaojing.ndkapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * �ṩ���������ߺ������ݱ���ID�ţ���properties�ļ��л�ȡ������Ϣ
 * @author wei
 *
 */
public class AlarmResources {
	//����������Ϣ�ļ�·��
	private static final String strMotion  ="/com/gaojing/resources/alarms/mechinealarms.properties";  
	//PLC������Ϣ�ļ�·��
	private static final String strPLC="/com/gaojing/resources/alarms/plcalarms.properties";
	
	/**
	 * ���ݱ������ڱ��ز��Ҷ�Ӧ�ı�����Ϣ
	 * @param alramid:������
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

package com.cnc.hangtian.alarmfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.cnc.gaojing.ndkapi.AlarmResources;

public class FindAlarmByIdHangtian {
	
	//机床报警信息文件路径
	private static final String alarmfilepath  ="/com/cnc/hangtian/alarmfile/hangtianalarminfo.properties";  
		
	/**
	 * 根据报警号在本地查找对应的报警信息
	 * @param alramid:报警号
	 * @return
	 */
	public static  String  getAlarmInfoById(String alarmNo){

		String alarmInfo=null;
		InputStream in=null;
		Properties  pro=null;
			
		try {
			in=AlarmResources.class.getResourceAsStream(alarmfilepath);
			pro=new Properties();
			pro.load(new InputStreamReader(in, "gbk"));
			alarmInfo =pro.getProperty(alarmNo);
			
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

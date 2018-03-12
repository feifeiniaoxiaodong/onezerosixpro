package com.cnc.daq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;

public class DaqData {

	static final String TAG="DaqData...";
	public static final String forthG="liantong";
//	private static String cncid="beihangmechine001";		//数控机床ID
	private static String androidId=null;	//android系统的ID号
//	private static DataReg dataReg = null; //注册信息
//	private static DataLog dataLog =null;   //登录和登出信息
	

	private static LinkedList<DataAlarm> listDataAlarm = new LinkedList<DataAlarm>();//储存报警信息
	
	private static List<DataReg>  listDataReg= Collections.synchronizedList( new ArrayList<DataReg>());
	private static List<DataLog>  listDataLog=  Collections.synchronizedList(new ArrayList<DataLog>());
	
	/*
	 * 比较报警信息是否相同，如果相同则返回true
	 */
	public static boolean compareAlarmData(DataAlarm dataAlarm1, DataAlarm dataAlarm2)
	{		
		boolean same = false;
		if(dataAlarm1!=null && dataAlarm2 !=null){
			if(dataAlarm1.getNo().equals(dataAlarm2.getNo())&&
					dataAlarm1.getCtt().equals(dataAlarm2.getCtt())&&
					dataAlarm1.getId().equals(dataAlarm2.getId()))  //比较机床ID，区分不同机床的报警信息
			{	
				return true;
			}
		}					
		return same;
	}
	
//	public static String getCncid() {
//		return cncid;
//	}
//
//	public static void setCncid(String cncid) {
//		DaqData.cncid = cncid;
//	}

	public static String getAndroidId() {
		return androidId;
	}
	

	public static String getForthg() {
		return forthG;
	}

	public static void setAndroidId(String androidId) {
		DaqData.androidId = androidId;
	}

//	public static DataReg getDataReg() {
//		return dataReg;
//	}
//	public static void setDataReg(DataReg dataReg) {
//		DaqData.dataReg = dataReg;
//	}
//	
//	public static DataLog getDataLog() {
//		return dataLog;
//	}
//
//	public static void setDataLog(DataLog dataLog) {
//		DaqData.dataLog = dataLog;
//	}

	public static LinkedList<DataAlarm> getListDataAlarm() {
		return listDataAlarm;
	}

	
	/**
	 * 判断报警信息是否是新的报警信息
	 * @param dataAlarm
	 * @return
	 */
	public static boolean whetherNewAlarm(DataAlarm dataAlarm) {
		boolean newAlarm = true;//
		if(dataAlarm != null)//输入的报警不为空
		{			
			for (DataAlarm  dataAlarmOriginal : listDataAlarm) {
				if(compareAlarmData(dataAlarm, dataAlarmOriginal))
					return false;
			}			
		}
		return newAlarm;
	}
	
	/**
	 * 检测报警信息是否已经删除
	 * @param dataAlarm
	 * @param listDataAlarm
	 * @return
	 */
	public static boolean whetherRelAlarm(DataAlarm dataAlarm,LinkedList<DataAlarm> listDataAlarm) {
		boolean newAlarm = true; //
		if(dataAlarm != null &&  listDataAlarm!=null)//输入的报警不为空
		{			
			for (DataAlarm  dataAlarmOriginal : listDataAlarm) {
				if(compareAlarmData(dataAlarm, dataAlarmOriginal))
					return false;
			}			
		}
		return newAlarm;
	}

	public static List<DataReg> getListDataReg() {
		return listDataReg;
	}
	public static List<DataLog> getListDataLog() {
		return listDataLog;
	}

}

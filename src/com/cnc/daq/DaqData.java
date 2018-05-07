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
	private static long    cacheinfocount=0; //本地缓存数据量

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

	public synchronized static long getCacheinfocount() {
		return cacheinfocount;
	}

	public synchronized static void setCacheinfocount(long cacheinfocount) {
		DaqData.cacheinfocount = cacheinfocount;
	}


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

	/**
	 * 注册信息和日志信息列表的存储没有使用线程同步，此处没有必须用，原因：
	 * 1、list已经是同步队列，满足使用需求
	 * 2、各采集线程只需要向队列中存数据
	 * 3、数据发送线程中 先取队列头部数据，再删除该数据，这两个操作在同一线程中顺序进行，不存在数据删完取不到数据的情况
	 * 如果有多个发送线程共同对队列的数据取和删的话，就需要增加互斥锁
	 */
	
	/**
	 * 获取当前的注册信息
	 * @return DataReg对象，没有返回null
	 */
	public static DataReg  getDataReg(){
		DataReg dataReg=null;
		
		if( !listDataReg.isEmpty()){
			dataReg=listDataReg.get(0);
		}
		
		return dataReg;
	}
	/**
	 * 保存注册信息到队列
	 * @param dataReg
	 */
	public static void saveDataReg(DataReg dataReg){
		if(dataReg instanceof DataReg){
			listDataReg.add(dataReg);
		}		
	}
	/**
	 * 删除已成功发送的注册信息
	 */
	public static void delDataReg(DataReg dataReg){
		if(dataReg instanceof DataReg){
			listDataReg.remove(dataReg);
		}
	}
	
	/**
	 * 获取日志信息
	 * @return 没有返回null
	 */
	public static DataLog getDataLog(){
		DataLog dataLog=null;
		if(! listDataLog.isEmpty()){
			dataLog=listDataLog.get(0);
		}
		return dataLog;
	}
	/**
	 * 保存日志信息到队列
	 * @param dataLog
	 */
	public static void saveDataLog(DataLog dataLog){
		if(dataLog instanceof DataLog){
			listDataLog.add(dataLog);
		}
	}
	/**
	 * 移除成功发送的日志信息
	 * @param dataLog
	 */
	public static void delDataLog(DataLog dataLog){
		if(dataLog instanceof DataLog){
			listDataLog.remove(dataLog);
		}
	}
	
	

}

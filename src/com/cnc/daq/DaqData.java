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
//	private static String cncid="beihangmechine001";		//���ػ���ID
	private static String androidId=null;	//androidϵͳ��ID��
//	private static DataReg dataReg = null; //ע����Ϣ
//	private static DataLog dataLog =null;   //��¼�͵ǳ���Ϣ
	private static long    cacheinfocount=0; //���ػ���������

	private static LinkedList<DataAlarm> listDataAlarm = new LinkedList<DataAlarm>();//���汨����Ϣ
	
	private static List<DataReg>  listDataReg= Collections.synchronizedList( new ArrayList<DataReg>());
	private static List<DataLog>  listDataLog=  Collections.synchronizedList(new ArrayList<DataLog>());
	
	
	
	/*
	 * �Ƚϱ�����Ϣ�Ƿ���ͬ�������ͬ�򷵻�true
	 */
	public static boolean compareAlarmData(DataAlarm dataAlarm1, DataAlarm dataAlarm2)
	{		
		boolean same = false;
		if(dataAlarm1!=null && dataAlarm2 !=null){
			if(dataAlarm1.getNo().equals(dataAlarm2.getNo())&&
					dataAlarm1.getCtt().equals(dataAlarm2.getCtt())&&
					dataAlarm1.getId().equals(dataAlarm2.getId()))  //�Ƚϻ���ID�����ֲ�ͬ�����ı�����Ϣ
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
	 * �жϱ�����Ϣ�Ƿ����µı�����Ϣ
	 * @param dataAlarm
	 * @return
	 */
	public static boolean whetherNewAlarm(DataAlarm dataAlarm) {
		boolean newAlarm = true;//
		if(dataAlarm != null)//����ı�����Ϊ��
		{			
			for (DataAlarm  dataAlarmOriginal : listDataAlarm) {
				if(compareAlarmData(dataAlarm, dataAlarmOriginal))
					return false;
			}			
		}
		return newAlarm;
	}
	
	/**
	 * ��ⱨ����Ϣ�Ƿ��Ѿ�ɾ��
	 * @param dataAlarm
	 * @param listDataAlarm
	 * @return
	 */
	public static boolean whetherRelAlarm(DataAlarm dataAlarm,LinkedList<DataAlarm> listDataAlarm) {
		boolean newAlarm = true; //
		if(dataAlarm != null &&  listDataAlarm!=null)//����ı�����Ϊ��
		{			
			for (DataAlarm  dataAlarmOriginal : listDataAlarm) {
				if(compareAlarmData(dataAlarm, dataAlarmOriginal))
					return false;
			}			
		}
		return newAlarm;
	}

	/**
	 * ע����Ϣ����־��Ϣ�б�Ĵ洢û��ʹ���߳�ͬ�����˴�û�б����ã�ԭ��
	 * 1��list�Ѿ���ͬ�����У�����ʹ������
	 * 2�����ɼ��߳�ֻ��Ҫ������д�����
	 * 3�����ݷ����߳��� ��ȡ����ͷ�����ݣ���ɾ�������ݣ�������������ͬһ�߳���˳����У�����������ɾ��ȡ�������ݵ����
	 * ����ж�������̹߳�ͬ�Զ��е�����ȡ��ɾ�Ļ�������Ҫ���ӻ�����
	 */
	
	/**
	 * ��ȡ��ǰ��ע����Ϣ
	 * @return DataReg����û�з���null
	 */
	public static DataReg  getDataReg(){
		DataReg dataReg=null;
		
		if( !listDataReg.isEmpty()){
			dataReg=listDataReg.get(0);
		}
		
		return dataReg;
	}
	/**
	 * ����ע����Ϣ������
	 * @param dataReg
	 */
	public static void saveDataReg(DataReg dataReg){
		if(dataReg instanceof DataReg){
			listDataReg.add(dataReg);
		}		
	}
	/**
	 * ɾ���ѳɹ����͵�ע����Ϣ
	 */
	public static void delDataReg(DataReg dataReg){
		if(dataReg instanceof DataReg){
			listDataReg.remove(dataReg);
		}
	}
	
	/**
	 * ��ȡ��־��Ϣ
	 * @return û�з���null
	 */
	public static DataLog getDataLog(){
		DataLog dataLog=null;
		if(! listDataLog.isEmpty()){
			dataLog=listDataLog.get(0);
		}
		return dataLog;
	}
	/**
	 * ������־��Ϣ������
	 * @param dataLog
	 */
	public static void saveDataLog(DataLog dataLog){
		if(dataLog instanceof DataLog){
			listDataLog.add(dataLog);
		}
	}
	/**
	 * �Ƴ��ɹ����͵���־��Ϣ
	 * @param dataLog
	 */
	public static void delDataLog(DataLog dataLog){
		if(dataLog instanceof DataLog){
			listDataLog.remove(dataLog);
		}
	}
	
	

}

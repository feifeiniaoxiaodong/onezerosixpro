package com.cnc.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cnc.DataBaseService.DBService;
import com.cnc.daq.DaqData;
import com.cnc.daqnew.HandleMsgTypeMcro;
import com.cnc.domain.DataAlarm;

//������Ϣ�洢�����˹�����
public class AlarmFilterList {
	
	private static final String TAG="AlarmFilterList..."; 
	private Handler    handler =null ;  //�ѱ�����Ϣ���͵����ݴ����߳̽��д���
	private List<DataAlarm> nowAlarmList =new LinkedList<>();//��ǰ���ڷ����ı���
	@SuppressLint("SimpleDateFormat") 
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//ʱ�����ʽ
	private String stampTime = formatter.format(System.currentTimeMillis());
	
	public AlarmFilterList(Handler handler){
		this.handler=handler;
	}
	
	public void saveCollectedAlarmList( List<DataAlarm> collectAlarmList){
		stampTime = formatter.format(System.currentTimeMillis());//ÿ�ε��û�������Ҫ����ʱ���
		
		//��ǰû�б�������
		if( this.nowAlarmList.isEmpty()){
			for (DataAlarm dataAlarm : collectAlarmList) {
				dataAlarm.setF((byte)0);	//��ʶ���Ǳ�������
				nowAlarmList.add(dataAlarm);//��¼��ǰ���еı���
				handleAlarmItem(dataAlarm);//���汨����Ϣ
			}
				
		}else{ //��ǰ�б�������
			if(collectAlarmList.isEmpty()){    //��ǰû�вɼ���������Ϣ��˵��������Ϣ�������
				for(DataAlarm tmpAlarm: nowAlarmList){
					tmpAlarm.setF((byte)1); //����������
					tmpAlarm.setTime(stampTime); //�������ʱ���
					handleAlarmItem(tmpAlarm);	//���汨����Ϣ				
				}
				nowAlarmList.clear(); //����������ˣ���յ�ǰ�����б�
			}else{  //��ǰ�б������� ������Ҳ�ɼ����˱�����Ϣ
				//�����ɼ����ı�����Ϣ��������ǰ�����б����Ƿ��У�û�еĻ��������·����ı�������¼ ������У������ñ�����δ����������н����κβ���
				for(DataAlarm tmpAlarm: collectAlarmList){
					if(whetherNewAlarm(tmpAlarm,nowAlarmList)){
						tmpAlarm.setF((byte)0);
						this.nowAlarmList.add(tmpAlarm); 
						handleAlarmItem(tmpAlarm);//���汨����Ϣ
					}	
				}
				
				//������ǰ�ı����б� ����鱨���Ƿ���
				for(DataAlarm tmpAlarm: nowAlarmList){
					if(whetherNewAlarm(tmpAlarm, collectAlarmList)){ //�����ǰ�����б��еı�����Ϣ���²ɼ������б����Ҳ�����˵������������
						nowAlarmList.remove(tmpAlarm); 				//��ӵ�ǰ������Ϣ�б����Ƴ�����������Ϣ�����ñ���������־��ʱ��������浽���ݿ⣬׼�����͵�������
						tmpAlarm.setF((byte)1);
						tmpAlarm.setTime(stampTime);
						handleAlarmItem(tmpAlarm);//���汨����Ϣ						
					}
				}			
			}
				
		}
	
	}

	//���汨����Ϣ
	//���ڶ�д���ݿ⻨ʱ��ϳ������԰���Ϣ���͸�handler�����߳̽��б��棬������ݲɼ��̵߳Ĵ����ٶ�
	private void handleAlarmItem(DataAlarm tmpAlarm){
//		DBService.getInstanceDBService().saveAlarmData(tmpAlarm);//���浽���ݿ⣬׼�����͵�������
		Message  msg= this.handler.obtainMessage();
		msg.what=HandleMsgTypeMcro.MSG_ALRAM; //������Ϣ
		msg.obj= tmpAlarm;
		this.handler.sendMessage(msg);//������Ϣ
	}


	public List<DataAlarm> getNowAlarmList() {
		return nowAlarmList;
	}
	
	
	/*
	 * �Ƚϱ�����Ϣ�Ƿ���ͬ�������ͬ�򷵻�true
	 */
	private  boolean compareAlarmData(DataAlarm dataAlarm1, DataAlarm dataAlarm2)
	{		
		boolean same = false;
		if(dataAlarm1!=null && dataAlarm2 !=null){
			if(dataAlarm1.getNo().equals(dataAlarm2.getNo())&&
					dataAlarm1.getCtt().equals(dataAlarm2.getCtt())&&
					dataAlarm1.getId().equals(dataAlarm2.getId()))  //�Ƚϻ���ID�����ֲ�ͬ�����ı�����Ϣ
			{	
				Log.d(TAG,"alarm �Ƚ���ͬ");
				Log.d(TAG, dataAlarm1.toString()  + "======"+dataAlarm2.toString());
				return true;
			}
		}	
		Log.d(TAG,"alarm �Ƚϲ�ͬ");
		Log.d(TAG, dataAlarm1.toString()  + "======"+dataAlarm2.toString());
		return same;
	}
	
	
	/**
	 * �жϱ�����Ϣ�Ƿ����µı�����Ϣ
	 * @param dataAlarm
	 * @return
	 */
	private  boolean whetherNewAlarm(DataAlarm dataAlarm,List<DataAlarm> alarmList) {
		boolean newAlarm = true;//
		if(dataAlarm != null && alarmList!=null){//����ı�����Ϊ��					
			for (DataAlarm  dataAlarmOriginal : alarmList) {
				if(compareAlarmData(dataAlarm, dataAlarmOriginal))
					return false;
			}			
		}
		return newAlarm;
	}
	
	
	

}

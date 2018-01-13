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

//报警信息存储、过滤工具类
public class AlarmFilterList {
	
	private static final String TAG="AlarmFilterList..."; 
	private Handler    handler =null ;  //把报警信息发送到数据处理线程进行处理
	private List<DataAlarm> nowAlarmList =new LinkedList<>();//当前正在发生的报警
	@SuppressLint("SimpleDateFormat") 
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//时间戳格式
	private String stampTime = formatter.format(System.currentTimeMillis());
	
	public AlarmFilterList(Handler handler){
		this.handler=handler;
	}
	
	public void saveCollectedAlarmList( List<DataAlarm> collectAlarmList){
		stampTime = formatter.format(System.currentTimeMillis());//每次调用还函数都要更新时间戳
		
		//当前没有报警发生
		if( this.nowAlarmList.isEmpty()){
			for (DataAlarm dataAlarm : collectAlarmList) {
				dataAlarm.setF((byte)0);	//标识这是报警发生
				nowAlarmList.add(dataAlarm);//记录当前所有的报警
				handleAlarmItem(dataAlarm);//保存报警信息
			}
				
		}else{ //当前有报警发生
			if(collectAlarmList.isEmpty()){    //当前没有采集到报警信息，说明报警信息都解除了
				for(DataAlarm tmpAlarm: nowAlarmList){
					tmpAlarm.setF((byte)1); //报警解除标记
					tmpAlarm.setTime(stampTime); //报警解除时间戳
					handleAlarmItem(tmpAlarm);	//保存报警信息				
				}
				nowAlarmList.clear(); //报警都解除了，清空当前报警列表
			}else{  //当前有报警发生 ，并且也采集到了报警信息
				//遍历采集到的报警信息，看看当前报警列表中是否有：没有的话表明是新发生的报警，记录 ；如果有，表明该报警还未解除，不进行进行任何操作
				for(DataAlarm tmpAlarm: collectAlarmList){
					if(whetherNewAlarm(tmpAlarm,nowAlarmList)){
						tmpAlarm.setF((byte)0);
						this.nowAlarmList.add(tmpAlarm); 
						handleAlarmItem(tmpAlarm);//保存报警信息
					}	
				}
				
				//遍历当前的报警列表 ，检查报警是否解除
				for(DataAlarm tmpAlarm: nowAlarmList){
					if(whetherNewAlarm(tmpAlarm, collectAlarmList)){ //如果当前报警列表中的报警信息在新采集到的列表中找不到，说明报警消除了
						nowAlarmList.remove(tmpAlarm); 				//则从当前报警信息列表中移除该条报警信息，设置报警消除标志和时间戳，保存到数据库，准备发送到服务器
						tmpAlarm.setF((byte)1);
						tmpAlarm.setTime(stampTime);
						handleAlarmItem(tmpAlarm);//保存报警信息						
					}
				}			
			}
				
		}
	
	}

	//保存报警信息
	//由于读写数据库花时间较长，所以把信息发送给handler处理线程进行保存，提高数据采集线程的处理速度
	private void handleAlarmItem(DataAlarm tmpAlarm){
//		DBService.getInstanceDBService().saveAlarmData(tmpAlarm);//保存到数据库，准备发送到服务器
		Message  msg= this.handler.obtainMessage();
		msg.what=HandleMsgTypeMcro.MSG_ALRAM; //报警信息
		msg.obj= tmpAlarm;
		this.handler.sendMessage(msg);//发送消息
	}


	public List<DataAlarm> getNowAlarmList() {
		return nowAlarmList;
	}
	
	
	/*
	 * 比较报警信息是否相同，如果相同则返回true
	 */
	private  boolean compareAlarmData(DataAlarm dataAlarm1, DataAlarm dataAlarm2)
	{		
		boolean same = false;
		if(dataAlarm1!=null && dataAlarm2 !=null){
			if(dataAlarm1.getNo().equals(dataAlarm2.getNo())&&
					dataAlarm1.getCtt().equals(dataAlarm2.getCtt())&&
					dataAlarm1.getId().equals(dataAlarm2.getId()))  //比较机床ID，区分不同机床的报警信息
			{	
				Log.d(TAG,"alarm 比较相同");
				Log.d(TAG, dataAlarm1.toString()  + "======"+dataAlarm2.toString());
				return true;
			}
		}	
		Log.d(TAG,"alarm 比较不同");
		Log.d(TAG, dataAlarm1.toString()  + "======"+dataAlarm2.toString());
		return same;
	}
	
	
	/**
	 * 判断报警信息是否是新的报警信息
	 * @param dataAlarm
	 * @return
	 */
	private  boolean whetherNewAlarm(DataAlarm dataAlarm,List<DataAlarm> alarmList) {
		boolean newAlarm = true;//
		if(dataAlarm != null && alarmList!=null){//输入的报警不为空					
			for (DataAlarm  dataAlarmOriginal : alarmList) {
				if(compareAlarmData(dataAlarm, dataAlarmOriginal))
					return false;
			}			
		}
		return newAlarm;
	}
	
	
	

}

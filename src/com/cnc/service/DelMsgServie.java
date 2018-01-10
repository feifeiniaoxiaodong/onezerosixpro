package com.cnc.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.cnc.DataBaseService.DBService;
import com.cnc.daq.DaqData;
import com.cnc.daqnew.HandleMsgTypeMcro;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class DelMsgServie extends Service {
	
	static final String TAG="DelMsgServie";
	private static Handler handlerService =null;
	DataDealBinder dataDealBinder =new DataDealBinder();
	
	//服务首次创建的时候调用
	@Override
	public void onCreate() {
		//获取androidID
		String androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);						
		DaqData.setAndroidId(androidId);
		Log.d(TAG, "androidId::" + androidId);	
		
		//单独开启一个线程处理Service Handler 消息
		new Thread("MyHandlerThread"){
			public void run() {
				Looper.prepare();
				handlerService=new ServiceHandler();
				Looper.loop();	
			};			
		}.start();
		Log.d(TAG, "onCreate");
		super.onCreate();
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {		
		return dataDealBinder;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		reStartService(); //重启服务
		super.onDestroy();
	}
	
	private void reStartService(){
		Context context= getApplicationContext();
		Intent intent=new Intent(context , DelMsgServie.class);
		context.startService(intent);
	}
	
	public static Handler getHandlerService() {
		return handlerService;
	}
		
	static class ServiceHandler   extends Handler {
		
		@SuppressLint("SimpleDateFormat") 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//时间戳格式
		int count = 1;	//记录采集的次数
		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			
			 switch(msg.what) {
	         case HandleMsgTypeMcro.MSG_ISUCCESS://初始化成功 0
	         	Log.i(TAG, (String)msg.obj);            		
	             break;
	         case HandleMsgTypeMcro.MSG_IFAILURE://初始化失败 1
	         	Log.i(TAG, (String)msg.obj);//打印出故障消息，并重启服务
//	         	stopSelf();//关闭自身服务        	
//	         	daqActivity.stopServiceActivity();//关闭服务
	             break;
	         case HandleMsgTypeMcro.MSG_LFAILURE://登录或者链接失败 2
	         	Log.i(TAG, (String)msg.obj);//打印出故障消息，并重启服务
//	         	stopSelf();//关闭自身服务
	            break;
	         case HandleMsgTypeMcro.MSG_DFAILURE://获取信息失败，重启服务 3
	         	Log.i(TAG, (String)msg.obj);//打印出故障消息，并重启服务
//	         	stopSelf();//关闭自身服务
	             break;
	/**************************以下是报警信息处理流程************************************************/               
	         case HandleMsgTypeMcro.MSG_ALRAM://报警信息 10
	         	@SuppressWarnings("unchecked")
					LinkedList<DataAlarm> listDataAlarm = (LinkedList<DataAlarm>)msg.obj;
	         	showDataAlarm(listDataAlarm);
	         	String strTime=formatter.format(new Date()); 
	         	
	         	if(DaqData.getListDataAlarm().size() == 0){//如果当前的报警记录为零，则采集到的报警数不为零，说明有新的报警进入
	         	
	         		for (DataAlarm dataAlarm : listDataAlarm) {
							dataAlarm.setF((byte)0);	//标识这是报警发生
							DaqData.getListDataAlarm().add(dataAlarm);//记录当前所有的报警
//							alarmHandle(dataAlarm); //保存报警信息到数据库
							DBService.getInstanceDBService().saveAlarmData(dataAlarm);
						}
	         	}else{	//如果当前已经有了报警记录
	         	
						if(listDataAlarm.size() == 0)//已有报警，这次采集不到报警信息，所以报警全部消除
						{
							for (DataAlarm dataAlarm : DaqData.getListDataAlarm()) 
							{
								dataAlarm.setF((byte)1);//标识这是报警结束
								dataAlarm.setTime(strTime);//报警结束时间
								DBService.getInstanceDBService().saveAlarmData(dataAlarm);
							}
							DaqData.getListDataAlarm().clear();//清空这个链表
						}
						else //已有报警和此次报警都不为零，这种情况最复杂，但是也是最少见
						{
							for (DataAlarm dataAlarm : listDataAlarm) {
								if(DaqData.whetherNewAlarm(dataAlarm))//确认是否是新的报警信息
								{
									dataAlarm.setF((byte)0);//标识这是报警发生
									DaqData.getListDataAlarm().add(dataAlarm);//添加到当前报警记录
//									alarmHandle(dataAlarm);//报警处理，添加到SQLite数据库中
									DBService.getInstanceDBService().saveAlarmData(dataAlarm);
								}
							}
							for (DataAlarm dataAlarm : DaqData.getListDataAlarm()) //找出当前被清除的报警信息
							{	//如果报警记录中有，而当前报警中却没有找到该报警，说明这条报警信息已经消除
								if(DaqData.whetherRelAlarm(dataAlarm,listDataAlarm))//确认报警信息记录在已经消失
								{
									DaqData.getListDataAlarm().remove(dataAlarm);
									dataAlarm.setF((byte)1);//标识这是报警结束
									dataAlarm.setTime(strTime);//报警结束时间
//									alarmHandle(dataAlarm);//报警处理，添加到SQLite数据库中	
									DBService.getInstanceDBService().saveAlarmData(dataAlarm);
								}
							}
						}
					}             	        		
	         	break;
	/**************************以下是注册信息和运行信息的储存************************************************/ 
	         case HandleMsgTypeMcro.MSG_REG://得到机床的基本信息 11，注册信息
//	         	DataReg dataReg = (DataReg)msg.obj;
//	         	DaqData.setDataReg(dataReg);       //保存注册信息
//	         	ChildHandler.sendMessage(msg);//把注册消息送到发送子线程去处理       	
//	         	showMacInfo(dataReg);            	
	            break;
	         case HandleMsgTypeMcro.MSG_RUN://得到运行中的基本信息 12，运行信息            	
	         	DataRun dataRun = (DataRun)msg.obj;
	         	count = msg.arg1;
	         	showRunInfo(count, dataRun);  
	         	
	         	DBService.getInstanceDBService().saveRunData(dataRun);//直接存入数据库
	            break; 
	         case HandleMsgTypeMcro.MSG_TEST://测试信息
	         	Log.d(TAG, "测试信息");            	
	             break;
	         case HandleMsgTypeMcro.MSG_DELAYTIME://延时时间
	        	 
//	        	 daqActivity.setTvDelayTime((String)msg.obj);
	        	 break;
	         case HandleMsgTypeMcro.MSG_COUNTRUN: //显示本地数据库中未发送的运行信息的条数
//	        	 daqActivity.setTvCount_runinfo((String)msg.obj);
	        	 break;
	         default:
	        	 break;
	        	 
	         }//end switch()
	
		}//end handle message
		
		private void showRunInfo(int count, DataRun dataRun){	
			Log.d(TAG,count + "--" + dataRun.getId()+ "::" + dataRun.getTime());		
		}
		
		private void showMacInfo(DataReg dataReg) {
			// TODO Auto-generated method stub
			Log.d(TAG, "macInfo.SN_NUM::" + dataReg.getId());
			Log.d(TAG, "macInfo.VER::" + dataReg.getVer());
			Log.d(TAG, "macInfo.TIME::" + dataReg.getTime());		
		}	

		private void showDataAlarm(LinkedList<DataAlarm> listDataAlarm) {	
			for (DataAlarm dataAlarm : listDataAlarm) 
			{
				Log.d(TAG, dataAlarm.getNo()+"+" + dataAlarm.getCtt() + ":" + dataAlarm.getTime());
			}		
		}
	}
	
	
	
	Map<String ,Runnable >  map =new HashMap<>();
	
	//绑定服务允许的操作
	public  class DataDealBinder extends Binder{
		//开启华中数控采集线程
		public  void startHzThread(String ip){
			startHzThread(ip,21);
		}
		public  void startHzThread(String ip,int port){
			
		}
	
	}

}

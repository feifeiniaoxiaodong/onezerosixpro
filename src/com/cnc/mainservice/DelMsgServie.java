package com.cnc.mainservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.cnc.daq.DaqData;
import com.cnc.db.service.DBService;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.net.datasend.HandleMsgTypeMcro;

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
//	DataDealBinder dataDealBinder =new DataDealBinder();
	
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
		Log.d(TAG, "service onCreate");
		super.onCreate();
	}
		
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "service onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {		
		return null;
	}
	
	@Override
	public void onDestroy() {		
		reStartService(); //重启服务
		super.onDestroy();
	}
	
	//服务重启函数
	private void reStartService(){
		Context context= getApplicationContext();
		Intent intent=new Intent(context , DelMsgServie.class);
		context.startService(intent);
	}
	
	public static Handler getHandlerService() {
		return handlerService;
	}
		
	//数据处理Handler类
	class ServiceHandler   extends Handler {
		
		@SuppressLint("SimpleDateFormat") 
		private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//时间戳格式
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
	         	DataAlarm dataAlarm=(DataAlarm)msg.obj;	         	
	         	DBService.getInstanceDBService().saveAlarmData(dataAlarm);//保存到数据库，准备发送到服务器
				            	        		
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
	
	
	
/*	Map<String ,Runnable >  map =new HashMap<>();
	
	//绑定服务允许的操作
	public  class DataDealBinder extends Binder{
		//开启华中数控采集线程
		public  void startHzThread(String ip){
			//startHzThread(ip,21);
		}
		public  void startHzThread(String ip,int port){
			
		}
	
	}*/

}

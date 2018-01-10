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
	
	//�����״δ�����ʱ�����
	@Override
	public void onCreate() {
		//��ȡandroidID
		String androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);						
		DaqData.setAndroidId(androidId);
		Log.d(TAG, "androidId::" + androidId);	
		
		//��������һ���̴߳���Service Handler ��Ϣ
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
		reStartService(); //��������
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
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//ʱ�����ʽ
		int count = 1;	//��¼�ɼ��Ĵ���
		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			
			 switch(msg.what) {
	         case HandleMsgTypeMcro.MSG_ISUCCESS://��ʼ���ɹ� 0
	         	Log.i(TAG, (String)msg.obj);            		
	             break;
	         case HandleMsgTypeMcro.MSG_IFAILURE://��ʼ��ʧ�� 1
	         	Log.i(TAG, (String)msg.obj);//��ӡ��������Ϣ������������
//	         	stopSelf();//�ر��������        	
//	         	daqActivity.stopServiceActivity();//�رշ���
	             break;
	         case HandleMsgTypeMcro.MSG_LFAILURE://��¼��������ʧ�� 2
	         	Log.i(TAG, (String)msg.obj);//��ӡ��������Ϣ������������
//	         	stopSelf();//�ر��������
	            break;
	         case HandleMsgTypeMcro.MSG_DFAILURE://��ȡ��Ϣʧ�ܣ��������� 3
	         	Log.i(TAG, (String)msg.obj);//��ӡ��������Ϣ������������
//	         	stopSelf();//�ر��������
	             break;
	/**************************�����Ǳ�����Ϣ��������************************************************/               
	         case HandleMsgTypeMcro.MSG_ALRAM://������Ϣ 10
	         	@SuppressWarnings("unchecked")
					LinkedList<DataAlarm> listDataAlarm = (LinkedList<DataAlarm>)msg.obj;
	         	showDataAlarm(listDataAlarm);
	         	String strTime=formatter.format(new Date()); 
	         	
	         	if(DaqData.getListDataAlarm().size() == 0){//�����ǰ�ı�����¼Ϊ�㣬��ɼ����ı�������Ϊ�㣬˵�����µı�������
	         	
	         		for (DataAlarm dataAlarm : listDataAlarm) {
							dataAlarm.setF((byte)0);	//��ʶ���Ǳ�������
							DaqData.getListDataAlarm().add(dataAlarm);//��¼��ǰ���еı���
//							alarmHandle(dataAlarm); //���汨����Ϣ�����ݿ�
							DBService.getInstanceDBService().saveAlarmData(dataAlarm);
						}
	         	}else{	//�����ǰ�Ѿ����˱�����¼
	         	
						if(listDataAlarm.size() == 0)//���б�������βɼ�����������Ϣ�����Ա���ȫ������
						{
							for (DataAlarm dataAlarm : DaqData.getListDataAlarm()) 
							{
								dataAlarm.setF((byte)1);//��ʶ���Ǳ�������
								dataAlarm.setTime(strTime);//��������ʱ��
								DBService.getInstanceDBService().saveAlarmData(dataAlarm);
							}
							DaqData.getListDataAlarm().clear();//����������
						}
						else //���б����ʹ˴α�������Ϊ�㣬���������ӣ�����Ҳ�����ټ�
						{
							for (DataAlarm dataAlarm : listDataAlarm) {
								if(DaqData.whetherNewAlarm(dataAlarm))//ȷ���Ƿ����µı�����Ϣ
								{
									dataAlarm.setF((byte)0);//��ʶ���Ǳ�������
									DaqData.getListDataAlarm().add(dataAlarm);//��ӵ���ǰ������¼
//									alarmHandle(dataAlarm);//����������ӵ�SQLite���ݿ���
									DBService.getInstanceDBService().saveAlarmData(dataAlarm);
								}
							}
							for (DataAlarm dataAlarm : DaqData.getListDataAlarm()) //�ҳ���ǰ������ı�����Ϣ
							{	//���������¼���У�����ǰ������ȴû���ҵ��ñ�����˵������������Ϣ�Ѿ�����
								if(DaqData.whetherRelAlarm(dataAlarm,listDataAlarm))//ȷ�ϱ�����Ϣ��¼���Ѿ���ʧ
								{
									DaqData.getListDataAlarm().remove(dataAlarm);
									dataAlarm.setF((byte)1);//��ʶ���Ǳ�������
									dataAlarm.setTime(strTime);//��������ʱ��
//									alarmHandle(dataAlarm);//����������ӵ�SQLite���ݿ���	
									DBService.getInstanceDBService().saveAlarmData(dataAlarm);
								}
							}
						}
					}             	        		
	         	break;
	/**************************������ע����Ϣ��������Ϣ�Ĵ���************************************************/ 
	         case HandleMsgTypeMcro.MSG_REG://�õ������Ļ�����Ϣ 11��ע����Ϣ
//	         	DataReg dataReg = (DataReg)msg.obj;
//	         	DaqData.setDataReg(dataReg);       //����ע����Ϣ
//	         	ChildHandler.sendMessage(msg);//��ע����Ϣ�͵��������߳�ȥ����       	
//	         	showMacInfo(dataReg);            	
	            break;
	         case HandleMsgTypeMcro.MSG_RUN://�õ������еĻ�����Ϣ 12��������Ϣ            	
	         	DataRun dataRun = (DataRun)msg.obj;
	         	count = msg.arg1;
	         	showRunInfo(count, dataRun);  
	         	
	         	DBService.getInstanceDBService().saveRunData(dataRun);//ֱ�Ӵ������ݿ�
	            break; 
	         case HandleMsgTypeMcro.MSG_TEST://������Ϣ
	         	Log.d(TAG, "������Ϣ");            	
	             break;
	         case HandleMsgTypeMcro.MSG_DELAYTIME://��ʱʱ��
	        	 
//	        	 daqActivity.setTvDelayTime((String)msg.obj);
	        	 break;
	         case HandleMsgTypeMcro.MSG_COUNTRUN: //��ʾ�������ݿ���δ���͵�������Ϣ������
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
	
	//�󶨷�������Ĳ���
	public  class DataDealBinder extends Binder{
		//�����������زɼ��߳�
		public  void startHzThread(String ip){
			startHzThread(ip,21);
		}
		public  void startHzThread(String ip,int port){
			
		}
	
	}

}

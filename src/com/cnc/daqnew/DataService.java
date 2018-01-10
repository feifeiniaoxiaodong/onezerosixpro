package com.cnc.daqnew;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cnc.DataBaseService.DBService;
import com.cnc.daq.DaqData;
import com.example.wei.gsknetclient_studio.GSKDataCollectThread;




import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
/**
 * ����
 * @author wei
 *
 */
public class DataService extends Service {

	private static final String TAG="DataService...";
//	private DBService   dbServicenew=null;   //���ݿ�������
//	private Handler     daqActivityHandler=null; //UI�̵߳�Handler
	
	private   final IBinder  dataDealBinder=new DataDealBinder();

	//�״δ�������ʱ��ϵͳ���ô˷���
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();		
		String androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);						//�õ�androidID
		DaqData.setAndroidId(androidId);
		Log.d(TAG, "androidId::" + androidId);	
	
		//��ʼ�����ݿ⡣�����ڷ��񱻴�����ʱ��ų�ʼ�����ݿ⣬�����̨�Ѿ�����ͬ�������ݿ⣬��ô��Ĭ����ԭ�����Ǹ�
		try{
//			if(dbServicenew==null){  //�Ѿ�ʹ�õ���ģʽ
//				dbServicenew = new DBService(getApplicationContext()); //�õ�SQLite���ݿ�������
//				Log.i(TAG, "����onCreate()...�����ɹ�");
//			}						
		}catch(Exception e)
		{
			Log.d(TAG, e.getMessage());
		}
		Log.d(TAG, "����onCreate()...�����ɹ�");
		
	}

	//��ʼ���ݲɼ������ݴ������߳�
	//���������������
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//�������ݲɼ�����
//			DaqThread daqThread = new DaqThread();
//			new Thread(daqThread).start();
//			//���������߳�
//	    	Transmit transmit = new Transmit();
//			new Thread(transmit).start();
		
//	    	Transmit_Test_offline  transmit1=new Transmit_Test_offline();
//			new Thread(transmit1).start();
		
//			this.ChildHandler = transmit.getHandler();//�õ����̵߳�handler
		
		Log.d(TAG, "onStartCommand...��ʼ����");	
		return super.onStartCommand(intent, flags, startId); 
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind...����");	
		return dataDealBinder;
	}
		
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		restartService();//�������񣬱�֤���񲻱��ص�		
	}
	
	//��������
	private void restartService() {
		// TODO Auto-generated method stub
    	//�������ݲɼ�����
		Context crt = this.getApplicationContext();
		Intent service = new Intent(crt,DataService.class);//��ʽ��ͼ
		crt.startService(service);//Intent�������(Service)		
	}
	
	
	
	/**
	 * ����Binder�࣬����Activity�󶨵�ʱ�򷵻ظ�Binder����ͨ��Binder������Activity�в���Service�ĺ���
	 * @author wei
	 *
	 */
	 public class DataDealBinder extends Binder{
		 
		 ExecutorService exec=null;
		//��ȡ�������
		 public DataService getService(){
		      return DataService.this;
		 }
		 
		 GSKDataCollectThread  gsthread=null;  //�������ݲɼ��̶߳���
		 
		//�������ݹ����߳�
		public void startDataThread(Handler handler){
			
			exec=Executors.newCachedThreadPool();	
		
			//�������ݲɼ��߳�
//			HzDataCollectThread dataCollectThread=new HzDataCollectThread(handler,HandleMsgTypeMcro.HUAZHONG1_5);
//			exec.execute(dataCollectThread);			
//			Log.d(TAG, "DataCollectThread...����");	
			
			//�ɼ��ڶ�̨����
			/*DataCollectThread dataCollectThread2=new DataCollectThread(handler,HandleMsgTypeMcro.HUAZHONG1_1);
			exec.execute(dataCollectThread2);
			Log.d(TAG, "DataCollectThread...����");*/
			
			//�������ݷ����߳�
//			DataTransmitThread dataTransmitThread=new DataTransmitThread(handler);			
//			exec.execute(dataTransmitThread);
//			Log.d(TAG, "DataTransmitThread...����");

//			Transmit_Test_offline  transmit1=new Transmit_Test_offline();
//			new Thread(transmit1).start();
			
//			gsthread=new Caijixinxi("GSKThread","192.168.188.128",handler);
//			gsthread.start();
					
		}
		
		public void stopgskThread(){
			if(gsthread!=null){
//				gsthread.setThreadfalg(false);
			}
			
		}
		
		public void stopService(){						
			stopSelf();//�ر��������
		}
		
		public void shutDownThread(){
//			exec.shutdownNow();
		}
				
	}
	
	
}

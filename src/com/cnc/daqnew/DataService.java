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
 * 服务
 * @author wei
 *
 */
public class DataService extends Service {

	private static final String TAG="DataService...";
//	private DBService   dbServicenew=null;   //数据库服务对象
//	private Handler     daqActivityHandler=null; //UI线程的Handler
	
	private   final IBinder  dataDealBinder=new DataDealBinder();

	//首次创建服务时，系统调用此方法
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();		
		String androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);						//得到androidID
		DaqData.setAndroidId(androidId);
		Log.d(TAG, "androidId::" + androidId);	
	
		//初始化数据库。这是在服务被创建的时候才初始化数据库，如果后台已经存在同名的数据库，那么就默认用原来的那个
		try{
//			if(dbServicenew==null){  //已经使用单例模式
//				dbServicenew = new DBService(getApplicationContext()); //得到SQLite数据库服务对象
//				Log.i(TAG, "服务onCreate()...创建成功");
//			}						
		}catch(Exception e)
		{
			Log.d(TAG, e.getMessage());
		}
		Log.d(TAG, "服务onCreate()...创建成功");
		
	}

	//开始数据采集和数据传输子线程
	//允许组件启动服务
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//开启数据采集服务
//			DaqThread daqThread = new DaqThread();
//			new Thread(daqThread).start();
//			//开启传输线程
//	    	Transmit transmit = new Transmit();
//			new Thread(transmit).start();
		
//	    	Transmit_Test_offline  transmit1=new Transmit_Test_offline();
//			new Thread(transmit1).start();
		
//			this.ChildHandler = transmit.getHandler();//得到子线程的handler
		
		Log.d(TAG, "onStartCommand...开始服务");	
		return super.onStartCommand(intent, flags, startId); 
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind...开启");	
		return dataDealBinder;
	}
		
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		restartService();//重启服务，保证服务不被关掉		
	}
	
	//重启服务
	private void restartService() {
		// TODO Auto-generated method stub
    	//开启数据采集服务
		Context crt = this.getApplicationContext();
		Intent service = new Intent(crt,DataService.class);//显式意图
		crt.startService(service);//Intent激活组件(Service)		
	}
	
	
	
	/**
	 * 定义Binder类，在与Activity绑定的时候返回该Binder对象，通过Binder对象在Activity中操作Service的函数
	 * @author wei
	 *
	 */
	 public class DataDealBinder extends Binder{
		 
		 ExecutorService exec=null;
		//获取服务对象
		 public DataService getService(){
		      return DataService.this;
		 }
		 
		 GSKDataCollectThread  gsthread=null;  //广数数据采集线程对象
		 
		//开启数据工作线程
		public void startDataThread(Handler handler){
			
			exec=Executors.newCachedThreadPool();	
		
			//开启数据采集线程
//			HzDataCollectThread dataCollectThread=new HzDataCollectThread(handler,HandleMsgTypeMcro.HUAZHONG1_5);
//			exec.execute(dataCollectThread);			
//			Log.d(TAG, "DataCollectThread...开启");	
			
			//采集第二台机床
			/*DataCollectThread dataCollectThread2=new DataCollectThread(handler,HandleMsgTypeMcro.HUAZHONG1_1);
			exec.execute(dataCollectThread2);
			Log.d(TAG, "DataCollectThread...开启");*/
			
			//开启数据发送线程
//			DataTransmitThread dataTransmitThread=new DataTransmitThread(handler);			
//			exec.execute(dataTransmitThread);
//			Log.d(TAG, "DataTransmitThread...开启");

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
			stopSelf();//关闭自身服务
		}
		
		public void shutDownThread(){
//			exec.shutdownNow();
		}
				
	}
	
	
}

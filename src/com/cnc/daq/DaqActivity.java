package com.cnc.daq;

import com.cnc.daqnew.DataService;
import com.cnc.daqnew.MsgDealHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 主界面Activity
 * @author wei
 *
 */
public class DaqActivity extends Activity implements View.OnClickListener{
	private final static String TAG="DaqActivity...";
	
	private static Handler  mHandler=null;
	private  DataService.DataDealBinder  dataDealBinder =null;//绑定Service的Binder
	private  DataService  dataService=null;  //数据管理服务
	TextView tvDelayTime=null;
	TextView  count_runinfo=null;//未发送的运行信息的条数
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_daq);//定义用户界面布局
		setContentView(R.layout.daqactivity);//定义用户界面布局
		
		mHandler=new DaqActivityHandler();//创建该活动自己的Handler
		
		Button button = (Button)findViewById(R.id.mybtn); //开启数据采集按钮
		Button bt2=     (Button)findViewById(R.id.bt2);    //按钮
		bt2.setText("开启mainActivity");
		tvDelayTime=(TextView)findViewById(R.id.delaytime);//文本框，延时时间
		count_runinfo=(TextView)findViewById(R.id.count);
		
		button.setOnClickListener(this);
		bt2.setOnClickListener(this);
		
//		mHandler=new MsgDealHandler(DaqActivity.this);//创建Handler
		
		startDaqService();	//开启服务	
	}
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
			case R.id.mybtn:	//数据采集按钮
				
				setTvDelayTime("nihao");

//				Transmit_Test_offline  transmit1=new Transmit_Test_offline();
//				new Thread(transmit1).start();
				
//				Log.d(TAG, "点击了开启服务按钮");
//				startDaqService();	//开启服务							
//				Log.d(TAG, "绑定服务到Activity...");
//				Intent bindIntent=new Intent(this ,com.cnc.daqnew.DataService.class);
//				bindService(bindIntent,mconnection,BIND_AUTO_CREATE); //绑定服务			
//				dataDealBinder.startDataThread(mHandler);//开启两个子线程
				
			break;
			case R.id.bt2:
//				dataDealBinder.startDataThread(mHandler); //开启两个子线程
				
				Intent intent= new Intent(DaqActivity.this , MainActivity.class);
//				intent.putExtra("key", "wei");
				startActivity(intent);
				
				break;
			
			default:
				
			break;				
		}		
	}
	
	//修改textview内容，延时时间
	public void setTvDelayTime(String s){
		tvDelayTime.setText(s);
	}
	//设置当前未发送运行的条数
	public void setTvCount_runinfo(String s){
		count_runinfo.setText(s);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(mconnection);//在Activity销毁的时候解绑服务
	}
		
	/**
	 * 开启服务
	 */
	private void startDaqService() {
		//开启数据采集服务
		Context crt =this.getApplicationContext();
		//开启服务
		Intent service = new Intent(crt, com.cnc.daqnew.DataService.class);//显式意图
		crt.startService(service);
		Log.d(TAG, "开启服务...");
		//绑定服务到Activity
		Intent bindIntent=new Intent(this ,com.cnc.daqnew.DataService.class);
		bindService(bindIntent,mconnection,BIND_AUTO_CREATE); //绑定服务
		Log.d(TAG, "绑定服务到Activity...");
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daq, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Toast.makeText(this, "You clicked Action.",Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	
	
	//服务和activity进行绑定
    private ServiceConnection mconnection=new ServiceConnection(){		
    	@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG,"执行connection操作...");
			dataDealBinder=(DataService.DataDealBinder)service;  //获取绑定服务的Binder对象	
			dataService=dataDealBinder.getService();     		//获取服务对象实例
			
			dataDealBinder.startDataThread(mHandler);//开启两个子线程			
		}
    	
		@Override
		public void onServiceDisconnected(ComponentName name) {	
			stopServiceActivity();
		}
    	
    };

    //暂停服务
    public  void stopServiceActivity(){
    	dataDealBinder.stopService();   	
    }
    
    public static Handler getmHandler() {
		return mHandler;
	}
    
    
    //DaqActivity handler
    @SuppressLint("HandlerLeak") 
    class DaqActivityHandler extends Handler{
    	
    	@Override
    	public void handleMessage(Message msg) {
    		
    	}    	
    }
   

}









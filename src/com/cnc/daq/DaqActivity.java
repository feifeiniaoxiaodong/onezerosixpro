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
 * ������Activity
 * @author wei
 *
 */
public class DaqActivity extends Activity implements View.OnClickListener{
	private final static String TAG="DaqActivity...";
	
	private static Handler  mHandler=null;
	private  DataService.DataDealBinder  dataDealBinder =null;//��Service��Binder
	private  DataService  dataService=null;  //���ݹ������
	TextView tvDelayTime=null;
	TextView  count_runinfo=null;//δ���͵�������Ϣ������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_daq);//�����û����沼��
		setContentView(R.layout.daqactivity);//�����û����沼��
		
		mHandler=new DaqActivityHandler();//�����û�Լ���Handler
		
		Button button = (Button)findViewById(R.id.mybtn); //�������ݲɼ���ť
		Button bt2=     (Button)findViewById(R.id.bt2);    //��ť
		bt2.setText("����mainActivity");
		tvDelayTime=(TextView)findViewById(R.id.delaytime);//�ı�����ʱʱ��
		count_runinfo=(TextView)findViewById(R.id.count);
		
		button.setOnClickListener(this);
		bt2.setOnClickListener(this);
		
//		mHandler=new MsgDealHandler(DaqActivity.this);//����Handler
		
		startDaqService();	//��������	
	}
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
			case R.id.mybtn:	//���ݲɼ���ť
				
				setTvDelayTime("nihao");

//				Transmit_Test_offline  transmit1=new Transmit_Test_offline();
//				new Thread(transmit1).start();
				
//				Log.d(TAG, "����˿�������ť");
//				startDaqService();	//��������							
//				Log.d(TAG, "�󶨷���Activity...");
//				Intent bindIntent=new Intent(this ,com.cnc.daqnew.DataService.class);
//				bindService(bindIntent,mconnection,BIND_AUTO_CREATE); //�󶨷���			
//				dataDealBinder.startDataThread(mHandler);//�����������߳�
				
			break;
			case R.id.bt2:
//				dataDealBinder.startDataThread(mHandler); //�����������߳�
				
				Intent intent= new Intent(DaqActivity.this , MainActivity.class);
//				intent.putExtra("key", "wei");
				startActivity(intent);
				
				break;
			
			default:
				
			break;				
		}		
	}
	
	//�޸�textview���ݣ���ʱʱ��
	public void setTvDelayTime(String s){
		tvDelayTime.setText(s);
	}
	//���õ�ǰδ�������е�����
	public void setTvCount_runinfo(String s){
		count_runinfo.setText(s);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(mconnection);//��Activity���ٵ�ʱ�������
	}
		
	/**
	 * ��������
	 */
	private void startDaqService() {
		//�������ݲɼ�����
		Context crt =this.getApplicationContext();
		//��������
		Intent service = new Intent(crt, com.cnc.daqnew.DataService.class);//��ʽ��ͼ
		crt.startService(service);
		Log.d(TAG, "��������...");
		//�󶨷���Activity
		Intent bindIntent=new Intent(this ,com.cnc.daqnew.DataService.class);
		bindService(bindIntent,mconnection,BIND_AUTO_CREATE); //�󶨷���
		Log.d(TAG, "�󶨷���Activity...");
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
	
	//�����activity���а�
    private ServiceConnection mconnection=new ServiceConnection(){		
    	@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG,"ִ��connection����...");
			dataDealBinder=(DataService.DataDealBinder)service;  //��ȡ�󶨷����Binder����	
			dataService=dataDealBinder.getService();     		//��ȡ�������ʵ��
			
			dataDealBinder.startDataThread(mHandler);//�����������߳�			
		}
    	
		@Override
		public void onServiceDisconnected(ComponentName name) {	
			stopServiceActivity();
		}
    	
    };

    //��ͣ����
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









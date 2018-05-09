package com.cnc.daq;

//import java.text.ParsePosition;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
import java.util.HashMap;
//import java.util.List;
//import java.util.Timer;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import java.text.SimpleDateFormat;

import com.cnc.broadcast.BroadcastAction;
import com.cnc.broadcast.BroadcastType;
import com.cnc.domain.UiDataAlarmRun;
import com.cnc.domain.UiDataNo;
import com.cnc.gaojing.GJDataCollectThread;
//import com.cnc.hangtian.thread.HangTianDataCollectThread;
import com.cnc.hangtian.thread.HangtianDataCollectThreadnew;
import com.cnc.huazhong.dc.CommonDataCollectThreadInterface;
import com.cnc.huazhong.dc.HzDataCollectThread;
import com.cnc.net.datasend.DataTransmitThread;
import com.cnc.net.datasend.HandleMsgTypeMcro;
//import com.cnc.test.TestGJMultiThread;
import com.cnc.utils.TimeUtil;
import com.example.wei.gsknetclient_studio.GSKDataCollectThread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

//import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
//import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	final String TAG="mainactivity";
	static Handler  mainActivityHandler=null;
//	private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");//ʱ�����ʽ
	private SharedPreferences pref;	
	private SharedPreferences.Editor editor ;
	static ExecutorService exec=null;
	
	static Map<String, ItemViewHolder>  viewmapgGsk=new HashMap<>(); //�����ͺ��칫�ô�map
	
	static ItemViewHolder itemHuazhong ;
//					itemGaojing;
	
	static TextView  cachenum  ,sendnum  ,speed ;
	
//	Map<String, Runnable> threadmap=new HashMap<>();
	static DataTransmitThread dataTransmitThread=null; //���ݷ����߳�
	static String  currentSpinSelItem_Hz=null;
//			currentSpinSelItem_Gj=null;
	
	static String current_HZ_NoIP = null;		//Huazhong
	static HzDataCollectThread currentHZDcObj=null;
	
//	static String current_Gj_NoIP= null;
//	static GJDataCollectThread currentGjDcObj=null;//Gaojing
	
	String [] gskIpArray=null; 		//gsk ip list
	String [] hangtianIpArray=null; //hangtian ip list
	String [] gaojingIpArray=null;  //gaojing  ip list
	
	static Map<String ,GSKDataCollectThread>  mapgskThreadobj=new HashMap<>();
	static Map<String ,HangtianDataCollectThreadnew> mapHangtianThreadObj=new HashMap<>();
	static Map<String, CommonDataCollectThreadInterface> mapThreadObj=new HashMap<>();
	
	LocalBroadcastManager localBroadcastManager =null; //���ع㲥
	LocalReceiver localReceiver=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		mainActivityHandler=new mainHandler();
		pref= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		exec=Executors.newCachedThreadPool();							  //�̳߳�
		gskIpArray =getResources().getStringArray(R.array.gskip);   	  //��������ip��ַ
		hangtianIpArray=getResources().getStringArray(R.array.hangtianip);//��������IP��ַ
		gaojingIpArray=getResources().getStringArray(R.array.gaojingip); //�����߾�IP��ַ
		initViewMap();
		//ע�᱾�ع㲥
		localBroadcastManager=LocalBroadcastManager.getInstance(this);
		IntentFilter filter=new IntentFilter(BroadcastAction.SendThread_PARAMALL); 
		filter.addAction(BroadcastAction.ACTION_HANGTIAN_RUN_ALARM);
		localReceiver=new LocalReceiver();
		localBroadcastManager.registerReceiver(localReceiver, filter);
		
		startTask(); //�������ݲɼ��ͷ����߳�
	}
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();	
		localBroadcastManager.unregisterReceiver(localReceiver);//ȡ��ע��㲥
	}
	
	
	//��������
	private void startTask(){

		new Thread(){
			public void run() {
				//���������߳�
				if(dataTransmitThread==null){
					dataTransmitThread=new DataTransmitThread();
//					new Thread(dataTransmitThread).start();
					exec.execute(dataTransmitThread);
					Log.d(TAG,"���������ݷ����߳�");
				}
				
				runOnUiThread(new Runnable() {				
					@Override
					public void run() {
						startDefaultThread(); //�����ϴιػ�ʱ�������߳�						
					}
				});
				
				//���ö�ʱ��������
//				Timer timer=new Timer(false);
//				timer.scheduleAtFixedRate(new startTask(), 1000*60*5, 1000*60*55*1);//ÿ��55����ִ��һ������
//				timer.scheduleAtFixedRate(new stopTask(), 1000*60*2, 1000*60*55*1); //
						
			} //end run			
		}.start();		
	}
		
	@SuppressLint("HandlerLeak") 
	class mainHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			String strlabel=null;
			ItemViewHolder itemViewHolder=null;
			
			switch (msg.what) {
			case HandleMsgTypeMcro.HUAZHONG_UINO:         //huazhong cncid and android id
				UiDataNo uidatano=(UiDataNo)msg.obj;

				itemHuazhong.getIdcnc().setText(uidatano.getIdcnc());
				itemHuazhong.getIdandroid().setText(uidatano.getIdandroid());				
				break;
			case HandleMsgTypeMcro.HUAZHONG_UIALARM:      //huazhong running and alarm information
				UiDataAlarmRun uialarmrun=(UiDataAlarmRun)msg.obj;
				itemHuazhong.getAlarm().setText(uialarmrun.getAlarminfo());
				itemHuazhong.getRuninfo().setText(uialarmrun.getRuninfo());				
				break;	
			case HandleMsgTypeMcro.GAOJING_UINO:         //Gaojing cncid and android id
				UiDataNo gjdatano=(UiDataNo)msg.obj;
				strlabel=gjdatano.getThreadlabel();
				if(strlabel!=null){
					viewmapgGsk.get(strlabel).getIdcnc().setText(gjdatano.getIdcnc());
					viewmapgGsk.get(strlabel).getIdandroid().setText(gjdatano.getIdandroid());
				}

				break;
			case HandleMsgTypeMcro.GAOJING_UIALARM:      //Gaojing running and alarm information
				UiDataAlarmRun gjUiDataAlarmRun=(UiDataAlarmRun)msg.obj;
				strlabel= gjUiDataAlarmRun.getThreadlabel();
				if(strlabel!=null){
					viewmapgGsk.get(strlabel).getAlarm().setText(gjUiDataAlarmRun.getAlarminfo());
					viewmapgGsk.get(strlabel).getRuninfo().setText(gjUiDataAlarmRun.getRuninfo());
				}
				
				break;
			case HandleMsgTypeMcro.GSK_UINO: 			//gsk
				UiDataNo gskdatano=(UiDataNo)msg.obj;
				strlabel =gskdatano.getThreadlabel();
				if(strlabel!=null){
					itemViewHolder=viewmapgGsk.get(strlabel);
					itemViewHolder.getIdcnc().setText(gskdatano.getIdcnc());
					itemViewHolder.getIdandroid().setText(gskdatano.getIdandroid());
				}
				break;
			case HandleMsgTypeMcro.GSK_UIALARM:   	//gsk
				UiDataAlarmRun gskUiDataAlarmRun=(UiDataAlarmRun)msg.obj;
				strlabel=gskUiDataAlarmRun.getThreadlabel();
				itemViewHolder=viewmapgGsk.get(strlabel);
				itemViewHolder.getAlarm().setText(gskUiDataAlarmRun.getAlarminfo());
				itemViewHolder.getRuninfo().setText(gskUiDataAlarmRun.getRuninfo());
						
				break;						
			default:{}				
			}	
		}
	}
	
	/**
	 * �����ϴο������߳�
	 */
	private  void startDefaultThread(){
							
		//�����߳�
		String preipHz = pref.getString("huazhong", null);
		if(preipHz!=null && !preipHz.trim().equals("") ){
			//�����߳�
			startHzThread(preipHz);
		}
			
		//���������߾����ݲɼ��߳�
		for(int i=0; i<gaojingIpArray.length ;i++){
			String str=gaojingIpArray[i];
			String no=null , preNoip=null;
			if(str!=null && !"".equals(str.trim())){
				no=str.substring(0, str.indexOf(':'));
				preNoip=pref.getString(no, null);
				
				if(preNoip!=null ){ //�������߳�ʱӦ��֤���̻߳�δ����
					startGjThread(preNoip);//���������߾��������ݲɼ��߳�				
				}
			}
		}
		
		
		//���������������ݲɼ��߳�
		for(int i=0;i<gskIpArray.length;i++){
			String str= gskIpArray[i];
			String no=null , preNoip=null;
			if(str!=null && !"".equals(str.trim())){
				no=str.substring(0, str.indexOf(':'));
				preNoip=pref.getString(no, null);
				
				if(preNoip!=null ){ //�������߳�ʱӦ��֤���̻߳�δ����
					startGskThread(preNoip);//���������������ݲɼ��߳�				
				}
			}
		}	
		//���������������ݲɼ��߳�
		for(int i=0;i<hangtianIpArray.length;i++){
			String str= hangtianIpArray[i];
			String no=null , preNoip=null;
			if(str!=null && !"".equals(str.trim())){
				no=str.substring(0, str.indexOf(':'));
				preNoip=pref.getString(no, null);
				
				if(preNoip!=null ){ //�������߳�ʱӦ��֤���̻߳�δ����
					startHangtianThread(preNoip);//���������������ݲɼ��߳�				
				}
			}
		}
	}

	public static Handler getMainActivityHandler() {
		return mainActivityHandler;
	}
			
	//init all view  components id
	private void initViewMap(){
		
	ItemViewHolder	itemGsk01=null,
					itemGsk02=null,
					itemGsk03=null,
					itemGsk04=null,
					itemGsk05=null;
			
		cachenum=(TextView)findViewById(R.id.txcachenum);		
		sendnum=(TextView)findViewById(R.id.txsendno);		
		speed=(TextView)findViewById(R.id.txspeed);
	
		Log.d(TAG,"initViewMap");
		TextView no = (TextView)findViewById(R.id.gaojing1).findViewById(R.id.no);
		TextView ip = (TextView)findViewById(R.id.gaojing1).findViewById(R.id.ip);
		TextView idcnc = (TextView)findViewById(R.id.gaojing1).findViewById(R.id.idcnc);
		TextView idandroid = (TextView)findViewById(R.id.gaojing1).findViewById(R.id.idandroid);
		TextView alarm = (TextView)findViewById(R.id.gaojing1).findViewById(R.id.alarm);
		TextView run = (TextView)findViewById(R.id.gaojing1).findViewById(R.id.run);
		Spinner  spinner=(Spinner)findViewById(R.id.gaojing1).findViewById(R.id.spinner);
		Button btstart=(Button) findViewById(R.id.gaojing1).findViewById(R.id.btstart);
		Button btstop=(Button) findViewById(R.id.gaojing1).findViewById(R.id.btstop);
		ItemViewHolder tempgaojing1=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		viewmapgGsk.put("Gaojing1_1", tempgaojing1);
		//itemGaojing=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		
		 no = (TextView)findViewById(R.id.gaojing2).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.gaojing2).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.gaojing2).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.gaojing2).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.gaojing2).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.gaojing2).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.gaojing2).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.gaojing2).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.gaojing2).findViewById(R.id.btstop);
		 ItemViewHolder tempgaojing2=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		 viewmapgGsk.put("Gaojing1_2", tempgaojing2);
		 
		 no = (TextView)findViewById(R.id.gaojing3).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.gaojing3).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.gaojing3).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.gaojing3).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.gaojing3).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.gaojing3).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.gaojing3).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.gaojing3).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.gaojing3).findViewById(R.id.btstop);
		 ItemViewHolder tempgaojing3=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		 viewmapgGsk.put("Gaojing1_3", tempgaojing3);
		 
		 no = (TextView)findViewById(R.id.gaojing4).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.gaojing4).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.gaojing4).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.gaojing4).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.gaojing4).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.gaojing4).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.gaojing4).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.gaojing4).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.gaojing4).findViewById(R.id.btstop);
		 ItemViewHolder tempgaojing4=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		 viewmapgGsk.put("Gaojing1_4", tempgaojing4);
		 
		 no = (TextView)findViewById(R.id.gaojing5).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.gaojing5).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.gaojing5).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.gaojing5).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.gaojing5).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.gaojing5).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.gaojing5).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.gaojing5).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.gaojing5).findViewById(R.id.btstop);
		 ItemViewHolder tempgaojing5=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		 viewmapgGsk.put("Gaojing1_5", tempgaojing5);
				 
		 no = (TextView)findViewById(R.id.huazhong).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.huazhong).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.huazhong).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.huazhong).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.huazhong).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.huazhong).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.huazhong).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.huazhong).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.huazhong).findViewById(R.id.btstop);
		 itemHuazhong=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);

		
		 no = (TextView)findViewById(R.id.gsk1).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.gsk1).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.gsk1).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.gsk1).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.gsk1).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.gsk1).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.gsk1).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.gsk1).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.gsk1).findViewById(R.id.btstop);
		 itemGsk01=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
//		 viewmap.put("gsk1", itemViewHolder);
		 
		 no = (TextView)findViewById(R.id.gsk2).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.gsk2).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.gsk2).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.gsk2).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.gsk2).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.gsk2).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.gsk2).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.gsk2).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.gsk2).findViewById(R.id.btstop);
		 itemGsk02=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
//		 viewmap.put("gsk2", itemViewHolder);
		 
		 no = (TextView)findViewById(R.id.gsk3).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.gsk3).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.gsk3).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.gsk3).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.gsk3).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.gsk3).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.gsk3).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.gsk3).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.gsk3).findViewById(R.id.btstop);
		 itemGsk03=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
//		 viewmap.put("gsk3", itemViewHolder);
		 
		 no = (TextView)findViewById(R.id.gsk4).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.gsk4).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.gsk4).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.gsk4).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.gsk4).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.gsk4).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.gsk4).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.gsk4).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.gsk4).findViewById(R.id.btstop);
		 itemGsk04=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
//		 viewmap.put("gsk4", itemViewHolder);
		 
		 no = (TextView)findViewById(R.id.gsk5).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.gsk5).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.gsk5).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.gsk5).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.gsk5).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.gsk5).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.gsk5).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.gsk5).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.gsk5).findViewById(R.id.btstop);
		 itemGsk05=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
//		 viewmap.put("gsk5", itemViewHolder);
		
		 viewmapgGsk.put("Gsk1_1", itemGsk01);
		 viewmapgGsk.put("Gsk1_2", itemGsk02);
		 viewmapgGsk.put("Gsk1_3", itemGsk03);
		 viewmapgGsk.put("Gsk1_4", itemGsk04);
		 viewmapgGsk.put("Gsk1_5", itemGsk05);
		 
		 //��������
		 no = (TextView)findViewById(R.id.hangtian1).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.hangtian1).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.hangtian1).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.hangtian1).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.hangtian1).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.hangtian1).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.hangtian1).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.hangtian1).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.hangtian1).findViewById(R.id.btstop);
		 ItemViewHolder itemViewHolder1=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		 viewmapgGsk.put("hangtian1", itemViewHolder1);
		 
		 no = (TextView)findViewById(R.id.hangtian2).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.hangtian2).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.hangtian2).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.hangtian2).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.hangtian2).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.hangtian2).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.hangtian2).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.hangtian2).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.hangtian2).findViewById(R.id.btstop);
		 ItemViewHolder itemViewHolder2=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		 viewmapgGsk.put("hangtian2", itemViewHolder2);
		 
		 no = (TextView)findViewById(R.id.hangtian3).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.hangtian3).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.hangtian3).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.hangtian3).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.hangtian3).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.hangtian3).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.hangtian3).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.hangtian3).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.hangtian3).findViewById(R.id.btstop);
		 ItemViewHolder itemViewHolder3=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		 viewmapgGsk.put("hangtian3", itemViewHolder3);
		 
		 no = (TextView)findViewById(R.id.hangtian4).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.hangtian4).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.hangtian4).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.hangtian4).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.hangtian4).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.hangtian4).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.hangtian4).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.hangtian4).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.hangtian4).findViewById(R.id.btstop);
		 ItemViewHolder itemViewHolder4=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		 viewmapgGsk.put("hangtian4", itemViewHolder4);
		 
		 
		 no = (TextView)findViewById(R.id.hangtian5).findViewById(R.id.no);
		 ip = (TextView)findViewById(R.id.hangtian5).findViewById(R.id.ip);
		 idcnc = (TextView)findViewById(R.id.hangtian5).findViewById(R.id.idcnc);
		 idandroid = (TextView)findViewById(R.id.hangtian5).findViewById(R.id.idandroid);
		 alarm = (TextView)findViewById(R.id.hangtian5).findViewById(R.id.alarm);
		 run = (TextView)findViewById(R.id.hangtian5).findViewById(R.id.run);
		 spinner=(Spinner)findViewById(R.id.hangtian5).findViewById(R.id.spinner);
		 btstart=(Button) findViewById(R.id.hangtian5).findViewById(R.id.btstart);
		 btstop=(Button) findViewById(R.id.hangtian5).findViewById(R.id.btstop);
		 ItemViewHolder itemViewHolder5=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);
		 viewmapgGsk.put("hangtian5", itemViewHolder5);
		 		 
		 setClickEven();
		 initGskviewIPandNo();
		 initHangtianviewIPandNo();	
		 initGaojingviewIPandNo();
		 
	}


	//���ð�ť����¼�
	private void setClickEven(){
		
		//���� start button
		itemHuazhong.getBtstart().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {					
				startHzThread(currentSpinSelItem_Hz);// start Huazhong data collection thread and save ip to preference				
			}
		});
		
		//huazhong stop button
		itemHuazhong.getBtstop().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				stopHzThread(true);		//�ر��߳�		
			}
		});
		
		//Huazhong spinner click setting 
		final Spinner spinnerHz=itemHuazhong.getSpinner();		
		String[] mItemshz=getResources().getStringArray(R.array.huazhongip);
		ArrayAdapter<String> adapterHz=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItemshz);
		spinnerHz.setAdapter(adapterHz);
		
		spinnerHz.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id 	) {
				currentSpinSelItem_Hz=spinnerHz.getSelectedItem().toString().trim();						
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	
				Toast.makeText(MainActivity.this, "select nothing", Toast.LENGTH_SHORT).show();
			}
		});
		
		/*<<<<==================================>>>>*/
	
		//�߾� start button
	/*	itemGaojing.getBtstart().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				startGjThread(currentSpinSelItem_Gj);
			}
		});
		//gaojing stop button 
		itemGaojing.getBtstop().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				stopGjThread(true);
			}
		});	*/
/*		final Spinner spinnerGj=itemGaojing.getSpinner(); //gaojing spinner
		String[] mItemsGj=getResources().getStringArray(R.array.gaojingip);
		ArrayAdapter<String> adapterGj=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, mItemsGj);
		spinnerGj.setAdapter(adapterGj);
		spinnerGj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
				int pos, long id) {
				currentSpinSelItem_Gj=spinnerGj.getSelectedItem().toString().trim();				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}
		});	*/
		
		for(int i=0;i<gaojingIpArray.length;i++){
			String no=null; //label
			final String strItem=gaojingIpArray[i];
			if(strItem!=null && !"".equals(strItem.trim())){
				no=strItem.substring(0, strItem.indexOf(':'));
				ItemViewHolder itemViewHolder= viewmapgGsk.get(no);
				itemViewHolder.getBtstart().setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						startGjThread(strItem);					
					}
				});				
				itemViewHolder.getBtstop().setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						stopGjThread(strItem,true);
					}
				});
			}
		}
		
		/*<<<<==================================>>>>*/
		//���� click event 
		for(int i=0;i< gskIpArray.length;i++){
			String no=null;
			final String strItem=gskIpArray[i];
			if(strItem!=null && !"".equals(strItem.trim())){
				no=strItem.substring(0, strItem.indexOf(':'));
				ItemViewHolder itemViewHolder= viewmapgGsk.get(no);
				itemViewHolder.getBtstart().setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						startGskThread(strItem);						
					}
				});				
				itemViewHolder.getBtstop().setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						stopGskThread(strItem,true);
					}
				});						
			}
		}
		
		/*<<<<================��������==================>>>>*/
		for(int i=0; i<hangtianIpArray.length;i++){
			String no=null;
			final String strNOIP=hangtianIpArray[i];
			if(strNOIP!=null && !"".equals(strNOIP.trim())){
				no=strNOIP.substring(0, strNOIP.indexOf(':'));
				ItemViewHolder itemViewHolder= viewmapgGsk.get(no);
				itemViewHolder.getBtstart().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startHangtianThread(strNOIP);						
					}					
				});
				itemViewHolder.getBtstop().setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						stopHangtianThread(strNOIP, true);
					}					
				});
			}
		}				
	}
		
	//���������߳�
	private void startHzThread(String spinItem_NOIP){
		String ip=null,no=null;
		
		if(spinItem_NOIP!=null && !"".equals(spinItem_NOIP)){
			 ip=spinItem_NOIP.substring(spinItem_NOIP.indexOf(':')+1);
			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));
			 itemHuazhong.getNo().setText("NO:"+no);
			 itemHuazhong.getIp().setText("IP:"+ip);
			 
			 if(ip!=null && !"".equals(ip)){
				if(currentHZDcObj!=null){
					currentHZDcObj.stopCollect(); //��ֹ֮ǰ���߳�
				}
				currentHZDcObj=new HzDataCollectThread(ip);
				exec.execute(currentHZDcObj);//�����߳�				

				itemHuazhong.getBtstart().setEnabled(false);
				itemHuazhong.getBtstop().setEnabled(true);								
				Log.d(TAG,"startHzThread�����˻������ݲɼ��߳�");
			 }
		}else{
			 currentHZDcObj=null;
			 itemHuazhong.getNo().setText("NO:");
			 itemHuazhong.getIp().setText("IP:");
		}

		editor =pref.edit();
		editor.putString("huazhong", spinItem_NOIP); //�־û�����
		editor.apply();				
	}
	
	//�رջ����߳�
	private void stopHzThread(boolean repref){
		
		if(currentHZDcObj!=null){
			currentHZDcObj.stopCollect(); //�ر����ݲɼ��߳�
			currentHZDcObj=null;

			itemHuazhong.getBtstart().setEnabled(true);
			itemHuazhong.getBtstop().setEnabled(false);
		
			Log.d(TAG,"stopHzThread�ر��˻������ݲɼ��߳�");
			if(repref){
				editor =pref.edit();
				editor.remove("huazhong");
				editor.apply();					
			}		
		}
	}
	
	//start gaojing thread
	private void startGjThread(String spinItem_NOIP){
		String ip=null,no=null ,threadlabel=null;
		
		if(spinItem_NOIP!=null && !"".equals(spinItem_NOIP)){
			 ip=spinItem_NOIP.substring(spinItem_NOIP.indexOf(':')+1);
			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));
			 threadlabel=no;		 
			 if(ip!=null && ip.startsWith("192.168.188.")){
				//�ظ����ӣ�Ҫ��֮ǰ�����ӶϿ�
				if(mapThreadObj.get(no) instanceof GJDataCollectThread){
					((GJDataCollectThread)mapThreadObj.get(no)).stopCollect();
					mapThreadObj.remove(no);
				}
							
				GJDataCollectThread gjDataCollectThread=new GJDataCollectThread(ip,threadlabel);	
				exec.execute(gjDataCollectThread);//�����߳�	
				mapThreadObj.put(no, gjDataCollectThread);
				
				viewmapgGsk.get(no).getBtstart().setEnabled(false);
				viewmapgGsk.get(no).getBtstop().setEnabled(true);
				
				Log.d(TAG,"�����˸߾�����"+spinItem_NOIP+"�ɼ��߳�");				
			 }
		
			editor =pref.edit();
			editor.putString(no, spinItem_NOIP); //�־û�����
			editor.apply();
		}
	}
	
	//stop gaojing thread
	private void stopGjThread(String spinItem_NOIP,boolean repref){
		String no=null;
		if(spinItem_NOIP!=null && !spinItem_NOIP.equals("")){
			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));
			 GJDataCollectThread gjDataCollectThread=(GJDataCollectThread)mapThreadObj.get(no);
			 if(gjDataCollectThread!=null){
				 gjDataCollectThread.stopCollect();
				 mapThreadObj.remove(no);
				 
				 viewmapgGsk.get(no).getBtstart().setEnabled(true);
				 viewmapgGsk.get(no).getBtstop().setEnabled(false);
				 
				 if(repref){
					 editor =pref.edit();
					 editor.remove(no);
					 editor.apply(); 
				 }					 
			 }
		}
	}
	
	//��ʼ�������߾��Ľ���
	private void initGaojingviewIPandNo(){
		ItemViewHolder  itemViewHolder=null;
		String str ,no,ip ;
		if(gaojingIpArray==null){
			return ;
		}
		for(int i=0;i< gaojingIpArray.length;i++){
			str= gaojingIpArray[i];		
			if(str!=null && !"".equals(str.trim())){
				no=str.substring(0, str.indexOf(':'));
				ip=str.substring(str.indexOf(':')+1);
				itemViewHolder=viewmapgGsk.get(no);
				if(itemViewHolder!=null){
					itemViewHolder.getNo().setText("NO:"+no);
					itemViewHolder.getIp().setText("IP:"+ip);
				}				
			}
		}				
	}
	
	//start gsk data collect thread	
	private void startGskThread(String spinItem_NOIP ){
		String ip=null,no=null;
		if(spinItem_NOIP!=null && !spinItem_NOIP.equals("")){
			ip=spinItem_NOIP.substring(spinItem_NOIP.indexOf(':')+1);
			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));//��Ϊ Thread label���ֹ����ɼ��߳�
			 if(ip!=null && ip.startsWith("192.168.188.")){
				 if(mapgskThreadobj.get(no) instanceof GSKDataCollectThread){   //����ڿ����߳��Ѿ����̴߳򿪣���ر�֮ǰ���߳����¿����µ��߳�
					 ((GSKDataCollectThread)mapgskThreadobj.get(no)).stopCollect();
					 mapgskThreadobj.remove(no);
				 }
				 GSKDataCollectThread gskobj=new GSKDataCollectThread(ip,no);
				 mapgskThreadobj.put(no, gskobj);
				 exec.execute(gskobj);
				 
				 viewmapgGsk.get(no).getBtstart().setEnabled(false);
				 viewmapgGsk.get(no).getBtstop().setEnabled(true);
			 }			 
			editor =pref.edit();
			editor.putString(no , spinItem_NOIP); //�־û�����
			editor.apply();				
		}		
	}
	
	//stop gsk data collect thread
	private void stopGskThread(String spinItem_NOIP, boolean repref){
		String no=null;
		if(spinItem_NOIP!=null && !spinItem_NOIP.equals("")){

			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));
			 GSKDataCollectThread gskobj= mapgskThreadobj.get(no) ; //ֹͣ�߳�
			 if(gskobj !=null){
				 gskobj.stopCollect();
				 mapgskThreadobj.remove(no);
				
				 viewmapgGsk.get(no).getBtstart().setEnabled(true);
				 viewmapgGsk.get(no).getBtstop().setEnabled(false);
						
				 if(repref){
					 editor =pref.edit();
					 editor.remove(no);
					 editor.apply(); 
				 }				 
			 } 
		}		
	}	
	
	//��ʼ�������Ľ���
	private void initGskviewIPandNo(){
		String str ,no,ip;
		ItemViewHolder  itemViewHolder=null;
		if(gskIpArray==null){
			return ;
		}
		for(int i=0;i< gskIpArray.length;i++){
			str= gskIpArray[i];			
			if(str!=null && !"".equals(str.trim())){
				no=str.substring(0, str.indexOf(':'));
				ip=str.substring(str.indexOf(':')+1);
				itemViewHolder=viewmapgGsk.get(no);
				if(itemViewHolder!=null){
					itemViewHolder.getNo().setText("NO:"+no);
					itemViewHolder.getIp().setText("IP:"+ip);
				}				
			}
		}				
	}
	
	
	
	//��������
	//���������������ݲɼ��߳�
/*	private void startHangtianThread2(String spinItem_NOIP ){
		String ip=null,no=null;
		if(spinItem_NOIP!=null && !spinItem_NOIP.equals("")){
			ip=spinItem_NOIP.substring(spinItem_NOIP.indexOf(':')+1);
			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));//��Ϊ Thread label���ֹ����ɼ��߳�
			 if(ip!=null && ip.startsWith("192.168.188.")){
				if( mapHangtianThreadObj.get(no) instanceof HangTianDataCollectThread){
					mapHangtianThreadObj.get(no).stopCollect();
					mapHangtianThreadObj.remove(no);
				}
				
				HangTianDataCollectThread hangTianDataCollectThread=new HangTianDataCollectThread(ip, no);
				exec.execute(hangTianDataCollectThread);
				mapHangtianThreadObj.put(no, hangTianDataCollectThread);
				
				viewmapgGsk.get(no).getBtstart().setEnabled(false);
				viewmapgGsk.get(no).getBtstop().setEnabled(true);
			 }
			 
			editor =pref.edit();
			editor.putString(no , spinItem_NOIP); //�־û�����
			editor.apply();	
		}
	}*/
	
	//��������
	//���������������ݲɼ��߳�
	private void startHangtianThread(String spinItem_NOIP ){
		String ip=null,no=null ,threadlabel=null;
		if(spinItem_NOIP!=null && !spinItem_NOIP.equals("")){
			ip=spinItem_NOIP.substring(spinItem_NOIP.indexOf(':')+1);
			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));//��Ϊ Thread label���ֹ����ɼ��߳�
			 threadlabel=no;
			 if(ip!=null && ip.startsWith("192.168.188.")){
				if( mapHangtianThreadObj.get(no) instanceof HangtianDataCollectThreadnew){
					mapHangtianThreadObj.get(no).stopCollect();
					mapHangtianThreadObj.remove(no);
				}
				
				HangtianDataCollectThreadnew hangTianDataCollectThread=new HangtianDataCollectThreadnew(ip, threadlabel);
				exec.execute(hangTianDataCollectThread);
				mapHangtianThreadObj.put(no, hangTianDataCollectThread);
				
				viewmapgGsk.get(no).getBtstart().setEnabled(false);
				viewmapgGsk.get(no).getBtstop().setEnabled(true);
			 }
			 
			editor =pref.edit();
			editor.putString(no , spinItem_NOIP); //�־û�����
			editor.apply();	
		}
	}
	
	//�رպ����������ݲɼ��߳�
/*	private void stopHangtianThread2(String spinItem_NOIP, boolean repref){
		String no=null;
		if(spinItem_NOIP!=null && !spinItem_NOIP.equals("")){

			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));
			 HangTianDataCollectThread hangTianDataCollectThreadObj= mapHangtianThreadObj.get(no);
			 if(hangTianDataCollectThreadObj !=null){
				 hangTianDataCollectThreadObj.stopCollect();
				 mapHangtianThreadObj.remove(no);
				
				 viewmapgGsk.get(no).getBtstart().setEnabled(true);
				 viewmapgGsk.get(no).getBtstop().setEnabled(false);
						
				 if(repref){
					 editor =pref.edit();
					 editor.remove(no);
					 editor.apply(); 
				 }				 
			 }			 
		}
	}*/
	
	//�رպ����������ݲɼ��߳�
	private void stopHangtianThread(String spinItem_NOIP, boolean repref){
		String no=null;
		if(spinItem_NOIP!=null && !spinItem_NOIP.equals("")){

			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));
			 HangtianDataCollectThreadnew hangTianDataCollectThreadObj= mapHangtianThreadObj.get(no);
			 if(hangTianDataCollectThreadObj !=null){
				 hangTianDataCollectThreadObj.stopCollect();
				 mapHangtianThreadObj.remove(no);
				
				 viewmapgGsk.get(no).getBtstart().setEnabled(true);
				 viewmapgGsk.get(no).getBtstop().setEnabled(false);
						
				 if(repref){
					 editor =pref.edit();
					 editor.remove(no);
					 editor.apply(); 
				 }				 
			 }			 
		}
	}
	
	
	//��ʼ���������ؽ���
	private void initHangtianviewIPandNo(){
		ItemViewHolder  itemViewHolder=null;
		if(hangtianIpArray==null){
			return ;
		}
		for(int i=0;i< hangtianIpArray.length;i++){
			String str= hangtianIpArray[i];
			String no=null , ip=null;
			if(str!=null && !"".equals(str.trim())){
				no=str.substring(0, str.indexOf(':'));
				ip=str.substring(str.indexOf(':')+1);
				itemViewHolder=viewmapgGsk.get(no);
				if(itemViewHolder!=null){
					itemViewHolder.getNo().setText("NO:"+no);
					itemViewHolder.getIp().setText("IP:"+ip);
				}				
			}
		}				
	}
	
	
	//ֹͣ�ɼ��������ݲɼ��߳�
	private void offLineAllDatacollectThread(){
		String str;
		//ֹͣ�������ݲɼ�
		stopHzThread(false); //ֹͣ���ݲɼ������ǲ��Ƴ�pref�б���Ĳɼ���ַ
		
		//ֹͣ�߾����ݲɼ�
		for(int i=0;i<gaojingIpArray.length;i++){
			 str=gaojingIpArray[i];
			 stopGjThread(str, false);
		}
				
		//ֹͣ�������ݲɼ�
		for(int i=0;i<gskIpArray.length;i++){
			str= gskIpArray[i];
			stopGskThread(str, false);
		}	
		//ֹͣ�����������ݲɼ��߳�
		for(int i=0;i< hangtianIpArray.length;i++){
			str=hangtianIpArray[i];
			stopHangtianThread(str, false);
		}		
	}

	//��ʱ�������񣬿�ʼ���ݵĲɼ��ͷ���
	class  startTask extends TimerTask{
		@Override
		public void run() {
			
//			String time=formatter.format(Calendar.getInstance().getTime());
			String time=TimeUtil.getSimpleTime(); //��ȡʱ���
			int hour=Integer.parseInt(time.substring(0, time.indexOf(':')));
//			int  minute=Integer.parseInt(time.substring(time.indexOf(':')+1, time.indexOf(':')+3));
			if( hour==7 ){   //7������
				//���������߳�
				if(dataTransmitThread==null){
					dataTransmitThread=new DataTransmitThread();
					exec.execute(dataTransmitThread);
					Log.d(TAG,"startTask���������ݷ����߳�");
				}								
			/*	try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/				
				runOnUiThread(new Runnable() {					
					@Override
					public void run() {						
						startDefaultThread(); //�����ϴιػ�ʱ�������߳�
					}
				});				
			}
			Log.d(TAG,"startTaskִ���˶�ʱ��������");
		}//end run		
	}

	//��ʱ�������ݲɼ��ͷ��Ͷ�����
	class stopTask extends TimerTask{
	
		@Override
		public void run() {
			
//			String time=formatter.format(Calendar.getInstance().getTime());
			String time=TimeUtil.getSimpleTime();
			int hour=Integer.parseInt(time.substring(0, time.indexOf(':')));
//			int  minute=Integer.parseInt(time.substring(time.indexOf(':')+1, time.indexOf(':')+3));
			if( hour>=18 ){  //6������
				
				runOnUiThread(new Runnable() {					
					@Override
					public void run() {						
						offLineAllDatacollectThread(); //��ʱ�������ݲɼ��߳�
					}
				});
								
		/*		try {
					Thread.sleep(1000*30); //3���Ӻ���ֹͣ���ݷ����߳�
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
				
				//�ر����ݷ����̣߳�ֹͣ���ݷ���
				if(dataTransmitThread !=null && dataTransmitThread.getIsCountinueRun()){
					dataTransmitThread.setIsCountinueRun(false);
					dataTransmitThread=null;
				}
			}
			Log.d(TAG,"stopTaskִ���˶�ʱ��������");						
		}		
	}
	
	
	//���ع㲥������
	class  LocalReceiver extends BroadcastReceiver{  	         
		@SuppressWarnings("deprecation")
		@Override  
         public void onReceive(Context context,Intent intent){ 

             if(intent==null) return ;        
			 String action=intent.getAction();
			 Bundle	 bundle= intent.getExtras();
			
             if(action.equals(BroadcastAction.SendThread_PARAMALL)){
	        	  //�������ݵ����в���
            	 String strlocalnum =bundle.getString(BroadcastType.MSGLOCAL);
            	 if(strlocalnum!=null && !"".equals(strlocalnum)){
            		  cachenum.setText(strlocalnum); //��ʾ�������ݿ��л�����������
            	 }
	        	 	        	  
	        	  String strsendcount=bundle.getString(BroadcastType.SENDCOUNT);//��ʾ�ѷ�����������	        	 	        	 
	        	  if(strsendcount!=null &&  !"".equals(strsendcount)){
	        		  sendnum.setText(strsendcount); 
	        	  }
	        	  
	        	  String strspeed=bundle.getString(BroadcastType.SENDSPEED); //��������
	        	  String strspeedcolor=bundle.getString(BroadcastType.SENDColor); //��������������ɫ
	        	  if(strspeed!=null && !"".equals(strspeed)){
	        		  speed.setText(strspeed);
	        		  if(strspeedcolor!=null && strspeedcolor.equals("red")){
	        			  speed.setTextColor(getResources().getColor(R.color.red)); //�������Ϊ��ɫ
	        		  }else{
	        			  speed.setTextColor(getResources().getColor(R.color.black));
	        		  }	        		  
	        	  }	 	        	  
	          }else if(action.equals(BroadcastAction.ACTION_HANGTIAN_RUN_ALARM)){ //�����������Ϣ�ͱ�����Ϣ
	        	  String no=bundle.getString("threadlabel");
	        	  
	        	  String type=bundle.getString("type");
	        	  
	        	  if(BroadcastType.HANGTIAN_RUN.equals(type) && no!=null){
	        		  viewmapgGsk.get(no).getRuninfo().setText("RunInfo: "+bundle.getString(BroadcastType.HANGTIAN_RUN));
	        	  }else if( BroadcastType.HANGTAIN_ALARM.equals(type) && no!=null){
	        		  viewmapgGsk.get(no).getAlarm().setText("Alarm: "+bundle.getString(BroadcastType.HANGTAIN_ALARM));
	        	  }else if( "cncid".equals(type)&& no!=null){
	        		  viewmapgGsk.get(no).getIdcnc().setText("IDCnc: "+bundle.getString("cncid"));
	        	  }else if("androidid".equals(type)&& no!=null){
	        		  viewmapgGsk.get(no).getIdandroid().setText("IDAndroid: "+ bundle.getString("androidid"));
	        	  }	        	  
	          }
        }   
     }
	
}



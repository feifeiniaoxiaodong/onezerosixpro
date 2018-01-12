package com.cnc.daq;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cnc.daqnew.DataTransmitThread;
import com.cnc.daqnew.HandleMsgTypeMcro;
import com.cnc.daqnew.HzDataCollectThread;
import com.cnc.domain.UiDataAlarmRun;
import com.cnc.domain.UiDataNo;
import com.cnc.gaojing.GJDataCollectThread;
import com.example.wei.gsknetclient_studio.GSKDataCollectThread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
	
	SharedPreferences pref;
	 ExecutorService exec=null;
	SharedPreferences.Editor editor ;
	
//	Map<String, ItemViewHolder>  viewmap=new HashMap<>();
	ItemViewHolder itemHuazhong =null,
					itemGaojing=null,
					itemGsk01=null,
					itemGsk02=null,
					itemGsk03=null,
					itemGsk04=null,
					itemGsk05=null;
	TextView  cachenum , delay ,sendnum ,packsize ,speed ;
	

	Map<String, Runnable> threadmap=new HashMap<>();
	

	String currentSpinSelItem_Hz=null,
			currentSpinSelItem_Gj=null;
	
	String current_HZ_NoIP = null;		//Huazhong
	HzDataCollectThread currentHZDcObj=null;
	
	String current_Gj_NoIP= null;
	GJDataCollectThread currentGjDcObj=null;//Gaojing
	
	String current_Gsk_NoIP = null;			//guangzhou shu kong 
	GSKDataCollectThread currentGskDcobj=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		mainActivityHandler=new mainHandler();
		pref= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		exec=Executors.newCachedThreadPool();//线程池
		initViewMap();
		setClickEven();
		startDefaultThread();
		
		//开启发送线程
		DataTransmitThread dataTransmitThread=new DataTransmitThread();
		exec.execute(dataTransmitThread);
		Log.d(TAG,"开启了数据发送线程");

	}
	
	
	@SuppressLint("HandlerLeak") 
	class mainHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandleMsgTypeMcro.HUAZHONG_UINO:  //huazhong cncid and android id
				UiDataNo uidatano=(UiDataNo)msg.obj;
//				itemHuazhong.getNo().setText(uidatano.getNo());
//				itemHuazhong.getIp().setText(uidatano.getIp());
				itemHuazhong.getIdcnc().setText(uidatano.getIdcnc());
				itemHuazhong.getIdandroid().setText(uidatano.getIdandroid());
				
				break;
			case HandleMsgTypeMcro.HUAZHONG_UIALARM: //huazhong running and alarm information
				UiDataAlarmRun uialarmrun=(UiDataAlarmRun)msg.obj;
				itemHuazhong.getAlarm().setText(uialarmrun.getAlarminfo());
				itemHuazhong.getRuninfo().setText(uialarmrun.getRuninfo());
				
				break;	
			case HandleMsgTypeMcro.GAOJING_UINO: //Gaojing cncid and android id
				UiDataNo gjdatano=(UiDataNo)msg.obj;
				itemGaojing.getIdcnc().setText(gjdatano.getIdcnc());
				itemGaojing.getIdandroid().setText(gjdatano.getIdandroid());
				break;
			case HandleMsgTypeMcro.GAOJING_UIALARM://Gaojing running and alarm information
				UiDataAlarmRun gjUiDataAlarmRun=(UiDataAlarmRun)msg.obj;
				itemGaojing.getAlarm().setText(gjUiDataAlarmRun.getAlarminfo());
				itemGaojing.getRuninfo().setText(gjUiDataAlarmRun.getRuninfo());
				break;
			case HandleMsgTypeMcro.GSK01:
				break;
			case HandleMsgTypeMcro.GSK02:
				break;
			case HandleMsgTypeMcro.GSK03:
				break;
			case HandleMsgTypeMcro.GSK04:
				break;
			case HandleMsgTypeMcro.GSK05:
				break;
				
			//处理数据发送信息
			case HandleMsgTypeMcro.MSG_DELAYTIME://延时时间
				String str =(String )msg.obj;
				String[] strs= str.split(":");
				delay.setText("delay: "+strs[0]); //
				packsize.setText("packSize:"+strs[1]);
				speed.setText("speed:"+strs[2]); 
				
				break;
			case HandleMsgTypeMcro.MSG_COUNTRUN: //CacheNum
				String strcache =(String)msg.obj;
				cachenum.setText("cacheNum:"+strcache+"条");
				break;
			default:
				break;
			}
	
		}
	}
	
	/**
	 * 开启上次开启的线程，单厂家只开启一台
	 */
	private void startDefaultThread(){
		//华中线程
		String preip = pref.getString("huazhong", null);
		if(preip!=null && !preip.trim().equals("")){
			//开启线程
			startHzThread(preip);
		}
	}
	
	
	public static Handler getMainActivityHandler() {
		return mainActivityHandler;
	}
	
	
	class ItemViewHolder{
		TextView  no,ip,
				 idcnc,
				 idandroid,
				 alarm,
				 runinfo;
		Spinner   spinner;
		Button    btstart ,btstop;

		public ItemViewHolder() {		
		}

		public ItemViewHolder(TextView no, TextView ip, TextView idcnc,
				TextView idandroid, TextView alarm, TextView runinfo,
				Spinner spinner, Button btstart, Button btstop) {
			
			this.no = no;
			this.ip = ip;
			this.idcnc = idcnc;
			this.idandroid = idandroid;
			this.alarm = alarm;
			this.runinfo = runinfo;
			this.spinner = spinner;
			this.btstart = btstart;
			this.btstop = btstop;
		}


		public TextView getIp() {
			return ip;
		}

		public TextView getNo() {
			return no;
		}

		public TextView getIdcnc() {
			return idcnc;
		}

		public TextView getIdandroid() {
			return idandroid;
		}

		public TextView getAlarm() {
			return alarm;
		}

		public TextView getRuninfo() {
			return runinfo;
		}

		public Spinner getSpinner() {
			return spinner;
		}

		public Button getBtstart() {
			return btstart;
		}

		public Button getBtstop() {
			return btstop;
		}	
	}
	

	//init all view  components id
	private void initViewMap(){
		
		cachenum=(TextView)findViewById(R.id.txcachenum);
		delay =(TextView)findViewById(R.id.txdelay);
		sendnum=(TextView)findViewById(R.id.txsendno);
		packsize=(TextView)findViewById(R.id.txpakagesize);
		speed=(TextView)findViewById(R.id.txspeed);
	
		Log.d(TAG,"initViewMap");
		TextView no = (TextView)findViewById(R.id.gaojing).findViewById(R.id.no);
		TextView ip = (TextView)findViewById(R.id.gaojing).findViewById(R.id.ip);
		TextView idcnc = (TextView)findViewById(R.id.gaojing).findViewById(R.id.idcnc);
		TextView idandroid = (TextView)findViewById(R.id.gaojing).findViewById(R.id.idandroid);
		TextView alarm = (TextView)findViewById(R.id.gaojing).findViewById(R.id.alarm);
		TextView run = (TextView)findViewById(R.id.gaojing).findViewById(R.id.run);
		Spinner  spinner=(Spinner)findViewById(R.id.gaojing).findViewById(R.id.spinner);
		Button btstart=(Button) findViewById(R.id.gaojing).findViewById(R.id.btstart);
		Button btstop=(Button) findViewById(R.id.gaojing).findViewById(R.id.btstop);		
		itemGaojing=new ItemViewHolder(no, ip, idcnc, idandroid, alarm, run, spinner, btstart, btstop);

		
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
//		 viewmap.put("huazhong", itemViewHolder);
		
//		viewmap.get("gaojing").getNo().setText("No:高精");
//		viewmap.get("huazhong").getNo().setText("No:华中");
		
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
		 
	}


	private void setClickEven(){
		
		//华中 start button
		itemHuazhong.getBtstart().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {					
					startHzThread(currentSpinSelItem_Hz);// start Huazhong data collection thread and save ip to preference				
				}
			});
//		huazhong stop button
		itemHuazhong.getBtstop().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopHzThread();		//关闭线程		
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
	
		//高精 start button
		itemGaojing.getBtstart().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startGjThread(currentSpinSelItem_Gj);
			}
		});
		//gaojing stop button 
		itemGaojing.getBtstop().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				stopGjThread();
			}
		});	
		final Spinner spinnerGj=itemGaojing.getSpinner(); //gaojing spinner
		String[] mItemsGj=getResources().getStringArray(R.array.gaojingip);
		ArrayAdapter<String> adapterGj=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, mItemsGj);
		spinnerGj.setAdapter(adapterGj);
		spinnerGj.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				currentSpinSelItem_Gj=spinnerGj.getSelectedItem().toString().trim();				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub				
			}
		});	
		
		/*<<<<==================================>>>>*/
		
		
	}
	
	
	//开启华中线程
	private void startHzThread(String spinItem_NOIP){
		String ip=null,no=null;
		
		if(spinItem_NOIP!=null && !"".equals(spinItem_NOIP)){
			 ip=spinItem_NOIP.substring(spinItem_NOIP.indexOf(':')+1);
			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));
			 itemHuazhong.getNo().setText("No:"+no);
			 itemHuazhong.getIp().setText("IP:"+ip);
			 
			 if(ip!=null && !"".equals(ip)){
				currentHZDcObj=new HzDataCollectThread(ip);
				current_HZ_NoIP=spinItem_NOIP; 
				exec.execute(currentHZDcObj);//开启线程				

				itemHuazhong.getBtstart().setEnabled(false);
				itemHuazhong.getBtstop().setEnabled(true);
				Log.d(TAG,"开启了华中数据采集线程");
			 }
		}else{
			 currentHZDcObj=null;
			 current_HZ_NoIP=null;
			 itemHuazhong.getNo().setText("No:");
			 itemHuazhong.getIp().setText("IP:");
		}

		editor =pref.edit();
		editor.putString("huazhong", spinItem_NOIP); //持久化保存
		editor.apply();
		
	}
	
	//关闭华中线程
	private void stopHzThread(){
		
		if(currentHZDcObj!=null){
			currentHZDcObj.stopCollect(); //关闭数据采集线程
			current_HZ_NoIP=null;
			currentHZDcObj=null;
			itemHuazhong.getBtstart().setEnabled(true);
			itemHuazhong.getBtstop().setEnabled(false);
			Log.d(TAG,"关闭了华中数据采集线程");
		}
	}
	//start gaojing thread
	private void startGjThread(String spinItem_NOIP){
		String ip=null,no=null;
		
		if(spinItem_NOIP!=null && !"".equals(spinItem_NOIP)){
			 ip=spinItem_NOIP.substring(spinItem_NOIP.indexOf(':')+1);
			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));
			 itemGaojing.getNo().setText("No:"+no);
			 itemGaojing.getIp().setText("IP:"+ip);
			 
			 if(ip!=null && !"".equals(ip)){
				currentGjDcObj=new GJDataCollectThread(ip);					
				current_Gj_NoIP=spinItem_NOIP; 
				exec.execute(currentGjDcObj);//开启线程	
				
				itemGaojing.getBtstart().setEnabled(false);
				itemGaojing.getBtstop().setEnabled(true);
				Log.d(TAG,"开启了高精数据采集线程");
			 }
		}else{
			currentGjDcObj=null;
			current_Gj_NoIP=null;
			itemGaojing.getNo().setText("No:");
			itemGaojing.getIp().setText("IP:");
		}

		editor =pref.edit();
		editor.putString("gaojing", spinItem_NOIP); //持久化保存
		editor.apply();
	}
	//stop gaojing thread
	private void stopGjThread(){
		if(currentGjDcObj!=null){
			currentGjDcObj.stopCollect(); //关闭数据采集线程
			current_Gj_NoIP=null;
			currentGjDcObj=null;
			
			itemGaojing.getBtstart().setEnabled(true);
			itemGaojing.getBtstop().setEnabled(false);
			Log.d(TAG,"关闭了高精数据采集线程");
		}
	}
	
	
	
}
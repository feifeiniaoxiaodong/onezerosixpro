package com.cnc.daq;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.SimpleDateFormat;
import com.cnc.daqnew.DataTransmitThread;
import com.cnc.daqnew.HandleMsgTypeMcro;
import com.cnc.daqnew.HzDataCollectThread;
import com.cnc.domain.UiDataAlarmRun;
import com.cnc.domain.UiDataNo;
import com.cnc.gaojing.GJDataCollectThread;
import com.cnc.test.TestGJMultiThread;
import com.example.wei.gsknetclient_studio.GSKDataCollectThread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;

import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
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
	private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");//ʱ�����ʽ
	private SharedPreferences pref;	
	private SharedPreferences.Editor editor ;
	ExecutorService exec=null;
	
	Map<String, ItemViewHolder>  viewmapgGsk=new HashMap<>();
	
	ItemViewHolder itemHuazhong =null,
					itemGaojing=null;
	
	TextView  cachenum , delay ,sendnum ,packsize ,speed ;
	
//	Map<String, Runnable> threadmap=new HashMap<>();
	DataTransmitThread dataTransmitThread=null; //���ݷ����߳�
	String  currentSpinSelItem_Hz=null,
			currentSpinSelItem_Gj=null;
	
	String current_HZ_NoIP = null;		//Huazhong
	HzDataCollectThread currentHZDcObj=null;
	
	String current_Gj_NoIP= null;
	GJDataCollectThread currentGjDcObj=null;//Gaojing
	
	String [] gskIpArray=null; //gsk ip list
	String current_Gsk_NoIP = null;			//guangzhou shu kong 
	GSKDataCollectThread  currentGskDcobj_1=null;
	Map<String ,GSKDataCollectThread>  mapgskThreadobj=new HashMap<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		mainActivityHandler=new mainHandler();
		pref= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		exec=Executors.newCachedThreadPool();//�̳߳�
		gskIpArray =getResources().getStringArray(R.array.gskip);   //����ip��ַ
		
		initViewMap();
		
		startTask(); //�������ݲɼ��ͷ����߳�

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
								
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				runOnUiThread(new Runnable() {				
					@Override
					public void run() {
						startDefaultThread(); //�����ϴιػ�ʱ�������߳�						
					}
				});
				
				//���ö�ʱ��������
				Timer timer=new Timer(false);
//				timer.scheduleAtFixedRate(new startTask(), 1000*60*5, 1000*60*6*1);//ÿ��55����ִ��һ������
//				timer.scheduleAtFixedRate(new stopTask(), 1000*60*2, 1000*60*6*1); //���ԣ��Զ����ߺ����߻᲻�ᵼ������
				
				timer.scheduleAtFixedRate(new startTask(), 1000*60*5, 1000*60*55*1);//ÿ��55����ִ��һ������
				timer.scheduleAtFixedRate(new stopTask(), 1000*60*2, 1000*60*55*1); //���ԣ��Զ����ߺ����߻᲻�ᵼ������
						
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
				itemGaojing.getIdcnc().setText(gjdatano.getIdcnc());
				itemGaojing.getIdandroid().setText(gjdatano.getIdandroid());
				break;
			case HandleMsgTypeMcro.GAOJING_UIALARM:      //Gaojing running and alarm information
				UiDataAlarmRun gjUiDataAlarmRun=(UiDataAlarmRun)msg.obj;
				itemGaojing.getAlarm().setText(gjUiDataAlarmRun.getAlarminfo());
				itemGaojing.getRuninfo().setText(gjUiDataAlarmRun.getRuninfo());
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
			
			//�������ݷ�����Ϣ
			case HandleMsgTypeMcro.MSG_DELAYTIME://��ʱʱ��
				String str =(String )msg.obj;
				String[] strs= str.split(":");
				delay.setText("delay: "+strs[0]); //
				packsize.setText("packSize:"+strs[1]);
				speed.setText("speed:"+strs[2]); 
				
				break;
			case HandleMsgTypeMcro.MSG_COUNTRUN: //CacheNum
				String strcache =(String)msg.obj;
				cachenum.setText("cacheNum:"+strcache+"��");
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
			
		//�߾�
		String preipGj=pref.getString("gaojing", null);
		if(preipGj!=null && !preipGj.equals("") ){
			startGjThread(preipGj);
		}
		
		for(int i=0;i<gskIpArray.length;i++){
			String str= gskIpArray[i];
			String no=null , preNoip=null;
			if(str!=null && !"".equals(str.trim())){
				no=str.substring(0, str.indexOf(':'));
				preNoip=pref.getString(no, null);
				
				if(preNoip!=null ){ //�������߳�ʱӦ��֤���̻߳�δ����
					startGskThread(preNoip);//�����������ݲɼ��߳�				
				}
			}
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
		
	ItemViewHolder	itemGsk01=null,
					itemGsk02=null,
					itemGsk03=null,
					itemGsk04=null,
					itemGsk05=null;
			
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
		
//		viewmap.get("gaojing").getNo().setText("No:�߾�");
//		viewmap.get("huazhong").getNo().setText("No:����");
		
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
		 
		 setClickEven();
		 initGskviewIPandNo();
				
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
//		huazhong stop button
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
				stopGjThread(true);
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
				currentSpinSelItem_Gj=spinnerGj.getSelectedItem().toString().trim();				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}
		});	
		
		//���� click event 
		for(int i=0;i< gskIpArray.length;i++){
			String no;
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
		String ip=null,no=null;
		
		if(spinItem_NOIP!=null && !"".equals(spinItem_NOIP)){
			 ip=spinItem_NOIP.substring(spinItem_NOIP.indexOf(':')+1);
			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));
			 itemGaojing.getNo().setText("NO:"+no);
			 itemGaojing.getIp().setText("IP:"+ip);
			 
			 if(ip!=null && !"".equals(ip)){
				if(currentGjDcObj!=null){
					currentGjDcObj.stopCollect(); //��ֹ֮ǰ�߳�
				}
				currentGjDcObj=new GJDataCollectThread(ip);	
				exec.execute(currentGjDcObj);//�����߳�	
				
				itemGaojing.getBtstart().setEnabled(false);
				itemGaojing.getBtstop().setEnabled(true);
				Log.d(TAG,"startGjThread�����˸߾����ݲɼ��߳�");
			 }
		}else{
			currentGjDcObj=null;
			itemGaojing.getNo().setText("NO:");
			itemGaojing.getIp().setText("IP:");			
		}

		editor =pref.edit();
		editor.putString("gaojing", spinItem_NOIP); //�־û�����
		editor.apply();
	}
	
	//stop gaojing thread
	private void stopGjThread(boolean repref){
		if(currentGjDcObj!=null){
			currentGjDcObj.stopCollect(); //�ر����ݲɼ��߳�
			currentGjDcObj=null;
		
			itemGaojing.getBtstart().setEnabled(true);
			itemGaojing.getBtstop().setEnabled(false);
			
			Log.d(TAG,"stopGjThread�ر��˸߾����ݲɼ��߳�");
			if( repref){
				editor =pref.edit();
				editor.remove("gaojing");
				editor.apply();	
			}				
		}
	}
	
	//start gsk data collect thread	
	private void startGskThread(String spinItem_NOIP ){
		String ip=null,no=null;
		if(spinItem_NOIP!=null && !spinItem_NOIP.equals("")){
			ip=spinItem_NOIP.substring(spinItem_NOIP.indexOf(':')+1);
			 no=spinItem_NOIP.substring(0, spinItem_NOIP.indexOf(':'));
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
		ItemViewHolder  itemViewHolder=null;
		if(gskIpArray==null){
			return ;
		}
		for(int i=0;i< gskIpArray.length;i++){
			String str= gskIpArray[i];
			String no=null , ip=null;
			if(str!=null && !"".equals(str.trim())){
				no=str.substring(0, str.indexOf(':'));
				ip=str.substring(str.indexOf(':')+1);
				itemViewHolder=viewmapgGsk.get(no);
				itemViewHolder.getNo().setText("NO:"+no);
				itemViewHolder.getIp().setText("IP:"+ip);
			}
		}
	}
	//ֹͣ�ɼ��������ݲɼ��߳�
	private void offLineAllDatacollectThread(){
		//ֹͣ�������ݲɼ�
		stopHzThread(false); //ֹͣ���ݲɼ������ǲ��Ƴ�pref�б���Ĳɼ���ַ
		//ֹͣ�߾����ݲɼ�
		stopGjThread(false);
		//ֹͣ�������ݲɼ�
		for(int i=0;i<gskIpArray.length;i++){
			String str= gskIpArray[i];
			stopGskThread(str, false);
		}	
	}

	//��ʱ�������񣬿�ʼ���ݵĲɼ��ͷ���
	class  startTask extends TimerTask{
		@Override
		public void run() {
			
			String time=formatter.format(Calendar.getInstance().getTime());
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
					Thread.sleep(1000);
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
			
			String time=formatter.format(Calendar.getInstance().getTime());
			int hour=Integer.parseInt(time.substring(0, time.indexOf(':')));
			int  minute=Integer.parseInt(time.substring(time.indexOf(':')+1, time.indexOf(':')+3));
			if( hour>=18  ){  //6������
				
				runOnUiThread(new Runnable() {					
					@Override
					public void run() {						
						offLineAllDatacollectThread(); //��ʱ�������ݲɼ��߳�
					}
				});
								
			/*	try {
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
}



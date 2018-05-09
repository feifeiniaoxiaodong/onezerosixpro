package com.cnc.gaojing;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

//import com.cnc.daq.DaqActivity;
import com.cnc.daq.DaqData;
import com.cnc.daq.MainActivity;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.DataType;
import com.cnc.domain.UiDataAlarmRun;
import com.cnc.domain.UiDataNo;
import com.cnc.gaojing.ndkapi.GJApiFunction;
import com.cnc.huazhong.dc.CommonDataCollectThreadInterface;
import com.cnc.mainservice.DelMsgService;
import com.cnc.net.datasend.HandleMsgTypeMcro;
import com.cnc.utils.AlarmFilterList;

import com.cnc.utils.SaveRunTime;
import com.cnc.utils.TimeUtil;

//import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 沈阳高精数据采集线程
 * @author wei
 *
 */
 public class GJDataCollectThread implements Runnable,CommonDataCollectThreadInterface {

	private final String TAG="gaojingDataCollectThread";
	 
    private volatile  boolean  threadflag=true; //线程循环执行标志，改为false时，线程退出循环，线程结束运行
	boolean boolGetMacInfo = false; 			//标识是否得到机床的基本信息	
	int     count = 1;     						//存储运行信息的id,标识这是第几次采集信息
		 
	private Handler delMsgHandler =null,
					mainActivityHandler=null;
		
	final private String  machineIP ;					//= "192.168.188.132"; //机床的IP地址
	final private String machine_SN ;					//数控系统ID，沈阳高精没有提供数控系统ID，暂用IP代替ID
	private AlarmFilterList   alarmFilterList =null;	//报警信息缓存过滤对象	
	private GJApiFunction gjApiFunction =null;			//高精DNC接口驱动
	String threadlabel =null;  //线程标记
	
	public GJDataCollectThread(String ip,String label){
		this(ip,0);
		this.threadlabel=label;
	}
	
	private GJDataCollectThread(String ip,int port){
		machineIP=ip;
		machine_SN="G"+ip;
		delMsgHandler=DelMsgService.getHandlerService();
		mainActivityHandler=MainActivity.getMainActivityHandler();
		this.alarmFilterList=new AlarmFilterList(delMsgHandler);
	}
			
	@Override
	public void run() {
		
		long starttime=Calendar.getInstance().getTimeInMillis(); //记录下线程开启的时间
		dnc.main dncmain =new dnc.main();
		
		gjApiFunction =new GJApiFunction(dncmain);//创建API工具类对象
		//建立连接
		dncmain.connectToNC(machineIP);
		//必须先连接机床才能检测到nml文件
		if(dncmain.status_nml==null){   //检查是否存在nml配置文件
			Log.d(TAG,"高精找不到nml配置文件");			
		}
		
		while(threadflag){
			
			if(!dnc.main.getConnnectState()){ //检测连接状态
				//重新连接				
				dncmain.connectToNC(machineIP);  //连接到机床
			
				if(dnc.main.getConnnectState()  && dncmain.status_nml!=null){ 
					sendMsg2Main("高精连接机床成功", HandleMsgTypeMcro.MSG_ISUCCESS); //					
				}else{
					sendMsg2Main("高精连接机床失败", HandleMsgTypeMcro.MSG_LFAILURE); //
					Log.d(TAG,"找不到nml配置文件");
					 //连接失败，一分钟后再连接
					try{
						Thread.sleep(1000*30);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else{ 				
				//已连接,采集数据
				dncmain.updateStatus();		//更新本地数据
				if(dncmain.msg != null)		//有数据更新
                {					
					daq(); 			 		//采集数据，发送到service去处理						
					dncmain.msg=null;		//清空msg标志
                }else{
                	Log.i(TAG,"dnc.main.msg为空！");
                }				
			}
						
			try {
				Thread.sleep(1000);//采集数据间隔时间设置为1S,因为采集过程耗时大约300毫秒，所以设置为700
			} catch (InterruptedException e) {				
				e.printStackTrace();
			} 	
		}//end while()
		
		//本地保存累计加工时间和开机时间
		starttime=Calendar.getInstance().getTimeInMillis()-starttime;
		SaveRunTime.saveOnTime(machineIP+"ontime", starttime/1000);
//		SaveRunTime.saveOnTime(machineIP+"runtime",  starttime/1000);
			
		//退出线程时断开连接，释放资源
		if(dnc.main.getConnnectState()){
			dncmain.disconnectToNC();		
		}
				
	} //end run()
		
	/**
	 * 数据采集函数
	 */
	private void daq() {
		//开始采集信息的各种事件，时间戳
		String strTime = TimeUtil.getTimestamp();
		
		if(!boolGetMacInfo)   //如果没有获得过机床的基本信息
		{
//			DaqData.setCncid(machineIP); //用IP地址代替机床的ID
			//注册信息
			DataReg dataReg = gjApiFunction.getDataReg();//获取注册信息					
			dataReg.setTime(strTime);		//设置采集的时间戳
			dataReg.setId(machine_SN);      //设置ID号
		
			DaqData.saveDataReg(dataReg); //保存注册信息
			long ontime=SaveRunTime.getOnTime(machineIP+"ontime");//开机时间
//			long runtime=SaveRunTime.getOnTime(machineIP+"runtime");//加工时间	
			long runtime=ontime;
			DataLog datalog=new DataLog(machine_SN,
					ontime,  
					runtime,
					strTime);									
			DaqData.saveDataLog(datalog);//保存登录信息
			
			UiDataNo uiDataNo=new UiDataNo("",machineIP,machine_SN , DaqData.getAndroidId());
			uiDataNo.setThreadlabel(threadlabel);
			sendMsg(mainActivityHandler,uiDataNo,HandleMsgTypeMcro.GAOJING_UINO,0,0); //发送消息到主界面，显示IP地址信息
			
			boolGetMacInfo = true;//置为true，表明已经得到了机床基本信息
			
		}else{   //采集运行信息和报警信息
			StringBuilder  sbalram=new StringBuilder();//报警信息
			
			//采集报警信息
			LinkedList<DataAlarm> listDataAlarm =gjApiFunction.getDataAlarm();
			for (DataAlarm dataAlarm : listDataAlarm) {
				dataAlarm.setId(machine_SN);//设置数控系统ID
				dataAlarm.setTime(strTime);//设置采集的时间戳
				sbalram.append(dataAlarm.getCtt()).append(":");
			}
			
			//如果采集到的报警信息不为零或者已有的报警信息不为零，那么就要对报警信息进行分析
			//对报警信息进行处理,必须要判断报警信息的来到是发生报警还是解除报警，这个分析过程留到主线程中
			if ((listDataAlarm.size() != 0)||(!alarmFilterList.getNowAlarmList().isEmpty())){
				alarmFilterList.saveCollectedAlarmList(listDataAlarm);
			}
			
			//采集运行信息
			DataRun dataRun =  gjApiFunction .getDataRun();
			dataRun.setId(machine_SN); //设置数控系统ID
			dataRun.setTime(strTime); //加时间戳
			sendMsg2Main(dataRun,HandleMsgTypeMcro.MSG_RUN,count);
					
			count++;//采集次数记录
			if(count == Integer.MAX_VALUE)//达到最大值的时候记得清零
				count = 1;	
			
			//发送到主线程
			UiDataAlarmRun uiDataAlarmRun=new UiDataAlarmRun(sbalram.toString(), dataRun.toString());
			uiDataAlarmRun.setThreadlabel(threadlabel);
			sendMsg(mainActivityHandler, uiDataAlarmRun, HandleMsgTypeMcro.GAOJING_UIALARM	, 0, 0);
		}
	}//end daq()
		
	/**
	 * 发送消息到主线程
	 * @param obj
	 * @param what
	 */
	private  void sendMsg2Main(Object obj, int what) {	
		sendMsg(delMsgHandler, obj, what,0,0);
	}
	
	/**
	 * 发送消息到主线程
	 */
	private  void sendMsg2Main(Object obj, int what, int arg1) {
		sendMsg(delMsgHandler, obj, what, arg1, 0);
	}

	/**
	 * 发送消息，通用型
	 * @param handler
	 * @param obj
	 * @param what
	 * @param arg1
	 * @param arg2
	 */
	private  void sendMsg(Handler handler,Object obj, int what, int arg1, int arg2){ 	
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		handler.sendMessage(msg);
	}
	
	/**
	 * 读取Resource.properties 的值
	 * @param key
	 * @return
	 */
/*	private String  getPath(String key){
		InputStream in=null;
		Properties pro=null;
		String path=null;
		
		pro =new Properties();
		in=GJDataCollectThread.class.getResourceAsStream(DataType.pathResource);
		
		try {
			pro.load(new InputStreamReader(in,"utf-8"));
			path=pro.getProperty(key);
			
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}finally{			
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}						
		}
		return path;
	}*/

	@Override
	public void stopCollect() {		
		threadflag=false;
	}

	@Override
	public boolean isThreadRunning() {
		  return threadflag;
	}

}

package com.cnc.net.datasend;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.cnc.broadcast.BroadcastAction;
import com.cnc.broadcast.BroadcastType;
import com.cnc.daq.DaqData;
import com.cnc.daq.MainActivity;
import com.cnc.daq.MyApplication;
import com.cnc.db.service.DBService;
import com.cnc.db.service.DataAlarmPlus;
import com.cnc.db.service.DataRunPlus;
import com.cnc.domain.DataDelayTime;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.DataType;
import com.cnc.domain.GeneralData;
import com.cnc.net.service.Post;
import com.cnc.utils.JsonUtil;
import com.cnc.utils.TimeUtil;

/**
 * 数据发送线程
 * @author wei
 *	
 */
public class DataTransmitThread implements Runnable{
	
	private final String TAG="DataTransmitThread...";
	private  volatile boolean  isCountinueRun=true;
   	boolean isRegister = false; //注册标志  
   	boolean isLogin=false; 		//登录登出标志
	String   path="http://47.94.200.41:8080/DataPush/rec";  //远端服务器地址
	
//	String path ="http://47.94.200.41:8080/DataPush/rec";//服务器的链接路径 ，阿里云服务器地址 
//	final private String path ="http://192.168.1.102:8080/DataPush/rec";//服务器的链接路径    	
//	final private String path ="http://111.198.20.237:7000/DataInputServiceTest/pushData";//服务器的链接路径，史胜阳服务器地址
		
	final String resultofPost = "ac"; 
	final String resultofExcep = "ero";
	
	private DBService dbservice=null;     		//SQLite数据库操作对象
//	private Handler   daqActvityHandler=null;  //Activity的Handler

	//单次发送数据条数
	int[]   keys={1,5,10,15,30,50,60,70,80,90,100,120,140,160,180,200,220,240,260,280,300};
	int  	pointer=0,
			len=keys.length;
	
	private  int nMsgSend=5 ; //单次发送数据条数，随着延时时间的变化进行动态调整
	
	public DataTransmitThread(){
		dbservice=DBService.getInstanceDBService();		
	}
	
/*	public DataTransmitThread(Handler handler) {
		daqActvityHandler=handler;
		dbservice=DBService.getInstanceDBService();
		
//		path=getPath("path"); //初始化远端服务器地址
	}*/

	//只要这个线程打开就一直向外发送信息，不用登录和注册都可以发送运行信息和报警信息
//	@Override
	public void run() {
//		DataDelayTime  delayTime=null;//延时时间对象声明
		String res = null;
//		long  countRun=0; //未发送的运行信息的条数
	
//		String sPath=getPath(HandleMsgTypeMcro.SERVICES_ALIYUN); //初始化远端服务器地址
		String sPath=getPath(HandleMsgTypeMcro.SERVICES_YANG); //初始化远端服务器地址
		if(sPath!=null){
			path=sPath;
		}		
		while(isCountinueRun) //如果注册信息与运行信息有消息，那就一直发送
		{

			DataReg dataReg=null;
			if((dataReg=DaqData.getDataReg()) instanceof DataReg){

				GeneralData generalData = new GeneralData();
				generalData.setDid(DataType.DataReg); //注册信息
				if(dataReg.getId()!=null) {
					generalData.setDt(JsonUtil.object2Json(dataReg));
					res = Post.sendData(path, JsonUtil.object2Json(generalData));//发送注册信息					
                    if(resultofPost.equals(res))
                    {
                    	DaqData.delDataReg(dataReg);
                        Log.d(TAG, "注册成功");
                    }
                    else {
                        Log.d(TAG,"注册失败！");
                    }						
				}					
			}

			DataLog dataLog=null;		
			if((dataLog=DaqData.getDataLog()) instanceof DataLog){
				
				GeneralData generalData2 = new GeneralData();
				generalData2.setDid(DataType.DataLog); //数据类型->登录登出信息
				if(dataLog.getId()!=null){
					generalData2.setDt(JsonUtil.object2Json(dataLog));
					res = Post.sendData(path, JsonUtil.object2Json(generalData2));//发送登录登出信息

                    if(resultofPost.equals(res))
                    {
                    	DaqData.delDataLog(dataLog);
                        Log.d(TAG, "登录登出成功");
                    }
                    else {
                        Log.d(TAG,"登录登出失败！");
                    }	
				}
			}
			
											
			//报警信息										
			sendAlarmInfo();
			
			//发送运行信息
//			sendRunInfo();	
			sendRunInfoZhi();
			//发送间隔
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}			
		} //end while()	

	} //end run()
	
	/**
	 * 发送报警信息
	 * 如果进行多线程发送的话需要对该方法进行同步,因为函数有访问共享资源
	 */
	private void sendAlarmInfo(){
		DataDelayTime  delayTime=null;//延时时间对象声明
		long  countAlarm=0; //未发送的运行信息的条数
		
		if((countAlarm=dbservice.getCountAlarmData())>0){
			DataAlarmPlus dataAlarmPlus = dbservice.findFirstRowAlarmDataP();//从Sqlite中读取报警信息
			//将数据打包发送
			GeneralData generalData = new GeneralData();
			generalData.setDid(DataType.DataAlarm); 		//报警信息
			generalData.setDt(JsonUtil.object2Json(dataAlarmPlus.dataAlarm));
			
			delayTime=getDataDelayTimeObj(DataType.DataAlarm,countAlarm,1);

			String res =sendPostGeneral(generalData,delayTime);//发送数据到服务器			
//			String res = Post.sendData(path, JsonUtil.object2Json(generalData));
			//判断是否发送成功						
			if(resultofPost.equals(res))
			{
				dbservice.deleteAlarmData(dataAlarmPlus.id);//发送信息成功后，从数据库中删除该条信息
				Log.d(TAG, "发送报警信息成功！");
				//发送延时时间
				if(delayTime!=null){
					sendDelayTime(delayTime);
				}
			}
			else /*if(resultofExcep.equals(res))*/{	//发送失败
//				login = false;			//发送失败就需要重新发送注册信息
				Log.d(TAG, "发送报警信息失败！");
//				Toast.makeText(MyApplication.getContext(),"发送报警信息失败!", Toast.LENGTH_SHORT).show();
			}			
									
		}	
	}
	
	/**
	 * 发送运行信息
	 * 如果进行多线程发送的话需要对该方法进行同步,因为函数有访问共享资源
	 */
	private void sendRunInfo(){
		DataDelayTime  delayTimeObj=null;//延时时间对象声明
		long  countRun=0; //未发送的运行信息的条数
				
		countRun=dbservice.getCountRunInfo();//检查SQLite数据库中运行信息的条数
			
		//发送特定条数的运行信息
		if(countRun>= keys[pointer = (pointer)%len] ){
			delayTimeObj=getDataDelayTimeObj(DataType.DataDelay,countRun); //获取一个延时信息对象
			  
			if(sendNumRunInfo(keys[pointer],delayTimeObj)){//发送指定条数的运行信息到服务器	
				//发送延时时间，到服务器
				if(delayTimeObj!=null){
					sendDelayTime(delayTimeObj);
				}				
				
				//界面显示发送参数,在这统一发送运行信息参数
				if(delayTimeObj!=null){
					sendBroadCast(BroadcastAction.SendThread_PARAMALL,
							
							BroadcastType.SENDCOUNT,"发送:"+delayTimeObj.getNumofmsg()+"条数据",
							BroadcastType.SENDSPEED,"发送速率:"+Post.speed(delayTimeObj.getDelaytime(), delayTimeObj.getPackagesize()));
				}
			}
			++pointer;
		}		
	}
	
	/**
	 * 动态调整发送运行信息的条数
	 * 根据delaytime,即:回路响应时间RTT,来调整单次发送数据条数
	 * 阈值设置为500ms
	 * if RTT < ＝300 , nMsgSend + 10 ; 
	 * else  if( 300 < RTT <=500)  nMsgSend +5 ;
	 * else if( 500 <RTT <700 )  nMsgSend -5 ;
	 * else  nMsgSend -10;
	 * 
	 * 如果进行多线程发送的话需要对该方法进行同步,因为函数有访问共享资源
	 * @add by wei 
	 * @time 2018/03/11
	 */
	private void sendRunInfoZhi(){
		DataDelayTime  delayTimeObj=null;//延时时间对象
 		if(nMsgSend<1) nMsgSend =5;	//防止发送速率变为0	
//		long countRun=dbservice.getCountRunInfo();//检查SQLite数据库中运行信息的条数,本地缓存信息条数
		long countRun=DaqData.getCacheinfocount();
		//发送特定条数的运行信息，可以根据发送延时进行动态调整		
		if(countRun>=nMsgSend){
			delayTimeObj=getDataDelayTimeObj(DataType.DataDelay,countRun); //获取一个延时信息对象
			if(sendNumRunInfo(nMsgSend,delayTimeObj)){//发送指定条数的运行信息到远程服务器	
				//数据发送成功
				sendDelayTime(delayTimeObj);//发送延时时间，到服务器
				//根据延时时间调整下次数据发送条数
				long delay=delayTimeObj.getDelaytime();//本次数据发送延时时间
				if(delay<=300) { nMsgSend+=15;}
				else if( 300<delay && delay<=400) { nMsgSend+=10;}
				else if(400<delay && delay<=700) {nMsgSend -=15;}
				else { nMsgSend-=25;}
				
				//界面显示发送参数,在这统一发送运行信息参数
				if(delayTimeObj!=null){
					sendBroadCast(BroadcastAction.SendThread_PARAMALL,
							
							BroadcastType.SENDCOUNT,"发送:"+delayTimeObj.getNumofmsg()+"条数据",
							BroadcastType.SENDSPEED,"发送速率:"+Post.speed(delayTimeObj.getDelaytime(), delayTimeObj.getPackagesize()));
				}
			
			}else{
				//发送失败			
				sendBroadCast(BroadcastAction.SendThread_PARAMALL,
						
						BroadcastType.SENDCOUNT,"发送:"+nMsgSend+"条数据",
						BroadcastType.SENDSPEED,"用时:"+delayTimeObj.getDelaytime()+"ms,发送失败",
						BroadcastType.SENDColor,"red");
				//发送失败,减小单次发送的数据量
				if(nMsgSend>10){  
					nMsgSend-=25; //防止发送数据为0条
				}
			}
		}/*else{ //数据条数较少
			sendBroadCast(BroadcastAction.SendThread_PARAMALL,
					BroadcastType.MSGLOCAL,"本地缓存数据:"+countRun+"条");					
		}*/
	}
		
	/**
	 * 发送延时时间
	 * 发送到阿里云服务器
	 * @param dt
	 */
	private void sendDelayTime(DataDelayTime dt){
		//发送延时时间对象到服务器
		String   aliyunpath="http://47.94.200.41:8080/DataPush/rec";  //远端服务器地址
		if(dt!=null && dt.getNumofmsg()>0 ){
			GeneralData generalData = new GeneralData();		
			generalData.setDid(DataType.DataDelay);  //数据类型
			
			generalData.setDt(JsonUtil.object2Json(dt));
			
			String res = null;
//			try {
			res = Post.sendData(aliyunpath,JsonUtil.object2Json(generalData));
//			} catch (SocketTimeoutException e) {				
//				e.printStackTrace();
//			}//发送数据	
			
			//判断是否发送成功						
			if(resultofPost.equals(res)){							
				Log.d(TAG, "发送计时信息成功！");
			}
			else /*if(resultofExcep.equals(res))*/{	//发送失败					
				Log.d(TAG, "发送计时信息失败！");
			}					
		}
	}
	
	/**
	 * 发送n条运行信息
	 * @param n：要发送的数据条数
	 */
	private void sendNumRunInfo(int n){		
		sendNumRunInfo( n, null);
	}
	
	/**
	 * 发送n条运行信息
	 * @param n	：要发送的数据条数
	 * @param delayTime ：记录发送延时的对象
	 */
	private boolean sendNumRunInfo(int n,DataDelayTime delayTime){		
		if(n<1)  return false;	
		StringBuilder  strRun=new StringBuilder(1024*100);
		
		DataRunPlus dataRunPlus = dbservice.findNumRowRunInfo(n);	//从本地数据库中读取n条运行数据	
		GeneralData generalData = new GeneralData();		
		generalData.setDid(DataType.DataRun);  //数据类型->运行数据
		
		if( dataRunPlus.getListRunData().size() == n){  // 那运行数据转化成json格式的字符串
			strRun.append("[");

			for(DataRun dataRun: dataRunPlus.getListRunData()){
				strRun.append(JsonUtil.object2Json(dataRun));
				strRun.append(",");
			}
			strRun.deleteCharAt(strRun.lastIndexOf(","));//删掉最后那个","
			strRun.append("]");
		
			generalData.setDt( strRun.toString());
			
			String result=sendPostGeneral(generalData,delayTime);//发送数据到服务器
			if(delayTime!=null){
				delayTime.setNumofmsg(n); //本次发送信息条数					
			}
			
			if(resultofPost.equals(result))
			{
				dbservice.deleteRunInfo(dataRunPlus.getId(), dataRunPlus.getIdEnd());//发送信息成功后，从数据库中删除该条信息				
				Log.d(TAG, "成功发送"+n+"条运行信息！" );							
				return true;
			}else /*if(resultofExcep.equals(result))*/{ //发送失败
//				login = false;
				Log.d(TAG, "发送"+n+"条运行信息失败！");
				return false;
			}
		}			
		Log.d(TAG, "SQLite数据库读回的运行信息条数与期望不符！！！");
		return false;		
	}
	
	//发送数据，测试延时时间
	private String sendPostGeneral(GeneralData generalData ){					
		return sendPostGeneral(generalData,null);
	}
		
	//发送数据，测试延时时间
	private String sendPostGeneral(GeneralData generalData ,DataDelayTime delayTime ){
		String response=null;
		long  	beforeSecond,
				afterSecond,
				timePeriod;	
		 		
		String  strJson=JsonUtil.object2Json(generalData); //本次发送的字符串
		long  countBytes=strJson.getBytes().length + 5;  //文本长度(byte)
		 
		beforeSecond=System.currentTimeMillis();; //发送之前时刻			
//		try {
		response = Post.sendData(path,strJson ); //发送数据
//		} catch (SocketTimeoutException e) {		
//			e.printStackTrace();
//		}
		afterSecond=System.currentTimeMillis();; //发送之后时刻
				
		timePeriod=afterSecond-beforeSecond; //计算发送所用时长,ms，回路响应时间RTT
		
		long speedn=countBytes*1000/timePeriod; //发送速率 ，单位  Byte/s
		
		if(delayTime!=null){    //设置延时对象的各项参数
			delayTime.setDelaytime(timePeriod);   //延时时间,ms
			delayTime.setPackagesize( countBytes ); //包大小,Bytes
			delayTime.setSpeed(speedn);			  //发送速度,Bytes/s
		}
				
		return response;
	}
	

	//获取延时时间对象	
	private DataDelayTime getDataDelayTimeObj(int dataType, long nOfInfoUnsent ){
		return getDataDelayTimeObj(dataType,nOfInfoUnsent,0);
	}
	
	private DataDelayTime getDataDelayTimeObj(int dataType, long nOfInfoUnsent ,long nOfInfoSent){
		
		
		String dateStamp=TimeUtil.getTimestamp(); //时间戳
		
		DataDelayTime  delaytemp=new DataDelayTime();

		delaytemp.setCncId(DaqData.forthG); //4G运营商
		delaytemp.setTerminalid(DaqData.getAndroidId());
		delaytemp.setDatatype(dataType);    //数据类型
		delaytemp.setNumofmsgunsent(nOfInfoUnsent); //未发送的本地数据
		delaytemp.setTs(dateStamp);       //时间戳
		delaytemp.setNumofmsg(nOfInfoSent); //本次发送数据的条数
		
		return delaytemp;
	}
	
	

	//发送本地广播消息	
	private void sendBroadCast(String action,String ... args){
		Intent intent =new Intent();
		intent.setAction(action);
		if(args.length%2 !=0 ) return ;
		for(int i=0;i<args.length;i+=2){
			intent.putExtra(args[i], args[i+1]);
		}
		LocalBroadcastManager.getInstance(MyApplication.getContext())
							 .sendBroadcast(intent);	
	}
	
		 
	private String  getPath(String key){
		InputStream in=null;
		Properties pro=null;
		String path=null;
		
		pro =new Properties();
		in=DataTransmitThread.class.getResourceAsStream(DataType.pathResource);
		
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
	}
	
	//停止数据发送线程
	public void setIsCountinueRun(boolean flag){
		this.isCountinueRun=flag;
	}
	//获取数据发送线程的状态
	public boolean getIsCountinueRun(){
		return isCountinueRun;
	}
	
}





















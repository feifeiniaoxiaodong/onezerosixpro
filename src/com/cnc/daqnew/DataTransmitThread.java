package com.cnc.daqnew;

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
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cnc.DataBaseService.DBService;
import com.cnc.DataBaseService.DataAlarmPlus;
import com.cnc.DataBaseService.DataRunPlus;
import com.cnc.daq.DaqData;
import com.cnc.daq.MainActivity;
import com.cnc.domain.DataDelayTime;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.DataType;
import com.cnc.domain.GeneralData;
import com.cnc.netService.Post;
import com.cnc.utils.JsonUtil;
import com.cnc.utils.LogLock;
import com.cnc.utils.RegLock;

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
	private Handler   daqActvityHandler=null;  //Activity的Handler
   	private Handler   mainActivityHandler =null;
	
	int[]   keys={1,5,10,15,30,50,60,70,80,90,100};
	int  	pointer=0,
			len=keys.length;
	
	public DataTransmitThread(){
		dbservice=DBService.getInstanceDBService();
		mainActivityHandler=MainActivity.getMainActivityHandler();
	}
	
	public DataTransmitThread(Handler handler) {
		daqActvityHandler=handler;
		dbservice=DBService.getInstanceDBService();
		mainActivityHandler=MainActivity.getMainActivityHandler();
//		path=getPath("path"); //初始化远端服务器地址
	}


	//只要这个线程打开就一直向外发送信息，不用登录和注册都可以发送运行信息和报警信息
//	@Override
	public void run() {
//		DataDelayTime  delayTime=null;//延时时间对象声明
		String res = null;
//		long  countRun=0; //未发送的运行信息的条数
	
		String sPath=getPath(HandleMsgTypeMcro.SERVICES_ALIYUN); //初始化远端服务器地址
//		String sPath=getPath(HandleMsgTypeMcro.SERVICES_YANG); //初始化远端服务器地址
		if(sPath!=null){
			path=sPath;
		}
		
		while(isCountinueRun) //如果注册信息与运行信息有消息，那就一直发送
		{
			synchronized(RegLock.class){
				//发送注册信息
				if(!DaqData.getListDataReg().isEmpty()){
					
					DataReg dataReg= DaqData.getListDataReg().get(0); 
					GeneralData generalData = new GeneralData();
					generalData.setDid(DataType.DataReg); //注册信息
					if(dataReg.getId()!=null) {
						 generalData.setDt(JsonUtil.object2Json(dataReg));
		                 try {
							res = Post.sendData(path, JsonUtil.object2Json(generalData));//发送注册信息
						} catch (SocketTimeoutException e) {							
							e.printStackTrace();
						}	
	                    if(resultofPost.equals(res))
	                    {
	                    	DaqData.getListDataReg().remove(0);
	                        Log.d(TAG, "注册成功");
	                    }
	                    else {
	                        Log.d(TAG,"注册失败！");
	                    }						
					}					
				}
			}
			
			synchronized(LogLock.class){
				if(!DaqData.getListDataLog().isEmpty()){
					DataLog dataLog=DaqData.getListDataLog().get(0);
					GeneralData generalData2 = new GeneralData();
					generalData2.setDid(DataType.DataLog); //数据类型->登录登出信息
					if(dataLog.getId()!=null){
						generalData2.setDt(JsonUtil.object2Json(dataLog));
		                try {
							res = Post.sendData(path, JsonUtil.object2Json(generalData2));//发送登录登出信息
						} catch (SocketTimeoutException e) {							
							e.printStackTrace();
						}	
	                    if(resultofPost.equals(res))
	                    {
	                    	DaqData.getListDataLog().remove(0);
	                        Log.d(TAG, "登录登出成功");
	                    }
	                    else {
	                        Log.d(TAG,"登录登出失败！");
	                    }	
					}
				}
			}
											
			//报警信息										
			sendAlarmInfo();
			
			//发送运行信息
			sendRunInfo();						
			//发送间隔
			try {
				Thread.sleep(500);
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
			}
			else if(resultofExcep.equals(res)){	//发送失败
//				login = false;			//发送失败就需要重新发送注册信息
				Log.d(TAG, "发送报警信息失败！");
			}
			
			//发送延时时间
			sendDelayTime(delayTime);			
		}	
	}
	
	/**
	 * 发送运行信息
	 * 如果进行多线程发送的话需要对该方法进行同步,因为函数有访问共享资源
	 */
	private void sendRunInfo(){
		DataDelayTime  delayTime=null;//延时时间对象声明
		long  countRun=0; //未发送的运行信息的条数
				
		countRun=dbservice.getCountRunInfo();//检查SQLite数据库中运行信息的条数
		String str=countRun+"";
		sendMsg2Main(str,HandleMsgTypeMcro.MSG_COUNTRUN);//发送给UI的Handler
		
		//发送特定条数的运行信息
		if(countRun>= keys[pointer = (pointer)%len] ){
			delayTime=getDataDelayTimeObj(DataType.DataDelay,countRun); //获取一个延时信息对象
			sendNumRunInfo(keys[pointer],delayTime);  //发送指定条数的运行信息到服务器	
			++pointer;					
		}
		//发送延时时间，到服务器
		sendDelayTime(delayTime);
	}
	
	/**
	 * 发送延时时间
	 * @param dt
	 */
	private void sendDelayTime(DataDelayTime dt){
		//发送延时时间对象到服务器
		if(dt!=null && dt.getNumofmsg()>0 ){
			GeneralData generalData = new GeneralData();		
			generalData.setDid(DataType.DataDelay);  //数据类型
			
			generalData.setDt(JsonUtil.object2Json(dt));
			
			String res = null;
			try {
				res = Post.sendData(path,JsonUtil.object2Json(generalData));
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//发送数据			
			//判断是否发送成功						
			if(resultofPost.equals(res)){							
				Log.d(TAG, "发送计时信息成功！");
			}
			else if(resultofExcep.equals(res)){	//发送失败					
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
	private void sendNumRunInfo(int n,DataDelayTime delayTime){		
			
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

			if(resultofPost.equals(result))
			{
				dbservice.deleteRunInfo(dataRunPlus.getId(), dataRunPlus.getIdEnd());//发送信息成功后，从数据库中删除该条信息
				
				Log.d(TAG, "成功发送"+n+"条运行信息！" );
			}
			else if(resultofExcep.equals(result)){
//				login = false;
				Log.d(TAG, "发送"+n+"条运行信息失败！");
			}
			
			if(delayTime!=null){
				delayTime.setNumofmsg(n); //本次发送的信息条数
			}
		
		}else{
			Log.d(TAG, "读回的运行信息条数与期望不符！！！");
			return ;
		}
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
			
		try {
			response = Post.sendData(path,strJson ); //发送数据
		} catch (SocketTimeoutException e) {		
			e.printStackTrace();
		}
		afterSecond=System.currentTimeMillis();; //发送之后时刻
				
		timePeriod=afterSecond-beforeSecond; //计算发送所用时长
		
		long speedn=countBytes*1000/timePeriod; //发送速率 ，单位  B/s
		
		if(delayTime!=null){    //设置延时对象的各项参数
			delayTime.setDelaytime(timePeriod);   //延时时间
			delayTime.setPackagesize( countBytes ); //包大小
			delayTime.setSpeed(speedn);			  //发送速度
		}
		
//		String str="用时:"+ timePeriod+"ms;"+ "数据大小: "+countBytes +"bytes;"+"发送速率:"+speed(timePeriod,countBytes);
		StringBuilder strb=new StringBuilder();
		strb.append(timePeriod+"ms").append(":");
		strb.append(countBytes+"bytes").append(":");
		strb.append(speed(timePeriod,countBytes));
		sendMsg2Main(strb.toString(),HandleMsgTypeMcro.MSG_DELAYTIME);//在主页面显示本次发送的延时和速度参数
		
		return response;
	}
	

	//获取延时时间对象	
	private DataDelayTime getDataDelayTimeObj(int dataType, long nOfInfoUnsent ){
		return getDataDelayTimeObj(dataType,nOfInfoUnsent,0);
	}
	
	private DataDelayTime getDataDelayTimeObj(int dataType, long nOfInfoUnsent ,long nOfInfoSent){
		
		@SuppressLint("SimpleDateFormat") 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//时间戳格式
		String dateStamp=formatter.format(new Date()); //时间戳
		
		DataDelayTime  delaytemp=new DataDelayTime();

		delaytemp.setCncId(DaqData.forthG); //4G运营商
		delaytemp.setTerminalid(DaqData.getAndroidId());
		delaytemp.setDatatype(dataType);    //数据类型
		delaytemp.setNumofmsgunsent(nOfInfoUnsent); //未发送的本地数据
		delaytemp.setTs(dateStamp);       //时间戳
		delaytemp.setNumofmsg(nOfInfoSent); //本次发送数据的条数
		
		return delaytemp;
	}
	
	
	//发送消息到主线程
	private void sendMsg2Main(Object obj, int what) {	
		sendMsg(mainActivityHandler, obj, what,0,0);
	}
	
	//发送消息到主线程
	private void sendMsg2Main(Object obj, int what, int arg1){ 	
		sendMsg(mainActivityHandler, obj, what, arg1, 0);
	}

	//发送消息，通用型
	private void sendMsg(Handler handler,Object obj, int what, int arg1, int arg2) {	
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		handler.sendMessage(msg);
	}
	
	
	 private String speed(long time,long len){		 
		 String result="";
		 if(time>0){
			 long s=len*1000/time;
			 result=s+"B/s";
			 if(s>1024){
				 s=s/1024;
				 result=s+"KB/s";
			 }
			 if(s>1024){
				 s=s/1024;
				 result=s+"MB/s";
			 }
			 if(s>1024){
				 s=s/1024;
				 result=s+"GB/s";
			 }			 
			 String size=len+"B";
			 if(len>1024){
				 len=len/1024;
				 size=len+"KB";
			 }			 
			 if(len>1024){
				 len=len/1024;
				 size=len+"MB";
			 }			 
			 String t=time+"ms";
			 if(time>1000){
				 time=time/1000;
				 t=time+"sec";
				 if(time>60){
					 time=time/60;
					 t=time+"min";
				 }
			 }
			 result=result+"("+size+","+t+")";	 
		 }
		 return result;
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
	
	
	public void setIsCountinueRun(boolean flag){
		this.isCountinueRun=flag;
	}
	
	public boolean getIsCountinueRun(){
		return isCountinueRun;
	}
	
}





















package com.cnc.hangtian.thread;

import java.io.IOException;
import java.util.LinkedList;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import bean.ALARM_INFO;
import bean.AXIS_INFO;
import bean.SYSTEM_INFO;

import com.cnc.broadcast.BroadcastAction;
import com.cnc.broadcast.BroadcastType;
import com.cnc.daq.DaqData;
import com.cnc.daq.MyApplication;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.hangtian.alarmfile.FindAlarmByIdHangtian;
import com.cnc.hangtian.domain.VersionHangtian;
import com.cnc.huazhong.dc.CommonDataCollectThreadInterface;
import com.cnc.mainservice.DelMsgService;
import com.cnc.net.datasend.HandleMsgTypeMcro;
import com.cnc.utils.AlarmFilterList;
import com.cnc.utils.JsonUtil;
import com.cnc.utils.TimeUtil;

import function.Start;

/**
 * 航天数控数据采集线程
 * @author wei
 *
 */
public class HangTianDataCollectThread implements Runnable,
		CommonDataCollectThreadInterface {
	
	private final String TAG="HangTianDataCollectThread";
	private volatile boolean threadRunningFlag=true;
	int     count = 1;     //存储运行信息的id,标识这是第几次采集信息
	private Start start=null;
	private static int cncNumber=2;
	private String threadLabel=null; //线程标记
	private static String machineIP="192.168.188.141";
	private final int  port=6665;
	private Handler delMsgHandler =null;
	private String CNCSystemType=null; 				//数控系统型号
	private String CNCSystemID=null;				//数控系统ID
	private AlarmFilterList   alarmFilterList =null; //报警信息缓存过滤对象
	boolean boolGetMacInfo = false;					 //标识是否得到机床的基本信息
	
	
	public HangTianDataCollectThread(String machineip, String threadlabel) {
		
		String num = machineip.substring( machineip.lastIndexOf(".")+1);		
		cncNumber=(Integer.parseInt(num)%10)+1;
		delMsgHandler =DelMsgService.getHandlerService();
		this.threadLabel=threadlabel; 						
		this.machineIP=machineip;	
		if(delMsgHandler!=null){
			alarmFilterList=new AlarmFilterList(delMsgHandler);
		}		
	}
	
	@Override
	public void run() {
		
		start = Start.getInstance();
		if(start==null){
			return ;
		}
		
		try{
			start.ClientConnectServer(cncNumber, machineIP, port);
			if(start.SocketKeepalive(cncNumber)){
				Log.d(TAG, "航天数控"+machineIP+"连接成功");
			}else{
				Log.d(TAG, "航天数控"+machineIP+"连接失败");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		while(threadRunningFlag){
			if(! start.SocketKeepalive(cncNumber)){
				try{
					start.ReConnectServer(cncNumber);
					if(start.SocketKeepalive(cncNumber)){
						Log.d(TAG, "航天数控"+machineIP+"重新连接成功");
					}else{
						Log.d(TAG, "航天数控"+machineIP+"重新连接失败");
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				//data acquisition
				getDaq();
			}
			
			try{
				Thread.sleep(1000);//采集间隔1秒
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}//end while
		
		//断开连接,造成闪退
		/*try {
			start.DeleteServer(cncNumber);
		} catch (Exception e) {			
			e.printStackTrace();
		}*/
		
	} //end run()

	
	//采集数据
    private void getDaq(){ 
    	SYSTEM_INFO systemInformation=null;
  	
    	if(!boolGetMacInfo){
    		//采集注册信息和登录登出信息
    		try {
				systemInformation= start.GetSysteminfo(cncNumber);//读取CNC系统信息
				if(systemInformation!=null){
					this.CNCSystemID=systemInformation.getSystemid().trim();
					this.CNCSystemType=systemInformation.getSystemtype().trim();
					String NCversion=systemInformation.getSystemver().trim(); //数控系统总版本号
					
					VersionHangtian versionHangtian=new VersionHangtian(NCversion, CNCSystemType);
					DataReg dataReg= new DataReg(this.CNCSystemID , 
												 this.CNCSystemType,
												JsonUtil.object2Json(versionHangtian) ,
												 TimeUtil.getTimestamp());
					DaqData.saveDataReg(dataReg); //保存注册信息	
					
					DataLog dataLog=new DataLog(CNCSystemID, systemInformation.getOntime(),
												systemInformation.getRuntime()	,  
												TimeUtil.getTimestamp());
					DaqData.saveDataLog(dataLog);
					boolGetMacInfo=true;	
					
					//发送到主界面显示  cncid  androidid
					sendBroadCast(BroadcastAction.ACTION_HANGTIAN_RUN_ALARM,
							"type" , "cncid" ,
							"threadlabel",threadLabel,
							"cncid" , CNCSystemID);
					
					sendBroadCast(BroadcastAction.ACTION_HANGTIAN_RUN_ALARM,
							"type" , "androidid" ,
							"threadlabel",threadLabel,
							"androidid" , DaqData.getAndroidId());
				}
			} catch (Exception e) {				
				e.printStackTrace();
			}
    		   				
    	}else{
    		//采集运行信息
    		collectRunInfo();
    		
    		//采集报警信息
    		collectAlarmInfo();
    		    		
    	}
    }
	
    /**
     * 采集航天数控运行信息
     */
    private void  collectRunInfo(){
    	SYSTEM_INFO systemInformation=null;
//    	ALARM_INFO  alarmInformation =null;
    	AXIS_INFO   axisInfomation =null;
    	
    	try {
			systemInformation= start.GetSysteminfo(cncNumber);//读取CNC系统信息
//			alarmInformation=start.GetAlarminfo(cncNumber);
			axisInfomation =start.GetAxisinfo(cncNumber);
		} catch (InterruptedException e) {				
			e.printStackTrace();
		}
		
		DataRun dataRun=new DataRun();
		dataRun.setId(CNCSystemID);
		dataRun.setCas((float)axisInfomation.getA_s_value());//轴实际转速
		dataRun.setCcs((float)axisInfomation.getC_s_value());//轴指令转速
		dataRun.setAload((float)systemInformation.getS_loadcurrent());//主轴负载电流
		
		long[] axisSpeed= axisInfomation.getA_f_value();//进给轴实际转速
		dataRun.setAspd1(axisSpeed[0]);
		dataRun.setAspd2(axisSpeed[1]);
		dataRun.setAspd3(axisSpeed[2]);
		dataRun.setAspd4(0);
		dataRun.setAspd5(0);
		
		float[] axisActualposition=axisInfomation.getA_axis_machine();//进给轴实际位置
		dataRun.setApst1(axisActualposition[0]);
		dataRun.setApst2(axisActualposition[1]);
		dataRun.setApst3(axisActualposition[2]);
		dataRun.setApst4(0);
		dataRun.setApst5(0);
		
		float[] axisCommandposition=axisInfomation.getC_axis();//进给轴指令位置
		dataRun.setCpst1(axisCommandposition[0]);
		dataRun.setCpst2(axisCommandposition[1]);
		dataRun.setCpst3(axisCommandposition[2]);
		dataRun.setCpst4(0);
		dataRun.setCpst5(0);
		
		long[] axisLoadCurrent= systemInformation.getAxis_loadcurrent();//进给轴负载电流
		dataRun.setLoad1(axisLoadCurrent[0]);
		dataRun.setLoad2(axisLoadCurrent[1]);
		dataRun.setLoad3(axisLoadCurrent[2]);
		dataRun.setLoad4(0);
		dataRun.setLoad5(0);
		   		
	    dataRun.setPd((short)systemInformation.getPd()); //运行程序编号
	    dataRun.setPn(systemInformation.getPn().trim());		//程序名
	    dataRun.setPs(systemInformation.getPs()+"");	//代码运行状态
	    dataRun.setPl(systemInformation.getPl());		//代码运行行
	    dataRun.setPm((short)0); 						//通道模态
	    dataRun.setTime(TimeUtil.getTimestamp());
	    
		sendMsg2Main(dataRun,HandleMsgTypeMcro.MSG_RUN,count);
		
		count++;//采集次数记录
		if(count == Integer.MAX_VALUE)//达到最大值的时候记得清零
			count = 1;	
    	
		//发送到主界面显示
		sendBroadCast(BroadcastAction.ACTION_HANGTIAN_RUN_ALARM,
				"type" , BroadcastType.HANGTIAN_RUN ,
				"threadlabel",threadLabel,
				 BroadcastType.HANGTIAN_RUN, dataRun.toString());
    }
    
    /**
     * 采集航天数控的报警信息
     */
    private void collectAlarmInfo(){
    	
    	ALARM_INFO  alarmInformation =null;
    	StringBuilder  currentAlarmString=new StringBuilder();//报警信息
    	LinkedList<DataAlarm> listDataAlarm =new LinkedList<>();
    	
    	try {			
			alarmInformation=start.GetAlarminfo(cncNumber);			
		} catch (InterruptedException e) {				
			e.printStackTrace();
		}

    	
    	int []alarmCodes=alarmInformation.getAlarmcode_array();//当前报警代码组
    	int i=0;
    	while(alarmCodes[i]>0 ){
    		String alarmCode= alarmCodes[i]+"";
			String alarmInfo=FindAlarmByIdHangtian.getAlarmInfoById(alarmCode);
			
			DataAlarm dataAlarm=new DataAlarm();
			dataAlarm.setId(CNCSystemID);
			dataAlarm.setF((byte)0);
			dataAlarm.setNo(alarmCode);
			dataAlarm.setTime(TimeUtil.getTimestamp());
			dataAlarm.setCtt(alarmInfo);
			
			listDataAlarm.add(dataAlarm);
			currentAlarmString.append(alarmInfo).append(":");
			i++;	    	 	   		
    	}
    	    	
		//如果采集到的报警信息不为零或者已有的报警信息不为零，那么就要对报警信息进行分析
		//对报警信息进行处理,必须要判断报警信息的来到是发生报警还是解除报警，这个分析过程留到主线程中
		if ((listDataAlarm.size() != 0)||(!alarmFilterList.getNowAlarmList().isEmpty())){
			alarmFilterList.saveCollectedAlarmList(listDataAlarm);
		}
    	
		//当前报警信息发送到主界面显示，没有报警发生时显示为空
		sendBroadCast(BroadcastAction.ACTION_HANGTIAN_RUN_ALARM,
				"type" , BroadcastType.HANGTAIN_ALARM ,
				"threadlabel",threadLabel,
				 BroadcastType.HANGTAIN_ALARM, currentAlarmString.length()>0 ? currentAlarmString.toString():"当前无报警发生");  
				
    }
    
	
	@Override
	public void stopCollect() {
		threadRunningFlag=false;
	}

	@Override
	public boolean isThreadRunning() {		
		return threadRunningFlag;
	}
	
	//用String trim()函数解决
	//删除字符串结尾处多余的字符，解决结尾处乱码问题
//	private String getUsefulString(String str){
//		String strl=null;
//		strl =str.substring(0, str.indexOf('\0'));
//		return strl;
//	}
	
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
	 * @param obj
	 * @param what
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
		
//		Message msg =new Message();
		msg.what = what;
		msg.obj = obj;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		handler.sendMessage(msg);
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


}

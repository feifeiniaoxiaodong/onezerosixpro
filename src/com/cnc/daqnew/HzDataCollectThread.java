package com.cnc.daqnew;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cnc.daq.DaqActivity;
import com.cnc.daq.DaqData;
import com.cnc.daq.MainActivity;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.DataType;
import com.cnc.domain.UiDataAlarmRun;
import com.cnc.domain.UiDataNo;
import com.cnc.huazhong.HncAPI;
import com.cnc.huazhong.HncSystem;
import com.cnc.netService.HncTools;
import com.cnc.netService.Intialize;
import com.cnc.service.DelMsgServie;
import com.cnc.utils.LogLock;
import com.cnc.utils.RegLock;
/**
 * 数据采集子线程
 * @author wei
 *
 */
public class HzDataCollectThread implements Runnable,DataCollect{
	
	private final String TAG="DataCollectThread...";
	 //线程循环执行标志，改为false时，线程退出循环，线程结束运行
    private volatile  boolean  threadflag=true; 
	public int Client = -1;	   //连接状态标识， -1标识未连接

	boolean boolGetMacInfo = false; //标识是否得到机床的基本信息
	int   macChannel = 0;//机床的通道信息
	int   count = 1;//存储运行信息的id,标识这是第几次采集信息
	
	
	private Handler  daqActivityHandler=null,
					 delMsgHandler =null;
	private Handler  mainHander=null ;
					
	String tp = "HNC-818A";//数控系统型号
	String machine_SN=null;//数控系统ID
//	String pathcnc=null;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//时间戳格式
	
	/*static {  
        // 加载动态库  
        System.loadLibrary("hncnet");  
        System.loadLibrary("hzHncAPI");  
    }*/

	String machineIP = "192.168.188.113"; //机床的IP地址
	int    machinePort = 21;			  //机床端口号
	
	public HzDataCollectThread(String ip,int port){
		this.delMsgHandler=DelMsgServie.getHandlerService();
		this.daqActivityHandler=DaqActivity.getmHandler();
		mainHander=MainActivity.getMainActivityHandler();
		machineIP=ip;
		machinePort=port;		
	}
	
	public HzDataCollectThread(Handler handler,String pathcnc) {
		this.daqActivityHandler=handler;	
//		this.pathcnc=pathcnc;
	}
		
	@Override
	public void run() {
		int inialRes = -1;//是否已经初始化
/*		String machineIP = "192.168.188.113"; //机床的IP地址
		int    machinePort = 21;			  //机床端口号
		
		String str=getPath(pathcnc),
			   strp=getPath("cncport");
		if(str != null && strp!=null ){
			machineIP=str;
			machinePort=Integer.parseInt(strp);
		}*/

		while(threadflag)
		{
			if(inialRes != 0)
				inialRes = new Intialize().inial();//首先初始化
			else//初始化成功
			{					
//				if(Client < 0)//没有连接机床
				if(HncAPI.HNCNetIsConnect(Client) != 0)
				{						
					//采集数据之前初始化，得到目的机器的IP和port
					Client = HncAPI.HNCNetConnect(machineIP,machinePort);//要连接的机床的IP和端口
					if (Client >= 0 )
						sendMsg2Main("连接机床成功", HandleMsgTypeMcro.MSG_ISUCCESS);//初始化成功						
					else{
						sendMsg2Main("连接机床失败", HandleMsgTypeMcro.MSG_IFAILURE);
						try {							
							Thread.sleep(1000*10); //连接机床失败过一分钟再连							
						} catch (InterruptedException e) {					
							e.printStackTrace();
						} 
					}					
				}
				else{				
					daq();//调用数据采集函数采集数据，同时把采集到的数据发送到主线程
				}			
			}
			
			//采集数据间隔时间,采集结束之后线程休眠一段时间
			try {
//				Thread.sleep(700);//采集数据间隔时间设置为1S,因为采集过程耗时大约300毫秒，所以设置为700
				Thread.sleep(1000);
			} catch (InterruptedException e) {					
				e.printStackTrace();
			} 
			
		}//end while(true)		
	}//end run()
	
	
	/**
	 * 数据采集函数
	 */
	private void daq() 
	{
		
		StringBuilder  sbalram=new StringBuilder();
		sbalram.append("Alarm:");
		
		String strTime = formatter.format(new Date());//开始采集信息的各种事件
		
		if(!boolGetMacInfo)   //如果没有获得过机床的基本信息
		{
			//注册信息
			DataReg dataReg = HncTools.getMacInfo(Client); //获取机床的基本信息			
			//机床ID
			machine_SN=dataReg.getId();
//				DaqData.setCncid(machine_SN);
			//得到通道号
			macChannel = HncAPI.HNCSystemGetValueInt(HncSystem.HNC_SYS_ACTIVE_CHAN, Client);//机床的通道信息
			dataReg.setTime(strTime);		//设置采集的时间戳
			dataReg.setTp(tp);   			//数控系统型号
			synchronized(RegLock.class){
				DaqData.getListDataReg().add(dataReg);
			}

			DataLog dataLog=new DataLog(machine_SN,0,0,strTime);//华中数控不提供“累计加工时间”和“累计运行时间”
			synchronized(LogLock.class){
				DaqData.getListDataLog().add(dataLog);
			}
			
			UiDataNo uiDataNo=new UiDataNo(getNoCnc(machineIP),machineIP,machine_SN , DaqData.getAndroidId());
			sendMsg(mainHander,uiDataNo,HandleMsgTypeMcro.HUAZHONG_UINO,0,0); //发送消息到活动，显示IP地址信息
			
			
			boolGetMacInfo = true;//置为true，表明已经得到了机床基本信息
		}
		else//采集运行信息和报警信息
		{						 
			//采集报警信息
			LinkedList<DataAlarm> listDataAlarm = HncTools.getAlarmData(Client);
			for (DataAlarm dataAlarm : listDataAlarm) {
				dataAlarm.setId(machine_SN);//设置数控系统ID
				dataAlarm.setTime(strTime);//设置采集的时间戳
				sbalram.append(dataAlarm.getCtt()).append(":");
			}
			//如果采集到的报警信息不为零或者已有的报警信息不为零，那么就要对报警信息进行分析
			//对报警信息进行处理,必须要判断报警信息的来到是发生报警还是解除报警，这个分析过程留到主线程中
			if ((listDataAlarm.size() != 0)||(DaqData.getListDataAlarm().size() != 0)) 
			{
				sendMsg2Main(listDataAlarm, HandleMsgTypeMcro.MSG_ALRAM, count);
			}		
			
			//采集运行信息
			DataRun dataRun = HncTools.getDataRun(Client,macChannel);
			dataRun.setId(machine_SN); //设置数控系统ID
			dataRun.setTime(strTime); //加时间戳
			sendMsg2Main(dataRun,HandleMsgTypeMcro.MSG_RUN,count);
					
			count++;//采集次数记录
			if(count == Integer.MAX_VALUE)//达到最大值的时候记得清零
				count = 1;
			
			//发送到主线程
			UiDataAlarmRun uiDataAlarmRun=new UiDataAlarmRun(sbalram.toString(), dataRun.toString());
			sendMsg(mainHander, uiDataAlarmRun, HandleMsgTypeMcro.HUAZHONG_UIALARM	, 0, 0);
			
		}
		
	}//end daq()函数
	
	/**
	 * 获取properties文件中值
	 * @param key
	 * @return
	 */
	/*private String  getPath(String key){
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
	}*/
	
	
	//发送消息到主线程
	private void sendMsg2Main(Object obj, int what) 
	{
		sendMsg(delMsgHandler, obj, what,0,0);
	}
	
	//发送消息到主线程
	private void sendMsg2Main(Object obj, int what, int arg1) 
	{
		sendMsg(delMsgHandler, obj, what, arg1, 0);
	}

	//发送消息，通用型
	private void sendMsg(Handler handler,Object obj, int what, int arg1, int arg2) 
	{
		Message msg = Message.obtain();			
		msg.what = what;
		msg.obj = obj;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		handler.sendMessage(msg);
	}

	@Override
	public void stopCollect() {
		threadflag=false;	//结束线程	
	}

	@Override
	public boolean isThreadRunning() {
		// TODO Auto-generated method stub
		  return threadflag;
	}
	
	private String getNoCnc(String ip){
		String NoCnc="1-1";
		if(ip==null){
			return null;
		}
		
		String lastip= ip.substring(ip.lastIndexOf('.')+1);
		int lastipint= Integer.valueOf(lastip);
		
		switch( lastipint){
			case 112:
				NoCnc="HUAZHONG1_1";
			break;
			case 114:
				NoCnc="HUAZHONG1_2";
			break;
			case 113:
				NoCnc="HUAZHONG1_3";
			break;
			case 111:
				NoCnc="HUAZHONG1_4";
			break;
			case 110:
				NoCnc="HUAZHONG1_5";
			break;
			default:{}
		}
		
		return NoCnc;
	}

	

}

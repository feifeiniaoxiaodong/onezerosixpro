package com.example.wei.gsknetclient_studio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.cnc.daq.DaqData;
import com.cnc.daq.MainActivity;
import com.cnc.daqnew.DataCollectInter;
import com.cnc.daqnew.HandleMsgTypeMcro;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.UiDataAlarmRun;
import com.cnc.domain.UiDataNo;
import com.cnc.gsk.data.domain.DataBHSAMPLE_STATIC;
import com.cnc.gsk.data.domain.DataVersion;
import com.cnc.gsk.data.domain.Mcronum;
import com.cnc.gsk.datautils.BytetoJavaUtil;
import com.cnc.netService.HncTools;
import com.cnc.service.DelMsgServie;
import com.cnc.utils.AlarmFilterList;
import com.cnc.utils.JsonUtil;
import com.cnc.utils.LogLock;
import com.cnc.utils.RegLock;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * 广州数控数据采集线程
 * Created by wei on 2017/4/15.
 */

public class GSKDataCollectThread implements Runnable ,DataCollectInter{
  
    //线程循环执行标志，改为false时，线程退出循环，线程结束运行
    private volatile  boolean  threadflag=true;  
    private static final String TAG="GSKDataCollect()... Thread";
/*    static {
        System.loadLibrary("gsknetw-lib");
    }
    	String pathcnc=null;
    */
   
    private long clientnum=0;  //操作句柄,为native类对象地址
    private String machineIP="192.168.188.128";
    int port=5000;
    int res=0;
    int linked=0; //连接状态
    boolean boolGetMacInfo = false; //标识是否得到机床的基本信息
	String threadlabel =null; //线程标记
	int   count = 1;//存储运行信息的id,标识这是第几次采集信息	
	private Handler  daqActivityHandler=null,
					 delMsgHandler =null,
					 mainActivityHandler=null;
			
	private String machine_SN=null;//数控系统ID

	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//时间戳格式
	private AlarmFilterList   alarmFilterList =null; //报警信息缓存过滤对象
	
	public GSKDataCollectThread(String ip,String threadlabel){	
		this(ip,5000);
		this.threadlabel=threadlabel;
	}
	
    public GSKDataCollectThread(String ip){
    	this(ip,5000);
    }
    public GSKDataCollectThread(String ip,int	port){    	
    	this.machineIP=ip;
    	this.port=port;
    	delMsgHandler=DelMsgServie.getHandlerService();
    	mainActivityHandler=MainActivity.getMainActivityHandler();
    	this.alarmFilterList=new  AlarmFilterList(delMsgHandler);
    }
	   
    @Override
    public void run(){

        clientnum=GSKNativeApi.GSKRM_Initialization(machineIP,port); //创建底层管理类对象
        if(clientnum<=0){
            Log.d(TAG, "创建GSKNetClient对象失败");
            return ;
        }

        res=GSKNativeApi.GSKRM_Connnect(clientnum);   //连接到CNC
        if(res<0){
            Log.d(TAG, "连接到机床失败");
        }else{
        	  Log.d(TAG, "连接到机床成功");
        }

/**********************************************************/
        
        while(threadflag){
        	
            linked=GSKNativeApi.GSKRM_GetConnectState(clientnum);//查连接状态
            if(linked<0){
                //重新进行连接
                GSKNativeApi.GSKRM_CloseConnect(clientnum);
                res=GSKNativeApi.GSKRM_Connnect(clientnum);   //连接到CNC
                if(res<0){
                    Log.d(TAG, "连接到机床失败");
                    try {
						Thread.sleep(1000*60); //连接失败，过一分钟再连
					} catch (InterruptedException e) {						
						e.printStackTrace();
					}
                }else{
                	 Log.d(TAG, "连接到机床成功");
                }
            }else{
                //采集数据
                getDaq();       
            }

            try{
               Thread.sleep(1000);//采集间隔1秒
            }catch(Exception e){
                e.printStackTrace();
            }
        }//end while  //end for
     
        //释放内存
        if( (res=GSKNativeApi.GSKRM_FreeObj(clientnum) )>0){
            clientnum=0;//清除对象后要把句柄号置0
        }

    } // end run

       
    //采集数据
    private void getDaq(){ 
    	String timeStr=formatter.format(new Date());//时间戳   	
    	//采集注册信息 和登入登出信息
    	if(!boolGetMacInfo){
    		
    		DataVersion dataVersion=getVersionInfo(); //读取版本信息
    		DataBHSAMPLE_STATIC bhSample=getBeiHangInfo();//获取运行信息、报警信息、登录登出信息集合
    		
//    		String tp = GSKNativeApi.GSKRM_GetCNCTypeName(clientnum);//数控系统型号，有待测试 ===>乱码
    		String tp = "25i";
    		
    		if(dataVersion!=null && bhSample!=null){
    			
    			machine_SN=dataVersion.getSoftWareNumber();//使用软件版本号作为数控系统唯一的ID	
    			String versionJson=JsonUtil.object2Json(dataVersion);
    			DataReg dataReg=new DataReg(machine_SN, 			//注册信息
    										tp,			//数控系统型号
    										versionJson, //版本信息
    										timeStr);
    			
    			synchronized(RegLock.class){
					DaqData.getListDataReg().add(dataReg);
				}
    			
    			DataLog dataLog=new DataLog(machine_SN, 
    										bhSample.getRuntime(), //累计运行时间
    										bhSample.getOntime(),  //累计加工时间
    										timeStr);
    			synchronized(LogLock.class){
					DaqData.getListDataLog().add(dataLog);
				}
    			boolGetMacInfo=true;
    			
    			UiDataNo uiDataNo=new UiDataNo("",machineIP,machine_SN , DaqData.getAndroidId());
    			uiDataNo.setThreadlabel(threadlabel); //thread label
    			sendMsg(mainActivityHandler,uiDataNo,HandleMsgTypeMcro.GSK_UINO,0,0); //发送消息到活动，显示IP地址信息    			
    		}		
    	}else{ //采集运行信息和报警信息
    		StringBuilder  sbalram=new StringBuilder();//报警信息
    		DataBHSAMPLE_STATIC bhSample=getBeiHangInfo();//获取运行信息、报警信息、登录登出信息集合
    		//采集报警信息
    		 List<DataAlarm> listDataAlarm =new LinkedList<DataAlarm>();
    		 if(bhSample!=null){
    			 String [] almhead=bhSample.getAlmhead();// 报警编号
//        		 String[] almtime=bhSample.getAlmtime();// 报警发生时间，读不出来显示“20NULL”
        		 String[] alminfor=bhSample.getAlminfor();// 报警信息内容,中文报警信息
        		 int alarm_num=almhead.length;
        		 for(int i=0;i< alarm_num;i++){
        			 String alarmNo= almhead[i].trim(); //报警编号
        			 if(alarmNo ==null || "".equals(alarmNo)){
        				 break;
        			 }
        			 DataAlarm dataAlarm=new DataAlarm(machine_SN, 
        					 							(byte)0 ,
        					 							alarmNo,
        					 							timeStr, 
        					 							alminfor[i] );
        			 listDataAlarm.add(dataAlarm);
        			 sbalram.append(alminfor[i]).append(":");
        		 }

    			//如果采集到的报警信息不为零或者已有的报警信息不为零，那么就要对报警信息进行分析
    			//对报警信息进行处理,必须要判断报警信息的来到是发生报警还是解除报警，这个分析过程留到主线程中
    			if ((listDataAlarm.size() != 0)||(!alarmFilterList.getNowAlarmList().isEmpty())){
    				alarmFilterList.saveCollectedAlarmList(listDataAlarm);
    			} 			 
    		     		     		
	    		//采集运行信息
				float aspdx[]=bhSample.getAspd(); //实际转速
				float apstx[]=bhSample.getApst();//进给轴指令位置
			    float cpstx[]=bhSample.getCpst();//进给轴实际位置
			    float loadx[]=bhSample.getLoad();//负载电流
				
				DataRun dataRun=new DataRun(machine_SN, bhSample.getCas(), bhSample.getCcs(), bhSample.getAload(), 
						aspdx[1], aspdx[2], aspdx[3], aspdx[4], aspdx[5],      //实际转速
						cpstx[1], cpstx[2], cpstx[3], cpstx[4], cpstx[5], 		//实际位置
						apstx[1], apstx[2], apstx[3], apstx[4], apstx[5], 		//指令位置
						loadx[1], loadx[2], loadx[3], loadx[4], loadx[5], 		//负载电流
						bhSample.getPrognum(), //运行程序编号
						bhSample.getProgname(), //程序名
						bhSample.getRunstatus(),//代码运行状态
						bhSample.getGclinenum(),//运行行号
						bhSample.getGcmode(),					   //通道模态
						timeStr) ;    			 //时间戳
				sendMsg2Main(dataRun,HandleMsgTypeMcro.MSG_RUN,count);
				
				count++;//采集次数记录
				if(count == Integer.MAX_VALUE)//达到最大值的时候记得清零
					count = 1;
				
				//发送到主线程
				UiDataAlarmRun uiDataAlarmRun=new UiDataAlarmRun(sbalram.toString(), dataRun.toString());
				uiDataAlarmRun.setThreadlabel(threadlabel);//thread label
				sendMsg(mainActivityHandler, uiDataAlarmRun, HandleMsgTypeMcro.GSK_UIALARM	, 0, 0);
    		}
    	}//end else
    }//end daq()

    @Override
    public boolean isThreadRunning() {
        return threadflag;
    }

    private DataVersion getVersionInfo(){
         DataVersion dataver=null;    //版本信息
        byte[] tmpbytes=GSKNativeApi.GSKRM_GetVersionInfo(clientnum);
        if(tmpbytes!=null) {
            dataver=BytetoJavaUtil.batoJavaver(tmpbytes);         //处理字节流信息
            if(dataver !=null){
                dataver.loginfor();
            }
        }else {
            Log.d("JNITest","getVersionInfo...读回的字节流为空");
        }
        return dataver;
    }


    private DataBHSAMPLE_STATIC getBeiHangInfo(){
        //读取北航信息采集接口
        DataBHSAMPLE_STATIC  dataBeiHang=null;
        byte[] tmpbytes= GSKNativeApi.GSKRM_GetBeihangInfo(clientnum);
        if(tmpbytes!=null){
            dataBeiHang=BytetoJavaUtil.batojavaDataBHSAMPLE_STATIC(tmpbytes);

            Log.d("JNITest",Thread.currentThread().getName());
//            Log.d("JNITest","北航数据"+ dataBeiHang.toString());
        }else{
            Log.d("JNITest","DataBHSAMPLE_STATIC...读回的字节流为空");
        }
        return dataBeiHang;
    }
    
       
	//发送消息到主线程
	private void sendMsg2Main(Object obj, int what) {	
		sendMsg(delMsgHandler, obj, what,0,0);
	}
	
	//发送消息到主线程
	private void sendMsg2Main(Object obj, int what, int arg1) {	
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
		 this.threadflag=false;		
	}
	
}

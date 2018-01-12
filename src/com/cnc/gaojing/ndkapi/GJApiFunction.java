package com.cnc.gaojing.ndkapi;

/*import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;*/
import java.util.LinkedList;

import android.util.Log;

//import com.cnc.daq.DaqData;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;

import com.cnc.domain.gaojing.DataVersion;
import com.cnc.utils.JsonUtil;


/*
 * 从机床读取数据工具类
 */
public class GJApiFunction {
	
	private  static final String TAG="Alarm Information Test...";
		
	/**
	 * 读取报警信息
	 * 读取的只是当前报警信息，如果没有报警发生读回null
	 * 报警号和报警信息都在这个读取的字符串里，是放在一起的
	 * @return
	 */
	public static  LinkedList<DataAlarm>  getDataAlarm(){
		LinkedList<DataAlarm> alarmlist=new LinkedList<DataAlarm>(); //新建报警信息列表对象
		
		String  alarmNo,  	//报警号
				alarmNum,	//报警号索引
				alarmInfo;	//报警信息
		
		String  alarmMotion,//运动报警
				alarmPLC;	 //PLC报警
//		public static String getAutoMotionErrorVal()当前运动报警
//		public static String getAutoPlcErrorVal()当前PLC报警
		
		//获取当前运动报警信息
		alarmMotion = dnc.main.getAutoMotionErrorVal();
		if(alarmMotion!= null){
			DataAlarm  alarmobj=new DataAlarm();

			alarmNo = alarmMotion.substring(0, alarmMotion.indexOf(':'));
			alarmNum= alarmNo.substring(alarmNo.indexOf(" ")+1);
			
			alarmInfo=AlarmResources.getAlarmInfoById("MOTION",alarmNum);
			
			alarmobj.setNo(alarmNo);
			alarmobj.setCtt(alarmInfo);
			
			alarmlist.add(alarmobj);
			
			Log.d(TAG,alarmobj.toString()); //打印报警信息
		}
		
		//当前PLC报警
		alarmPLC  =  dnc.main.getAutoPlcErrorVal() ;		
		if(alarmPLC!=null){			
			DataAlarm  alarmobj=new DataAlarm();
			
			alarmNo=alarmPLC.substring(0, alarmPLC.indexOf(":"));
			alarmNum=alarmNo.substring( alarmNo.indexOf(" ")+1);
			
			alarmInfo=AlarmResources.getAlarmInfoById("PLC",alarmNum);
			
			alarmobj.setNo(alarmNo);
			alarmobj.setCtt(alarmInfo);
			
			alarmlist.add(alarmobj);
		
			Log.d(TAG,alarmobj.toString()); //打印报警信息
		}
		
		//检查是否有急停报警信息
		if(dnc.main.getStatusIntVal(2006)==1){
			DataAlarm  alarmobj=new DataAlarm();
			
			alarmobj.setNo("MOTION 5001");
			alarmobj.setCtt("急停报警");			
			
			alarmlist.add(alarmobj); 
			
			Log.d(TAG,alarmobj.toString()); //打印报警信息
		}
					
		return alarmlist;		
	}
	
	/**
	 * 读取运行信息
	 * @return
	 */
	public static DataRun getDataRun(){
		DataRun  datarun=new DataRun();//新建运行信息对象
		
		datarun.setCas((float)dnc.main.getStatusDoubleVal(GJApiNum.SPINDLE_ACT_SPEED));//主轴实际速度
		datarun.setCcs((float)dnc.main.getStatusDoubleVal(GJApiNum.SPINDLE_COM_SPEED));//主轴指令速度
		datarun.setAload((float)dnc.main.getStatusDoubleVal(GJApiNum.SPINDLE_LOAD_CURRENT));//主轴负载电流
		
		datarun.setAspd1((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_X)); //进给轴实际转速X
		datarun.setAspd2((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_Y)); //进给轴实际转速Y
		datarun.setAspd3((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_Z)); //进给轴实际转速Z
//		datarun.setAspd1((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED)); //进给轴实际转速X
		datarun.setAspd2(0); //进给轴实际转速Y
		datarun.setAspd3(0); //进给轴实际转速Z
		datarun.setAspd4(0);
		datarun.setAspd5(0);
		
		datarun.setApst1((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_X));//进给轴实际位置
		datarun.setApst2((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_Y));
		datarun.setApst3((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_Z));
		datarun.setApst4(0);
		datarun.setApst5(0);
		datarun.setCpst1((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_X));//进给轴指令位置
		datarun.setCpst2((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_Y));
		datarun.setCpst3((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_Z));
		datarun.setCpst4(0);
		datarun.setCpst5(0);
		datarun.setLoad1((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_X));//进给轴负载电流
		datarun.setLoad2((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_Y));
		datarun.setLoad3((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_Z));
		datarun.setLoad4(0);
		datarun.setLoad5(0);
		
		datarun.setPd((short)0);  //运行程序编号,暂未给
		datarun.setPn(dnc.main.getStatusStringVal(GJApiNum.PROGRAM_NAME)); //当前程序名
		datarun.setPs(dnc.main.getStatusStringVal(GJApiNum.G_CODE_RUN_STAT));//代码运行状态
		datarun.setPl(dnc.main.getStatusIntVal(GJApiNum.G_CODE_LINE_NUM));// 代码运行行号
		
		int [] Gmode=dnc.main.getStatusArrayVal(GJApiNum.G_CODE_MODALITY); //G代码模态,int[20]
		datarun.setPm((short)Gmode[0]); //代码运行模态
	
		return datarun;
	}
	
	/**
	 * 获取注册信息	
	 * @return
	 */
	public static DataReg getDataReg(){
		String tmp=null;
		
		DataReg datareg=new DataReg();  //新建注册信息对象
		
		DataVersion dataver=new DataVersion();//新建版本信息对象
		dataver.setCnc_system_model(dnc.main.getStatusStringVal(GJApiNum.SYSTEM_MODEL));//系统型号
		dataver.setSoftware_number(dnc.main.getStatusStringVal(GJApiNum.SOFTWARE_VER));//软件版本号
		dataver.setHardware_number(dnc.main.getStatusStringVal(GJApiNum.HARDWARE_VER));//硬件版本号
		dataver.setSystem_kernel_number(dnc.main.getStatusStringVal(GJApiNum.SYSTEMKERNEL_VER));//系统内核版本号
			
//		datareg.setId(DaqData.getAndroidId());  //高精暂时使用AndroidID 代替数控系统ＩＤ,后改为用IP地址代替
		datareg.setVer(JsonUtil.toJson(dataver)); //版本信息
		datareg.setTp(dnc.main.getStatusStringVal(GJApiNum.SYSTEM_MODEL)); //数控系统型号
		
		return datareg;		
	}
	
	/**
	 * 获取登录信息
	 * @return
	 */
	public static DataLog getDataLog(){
		DataLog datalog=new DataLog();// 新建登录信息对象
	
		datalog.setOntime((long)dnc.main.getStatusDoubleVal(GJApiNum.TOTAL_RUN_TIME) );   //累计运行时间
		datalog.setRuntime((long)dnc.main.getStatusDoubleVal(GJApiNum.TOTAL_PROCESS_TIME)); //累计加工时间
		
		return datalog;	
	}

}

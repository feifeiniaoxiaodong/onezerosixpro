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

import com.cnc.gaojing.domain.DataVersion;
import com.cnc.utils.JsonUtil;


/**
 * 沈阳高精
 * 从机床读取数据工具类
 */
public class GJApiFunction {
	
	private  static final String TAG="Alarm Information Test...";
	
	dnc.main  dncmian ;
	public GJApiFunction(dnc.main dncmian){
		this.dncmian=dncmian;
	}
	
	/**
	 * 读取报警信息
	 * 读取的只是当前报警信息，如果没有报警发生读回null
	 * 报警号和报警信息都在这个读取的字符串里，是放在一起的
	 * @return
	 */
	public   LinkedList<DataAlarm>  getDataAlarm(){
		LinkedList<DataAlarm> alarmlist=new LinkedList<DataAlarm>(); //报警信息列表
		
		String  alarmNo,  	//报警号
				alarmNum,	//报警号索引
				alarmInfo;	//报警信息
		
		String  alarmMotion,//运动报警
				alarmPLC;	 //PLC报警
//		public static String getAutoMotionErrorVal()当前运动报警
//		public static String getAutoPlcErrorVal()当前PLC报警
		
		//获取当前运动报警信息
		alarmMotion = dncmian.getAutoMotionErrorVal();
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
		alarmPLC  = dncmian.getAutoPlcErrorVal() ;		
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
		if(dncmian.getStatusIntVal(2006)==1){
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
	public  DataRun getDataRun(){
		DataRun  datarun=new DataRun();//新建运行信息对象
		
		datarun.setCas((float)dncmian.getStatusDoubleVal(GJApiNum.SPINDLE_ACT_SPEED));//主轴实际速度
		datarun.setCcs((float)dncmian.getStatusDoubleVal(GJApiNum.SPINDLE_COM_SPEED));//主轴指令速度
		datarun.setAload((float)dncmian.getStatusDoubleVal(GJApiNum.SPINDLE_LOAD_CURRENT)/100);//主轴负载电流
		// 
		datarun.setAspd1((float)(dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_X)*6)); //进给轴实际转速X，由轴进给速度转化为轴转速，单位r/min
		datarun.setAspd2((float)(dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_Y)*6)); //进给轴实际转速Y
		datarun.setAspd3((float)(dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_Z)*6)); //进给轴实际转速Z
//		datarun.setAspd1((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED)); //进给轴实际转速X
		datarun.setAspd2(0); //进给轴实际转速Y
		datarun.setAspd3(0); //进给轴实际转速Z
		datarun.setAspd4(0);
		datarun.setAspd5(0);
		
		datarun.setApst1((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_X));//进给轴实际位置
		datarun.setApst2((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_Y));
		datarun.setApst3((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_Z));
		datarun.setApst4(0);
		datarun.setApst5(0);
		datarun.setCpst1((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_X));//进给轴指令位置
		datarun.setCpst2((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_Y));
		datarun.setCpst3((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_Z));
		datarun.setCpst4(0);
		datarun.setCpst5(0);
		datarun.setLoad1((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_X));//进给轴负载电流
		datarun.setLoad2((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_Y));
		datarun.setLoad3((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_Z));
		datarun.setLoad4(0);
		datarun.setLoad5(0);
		
		datarun.setPd((short)0);  //运行程序编号,暂未给
		datarun.setPn(dncmian.getStatusStringVal(GJApiNum.PROGRAM_NAME)); //当前程序名
		datarun.setPs(dncmian.getStatusStringVal(GJApiNum.G_CODE_RUN_STAT));//代码运行状态
		datarun.setPl(dncmian.getStatusIntVal(GJApiNum.G_CODE_LINE_NUM));// 代码运行行号
		
		int [] Gmode=dncmian.getStatusArrayVal(GJApiNum.G_CODE_MODALITY); //G代码模态,int[20]
		datarun.setPm((short)Gmode[0]); //代码运行模态
	
		return datarun;
	}
	
	/**
	 * 获取注册信息	
	 * @return
	 */
	public  DataReg getDataReg(){
		String tmp=null;
		
		DataReg datareg=new DataReg();  //新建注册信息对象
		
		DataVersion dataver=new DataVersion();//新建版本信息对象
		dataver.setCnc_system_model(dncmian.getStatusStringVal(GJApiNum.SYSTEM_MODEL));//系统型号
		dataver.setSoftware_number(dncmian.getStatusStringVal(GJApiNum.SOFTWARE_VER));//软件版本号
		dataver.setHardware_number(dncmian.getStatusStringVal(GJApiNum.HARDWARE_VER));//硬件版本号
		dataver.setSystem_kernel_number(dncmian.getStatusStringVal(GJApiNum.SYSTEMKERNEL_VER));//系统内核版本号
			
//		datareg.setId(DaqData.getAndroidId());  //高精暂时使用AndroidID 代替数控系统ＩＤ,后改为用IP地址代替
		datareg.setVer(JsonUtil.toJson(dataver)); //版本信息
		datareg.setTp(dncmian.getStatusStringVal(GJApiNum.SYSTEM_MODEL)); //数控系统型号
		
		return datareg;		
	}
	
	/**
	 * 获取登录信息
	 * @return
	 */
	public  DataLog getDataLog(){
		DataLog datalog=new DataLog();// 新建登录信息对象
	
		datalog.setOntime((long)dncmian.getStatusDoubleVal(GJApiNum.TOTAL_RUN_TIME) );   //累计运行时间
		datalog.setRuntime((long)dncmian.getStatusDoubleVal(GJApiNum.TOTAL_PROCESS_TIME)); //累计加工时间
		
		return datalog;	
	}

}

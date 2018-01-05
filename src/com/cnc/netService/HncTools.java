package com.cnc.netService;


import java.util.LinkedList;

import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.DataVer;
import com.cnc.huazhong.AlarmLevel;
import com.cnc.huazhong.AlarmType;
import com.cnc.huazhong.HncAPI;
import com.cnc.huazhong.HncAxis;
import com.cnc.huazhong.HncChannel;
import com.cnc.huazhong.HncSystem;
import com.cnc.utils.JsonUtil;


public class HncTools {	

	/*
	 * 采集机床的基本信息,包括数控系统的序列号和各种版本信息
	 */
	public static DataReg getMacInfo(int clientNo) {
		DataReg dataReg = new DataReg();//系统注册信息
		dataReg.setId(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_SN_NUM, clientNo));//系统ID 
		
		DataVer dataVer = new DataVer();//系统版本号
		dataVer.setCNC_VER(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_CNC_VER, clientNo));
		dataVer.setDRV_VER(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_DRV_VER, clientNo));
		dataVer.setNC_VER(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_NC_VER, clientNo));
		dataVer.setNCK_VER(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_NCK_VER, clientNo));
		dataVer.setPLC_VER(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_NCK_VER, clientNo));
		
		dataReg.setVer(JsonUtil.object2Json(dataVer));//出现异常	
		
		return dataReg;
	}
	
	//采集运行信息
	public static DataRun getDataRun(int clientNo,int channel) {
		DataRun dataRun = new DataRun();
		//主轴实际速度 （转速）
		dataRun.setCas((float)HncAPI.HNCChannelGetValueDouble(HncChannel.HNC_CHAN_ACT_SPDL_SPEED, channel, 0, clientNo));
		//主轴指令速度 （转速）
		dataRun.setCcs((float)HncAPI.HNCChannelGetValueDouble(HncChannel.HNC_CHAN_CMD_SPDL_SPEED, channel, 0, clientNo));
		//主轴负载电流,华中的主轴是第五个轴
		dataRun.setAload((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_LOAD_CUR, 5, clientNo));
		
		//轴1实际速度, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setAspd1((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_VEL, AxisType.X, clientNo));
		//轴2实际速度, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setAspd2((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_VEL, AxisType.Y, clientNo));
		//轴3实际速度, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setAspd3((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_VEL, AxisType.Z, clientNo));
		dataRun.setAspd4(0);//轴4没有，设置为零
		dataRun.setAspd5(0);//轴5没有，设置为零
		
		//轴1实际位置, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setApst1((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_POS_RCS, AxisType.X, clientNo));
		//轴2实际位置, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setApst2((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_POS_RCS, AxisType.Y, clientNo));
		//轴3实际位置, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setApst3((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_POS_RCS, AxisType.Z, clientNo));
		dataRun.setApst4(0);
		dataRun.setApst5(0);
		
		//轴1指令位置, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setCpst1((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_CMD_POS_RCS, AxisType.X, clientNo));
		//轴2指令位置, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setCpst2((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_CMD_POS_RCS, AxisType.Y, clientNo));
		//轴3指令位置, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setCpst3((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_CMD_POS_RCS, AxisType.Z, clientNo));
		dataRun.setCpst4(0);
		dataRun.setCpst5(0);
		
		//轴1负载电流, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setLoad1((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_LOAD_CUR, AxisType.X, clientNo));
		//轴2负载电流, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setLoad2((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_LOAD_CUR, AxisType.Y, clientNo));
		//轴3负载电流, i=0,X轴；i=1,Y轴；i=2,Z轴；
		dataRun.setLoad3((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_LOAD_CUR, AxisType.Z, clientNo));
		dataRun.setLoad4(0);
		dataRun.setLoad5(0);
		
		//运行程序编号
		dataRun.setPd((short)HncAPI.HNCChannelGetValueInt(HncChannel.HNC_CHAN_RUN_PROG, channel, 0, clientNo));
//		dataRun.setPn(pn);//暂无  程序名
//		dataRun.setPs(ps);//暂无  代码运行状态
		//运行代码行号
		dataRun.setPl(HncAPI.HNCChannelGetValueInt(HncChannel.HNC_CHAN_RUN_ROW, channel, 0, clientNo));
		//G代码模态
		dataRun.setPm((short)HncAPI.HNCChannelGetValueInt(HncChannel.HNC_CHAN_MODAL, channel, 0, clientNo));
		
		return dataRun;
	}	
	
	
	//得到机床报警信息,因为可以同时存在多个报警，所以报警信息用列表
	public static LinkedList<DataAlarm> getAlarmData(int clientNo)
	{		
		LinkedList<DataAlarm> listDataAlarm = new LinkedList<DataAlarm>();
		//采集报警信息
		int curAlarmNum = HncAPI.HNCAlarmGetNum(AlarmType.ALARM_TYPE_ALL, AlarmLevel.ALARM_ERR, clientNo);//报警信息条数
		for (int index = 0; index < curAlarmNum; index++)
		{
			DataAlarm dataAlarm = new DataAlarm();
			String strAlarmData = HncAPI.HNCAlarmGetData(AlarmType.ALARM_TYPE_ALL,  //读取报警信息
					AlarmLevel.ALARM_ERR, index, clientNo);
			if(strAlarmData != null)
			{				
				int tmpPosition = strAlarmData.indexOf("::");
				String strAlarmNO = strAlarmData.substring(0,tmpPosition);			//报警号
				strAlarmData = strAlarmData.substring(tmpPosition+2,strAlarmData.length());//报警信息
				
				dataAlarm.setNo(strAlarmNO);
				dataAlarm.setCtt(strAlarmData);		
				listDataAlarm.add(dataAlarm);
			}
		}
		return listDataAlarm;
	}
		
}

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
	 * �ɼ������Ļ�����Ϣ,��������ϵͳ�����кź͸��ְ汾��Ϣ
	 */
	public static DataReg getMacInfo(int clientNo) {
		DataReg dataReg = new DataReg();//ϵͳע����Ϣ
		dataReg.setId(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_SN_NUM, clientNo));//ϵͳID 
		
		DataVer dataVer = new DataVer();//ϵͳ�汾��
		dataVer.setCNC_VER(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_CNC_VER, clientNo));
		dataVer.setDRV_VER(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_DRV_VER, clientNo));
		dataVer.setNC_VER(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_NC_VER, clientNo));
		dataVer.setNCK_VER(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_NCK_VER, clientNo));
		dataVer.setPLC_VER(HncAPI.HNCSystemGetValueString(HncSystem.HNC_SYS_NCK_VER, clientNo));
		
		dataReg.setVer(JsonUtil.object2Json(dataVer));//�����쳣	
		
		return dataReg;
	}
	
	//�ɼ�������Ϣ
	public static DataRun getDataRun(int clientNo,int channel) {
		DataRun dataRun = new DataRun();
		//����ʵ���ٶ� ��ת�٣�
		dataRun.setCas((float)HncAPI.HNCChannelGetValueDouble(HncChannel.HNC_CHAN_ACT_SPDL_SPEED, channel, 0, clientNo));
		//����ָ���ٶ� ��ת�٣�
		dataRun.setCcs((float)HncAPI.HNCChannelGetValueDouble(HncChannel.HNC_CHAN_CMD_SPDL_SPEED, channel, 0, clientNo));
		//���Ḻ�ص���,���е������ǵ������
		dataRun.setAload((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_LOAD_CUR, 5, clientNo));
		
		//��1ʵ���ٶ�, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setAspd1((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_VEL, AxisType.X, clientNo));
		//��2ʵ���ٶ�, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setAspd2((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_VEL, AxisType.Y, clientNo));
		//��3ʵ���ٶ�, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setAspd3((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_VEL, AxisType.Z, clientNo));
		dataRun.setAspd4(0);//��4û�У�����Ϊ��
		dataRun.setAspd5(0);//��5û�У�����Ϊ��
		
		//��1ʵ��λ��, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setApst1((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_POS_RCS, AxisType.X, clientNo));
		//��2ʵ��λ��, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setApst2((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_POS_RCS, AxisType.Y, clientNo));
		//��3ʵ��λ��, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setApst3((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_ACT_POS_RCS, AxisType.Z, clientNo));
		dataRun.setApst4(0);
		dataRun.setApst5(0);
		
		//��1ָ��λ��, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setCpst1((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_CMD_POS_RCS, AxisType.X, clientNo));
		//��2ָ��λ��, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setCpst2((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_CMD_POS_RCS, AxisType.Y, clientNo));
		//��3ָ��λ��, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setCpst3((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_CMD_POS_RCS, AxisType.Z, clientNo));
		dataRun.setCpst4(0);
		dataRun.setCpst5(0);
		
		//��1���ص���, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setLoad1((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_LOAD_CUR, AxisType.X, clientNo));
		//��2���ص���, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setLoad2((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_LOAD_CUR, AxisType.Y, clientNo));
		//��3���ص���, i=0,X�᣻i=1,Y�᣻i=2,Z�᣻
		dataRun.setLoad3((float)HncAPI.HNCAxisGetValueDouble(HncAxis.HNC_AXIS_LOAD_CUR, AxisType.Z, clientNo));
		dataRun.setLoad4(0);
		dataRun.setLoad5(0);
		
		//���г�����
		dataRun.setPd((short)HncAPI.HNCChannelGetValueInt(HncChannel.HNC_CHAN_RUN_PROG, channel, 0, clientNo));
//		dataRun.setPn(pn);//����  ������
//		dataRun.setPs(ps);//����  ��������״̬
		//���д����к�
		dataRun.setPl(HncAPI.HNCChannelGetValueInt(HncChannel.HNC_CHAN_RUN_ROW, channel, 0, clientNo));
		//G����ģ̬
		dataRun.setPm((short)HncAPI.HNCChannelGetValueInt(HncChannel.HNC_CHAN_MODAL, channel, 0, clientNo));
		
		return dataRun;
	}	
	
	
	//�õ�����������Ϣ,��Ϊ����ͬʱ���ڶ�����������Ա�����Ϣ���б�
	public static LinkedList<DataAlarm> getAlarmData(int clientNo)
	{		
		LinkedList<DataAlarm> listDataAlarm = new LinkedList<DataAlarm>();
		//�ɼ�������Ϣ
		int curAlarmNum = HncAPI.HNCAlarmGetNum(AlarmType.ALARM_TYPE_ALL, AlarmLevel.ALARM_ERR, clientNo);//������Ϣ����
		for (int index = 0; index < curAlarmNum; index++)
		{
			DataAlarm dataAlarm = new DataAlarm();
			String strAlarmData = HncAPI.HNCAlarmGetData(AlarmType.ALARM_TYPE_ALL,  //��ȡ������Ϣ
					AlarmLevel.ALARM_ERR, index, clientNo);
			if(strAlarmData != null)
			{				
				int tmpPosition = strAlarmData.indexOf("::");
				String strAlarmNO = strAlarmData.substring(0,tmpPosition);			//������
				strAlarmData = strAlarmData.substring(tmpPosition+2,strAlarmData.length());//������Ϣ
				
				dataAlarm.setNo(strAlarmNO);
				dataAlarm.setCtt(strAlarmData);		
				listDataAlarm.add(dataAlarm);
			}
		}
		return listDataAlarm;
	}
		
}

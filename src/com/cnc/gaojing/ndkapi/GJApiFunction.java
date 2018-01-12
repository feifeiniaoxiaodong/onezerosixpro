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
 * �ӻ�����ȡ���ݹ�����
 */
public class GJApiFunction {
	
	private  static final String TAG="Alarm Information Test...";
		
	/**
	 * ��ȡ������Ϣ
	 * ��ȡ��ֻ�ǵ�ǰ������Ϣ�����û�б�����������null
	 * �����źͱ�����Ϣ���������ȡ���ַ�����Ƿ���һ���
	 * @return
	 */
	public static  LinkedList<DataAlarm>  getDataAlarm(){
		LinkedList<DataAlarm> alarmlist=new LinkedList<DataAlarm>(); //�½�������Ϣ�б����
		
		String  alarmNo,  	//������
				alarmNum,	//����������
				alarmInfo;	//������Ϣ
		
		String  alarmMotion,//�˶�����
				alarmPLC;	 //PLC����
//		public static String getAutoMotionErrorVal()��ǰ�˶�����
//		public static String getAutoPlcErrorVal()��ǰPLC����
		
		//��ȡ��ǰ�˶�������Ϣ
		alarmMotion = dnc.main.getAutoMotionErrorVal();
		if(alarmMotion!= null){
			DataAlarm  alarmobj=new DataAlarm();

			alarmNo = alarmMotion.substring(0, alarmMotion.indexOf(':'));
			alarmNum= alarmNo.substring(alarmNo.indexOf(" ")+1);
			
			alarmInfo=AlarmResources.getAlarmInfoById("MOTION",alarmNum);
			
			alarmobj.setNo(alarmNo);
			alarmobj.setCtt(alarmInfo);
			
			alarmlist.add(alarmobj);
			
			Log.d(TAG,alarmobj.toString()); //��ӡ������Ϣ
		}
		
		//��ǰPLC����
		alarmPLC  =  dnc.main.getAutoPlcErrorVal() ;		
		if(alarmPLC!=null){			
			DataAlarm  alarmobj=new DataAlarm();
			
			alarmNo=alarmPLC.substring(0, alarmPLC.indexOf(":"));
			alarmNum=alarmNo.substring( alarmNo.indexOf(" ")+1);
			
			alarmInfo=AlarmResources.getAlarmInfoById("PLC",alarmNum);
			
			alarmobj.setNo(alarmNo);
			alarmobj.setCtt(alarmInfo);
			
			alarmlist.add(alarmobj);
		
			Log.d(TAG,alarmobj.toString()); //��ӡ������Ϣ
		}
		
		//����Ƿ��м�ͣ������Ϣ
		if(dnc.main.getStatusIntVal(2006)==1){
			DataAlarm  alarmobj=new DataAlarm();
			
			alarmobj.setNo("MOTION 5001");
			alarmobj.setCtt("��ͣ����");			
			
			alarmlist.add(alarmobj); 
			
			Log.d(TAG,alarmobj.toString()); //��ӡ������Ϣ
		}
					
		return alarmlist;		
	}
	
	/**
	 * ��ȡ������Ϣ
	 * @return
	 */
	public static DataRun getDataRun(){
		DataRun  datarun=new DataRun();//�½�������Ϣ����
		
		datarun.setCas((float)dnc.main.getStatusDoubleVal(GJApiNum.SPINDLE_ACT_SPEED));//����ʵ���ٶ�
		datarun.setCcs((float)dnc.main.getStatusDoubleVal(GJApiNum.SPINDLE_COM_SPEED));//����ָ���ٶ�
		datarun.setAload((float)dnc.main.getStatusDoubleVal(GJApiNum.SPINDLE_LOAD_CURRENT));//���Ḻ�ص���
		
		datarun.setAspd1((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_X)); //������ʵ��ת��X
		datarun.setAspd2((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_Y)); //������ʵ��ת��Y
		datarun.setAspd3((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_Z)); //������ʵ��ת��Z
//		datarun.setAspd1((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED)); //������ʵ��ת��X
		datarun.setAspd2(0); //������ʵ��ת��Y
		datarun.setAspd3(0); //������ʵ��ת��Z
		datarun.setAspd4(0);
		datarun.setAspd5(0);
		
		datarun.setApst1((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_X));//������ʵ��λ��
		datarun.setApst2((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_Y));
		datarun.setApst3((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_Z));
		datarun.setApst4(0);
		datarun.setApst5(0);
		datarun.setCpst1((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_X));//������ָ��λ��
		datarun.setCpst2((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_Y));
		datarun.setCpst3((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_Z));
		datarun.setCpst4(0);
		datarun.setCpst5(0);
		datarun.setLoad1((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_X));//�����Ḻ�ص���
		datarun.setLoad2((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_Y));
		datarun.setLoad3((float)dnc.main.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_Z));
		datarun.setLoad4(0);
		datarun.setLoad5(0);
		
		datarun.setPd((short)0);  //���г�����,��δ��
		datarun.setPn(dnc.main.getStatusStringVal(GJApiNum.PROGRAM_NAME)); //��ǰ������
		datarun.setPs(dnc.main.getStatusStringVal(GJApiNum.G_CODE_RUN_STAT));//��������״̬
		datarun.setPl(dnc.main.getStatusIntVal(GJApiNum.G_CODE_LINE_NUM));// ���������к�
		
		int [] Gmode=dnc.main.getStatusArrayVal(GJApiNum.G_CODE_MODALITY); //G����ģ̬,int[20]
		datarun.setPm((short)Gmode[0]); //��������ģ̬
	
		return datarun;
	}
	
	/**
	 * ��ȡע����Ϣ	
	 * @return
	 */
	public static DataReg getDataReg(){
		String tmp=null;
		
		DataReg datareg=new DataReg();  //�½�ע����Ϣ����
		
		DataVersion dataver=new DataVersion();//�½��汾��Ϣ����
		dataver.setCnc_system_model(dnc.main.getStatusStringVal(GJApiNum.SYSTEM_MODEL));//ϵͳ�ͺ�
		dataver.setSoftware_number(dnc.main.getStatusStringVal(GJApiNum.SOFTWARE_VER));//����汾��
		dataver.setHardware_number(dnc.main.getStatusStringVal(GJApiNum.HARDWARE_VER));//Ӳ���汾��
		dataver.setSystem_kernel_number(dnc.main.getStatusStringVal(GJApiNum.SYSTEMKERNEL_VER));//ϵͳ�ں˰汾��
			
//		datareg.setId(DaqData.getAndroidId());  //�߾���ʱʹ��AndroidID ��������ϵͳ�ɣ�,���Ϊ��IP��ַ����
		datareg.setVer(JsonUtil.toJson(dataver)); //�汾��Ϣ
		datareg.setTp(dnc.main.getStatusStringVal(GJApiNum.SYSTEM_MODEL)); //����ϵͳ�ͺ�
		
		return datareg;		
	}
	
	/**
	 * ��ȡ��¼��Ϣ
	 * @return
	 */
	public static DataLog getDataLog(){
		DataLog datalog=new DataLog();// �½���¼��Ϣ����
	
		datalog.setOntime((long)dnc.main.getStatusDoubleVal(GJApiNum.TOTAL_RUN_TIME) );   //�ۼ�����ʱ��
		datalog.setRuntime((long)dnc.main.getStatusDoubleVal(GJApiNum.TOTAL_PROCESS_TIME)); //�ۼƼӹ�ʱ��
		
		return datalog;	
	}

}

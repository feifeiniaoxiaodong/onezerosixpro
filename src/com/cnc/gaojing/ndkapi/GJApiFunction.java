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
 * �����߾�
 * �ӻ�����ȡ���ݹ�����
 */
public class GJApiFunction {
	
	private  static final String TAG="Alarm Information Test...";
	
	dnc.main  dncmian ;
	public GJApiFunction(dnc.main dncmian){
		this.dncmian=dncmian;
	}
	
	/**
	 * ��ȡ������Ϣ
	 * ��ȡ��ֻ�ǵ�ǰ������Ϣ�����û�б�����������null
	 * �����źͱ�����Ϣ���������ȡ���ַ�����Ƿ���һ���
	 * @return
	 */
	public   LinkedList<DataAlarm>  getDataAlarm(){
		LinkedList<DataAlarm> alarmlist=new LinkedList<DataAlarm>(); //������Ϣ�б�
		
		String  alarmNo,  	//������
				alarmNum,	//����������
				alarmInfo;	//������Ϣ
		
		String  alarmMotion,//�˶�����
				alarmPLC;	 //PLC����
//		public static String getAutoMotionErrorVal()��ǰ�˶�����
//		public static String getAutoPlcErrorVal()��ǰPLC����
		
		//��ȡ��ǰ�˶�������Ϣ
		alarmMotion = dncmian.getAutoMotionErrorVal();
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
		alarmPLC  = dncmian.getAutoPlcErrorVal() ;		
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
		if(dncmian.getStatusIntVal(2006)==1){
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
	public  DataRun getDataRun(){
		DataRun  datarun=new DataRun();//�½�������Ϣ����
		
		datarun.setCas((float)dncmian.getStatusDoubleVal(GJApiNum.SPINDLE_ACT_SPEED));//����ʵ���ٶ�
		datarun.setCcs((float)dncmian.getStatusDoubleVal(GJApiNum.SPINDLE_COM_SPEED));//����ָ���ٶ�
		datarun.setAload((float)dncmian.getStatusDoubleVal(GJApiNum.SPINDLE_LOAD_CURRENT)/100);//���Ḻ�ص���
		// 
		datarun.setAspd1((float)(dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_X)*6)); //������ʵ��ת��X����������ٶ�ת��Ϊ��ת�٣���λr/min
		datarun.setAspd2((float)(dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_Y)*6)); //������ʵ��ת��Y
		datarun.setAspd3((float)(dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED_Z)*6)); //������ʵ��ת��Z
//		datarun.setAspd1((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_SPEED)); //������ʵ��ת��X
		datarun.setAspd2(0); //������ʵ��ת��Y
		datarun.setAspd3(0); //������ʵ��ת��Z
		datarun.setAspd4(0);
		datarun.setAspd5(0);
		
		datarun.setApst1((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_X));//������ʵ��λ��
		datarun.setApst2((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_Y));
		datarun.setApst3((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_ACT_POSITION_Z));
		datarun.setApst4(0);
		datarun.setApst5(0);
		datarun.setCpst1((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_X));//������ָ��λ��
		datarun.setCpst2((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_Y));
		datarun.setCpst3((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_COM_POSITION_Z));
		datarun.setCpst4(0);
		datarun.setCpst5(0);
		datarun.setLoad1((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_X));//�����Ḻ�ص���
		datarun.setLoad2((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_Y));
		datarun.setLoad3((float)dncmian.getStatusDoubleVal(GJApiNum.FEEDAXIS_LOAD_CURRENT_Z));
		datarun.setLoad4(0);
		datarun.setLoad5(0);
		
		datarun.setPd((short)0);  //���г�����,��δ��
		datarun.setPn(dncmian.getStatusStringVal(GJApiNum.PROGRAM_NAME)); //��ǰ������
		datarun.setPs(dncmian.getStatusStringVal(GJApiNum.G_CODE_RUN_STAT));//��������״̬
		datarun.setPl(dncmian.getStatusIntVal(GJApiNum.G_CODE_LINE_NUM));// ���������к�
		
		int [] Gmode=dncmian.getStatusArrayVal(GJApiNum.G_CODE_MODALITY); //G����ģ̬,int[20]
		datarun.setPm((short)Gmode[0]); //��������ģ̬
	
		return datarun;
	}
	
	/**
	 * ��ȡע����Ϣ	
	 * @return
	 */
	public  DataReg getDataReg(){
		String tmp=null;
		
		DataReg datareg=new DataReg();  //�½�ע����Ϣ����
		
		DataVersion dataver=new DataVersion();//�½��汾��Ϣ����
		dataver.setCnc_system_model(dncmian.getStatusStringVal(GJApiNum.SYSTEM_MODEL));//ϵͳ�ͺ�
		dataver.setSoftware_number(dncmian.getStatusStringVal(GJApiNum.SOFTWARE_VER));//����汾��
		dataver.setHardware_number(dncmian.getStatusStringVal(GJApiNum.HARDWARE_VER));//Ӳ���汾��
		dataver.setSystem_kernel_number(dncmian.getStatusStringVal(GJApiNum.SYSTEMKERNEL_VER));//ϵͳ�ں˰汾��
			
//		datareg.setId(DaqData.getAndroidId());  //�߾���ʱʹ��AndroidID ��������ϵͳ�ɣ�,���Ϊ��IP��ַ����
		datareg.setVer(JsonUtil.toJson(dataver)); //�汾��Ϣ
		datareg.setTp(dncmian.getStatusStringVal(GJApiNum.SYSTEM_MODEL)); //����ϵͳ�ͺ�
		
		return datareg;		
	}
	
	/**
	 * ��ȡ��¼��Ϣ
	 * @return
	 */
	public  DataLog getDataLog(){
		DataLog datalog=new DataLog();// �½���¼��Ϣ����
	
		datalog.setOntime((long)dncmian.getStatusDoubleVal(GJApiNum.TOTAL_RUN_TIME) );   //�ۼ�����ʱ��
		datalog.setRuntime((long)dncmian.getStatusDoubleVal(GJApiNum.TOTAL_PROCESS_TIME)); //�ۼƼӹ�ʱ��
		
		return datalog;	
	}

}

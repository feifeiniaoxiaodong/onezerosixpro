package com.cnc.daqnew;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.cnc.DataBaseService.DBService;
import com.cnc.daq.DaqActivity;
import com.cnc.daq.DaqData;
import com.cnc.daq.MyApplication;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;

import android.R;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MsgDealHandler extends Handler {
	
	private final String TAG = "MsgDealHandler...";	
	private DaqActivity daqActivity=null;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//ʱ�����ʽ
	
	public MsgDealHandler() {}
	
	public MsgDealHandler(DaqActivity daqactivity) {
		this.daqActivity=daqactivity;
	}
	
	int count = 1;	//��¼�ɼ��Ĵ���
	@Override
	public void handleMessage(Message msg) {
		
		super.handleMessage(msg);
				
		 switch(msg.what) {
         case HandleMsgTypeMcro.MSG_ISUCCESS://��ʼ���ɹ� 0
         	Log.i(TAG, (String)msg.obj);            		
             break;
         case HandleMsgTypeMcro.MSG_IFAILURE://��ʼ��ʧ�� 1
         	Log.i(TAG, (String)msg.obj);//��ӡ��������Ϣ������������
//         	stopSelf();//�ر��������        	
//         	daqActivity.stopServiceActivity();//�رշ���
             break;
         case HandleMsgTypeMcro.MSG_LFAILURE://��¼��������ʧ�� 2
         	Log.i(TAG, (String)msg.obj);//��ӡ��������Ϣ������������
//         	stopSelf();//�ر��������
            break;
         case HandleMsgTypeMcro.MSG_DFAILURE://��ȡ��Ϣʧ�ܣ��������� 3
         	Log.i(TAG, (String)msg.obj);//��ӡ��������Ϣ������������
//         	stopSelf();//�ر��������
             break;
/**************************�����Ǳ�����Ϣ��������************************************************/               
         case HandleMsgTypeMcro.MSG_ALRAM://������Ϣ 10
         	@SuppressWarnings("unchecked")
				LinkedList<DataAlarm> listDataAlarm = (LinkedList<DataAlarm>)msg.obj;
         	showDataAlarm(listDataAlarm);
         	String strTime=formatter.format(new Date()); 
         	
         	if(DaqData.getListDataAlarm().size() == 0){//�����ǰ�ı�����¼Ϊ�㣬��ɼ����ı�������Ϊ�㣬˵�����µı�������
         	
         		for (DataAlarm dataAlarm : listDataAlarm) {
						dataAlarm.setF((byte)0);	//��ʶ���Ǳ�������
						DaqData.getListDataAlarm().add(dataAlarm);//��¼��ǰ���еı���
//						alarmHandle(dataAlarm); //���汨����Ϣ�����ݿ�
						DBService.getInstanceDBService().saveAlarmData(dataAlarm);
					}
         	}else{	//�����ǰ�Ѿ����˱�����¼
         	
					if(listDataAlarm.size() == 0)//���б�������βɼ�����������Ϣ�����Ա���ȫ������
					{
						for (DataAlarm dataAlarm : DaqData.getListDataAlarm()) 
						{
							dataAlarm.setF((byte)1);//��ʶ���Ǳ�������
							dataAlarm.setTime(strTime);//��������ʱ��
							alarmHandle(dataAlarm);
						}
						DaqData.getListDataAlarm().clear();//����������
					}
					else //���б����ʹ˴α�������Ϊ�㣬���������ӣ�����Ҳ�����ټ�
					{
						for (DataAlarm dataAlarm : listDataAlarm) {
							if(DaqData.whetherNewAlarm(dataAlarm))//ȷ���Ƿ����µı�����Ϣ
							{
								dataAlarm.setF((byte)0);//��ʶ���Ǳ�������
								DaqData.getListDataAlarm().add(dataAlarm);//��ӵ���ǰ������¼
//								alarmHandle(dataAlarm);//����������ӵ�SQLite���ݿ���
								DBService.getInstanceDBService().saveAlarmData(dataAlarm);
							}
						}
						for (DataAlarm dataAlarm : DaqData.getListDataAlarm()) //�ҳ���ǰ������ı�����Ϣ
						{	//���������¼���У�����ǰ������ȴû���ҵ��ñ�����˵������������Ϣ�Ѿ�����
							if(DaqData.whetherRelAlarm(dataAlarm,listDataAlarm))//ȷ�ϱ�����Ϣ��¼���Ѿ���ʧ
							{
								DaqData.getListDataAlarm().remove(dataAlarm);
								dataAlarm.setF((byte)1);//��ʶ���Ǳ�������
								dataAlarm.setTime(strTime);//��������ʱ��
//								alarmHandle(dataAlarm);//����������ӵ�SQLite���ݿ���	
								DBService.getInstanceDBService().saveAlarmData(dataAlarm);
							}
						}
					}
				}             	        		
         	break;
/**************************������ע����Ϣ��������Ϣ�Ĵ���************************************************/ 
         case HandleMsgTypeMcro.MSG_REG://�õ������Ļ�����Ϣ 11��ע����Ϣ
//         	DataReg dataReg = (DataReg)msg.obj;
//         	DaqData.setDataReg(dataReg);       //����ע����Ϣ
//         	ChildHandler.sendMessage(msg);//��ע����Ϣ�͵��������߳�ȥ����       	
//         	showMacInfo(dataReg);            	
            break;
         case HandleMsgTypeMcro.MSG_RUN://�õ������еĻ�����Ϣ 12��������Ϣ            	
         	DataRun dataRun = (DataRun)msg.obj;
         	count = msg.arg1;
         	showRunInfo(count, dataRun);  
         	//������һ���ǲ��Դ���
         	DBService.getInstanceDBService().saveRunData(dataRun);//ֱ�Ӵ������ݿ�
            break; 
         case HandleMsgTypeMcro.MSG_TEST://������Ϣ
         	Log.d(TAG, "������Ϣ");            	
             break;
         case HandleMsgTypeMcro.MSG_DELAYTIME://��ʱʱ��
        	 
        	 daqActivity.setTvDelayTime((String)msg.obj);
        	 break;
         case HandleMsgTypeMcro.MSG_COUNTRUN: //��ʾ�������ݿ���δ���͵�������Ϣ������
        	 daqActivity.setTvCount_runinfo((String)msg.obj);
        	 break;
         default:
        	 break;
        	 
         }//end switch()
     }//end handleMessage(Message msg)
     
	
	
 	//��������DataAlarm dataAlarm
 	private void alarmHandle ( DataAlarm dataAlarm){ 	
 		DBService.getInstanceDBService().saveAlarmData(dataAlarm);		
 	}
		
		
	private void showRunInfo(int count, DataRun dataRun){	
		Log.d(TAG,count + "--" + dataRun.getId()+ "::" + dataRun.getTime());		
	}
	
	private void showMacInfo(DataReg dataReg) {
		// TODO Auto-generated method stub
		Log.d(TAG, "macInfo.SN_NUM::" + dataReg.getId());
		Log.d(TAG, "macInfo.VER::" + dataReg.getVer());
		Log.d(TAG, "macInfo.TIME::" + dataReg.getTime());		
	}	

	private void showDataAlarm(LinkedList<DataAlarm> listDataAlarm) {	
		for (DataAlarm dataAlarm : listDataAlarm) 
		{
			Log.d(TAG, dataAlarm.getNo()+"+" + dataAlarm.getCtt() + ":" + dataAlarm.getTime());
		}		
	}	
		
	
}

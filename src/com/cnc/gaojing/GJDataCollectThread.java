package com.cnc.gaojing;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import com.cnc.daq.DaqActivity;
import com.cnc.daq.DaqData;
import com.cnc.daq.MainActivity;
import com.cnc.daqnew.DataCollectInter;
import com.cnc.daqnew.HandleMsgTypeMcro;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.DataType;
import com.cnc.domain.UiDataAlarmRun;
import com.cnc.domain.UiDataNo;
import com.cnc.gaojing.ndkapi.GJApiFunction;
import com.cnc.service.DelMsgServie;
import com.cnc.utils.AlarmFilterList;

//import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * �����߾����ݲɼ��߳�
 * @author wei
 *
 */
 public class GJDataCollectThread implements Runnable,DataCollectInter {

	private final String TAG="DataCollectThread...";
	 //�߳�ѭ��ִ�б�־����Ϊfalseʱ���߳��˳�ѭ�����߳̽�������
    private volatile  boolean  threadflag=true; 
	boolean boolGetMacInfo = false; //��ʶ�Ƿ�õ������Ļ�����Ϣ	
	int     count = 1;     //�洢������Ϣ��id,��ʶ���ǵڼ��βɼ���Ϣ
	boolean hadconnected =false ;   //����״̬��־	
	private Handler  
					 delMsgHandler =null,
					 mainActivityHandler=null;
	final private String  machineIP ;//= "192.168.188.132"; //������IP��ַ
//	int    machinePort = 21;			  //�����˿ںţ��߾�����Ҫ���ö˿ں�
//	String  tp = "SYGJ-1000"; //����ϵͳ�ͺţ��߾�����ϵͳ�ͺ�����ϵͳ�ṩ
	final private String machine_SN ;//=machineIP; //����ϵͳID�������߾�û���ṩ����ϵͳID������IP����ID
	private AlarmFilterList   alarmFilterList =null; //������Ϣ������˶���
	
	private GJApiFunction gjApiFunction =null;
	
	public GJDataCollectThread(String ip){
		this(ip,0);
	}
	
	public GJDataCollectThread(String ip,int port){
		machineIP=ip;
		machine_SN="G"+ip;
		delMsgHandler=DelMsgServie.getHandlerService();
		mainActivityHandler=MainActivity.getMainActivityHandler();
		this.alarmFilterList=new AlarmFilterList(delMsgHandler);
	}
		
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//ʱ�����ʽ
	
	@Override
	public void run() {
		dnc.main dncmain =new dnc.main();
		
		gjApiFunction =new GJApiFunction(dncmain);
//		dnc.main.connectToNC(machineIP);  //���ӵ�����
		dncmain.connectToNC(machineIP);
		//���������ӻ������ܼ�⵽nml�ļ�
		if(dncmain.status_nml==null){   //����Ƿ����nml�����ļ�
			Log.d(TAG,"�߾��Ҳ���nml�����ļ�");			
		}
		
		while(threadflag){
			
			if(!dnc.main.getConnnectState()){ //�������״̬
				//δ���ӣ���������				
				dncmain.connectToNC(machineIP);  //���ӵ�����
				
				//���������ӻ������ܼ�⵽nml�ļ�
//				if(dnc.main.status_nml==null){ //����Ƿ����nml�����ļ�
//					Log.d(TAG,"�Ҳ���nml�����ļ�");			
//				}			
				if(dnc.main.getConnnectState()  && dncmain.status_nml!=null){ 
//					hadconnected=true;
					sendMsg2Main("�߾����ӻ����ɹ�", HandleMsgTypeMcro.MSG_ISUCCESS); //					
				}else{
					sendMsg2Main("�߾����ӻ���ʧ��", HandleMsgTypeMcro.MSG_LFAILURE); //
					Log.d(TAG,"�Ҳ���nml�����ļ�");
					 //����ʧ�ܣ�һ���Ӻ�������
					try{
						Thread.sleep(1000*60);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else{ //�Ѿ������ϻ���
				//������,�ɼ�����
				dncmain.updateStatus();//���±�������
				if(dncmain.msg != null)//�����ݸ���
                {
					//�ɼ�����
					daq();  //�ɼ����ݣ����͵�serviceȥ����						
					dncmain.msg=null;//���msg��־
                }else{
                	Log.i(TAG,"dnc.main.msgΪ�գ�");
                }				
			}
			
			//�ɼ����ݼ��ʱ��,�ɼ�����֮���߳�����һ��ʱ��
			try {
				Thread.sleep(1000);//�ɼ����ݼ��ʱ������Ϊ1S,��Ϊ�ɼ����̺�ʱ��Լ300���룬��������Ϊ700
			} catch (InterruptedException e) {				
				e.printStackTrace();
			} 	
		}//end while()
		
		if(dnc.main.getConnnectState()){
			dncmain.disconnectToNC();	//��ָ���쳣		
		}
				
	} //end run()
		
	/**
	 * ���ݲɼ�����
	 */
	private void daq() {
		
		String strTime = formatter.format(new Date());//��ʼ�ɼ���Ϣ�ĸ����¼���ʱ���
		
		if(!boolGetMacInfo)   //���û�л�ù������Ļ�����Ϣ
		{
//			DaqData.setCncid(machineIP); //��IP��ַ���������ID
			//ע����Ϣ
			DataReg dataReg = gjApiFunction.getDataReg();//��ȡע����Ϣ					
			dataReg.setTime(strTime);		//���òɼ���ʱ���
			dataReg.setId(machine_SN);      //����ID��

//			sendMsg2Main(dataReg, HandleMsgTypeMcro.MSG_REG);//��ʼ���ɹ�
			
			DataLog datalog=gjApiFunction.getDataLog();//��ȡ��¼��Ϣ
			datalog.setId(machine_SN);
			datalog.setTime(strTime);
			
			DaqData.getListDataLog().add(datalog); //�����¼��Ϣ
			DaqData.getListDataReg().add(dataReg); //����ע����Ϣ
			
			UiDataNo uiDataNo=new UiDataNo("",machineIP,machine_SN , DaqData.getAndroidId());
			sendMsg(mainActivityHandler,uiDataNo,HandleMsgTypeMcro.GAOJING_UINO,0,0); //������Ϣ�������ʾIP��ַ��Ϣ
			
			boolGetMacInfo = true;//��Ϊtrue�������Ѿ��õ��˻���������Ϣ
			
		}else{   //�ɼ�������Ϣ�ͱ�����Ϣ
			StringBuilder  sbalram=new StringBuilder();//������Ϣ
			
			//�ɼ�������Ϣ
			LinkedList<DataAlarm> listDataAlarm =gjApiFunction.getDataAlarm();
			for (DataAlarm dataAlarm : listDataAlarm) {
				dataAlarm.setId(machine_SN);//��������ϵͳID
				dataAlarm.setTime(strTime);//���òɼ���ʱ���
				sbalram.append(dataAlarm.getCtt()).append(":");
			}
			
			//����ɼ����ı�����Ϣ��Ϊ��������еı�����Ϣ��Ϊ�㣬��ô��Ҫ�Ա�����Ϣ���з���
			//�Ա�����Ϣ���д���,����Ҫ�жϱ�����Ϣ�������Ƿ����������ǽ��������������������������߳���
			if ((listDataAlarm.size() != 0)||(!alarmFilterList.getNowAlarmList().isEmpty())){
				alarmFilterList.saveCollectedAlarmList(listDataAlarm);
			}
			
			//�ɼ�������Ϣ
			DataRun dataRun =  gjApiFunction .getDataRun();
			dataRun.setId(machine_SN); //��������ϵͳID
			dataRun.setTime(strTime); //��ʱ���
			sendMsg2Main(dataRun,HandleMsgTypeMcro.MSG_RUN,count);
					
			count++;//�ɼ�������¼
			if(count == Integer.MAX_VALUE)//�ﵽ���ֵ��ʱ��ǵ�����
				count = 1;	
			
			//���͵����߳�
			UiDataAlarmRun uiDataAlarmRun=new UiDataAlarmRun(sbalram.toString(), dataRun.toString());
			sendMsg(mainActivityHandler, uiDataAlarmRun, HandleMsgTypeMcro.GAOJING_UIALARM	, 0, 0);
		}
	}    //end daq()
		
	/**
	 * ������Ϣ�����߳�
	 * @param obj
	 * @param what
	 */
	private  void sendMsg2Main(Object obj, int what) {	
		sendMsg(delMsgHandler, obj, what,0,0);
	}
	
	/**
	 * ������Ϣ�����߳�
	 */
	private  void sendMsg2Main(Object obj, int what, int arg1) {
		sendMsg(delMsgHandler, obj, what, arg1, 0);
	}

	/**
	 * ������Ϣ��ͨ����
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
	
	/**
	 * ��ȡResource.properties ��ֵ
	 * @param key
	 * @return
	 */
/*	private String  getPath(String key){
		InputStream in=null;
		Properties pro=null;
		String path=null;
		
		pro =new Properties();
		in=GJDataCollectThread.class.getResourceAsStream(DataType.pathResource);
		
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

	@Override
	public void stopCollect() {
		// TODO Auto-generated method stub
		threadflag=false;
	}

	@Override
	public boolean isThreadRunning() {
		// TODO Auto-generated method stub
		  return threadflag;
	}

}

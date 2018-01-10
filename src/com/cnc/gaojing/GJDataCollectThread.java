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
import com.cnc.daqnew.DataCollectInter;
import com.cnc.daqnew.HandleMsgTypeMcro;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.DataType;
import com.cnc.gaojing.ndkapi.GJApiFunction;
import com.cnc.service.DelMsgServie;

//import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

//�����߾����ݲɼ��߳�
 public class GJDataCollectThread implements Runnable,DataCollectInter {

	private final String TAG="DataCollectThread...";
	 //�߳�ѭ��ִ�б�־����Ϊfalseʱ���߳��˳�ѭ�����߳̽�������
    private volatile  boolean  threadflag=true; 
	boolean boolGetMacInfo = false; //��ʶ�Ƿ�õ������Ļ�����Ϣ	
	int     count = 1;     //�洢������Ϣ��id,��ʶ���ǵڼ��βɼ���Ϣ
	boolean hadconnected =false ;   //����״̬��־	
	private Handler  daqActivityHandler=null,
					 delMsgHandler =null;
	String  machineIP = "192.168.188.132"; //������IP��ַ
//	int    machinePort = 21;			  //�����˿ںţ��߾�����Ҫ���ö˿ں�
//	String  tp = "SYGJ-1000"; //����ϵͳ�ͺţ��߾�����ϵͳ�ͺ�����ϵͳ�ṩ
//	static  String machine_SN="192.168.188.132"; //����ϵͳID�������߾�û���ṩ����ϵͳID������IP����ID
//	String key;
	
	GJDataCollectThread(String ip,int port){
		machineIP=ip;
		delMsgHandler=DelMsgServie.getHandlerService();
		daqActivityHandler=DaqActivity.getmHandler();
	}
	
	GJDataCollectThread(String key){
//		daqActivityHandler=mHandler;
		daqActivityHandler=DaqActivity.getmHandler();
//		this.key=key;
	}
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//ʱ�����ʽ
	
	@Override
	public void run() {
		/*String str=getPath(key);
		if(str!=null){
			machineIP=str;
		}*/
		
		dnc.main.connectToNC(machineIP);  //���ӵ�����
		//���������ӻ������ܼ�⵽nml�ļ�
		if(dnc.main.status_nml==null){ //����Ƿ����nml�����ļ�
			Log.d(TAG,"�Ҳ���nml�����ļ�");			
		}
		
		while(threadflag){
			
			if(!dnc.main.getConnnectState()){ //�������״̬
				//δ���ӣ���������				
				dnc.main.connectToNC(machineIP);  //���ӵ�����
				
				//���������ӻ������ܼ�⵽nml�ļ�
//				if(dnc.main.status_nml==null){ //����Ƿ����nml�����ļ�
//					Log.d(TAG,"�Ҳ���nml�����ļ�");			
//				}				
				if(dnc.main.getConnnectState()  && dnc.main.status_nml==null){ 
					hadconnected=true;
					sendMsg2Main("���ӻ����ɹ�", HandleMsgTypeMcro.MSG_ISUCCESS); //
					Log.d(TAG,"�Ҳ���nml�����ļ�");
				}else{
					sendMsg2Main("���ӻ���ʧ��", HandleMsgTypeMcro.MSG_LFAILURE); //
				}
				 
				try{
					Thread.sleep(1000*10); //һ���Ӻ�������
				}catch(Exception e){
					e.printStackTrace();
				}
	 
			}else{ //�Ѿ������ϻ���
				//������,�ɼ�����
				dnc.main.updateStatus();//���±�������
				if(dnc.main.msg != null)//�����ݸ���
                {
					//�ɼ�����
					daq();  //�ɼ����ݣ����͵�serviceȥ����						
					dnc.main.msg=null;//���msg��־
                }else{
                	Log.i(TAG,"dnc.main.msgΪ�գ�");
                }				
			}
		//�ɼ����ݼ��ʱ��,�ɼ�����֮���߳�����һ��ʱ��
			try {
				Thread.sleep(1000);//�ɼ����ݼ��ʱ������Ϊ1S,��Ϊ�ɼ����̺�ʱ��Լ300���룬��������Ϊ700
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	
		}//end while()	
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
			DataReg dataReg = GJApiFunction.getDataReg();//��ȡע����Ϣ					
			dataReg.setTime(strTime);		//���òɼ���ʱ���
			dataReg.setId(machineIP);      //����ID��

//			sendMsg2Main(dataReg, HandleMsgTypeMcro.MSG_REG);//��ʼ���ɹ�
			
			DataLog datalog=GJApiFunction.getDataLog();//��ȡ��¼��Ϣ
			datalog.setId(machineIP);
			datalog.setTime(strTime);
			
			DaqData.getListDataLog().add(datalog); //�����¼��Ϣ
			DaqData.getListDataReg().add(dataReg); //����ע����Ϣ
			boolGetMacInfo = true;//��Ϊtrue�������Ѿ��õ��˻���������Ϣ
			
		}else{   //�ɼ�������Ϣ�ͱ�����Ϣ
			
			//�ɼ�������Ϣ
			LinkedList<DataAlarm> listDataAlarm =GJApiFunction.getDataAlarm();
			for (DataAlarm dataAlarm : listDataAlarm) {
				dataAlarm.setId(machineIP);//��������ϵͳID
				dataAlarm.setTime(strTime);//���òɼ���ʱ���
			}
			
			//����ɼ����ı�����Ϣ��Ϊ��������еı�����Ϣ��Ϊ�㣬��ô��Ҫ�Ա�����Ϣ���з���
			//�Ա�����Ϣ���д���,����Ҫ�жϱ�����Ϣ�������Ƿ����������ǽ��������������������������߳���
			if ((listDataAlarm.size() != 0)||(DaqData.getListDataAlarm().size() != 0)) 
			{
				sendMsg2Main(listDataAlarm, HandleMsgTypeMcro.MSG_ALRAM, count);
			}
			
			//�ɼ�������Ϣ
			DataRun dataRun = GJApiFunction.getDataRun();
			dataRun.setId(machineIP); //��������ϵͳID
			dataRun.setTime(strTime); //��ʱ���
			sendMsg2Main(dataRun,HandleMsgTypeMcro.MSG_RUN,count);
						
			count++;//�ɼ�������¼
			if(count == Integer.MAX_VALUE)//�ﵽ���ֵ��ʱ��ǵ�����
				count = 1;
		
		}
	}    //end daq()
	
	
	/**
	 * ������Ϣ�����߳�
	 * @param obj
	 * @param what
	 */
	private  void sendMsg2Main(Object obj, int what) 
	{
		sendMsg(delMsgHandler, obj, what,0,0);
	}
	
	/**
	 * ������Ϣ�����߳�
	 */
	private  void sendMsg2Main(Object obj, int what, int arg1) 
	{
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
	private  void sendMsg(Handler handler,Object obj, int what, int arg1, int arg2) 
	{
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

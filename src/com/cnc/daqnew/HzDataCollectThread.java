package com.cnc.daqnew;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cnc.daq.DaqActivity;
import com.cnc.daq.DaqData;
import com.cnc.daq.MainActivity;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.DataType;
import com.cnc.domain.UiDataAlarmRun;
import com.cnc.domain.UiDataNo;
import com.cnc.huazhong.HncAPI;
import com.cnc.huazhong.HncSystem;
import com.cnc.netService.HncTools;
import com.cnc.netService.Intialize;
import com.cnc.service.DelMsgServie;
import com.cnc.utils.LogLock;
import com.cnc.utils.RegLock;
/**
 * ���ݲɼ����߳�
 * @author wei
 *
 */
public class HzDataCollectThread implements Runnable,DataCollect{
	
	private final String TAG="DataCollectThread...";
	 //�߳�ѭ��ִ�б�־����Ϊfalseʱ���߳��˳�ѭ�����߳̽�������
    private volatile  boolean  threadflag=true; 
	public int Client = -1;	   //����״̬��ʶ�� -1��ʶδ����

	boolean boolGetMacInfo = false; //��ʶ�Ƿ�õ������Ļ�����Ϣ
	int   macChannel = 0;//������ͨ����Ϣ
	int   count = 1;//�洢������Ϣ��id,��ʶ���ǵڼ��βɼ���Ϣ
	
	
	private Handler  daqActivityHandler=null,
					 delMsgHandler =null;
	private Handler  mainHander=null ;
					
	String tp = "HNC-818A";//����ϵͳ�ͺ�
	String machine_SN=null;//����ϵͳID
//	String pathcnc=null;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//ʱ�����ʽ
	
	/*static {  
        // ���ض�̬��  
        System.loadLibrary("hncnet");  
        System.loadLibrary("hzHncAPI");  
    }*/

	String machineIP = "192.168.188.113"; //������IP��ַ
	int    machinePort = 21;			  //�����˿ں�
	
	public HzDataCollectThread(String ip,int port){
		this.delMsgHandler=DelMsgServie.getHandlerService();
		this.daqActivityHandler=DaqActivity.getmHandler();
		mainHander=MainActivity.getMainActivityHandler();
		machineIP=ip;
		machinePort=port;		
	}
	
	public HzDataCollectThread(Handler handler,String pathcnc) {
		this.daqActivityHandler=handler;	
//		this.pathcnc=pathcnc;
	}
		
	@Override
	public void run() {
		int inialRes = -1;//�Ƿ��Ѿ���ʼ��
/*		String machineIP = "192.168.188.113"; //������IP��ַ
		int    machinePort = 21;			  //�����˿ں�
		
		String str=getPath(pathcnc),
			   strp=getPath("cncport");
		if(str != null && strp!=null ){
			machineIP=str;
			machinePort=Integer.parseInt(strp);
		}*/

		while(threadflag)
		{
			if(inialRes != 0)
				inialRes = new Intialize().inial();//���ȳ�ʼ��
			else//��ʼ���ɹ�
			{					
//				if(Client < 0)//û�����ӻ���
				if(HncAPI.HNCNetIsConnect(Client) != 0)
				{						
					//�ɼ�����֮ǰ��ʼ�����õ�Ŀ�Ļ�����IP��port
					Client = HncAPI.HNCNetConnect(machineIP,machinePort);//Ҫ���ӵĻ�����IP�Ͷ˿�
					if (Client >= 0 )
						sendMsg2Main("���ӻ����ɹ�", HandleMsgTypeMcro.MSG_ISUCCESS);//��ʼ���ɹ�						
					else{
						sendMsg2Main("���ӻ���ʧ��", HandleMsgTypeMcro.MSG_IFAILURE);
						try {							
							Thread.sleep(1000*10); //���ӻ���ʧ�ܹ�һ��������							
						} catch (InterruptedException e) {					
							e.printStackTrace();
						} 
					}					
				}
				else{				
					daq();//�������ݲɼ������ɼ����ݣ�ͬʱ�Ѳɼ��������ݷ��͵����߳�
				}			
			}
			
			//�ɼ����ݼ��ʱ��,�ɼ�����֮���߳�����һ��ʱ��
			try {
//				Thread.sleep(700);//�ɼ����ݼ��ʱ������Ϊ1S,��Ϊ�ɼ����̺�ʱ��Լ300���룬��������Ϊ700
				Thread.sleep(1000);
			} catch (InterruptedException e) {					
				e.printStackTrace();
			} 
			
		}//end while(true)		
	}//end run()
	
	
	/**
	 * ���ݲɼ�����
	 */
	private void daq() 
	{
		
		StringBuilder  sbalram=new StringBuilder();
		sbalram.append("Alarm:");
		
		String strTime = formatter.format(new Date());//��ʼ�ɼ���Ϣ�ĸ����¼�
		
		if(!boolGetMacInfo)   //���û�л�ù������Ļ�����Ϣ
		{
			//ע����Ϣ
			DataReg dataReg = HncTools.getMacInfo(Client); //��ȡ�����Ļ�����Ϣ			
			//����ID
			machine_SN=dataReg.getId();
//				DaqData.setCncid(machine_SN);
			//�õ�ͨ����
			macChannel = HncAPI.HNCSystemGetValueInt(HncSystem.HNC_SYS_ACTIVE_CHAN, Client);//������ͨ����Ϣ
			dataReg.setTime(strTime);		//���òɼ���ʱ���
			dataReg.setTp(tp);   			//����ϵͳ�ͺ�
			synchronized(RegLock.class){
				DaqData.getListDataReg().add(dataReg);
			}

			DataLog dataLog=new DataLog(machine_SN,0,0,strTime);//�������ز��ṩ���ۼƼӹ�ʱ�䡱�͡��ۼ�����ʱ�䡱
			synchronized(LogLock.class){
				DaqData.getListDataLog().add(dataLog);
			}
			
			UiDataNo uiDataNo=new UiDataNo(getNoCnc(machineIP),machineIP,machine_SN , DaqData.getAndroidId());
			sendMsg(mainHander,uiDataNo,HandleMsgTypeMcro.HUAZHONG_UINO,0,0); //������Ϣ�������ʾIP��ַ��Ϣ
			
			
			boolGetMacInfo = true;//��Ϊtrue�������Ѿ��õ��˻���������Ϣ
		}
		else//�ɼ�������Ϣ�ͱ�����Ϣ
		{						 
			//�ɼ�������Ϣ
			LinkedList<DataAlarm> listDataAlarm = HncTools.getAlarmData(Client);
			for (DataAlarm dataAlarm : listDataAlarm) {
				dataAlarm.setId(machine_SN);//��������ϵͳID
				dataAlarm.setTime(strTime);//���òɼ���ʱ���
				sbalram.append(dataAlarm.getCtt()).append(":");
			}
			//����ɼ����ı�����Ϣ��Ϊ��������еı�����Ϣ��Ϊ�㣬��ô��Ҫ�Ա�����Ϣ���з���
			//�Ա�����Ϣ���д���,����Ҫ�жϱ�����Ϣ�������Ƿ����������ǽ��������������������������߳���
			if ((listDataAlarm.size() != 0)||(DaqData.getListDataAlarm().size() != 0)) 
			{
				sendMsg2Main(listDataAlarm, HandleMsgTypeMcro.MSG_ALRAM, count);
			}		
			
			//�ɼ�������Ϣ
			DataRun dataRun = HncTools.getDataRun(Client,macChannel);
			dataRun.setId(machine_SN); //��������ϵͳID
			dataRun.setTime(strTime); //��ʱ���
			sendMsg2Main(dataRun,HandleMsgTypeMcro.MSG_RUN,count);
					
			count++;//�ɼ�������¼
			if(count == Integer.MAX_VALUE)//�ﵽ���ֵ��ʱ��ǵ�����
				count = 1;
			
			//���͵����߳�
			UiDataAlarmRun uiDataAlarmRun=new UiDataAlarmRun(sbalram.toString(), dataRun.toString());
			sendMsg(mainHander, uiDataAlarmRun, HandleMsgTypeMcro.HUAZHONG_UIALARM	, 0, 0);
			
		}
		
	}//end daq()����
	
	/**
	 * ��ȡproperties�ļ���ֵ
	 * @param key
	 * @return
	 */
	/*private String  getPath(String key){
		InputStream in=null;
		Properties pro=null;
		String path=null;
		
		pro =new Properties();
		in=DataTransmitThread.class.getResourceAsStream(DataType.pathResource);
		
		
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
	
	
	//������Ϣ�����߳�
	private void sendMsg2Main(Object obj, int what) 
	{
		sendMsg(delMsgHandler, obj, what,0,0);
	}
	
	//������Ϣ�����߳�
	private void sendMsg2Main(Object obj, int what, int arg1) 
	{
		sendMsg(delMsgHandler, obj, what, arg1, 0);
	}

	//������Ϣ��ͨ����
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
		threadflag=false;	//�����߳�	
	}

	@Override
	public boolean isThreadRunning() {
		// TODO Auto-generated method stub
		  return threadflag;
	}
	
	private String getNoCnc(String ip){
		String NoCnc="1-1";
		if(ip==null){
			return null;
		}
		
		String lastip= ip.substring(ip.lastIndexOf('.')+1);
		int lastipint= Integer.valueOf(lastip);
		
		switch( lastipint){
			case 112:
				NoCnc="HUAZHONG1_1";
			break;
			case 114:
				NoCnc="HUAZHONG1_2";
			break;
			case 113:
				NoCnc="HUAZHONG1_3";
			break;
			case 111:
				NoCnc="HUAZHONG1_4";
			break;
			case 110:
				NoCnc="HUAZHONG1_5";
			break;
			default:{}
		}
		
		return NoCnc;
	}

	

}

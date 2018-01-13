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
import com.cnc.utils.AlarmFilterList;
import com.cnc.utils.LogLock;
import com.cnc.utils.RegLock;
/**
 * �������ݲɼ����߳�
 * @author wei
 *
 */
public class HzDataCollectThread implements Runnable,DataCollectInter{
	
	private final String TAG="DataCollectThread...";
	 //�߳�ѭ��ִ�б�־����Ϊfalseʱ���߳��˳�ѭ�����߳̽�������
    private volatile  boolean  threadflag=true; 
	public int Client = -1;	   //����״̬��ʶ�� -1��ʶδ����

	boolean boolGetMacInfo = false; //��ʶ�Ƿ�õ������Ļ�����Ϣ
	int   macChannel = 0;//������ͨ����Ϣ
	int   count = 1;//�洢������Ϣ��id,��ʶ���ǵڼ��βɼ���Ϣ
		
//	private Handler  daqActivityHandler=null,
	private Handler  delMsgHandler =null;
	private Handler  mainHander=null ;
					
	String tp = "HNC-818A";//����ϵͳ�ͺ�
	String machine_SN=null;//����ϵͳID

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//ʱ�����ʽ
	private AlarmFilterList   alarmFilterList =null; //������Ϣ������˶���
	
	/*static {  
        // ���ض�̬��  
        System.loadLibrary("hncnet");  
        System.loadLibrary("hzHncAPI");  
    }*/

	String machineIP = "192.168.188.113"; //������IP��ַ
	int    machinePort = 21;			  //�����˿ں�
	
	public HzDataCollectThread(String ip){
		this(ip,21);
	}
	
	public HzDataCollectThread(String ip,int port){
		this.delMsgHandler=DelMsgServie.getHandlerService();
		mainHander=MainActivity.getMainActivityHandler();
		machineIP=ip;
		machinePort=port;
		this.alarmFilterList=new AlarmFilterList(delMsgHandler);
	}
	
		
	@Override
	public void run() {
		int inialRes = -1;//�Ƿ��Ѿ���ʼ��    inialRes != 0
		while(threadflag){
			
			if(inialRes != 0){
				//���ȳ�ʼ��,ֻ�ܳ�ʼ��һ�Σ����ڲ�ͬ�Ļ���IP��ʼ����ͬ�Ķ˿ں�
				inialRes=Intialize.getInstance().inial6(getInitLocalport(machineIP));
			}else{ //��ʼ���ɹ�
				if(HncAPI.HNCNetIsConnect(Client) != 0)
				{
					HncAPI.HNCNetConnect(machineIP, machinePort);
					//�ɼ�����֮ǰ��ʼ�����õ�Ŀ�Ļ�����IP��port
					if (Client >= 0 )
						sendMsg2Main("�������ӻ����ɹ�", HandleMsgTypeMcro.MSG_ISUCCESS);//��ʼ���ɹ�						
					else{
						sendMsg2Main("�������ӻ���ʧ��", HandleMsgTypeMcro.MSG_IFAILURE);
						try {							
							Thread.sleep(1000*10); //���ӻ���ʧ�ܹ�һ��������							
						} catch (InterruptedException e) {					
							e.printStackTrace();
					    } 
					}					
				}else{				
					daq();//�������ݲɼ������ɼ����ݣ�ͬʱ�Ѳɼ��������ݷ��͵����߳�
				}			
			}

			//�ɼ����ݼ��ʱ��,�ɼ�����֮���߳�����һ��ʱ��
			try {
//				Thread.sleep(700);//�ɼ����ݼ��ʱ������Ϊ1S,��Ϊ�ɼ����̺�ʱ��Լ300���룬��������Ϊ700
				Thread.sleep(1000);
			}catch (InterruptedException e) {					
				e.printStackTrace();
			} 
			
		}//end while(true)	
		
		HncAPI.HNCNetExit();//�˳��̶߳Ͽ����ӣ�����������Ҫ���³�ʼ��
		
	}//end run()
	
	
	/**
	 * ���ݲɼ�����
	 */
	private void daq() 
	{			
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
			
			UiDataNo uiDataNo=new UiDataNo("",machineIP,machine_SN , DaqData.getAndroidId());
			sendMsg(mainHander,uiDataNo,HandleMsgTypeMcro.HUAZHONG_UINO,0,0); //������Ϣ�������ʾIP��ַ��Ϣ
					
			boolGetMacInfo = true;//��Ϊtrue�������Ѿ��õ��˻���������Ϣ
		}
		else//�ɼ�������Ϣ�ͱ�����Ϣ
		{	
			StringBuilder  sbalram=new StringBuilder();
			//�ɼ�������Ϣ
			LinkedList<DataAlarm> listDataAlarm = HncTools.getAlarmData(Client);
			for (DataAlarm dataAlarm : listDataAlarm) {
				dataAlarm.setId(machine_SN);//��������ϵͳID
				dataAlarm.setTime(strTime);//���òɼ���ʱ���
				sbalram.append(dataAlarm.getCtt()).append(":");
			}
			//����ɼ����ı�����Ϣ��Ϊ��������еı�����Ϣ��Ϊ�㣬��ô��Ҫ�Ա�����Ϣ���з���
			//�Ա�����Ϣ���д���,����Ҫ�жϱ�����Ϣ�������Ƿ����������ǽ��������������������������߳���
			if ((!listDataAlarm.isEmpty())|| (!alarmFilterList.getNowAlarmList().isEmpty())) 
			{
//				sendMsg2Main(listDataAlarm, HandleMsgTypeMcro.MSG_ALRAM, count);
				alarmFilterList.saveCollectedAlarmList(listDataAlarm);
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
		this.threadflag=false;	//�����߳�
		
	}

	@Override
	public boolean isThreadRunning() {		
		  return threadflag;
	}
	
	//����IP��ַ��ȡ��ͬ��ʼ���˿ں�
	//ʵ�ֲɼ��������л�
	private int getInitLocalport(String ip){
		if(ip==null || "".equals(ip.trim())){
			return 10015;
		}		
		int port=10015;
		String  num= ip.substring(ip.lastIndexOf('.')+1);
		int n = Integer.valueOf(num);	
		return n+9905;
	}


}

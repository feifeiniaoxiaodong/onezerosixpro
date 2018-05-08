package com.cnc.hangtian.thread;

import java.io.IOException;
import java.util.LinkedList;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import bean.ALARM_INFO;
import bean.AXIS_INFO;
import bean.SYSTEM_INFO;

import com.cnc.broadcast.BroadcastAction;
import com.cnc.broadcast.BroadcastType;
import com.cnc.daq.DaqData;
import com.cnc.daq.MyApplication;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.hangtian.alarmfile.FindAlarmByIdHangtian;
import com.cnc.hangtian.domain.VersionHangtian;
import com.cnc.huazhong.dc.CommonDataCollectThreadInterface;
import com.cnc.mainservice.DelMsgService;
import com.cnc.net.datasend.HandleMsgTypeMcro;
import com.cnc.utils.AlarmFilterList;
import com.cnc.utils.JsonUtil;
import com.cnc.utils.TimeUtil;

import function.Start;

/**
 * �����������ݲɼ��߳�
 * @author wei
 *
 */
public class HangTianDataCollectThread implements Runnable,
		CommonDataCollectThreadInterface {
	
	private final String TAG="HangTianDataCollectThread";
	private volatile boolean threadRunningFlag=true;
	int     count = 1;     //�洢������Ϣ��id,��ʶ���ǵڼ��βɼ���Ϣ
	private Start start=null;
	private static int cncNumber=2;
	private String threadLabel=null; //�̱߳��
	private static String machineIP="192.168.188.141";
	private final int  port=6665;
	private Handler delMsgHandler =null;
	private String CNCSystemType=null; 				//����ϵͳ�ͺ�
	private String CNCSystemID=null;				//����ϵͳID
	private AlarmFilterList   alarmFilterList =null; //������Ϣ������˶���
	boolean boolGetMacInfo = false;					 //��ʶ�Ƿ�õ������Ļ�����Ϣ
	
	
	public HangTianDataCollectThread(String machineip, String threadlabel) {
		
		String num = machineip.substring( machineip.lastIndexOf(".")+1);		
		cncNumber=(Integer.parseInt(num)%10)+1;
		delMsgHandler =DelMsgService.getHandlerService();
		this.threadLabel=threadlabel; 						
		this.machineIP=machineip;	
		if(delMsgHandler!=null){
			alarmFilterList=new AlarmFilterList(delMsgHandler);
		}		
	}
	
	@Override
	public void run() {
		
		start = Start.getInstance();
		if(start==null){
			return ;
		}
		
		try{
			start.ClientConnectServer(cncNumber, machineIP, port);
			if(start.SocketKeepalive(cncNumber)){
				Log.d(TAG, "��������"+machineIP+"���ӳɹ�");
			}else{
				Log.d(TAG, "��������"+machineIP+"����ʧ��");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		while(threadRunningFlag){
			if(! start.SocketKeepalive(cncNumber)){
				try{
					start.ReConnectServer(cncNumber);
					if(start.SocketKeepalive(cncNumber)){
						Log.d(TAG, "��������"+machineIP+"�������ӳɹ�");
					}else{
						Log.d(TAG, "��������"+machineIP+"��������ʧ��");
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				//data acquisition
				getDaq();
			}
			
			try{
				Thread.sleep(1000);//�ɼ����1��
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}//end while
		
		//�Ͽ�����,�������
		/*try {
			start.DeleteServer(cncNumber);
		} catch (Exception e) {			
			e.printStackTrace();
		}*/
		
	} //end run()

	
	//�ɼ�����
    private void getDaq(){ 
    	SYSTEM_INFO systemInformation=null;
  	
    	if(!boolGetMacInfo){
    		//�ɼ�ע����Ϣ�͵�¼�ǳ���Ϣ
    		try {
				systemInformation= start.GetSysteminfo(cncNumber);//��ȡCNCϵͳ��Ϣ
				if(systemInformation!=null){
					this.CNCSystemID=systemInformation.getSystemid().trim();
					this.CNCSystemType=systemInformation.getSystemtype().trim();
					String NCversion=systemInformation.getSystemver().trim(); //����ϵͳ�ܰ汾��
					
					VersionHangtian versionHangtian=new VersionHangtian(NCversion, CNCSystemType);
					DataReg dataReg= new DataReg(this.CNCSystemID , 
												 this.CNCSystemType,
												JsonUtil.object2Json(versionHangtian) ,
												 TimeUtil.getTimestamp());
					DaqData.saveDataReg(dataReg); //����ע����Ϣ	
					
					DataLog dataLog=new DataLog(CNCSystemID, systemInformation.getOntime(),
												systemInformation.getRuntime()	,  
												TimeUtil.getTimestamp());
					DaqData.saveDataLog(dataLog);
					boolGetMacInfo=true;	
					
					//���͵���������ʾ  cncid  androidid
					sendBroadCast(BroadcastAction.ACTION_HANGTIAN_RUN_ALARM,
							"type" , "cncid" ,
							"threadlabel",threadLabel,
							"cncid" , CNCSystemID);
					
					sendBroadCast(BroadcastAction.ACTION_HANGTIAN_RUN_ALARM,
							"type" , "androidid" ,
							"threadlabel",threadLabel,
							"androidid" , DaqData.getAndroidId());
				}
			} catch (Exception e) {				
				e.printStackTrace();
			}
    		   				
    	}else{
    		//�ɼ�������Ϣ
    		collectRunInfo();
    		
    		//�ɼ�������Ϣ
    		collectAlarmInfo();
    		    		
    	}
    }
	
    /**
     * �ɼ���������������Ϣ
     */
    private void  collectRunInfo(){
    	SYSTEM_INFO systemInformation=null;
//    	ALARM_INFO  alarmInformation =null;
    	AXIS_INFO   axisInfomation =null;
    	
    	try {
			systemInformation= start.GetSysteminfo(cncNumber);//��ȡCNCϵͳ��Ϣ
//			alarmInformation=start.GetAlarminfo(cncNumber);
			axisInfomation =start.GetAxisinfo(cncNumber);
		} catch (InterruptedException e) {				
			e.printStackTrace();
		}
		
		DataRun dataRun=new DataRun();
		dataRun.setId(CNCSystemID);
		dataRun.setCas((float)axisInfomation.getA_s_value());//��ʵ��ת��
		dataRun.setCcs((float)axisInfomation.getC_s_value());//��ָ��ת��
		dataRun.setAload((float)systemInformation.getS_loadcurrent());//���Ḻ�ص���
		
		long[] axisSpeed= axisInfomation.getA_f_value();//������ʵ��ת��
		dataRun.setAspd1(axisSpeed[0]);
		dataRun.setAspd2(axisSpeed[1]);
		dataRun.setAspd3(axisSpeed[2]);
		dataRun.setAspd4(0);
		dataRun.setAspd5(0);
		
		float[] axisActualposition=axisInfomation.getA_axis_machine();//������ʵ��λ��
		dataRun.setApst1(axisActualposition[0]);
		dataRun.setApst2(axisActualposition[1]);
		dataRun.setApst3(axisActualposition[2]);
		dataRun.setApst4(0);
		dataRun.setApst5(0);
		
		float[] axisCommandposition=axisInfomation.getC_axis();//������ָ��λ��
		dataRun.setCpst1(axisCommandposition[0]);
		dataRun.setCpst2(axisCommandposition[1]);
		dataRun.setCpst3(axisCommandposition[2]);
		dataRun.setCpst4(0);
		dataRun.setCpst5(0);
		
		long[] axisLoadCurrent= systemInformation.getAxis_loadcurrent();//�����Ḻ�ص���
		dataRun.setLoad1(axisLoadCurrent[0]);
		dataRun.setLoad2(axisLoadCurrent[1]);
		dataRun.setLoad3(axisLoadCurrent[2]);
		dataRun.setLoad4(0);
		dataRun.setLoad5(0);
		   		
	    dataRun.setPd((short)systemInformation.getPd()); //���г�����
	    dataRun.setPn(systemInformation.getPn().trim());		//������
	    dataRun.setPs(systemInformation.getPs()+"");	//��������״̬
	    dataRun.setPl(systemInformation.getPl());		//����������
	    dataRun.setPm((short)0); 						//ͨ��ģ̬
	    dataRun.setTime(TimeUtil.getTimestamp());
	    
		sendMsg2Main(dataRun,HandleMsgTypeMcro.MSG_RUN,count);
		
		count++;//�ɼ�������¼
		if(count == Integer.MAX_VALUE)//�ﵽ���ֵ��ʱ��ǵ�����
			count = 1;	
    	
		//���͵���������ʾ
		sendBroadCast(BroadcastAction.ACTION_HANGTIAN_RUN_ALARM,
				"type" , BroadcastType.HANGTIAN_RUN ,
				"threadlabel",threadLabel,
				 BroadcastType.HANGTIAN_RUN, dataRun.toString());
    }
    
    /**
     * �ɼ��������صı�����Ϣ
     */
    private void collectAlarmInfo(){
    	
    	ALARM_INFO  alarmInformation =null;
    	StringBuilder  currentAlarmString=new StringBuilder();//������Ϣ
    	LinkedList<DataAlarm> listDataAlarm =new LinkedList<>();
    	
    	try {			
			alarmInformation=start.GetAlarminfo(cncNumber);			
		} catch (InterruptedException e) {				
			e.printStackTrace();
		}

    	
    	int []alarmCodes=alarmInformation.getAlarmcode_array();//��ǰ����������
    	int i=0;
    	while(alarmCodes[i]>0 ){
    		String alarmCode= alarmCodes[i]+"";
			String alarmInfo=FindAlarmByIdHangtian.getAlarmInfoById(alarmCode);
			
			DataAlarm dataAlarm=new DataAlarm();
			dataAlarm.setId(CNCSystemID);
			dataAlarm.setF((byte)0);
			dataAlarm.setNo(alarmCode);
			dataAlarm.setTime(TimeUtil.getTimestamp());
			dataAlarm.setCtt(alarmInfo);
			
			listDataAlarm.add(dataAlarm);
			currentAlarmString.append(alarmInfo).append(":");
			i++;	    	 	   		
    	}
    	    	
		//����ɼ����ı�����Ϣ��Ϊ��������еı�����Ϣ��Ϊ�㣬��ô��Ҫ�Ա�����Ϣ���з���
		//�Ա�����Ϣ���д���,����Ҫ�жϱ�����Ϣ�������Ƿ����������ǽ��������������������������߳���
		if ((listDataAlarm.size() != 0)||(!alarmFilterList.getNowAlarmList().isEmpty())){
			alarmFilterList.saveCollectedAlarmList(listDataAlarm);
		}
    	
		//��ǰ������Ϣ���͵���������ʾ��û�б�������ʱ��ʾΪ��
		sendBroadCast(BroadcastAction.ACTION_HANGTIAN_RUN_ALARM,
				"type" , BroadcastType.HANGTAIN_ALARM ,
				"threadlabel",threadLabel,
				 BroadcastType.HANGTAIN_ALARM, currentAlarmString.length()>0 ? currentAlarmString.toString():"��ǰ�ޱ�������");  
				
    }
    
	
	@Override
	public void stopCollect() {
		threadRunningFlag=false;
	}

	@Override
	public boolean isThreadRunning() {		
		return threadRunningFlag;
	}
	
	//��String trim()�������
	//ɾ���ַ�����β��������ַ��������β����������
//	private String getUsefulString(String str){
//		String strl=null;
//		strl =str.substring(0, str.indexOf('\0'));
//		return strl;
//	}
	
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
	 * @param obj
	 * @param what
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
	
	//���ͱ��ع㲥��Ϣ	
	private void sendBroadCast(String action,String ... args){
		Intent intent =new Intent();
		intent.setAction(action);
		if(args.length%2 !=0 ) return ;
		for(int i=0;i<args.length;i+=2){
			intent.putExtra(args[i], args[i+1]);
		}
		LocalBroadcastManager.getInstance(MyApplication.getContext())
							 .sendBroadcast(intent);	
	}


}

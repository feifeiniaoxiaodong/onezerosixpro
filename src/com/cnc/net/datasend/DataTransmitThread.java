package com.cnc.net.datasend;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.cnc.broadcast.BroadcastAction;
import com.cnc.broadcast.BroadcastType;
import com.cnc.daq.DaqData;
import com.cnc.daq.MainActivity;
import com.cnc.daq.MyApplication;
import com.cnc.db.service.DBService;
import com.cnc.db.service.DataAlarmPlus;
import com.cnc.db.service.DataRunPlus;
import com.cnc.domain.DataDelayTime;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.DataType;
import com.cnc.domain.GeneralData;
import com.cnc.net.service.Post;
import com.cnc.utils.JsonUtil;
import com.cnc.utils.TimeUtil;

/**
 * ���ݷ����߳�
 * @author wei
 *	
 */
public class DataTransmitThread implements Runnable{
	
	private final String TAG="DataTransmitThread...";
	private  volatile boolean  isCountinueRun=true;
   	boolean isRegister = false; //ע���־  
   	boolean isLogin=false; 		//��¼�ǳ���־
	String   path="http://47.94.200.41:8080/DataPush/rec";  //Զ�˷�������ַ
	
//	String path ="http://47.94.200.41:8080/DataPush/rec";//������������·�� �������Ʒ�������ַ 
//	final private String path ="http://192.168.1.102:8080/DataPush/rec";//������������·��    	
//	final private String path ="http://111.198.20.237:7000/DataInputServiceTest/pushData";//������������·����ʷʤ����������ַ
		
	final String resultofPost = "ac"; 
	final String resultofExcep = "ero";
	
	private DBService dbservice=null;     		//SQLite���ݿ��������
//	private Handler   daqActvityHandler=null;  //Activity��Handler

	//���η�����������
	int[]   keys={1,5,10,15,30,50,60,70,80,90,100,120,140,160,180,200,220,240,260,280,300};
	int  	pointer=0,
			len=keys.length;
	
	private  int nMsgSend=5 ; //���η�������������������ʱʱ��ı仯���ж�̬����
	
	public DataTransmitThread(){
		dbservice=DBService.getInstanceDBService();		
	}
	
/*	public DataTransmitThread(Handler handler) {
		daqActvityHandler=handler;
		dbservice=DBService.getInstanceDBService();
		
//		path=getPath("path"); //��ʼ��Զ�˷�������ַ
	}*/

	//ֻҪ����̴߳򿪾�һֱ���ⷢ����Ϣ�����õ�¼��ע�ᶼ���Է���������Ϣ�ͱ�����Ϣ
//	@Override
	public void run() {
//		DataDelayTime  delayTime=null;//��ʱʱ���������
		String res = null;
//		long  countRun=0; //δ���͵�������Ϣ������
	
//		String sPath=getPath(HandleMsgTypeMcro.SERVICES_ALIYUN); //��ʼ��Զ�˷�������ַ
		String sPath=getPath(HandleMsgTypeMcro.SERVICES_YANG); //��ʼ��Զ�˷�������ַ
		if(sPath!=null){
			path=sPath;
		}		
		while(isCountinueRun) //���ע����Ϣ��������Ϣ����Ϣ���Ǿ�һֱ����
		{

			DataReg dataReg=null;
			if((dataReg=DaqData.getDataReg()) instanceof DataReg){

				GeneralData generalData = new GeneralData();
				generalData.setDid(DataType.DataReg); //ע����Ϣ
				if(dataReg.getId()!=null) {
					generalData.setDt(JsonUtil.object2Json(dataReg));
					res = Post.sendData(path, JsonUtil.object2Json(generalData));//����ע����Ϣ					
                    if(resultofPost.equals(res))
                    {
                    	DaqData.delDataReg(dataReg);
                        Log.d(TAG, "ע��ɹ�");
                    }
                    else {
                        Log.d(TAG,"ע��ʧ�ܣ�");
                    }						
				}					
			}

			DataLog dataLog=null;		
			if((dataLog=DaqData.getDataLog()) instanceof DataLog){
				
				GeneralData generalData2 = new GeneralData();
				generalData2.setDid(DataType.DataLog); //��������->��¼�ǳ���Ϣ
				if(dataLog.getId()!=null){
					generalData2.setDt(JsonUtil.object2Json(dataLog));
					res = Post.sendData(path, JsonUtil.object2Json(generalData2));//���͵�¼�ǳ���Ϣ

                    if(resultofPost.equals(res))
                    {
                    	DaqData.delDataLog(dataLog);
                        Log.d(TAG, "��¼�ǳ��ɹ�");
                    }
                    else {
                        Log.d(TAG,"��¼�ǳ�ʧ�ܣ�");
                    }	
				}
			}
			
											
			//������Ϣ										
			sendAlarmInfo();
			
			//����������Ϣ
//			sendRunInfo();	
			sendRunInfoZhi();
			//���ͼ��
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}			
		} //end while()	

	} //end run()
	
	/**
	 * ���ͱ�����Ϣ
	 * ������ж��̷߳��͵Ļ���Ҫ�Ը÷�������ͬ��,��Ϊ�����з��ʹ�����Դ
	 */
	private void sendAlarmInfo(){
		DataDelayTime  delayTime=null;//��ʱʱ���������
		long  countAlarm=0; //δ���͵�������Ϣ������
		
		if((countAlarm=dbservice.getCountAlarmData())>0){
			DataAlarmPlus dataAlarmPlus = dbservice.findFirstRowAlarmDataP();//��Sqlite�ж�ȡ������Ϣ
			//�����ݴ������
			GeneralData generalData = new GeneralData();
			generalData.setDid(DataType.DataAlarm); 		//������Ϣ
			generalData.setDt(JsonUtil.object2Json(dataAlarmPlus.dataAlarm));
			
			delayTime=getDataDelayTimeObj(DataType.DataAlarm,countAlarm,1);

			String res =sendPostGeneral(generalData,delayTime);//�������ݵ�������			
//			String res = Post.sendData(path, JsonUtil.object2Json(generalData));
			//�ж��Ƿ��ͳɹ�						
			if(resultofPost.equals(res))
			{
				dbservice.deleteAlarmData(dataAlarmPlus.id);//������Ϣ�ɹ��󣬴����ݿ���ɾ��������Ϣ
				Log.d(TAG, "���ͱ�����Ϣ�ɹ���");
				//������ʱʱ��
				if(delayTime!=null){
					sendDelayTime(delayTime);
				}
			}
			else /*if(resultofExcep.equals(res))*/{	//����ʧ��
//				login = false;			//����ʧ�ܾ���Ҫ���·���ע����Ϣ
				Log.d(TAG, "���ͱ�����Ϣʧ�ܣ�");
//				Toast.makeText(MyApplication.getContext(),"���ͱ�����Ϣʧ��!", Toast.LENGTH_SHORT).show();
			}			
									
		}	
	}
	
	/**
	 * ����������Ϣ
	 * ������ж��̷߳��͵Ļ���Ҫ�Ը÷�������ͬ��,��Ϊ�����з��ʹ�����Դ
	 */
	private void sendRunInfo(){
		DataDelayTime  delayTimeObj=null;//��ʱʱ���������
		long  countRun=0; //δ���͵�������Ϣ������
				
		countRun=dbservice.getCountRunInfo();//���SQLite���ݿ���������Ϣ������
			
		//�����ض�������������Ϣ
		if(countRun>= keys[pointer = (pointer)%len] ){
			delayTimeObj=getDataDelayTimeObj(DataType.DataDelay,countRun); //��ȡһ����ʱ��Ϣ����
			  
			if(sendNumRunInfo(keys[pointer],delayTimeObj)){//����ָ��������������Ϣ��������	
				//������ʱʱ�䣬��������
				if(delayTimeObj!=null){
					sendDelayTime(delayTimeObj);
				}				
				
				//������ʾ���Ͳ���,����ͳһ����������Ϣ����
				if(delayTimeObj!=null){
					sendBroadCast(BroadcastAction.SendThread_PARAMALL,
							
							BroadcastType.SENDCOUNT,"����:"+delayTimeObj.getNumofmsg()+"������",
							BroadcastType.SENDSPEED,"��������:"+Post.speed(delayTimeObj.getDelaytime(), delayTimeObj.getPackagesize()));
				}
			}
			++pointer;
		}		
	}
	
	/**
	 * ��̬��������������Ϣ������
	 * ����delaytime,��:��·��Ӧʱ��RTT,���������η�����������
	 * ��ֵ����Ϊ500ms
	 * if RTT < ��300 , nMsgSend + 10 ; 
	 * else  if( 300 < RTT <=500)  nMsgSend +5 ;
	 * else if( 500 <RTT <700 )  nMsgSend -5 ;
	 * else  nMsgSend -10;
	 * 
	 * ������ж��̷߳��͵Ļ���Ҫ�Ը÷�������ͬ��,��Ϊ�����з��ʹ�����Դ
	 * @add by wei 
	 * @time 2018/03/11
	 */
	private void sendRunInfoZhi(){
		DataDelayTime  delayTimeObj=null;//��ʱʱ�����
 		if(nMsgSend<1) nMsgSend =5;	//��ֹ�������ʱ�Ϊ0	
//		long countRun=dbservice.getCountRunInfo();//���SQLite���ݿ���������Ϣ������,���ػ�����Ϣ����
		long countRun=DaqData.getCacheinfocount();
		//�����ض�������������Ϣ�����Ը��ݷ�����ʱ���ж�̬����		
		if(countRun>=nMsgSend){
			delayTimeObj=getDataDelayTimeObj(DataType.DataDelay,countRun); //��ȡһ����ʱ��Ϣ����
			if(sendNumRunInfo(nMsgSend,delayTimeObj)){//����ָ��������������Ϣ��Զ�̷�����	
				//���ݷ��ͳɹ�
				sendDelayTime(delayTimeObj);//������ʱʱ�䣬��������
				//������ʱʱ������´����ݷ�������
				long delay=delayTimeObj.getDelaytime();//�������ݷ�����ʱʱ��
				if(delay<=300) { nMsgSend+=15;}
				else if( 300<delay && delay<=400) { nMsgSend+=10;}
				else if(400<delay && delay<=700) {nMsgSend -=15;}
				else { nMsgSend-=25;}
				
				//������ʾ���Ͳ���,����ͳһ����������Ϣ����
				if(delayTimeObj!=null){
					sendBroadCast(BroadcastAction.SendThread_PARAMALL,
							
							BroadcastType.SENDCOUNT,"����:"+delayTimeObj.getNumofmsg()+"������",
							BroadcastType.SENDSPEED,"��������:"+Post.speed(delayTimeObj.getDelaytime(), delayTimeObj.getPackagesize()));
				}
			
			}else{
				//����ʧ��			
				sendBroadCast(BroadcastAction.SendThread_PARAMALL,
						
						BroadcastType.SENDCOUNT,"����:"+nMsgSend+"������",
						BroadcastType.SENDSPEED,"��ʱ:"+delayTimeObj.getDelaytime()+"ms,����ʧ��",
						BroadcastType.SENDColor,"red");
				//����ʧ��,��С���η��͵�������
				if(nMsgSend>10){  
					nMsgSend-=25; //��ֹ��������Ϊ0��
				}
			}
		}/*else{ //������������
			sendBroadCast(BroadcastAction.SendThread_PARAMALL,
					BroadcastType.MSGLOCAL,"���ػ�������:"+countRun+"��");					
		}*/
	}
		
	/**
	 * ������ʱʱ��
	 * ���͵������Ʒ�����
	 * @param dt
	 */
	private void sendDelayTime(DataDelayTime dt){
		//������ʱʱ����󵽷�����
		String   aliyunpath="http://47.94.200.41:8080/DataPush/rec";  //Զ�˷�������ַ
		if(dt!=null && dt.getNumofmsg()>0 ){
			GeneralData generalData = new GeneralData();		
			generalData.setDid(DataType.DataDelay);  //��������
			
			generalData.setDt(JsonUtil.object2Json(dt));
			
			String res = null;
//			try {
			res = Post.sendData(aliyunpath,JsonUtil.object2Json(generalData));
//			} catch (SocketTimeoutException e) {				
//				e.printStackTrace();
//			}//��������	
			
			//�ж��Ƿ��ͳɹ�						
			if(resultofPost.equals(res)){							
				Log.d(TAG, "���ͼ�ʱ��Ϣ�ɹ���");
			}
			else /*if(resultofExcep.equals(res))*/{	//����ʧ��					
				Log.d(TAG, "���ͼ�ʱ��Ϣʧ�ܣ�");
			}					
		}
	}
	
	/**
	 * ����n��������Ϣ
	 * @param n��Ҫ���͵���������
	 */
	private void sendNumRunInfo(int n){		
		sendNumRunInfo( n, null);
	}
	
	/**
	 * ����n��������Ϣ
	 * @param n	��Ҫ���͵���������
	 * @param delayTime ����¼������ʱ�Ķ���
	 */
	private boolean sendNumRunInfo(int n,DataDelayTime delayTime){		
		if(n<1)  return false;	
		StringBuilder  strRun=new StringBuilder(1024*100);
		
		DataRunPlus dataRunPlus = dbservice.findNumRowRunInfo(n);	//�ӱ������ݿ��ж�ȡn����������	
		GeneralData generalData = new GeneralData();		
		generalData.setDid(DataType.DataRun);  //��������->��������
		
		if( dataRunPlus.getListRunData().size() == n){  // ����������ת����json��ʽ���ַ���
			strRun.append("[");

			for(DataRun dataRun: dataRunPlus.getListRunData()){
				strRun.append(JsonUtil.object2Json(dataRun));
				strRun.append(",");
			}
			strRun.deleteCharAt(strRun.lastIndexOf(","));//ɾ������Ǹ�","
			strRun.append("]");
		
			generalData.setDt( strRun.toString());
			
			String result=sendPostGeneral(generalData,delayTime);//�������ݵ�������
			if(delayTime!=null){
				delayTime.setNumofmsg(n); //���η�����Ϣ����					
			}
			
			if(resultofPost.equals(result))
			{
				dbservice.deleteRunInfo(dataRunPlus.getId(), dataRunPlus.getIdEnd());//������Ϣ�ɹ��󣬴����ݿ���ɾ��������Ϣ				
				Log.d(TAG, "�ɹ�����"+n+"��������Ϣ��" );							
				return true;
			}else /*if(resultofExcep.equals(result))*/{ //����ʧ��
//				login = false;
				Log.d(TAG, "����"+n+"��������Ϣʧ�ܣ�");
				return false;
			}
		}			
		Log.d(TAG, "SQLite���ݿ���ص�������Ϣ��������������������");
		return false;		
	}
	
	//�������ݣ�������ʱʱ��
	private String sendPostGeneral(GeneralData generalData ){					
		return sendPostGeneral(generalData,null);
	}
		
	//�������ݣ�������ʱʱ��
	private String sendPostGeneral(GeneralData generalData ,DataDelayTime delayTime ){
		String response=null;
		long  	beforeSecond,
				afterSecond,
				timePeriod;	
		 		
		String  strJson=JsonUtil.object2Json(generalData); //���η��͵��ַ���
		long  countBytes=strJson.getBytes().length + 5;  //�ı�����(byte)
		 
		beforeSecond=System.currentTimeMillis();; //����֮ǰʱ��			
//		try {
		response = Post.sendData(path,strJson ); //��������
//		} catch (SocketTimeoutException e) {		
//			e.printStackTrace();
//		}
		afterSecond=System.currentTimeMillis();; //����֮��ʱ��
				
		timePeriod=afterSecond-beforeSecond; //���㷢������ʱ��,ms����·��Ӧʱ��RTT
		
		long speedn=countBytes*1000/timePeriod; //�������� ����λ  Byte/s
		
		if(delayTime!=null){    //������ʱ����ĸ������
			delayTime.setDelaytime(timePeriod);   //��ʱʱ��,ms
			delayTime.setPackagesize( countBytes ); //����С,Bytes
			delayTime.setSpeed(speedn);			  //�����ٶ�,Bytes/s
		}
				
		return response;
	}
	

	//��ȡ��ʱʱ�����	
	private DataDelayTime getDataDelayTimeObj(int dataType, long nOfInfoUnsent ){
		return getDataDelayTimeObj(dataType,nOfInfoUnsent,0);
	}
	
	private DataDelayTime getDataDelayTimeObj(int dataType, long nOfInfoUnsent ,long nOfInfoSent){
		
		
		String dateStamp=TimeUtil.getTimestamp(); //ʱ���
		
		DataDelayTime  delaytemp=new DataDelayTime();

		delaytemp.setCncId(DaqData.forthG); //4G��Ӫ��
		delaytemp.setTerminalid(DaqData.getAndroidId());
		delaytemp.setDatatype(dataType);    //��������
		delaytemp.setNumofmsgunsent(nOfInfoUnsent); //δ���͵ı�������
		delaytemp.setTs(dateStamp);       //ʱ���
		delaytemp.setNumofmsg(nOfInfoSent); //���η������ݵ�����
		
		return delaytemp;
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
	
		 
	private String  getPath(String key){
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
	}
	
	//ֹͣ���ݷ����߳�
	public void setIsCountinueRun(boolean flag){
		this.isCountinueRun=flag;
	}
	//��ȡ���ݷ����̵߳�״̬
	public boolean getIsCountinueRun(){
		return isCountinueRun;
	}
	
}





















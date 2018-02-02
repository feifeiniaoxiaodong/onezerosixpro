package com.cnc.daqnew;

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
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cnc.DataBaseService.DBService;
import com.cnc.DataBaseService.DataAlarmPlus;
import com.cnc.DataBaseService.DataRunPlus;
import com.cnc.daq.DaqData;
import com.cnc.daq.MainActivity;
import com.cnc.domain.DataDelayTime;
import com.cnc.domain.DataLog;
import com.cnc.domain.DataReg;
import com.cnc.domain.DataRun;
import com.cnc.domain.DataType;
import com.cnc.domain.GeneralData;
import com.cnc.netService.Post;
import com.cnc.utils.JsonUtil;
import com.cnc.utils.LogLock;
import com.cnc.utils.RegLock;

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
	private Handler   daqActvityHandler=null;  //Activity��Handler
   	private Handler   mainActivityHandler =null;
	
	int[]   keys={1,5,10,15,30,50,60,70,80,90,100};
	int  	pointer=0,
			len=keys.length;
	
	public DataTransmitThread(){
		dbservice=DBService.getInstanceDBService();
		mainActivityHandler=MainActivity.getMainActivityHandler();
	}
	
	public DataTransmitThread(Handler handler) {
		daqActvityHandler=handler;
		dbservice=DBService.getInstanceDBService();
		mainActivityHandler=MainActivity.getMainActivityHandler();
//		path=getPath("path"); //��ʼ��Զ�˷�������ַ
	}


	//ֻҪ����̴߳򿪾�һֱ���ⷢ����Ϣ�����õ�¼��ע�ᶼ���Է���������Ϣ�ͱ�����Ϣ
//	@Override
	public void run() {
//		DataDelayTime  delayTime=null;//��ʱʱ���������
		String res = null;
//		long  countRun=0; //δ���͵�������Ϣ������
	
		String sPath=getPath(HandleMsgTypeMcro.SERVICES_ALIYUN); //��ʼ��Զ�˷�������ַ
//		String sPath=getPath(HandleMsgTypeMcro.SERVICES_YANG); //��ʼ��Զ�˷�������ַ
		if(sPath!=null){
			path=sPath;
		}
		
		while(isCountinueRun) //���ע����Ϣ��������Ϣ����Ϣ���Ǿ�һֱ����
		{
			synchronized(RegLock.class){
				//����ע����Ϣ
				if(!DaqData.getListDataReg().isEmpty()){
					
					DataReg dataReg= DaqData.getListDataReg().get(0); 
					GeneralData generalData = new GeneralData();
					generalData.setDid(DataType.DataReg); //ע����Ϣ
					if(dataReg.getId()!=null) {
						 generalData.setDt(JsonUtil.object2Json(dataReg));
		                 try {
							res = Post.sendData(path, JsonUtil.object2Json(generalData));//����ע����Ϣ
						} catch (SocketTimeoutException e) {							
							e.printStackTrace();
						}	
	                    if(resultofPost.equals(res))
	                    {
	                    	DaqData.getListDataReg().remove(0);
	                        Log.d(TAG, "ע��ɹ�");
	                    }
	                    else {
	                        Log.d(TAG,"ע��ʧ�ܣ�");
	                    }						
					}					
				}
			}
			
			synchronized(LogLock.class){
				if(!DaqData.getListDataLog().isEmpty()){
					DataLog dataLog=DaqData.getListDataLog().get(0);
					GeneralData generalData2 = new GeneralData();
					generalData2.setDid(DataType.DataLog); //��������->��¼�ǳ���Ϣ
					if(dataLog.getId()!=null){
						generalData2.setDt(JsonUtil.object2Json(dataLog));
		                try {
							res = Post.sendData(path, JsonUtil.object2Json(generalData2));//���͵�¼�ǳ���Ϣ
						} catch (SocketTimeoutException e) {							
							e.printStackTrace();
						}	
	                    if(resultofPost.equals(res))
	                    {
	                    	DaqData.getListDataLog().remove(0);
	                        Log.d(TAG, "��¼�ǳ��ɹ�");
	                    }
	                    else {
	                        Log.d(TAG,"��¼�ǳ�ʧ�ܣ�");
	                    }	
					}
				}
			}
											
			//������Ϣ										
			sendAlarmInfo();
			
			//����������Ϣ
			sendRunInfo();						
			//���ͼ��
			try {
				Thread.sleep(500);
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
			}
			else if(resultofExcep.equals(res)){	//����ʧ��
//				login = false;			//����ʧ�ܾ���Ҫ���·���ע����Ϣ
				Log.d(TAG, "���ͱ�����Ϣʧ�ܣ�");
			}
			
			//������ʱʱ��
			sendDelayTime(delayTime);			
		}	
	}
	
	/**
	 * ����������Ϣ
	 * ������ж��̷߳��͵Ļ���Ҫ�Ը÷�������ͬ��,��Ϊ�����з��ʹ�����Դ
	 */
	private void sendRunInfo(){
		DataDelayTime  delayTime=null;//��ʱʱ���������
		long  countRun=0; //δ���͵�������Ϣ������
				
		countRun=dbservice.getCountRunInfo();//���SQLite���ݿ���������Ϣ������
		String str=countRun+"";
		sendMsg2Main(str,HandleMsgTypeMcro.MSG_COUNTRUN);//���͸�UI��Handler
		
		//�����ض�������������Ϣ
		if(countRun>= keys[pointer = (pointer)%len] ){
			delayTime=getDataDelayTimeObj(DataType.DataDelay,countRun); //��ȡһ����ʱ��Ϣ����
			sendNumRunInfo(keys[pointer],delayTime);  //����ָ��������������Ϣ��������	
			++pointer;					
		}
		//������ʱʱ�䣬��������
		sendDelayTime(delayTime);
	}
	
	/**
	 * ������ʱʱ��
	 * @param dt
	 */
	private void sendDelayTime(DataDelayTime dt){
		//������ʱʱ����󵽷�����
		if(dt!=null && dt.getNumofmsg()>0 ){
			GeneralData generalData = new GeneralData();		
			generalData.setDid(DataType.DataDelay);  //��������
			
			generalData.setDt(JsonUtil.object2Json(dt));
			
			String res = null;
			try {
				res = Post.sendData(path,JsonUtil.object2Json(generalData));
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//��������			
			//�ж��Ƿ��ͳɹ�						
			if(resultofPost.equals(res)){							
				Log.d(TAG, "���ͼ�ʱ��Ϣ�ɹ���");
			}
			else if(resultofExcep.equals(res)){	//����ʧ��					
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
	private void sendNumRunInfo(int n,DataDelayTime delayTime){		
			
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

			if(resultofPost.equals(result))
			{
				dbservice.deleteRunInfo(dataRunPlus.getId(), dataRunPlus.getIdEnd());//������Ϣ�ɹ��󣬴����ݿ���ɾ��������Ϣ
				
				Log.d(TAG, "�ɹ�����"+n+"��������Ϣ��" );
			}
			else if(resultofExcep.equals(result)){
//				login = false;
				Log.d(TAG, "����"+n+"��������Ϣʧ�ܣ�");
			}
			
			if(delayTime!=null){
				delayTime.setNumofmsg(n); //���η��͵���Ϣ����
			}
		
		}else{
			Log.d(TAG, "���ص�������Ϣ��������������������");
			return ;
		}
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
			
		try {
			response = Post.sendData(path,strJson ); //��������
		} catch (SocketTimeoutException e) {		
			e.printStackTrace();
		}
		afterSecond=System.currentTimeMillis();; //����֮��ʱ��
				
		timePeriod=afterSecond-beforeSecond; //���㷢������ʱ��
		
		long speedn=countBytes*1000/timePeriod; //�������� ����λ  B/s
		
		if(delayTime!=null){    //������ʱ����ĸ������
			delayTime.setDelaytime(timePeriod);   //��ʱʱ��
			delayTime.setPackagesize( countBytes ); //����С
			delayTime.setSpeed(speedn);			  //�����ٶ�
		}
		
//		String str="��ʱ:"+ timePeriod+"ms;"+ "���ݴ�С: "+countBytes +"bytes;"+"��������:"+speed(timePeriod,countBytes);
		StringBuilder strb=new StringBuilder();
		strb.append(timePeriod+"ms").append(":");
		strb.append(countBytes+"bytes").append(":");
		strb.append(speed(timePeriod,countBytes));
		sendMsg2Main(strb.toString(),HandleMsgTypeMcro.MSG_DELAYTIME);//����ҳ����ʾ���η��͵���ʱ���ٶȲ���
		
		return response;
	}
	

	//��ȡ��ʱʱ�����	
	private DataDelayTime getDataDelayTimeObj(int dataType, long nOfInfoUnsent ){
		return getDataDelayTimeObj(dataType,nOfInfoUnsent,0);
	}
	
	private DataDelayTime getDataDelayTimeObj(int dataType, long nOfInfoUnsent ,long nOfInfoSent){
		
		@SuppressLint("SimpleDateFormat") 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//ʱ�����ʽ
		String dateStamp=formatter.format(new Date()); //ʱ���
		
		DataDelayTime  delaytemp=new DataDelayTime();

		delaytemp.setCncId(DaqData.forthG); //4G��Ӫ��
		delaytemp.setTerminalid(DaqData.getAndroidId());
		delaytemp.setDatatype(dataType);    //��������
		delaytemp.setNumofmsgunsent(nOfInfoUnsent); //δ���͵ı�������
		delaytemp.setTs(dateStamp);       //ʱ���
		delaytemp.setNumofmsg(nOfInfoSent); //���η������ݵ�����
		
		return delaytemp;
	}
	
	
	//������Ϣ�����߳�
	private void sendMsg2Main(Object obj, int what) {	
		sendMsg(mainActivityHandler, obj, what,0,0);
	}
	
	//������Ϣ�����߳�
	private void sendMsg2Main(Object obj, int what, int arg1){ 	
		sendMsg(mainActivityHandler, obj, what, arg1, 0);
	}

	//������Ϣ��ͨ����
	private void sendMsg(Handler handler,Object obj, int what, int arg1, int arg2) {	
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		handler.sendMessage(msg);
	}
	
	
	 private String speed(long time,long len){		 
		 String result="";
		 if(time>0){
			 long s=len*1000/time;
			 result=s+"B/s";
			 if(s>1024){
				 s=s/1024;
				 result=s+"KB/s";
			 }
			 if(s>1024){
				 s=s/1024;
				 result=s+"MB/s";
			 }
			 if(s>1024){
				 s=s/1024;
				 result=s+"GB/s";
			 }			 
			 String size=len+"B";
			 if(len>1024){
				 len=len/1024;
				 size=len+"KB";
			 }			 
			 if(len>1024){
				 len=len/1024;
				 size=len+"MB";
			 }			 
			 String t=time+"ms";
			 if(time>1000){
				 time=time/1000;
				 t=time+"sec";
				 if(time>60){
					 time=time/60;
					 t=time+"min";
				 }
			 }
			 result=result+"("+size+","+t+")";	 
		 }
		 return result;
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
	
	
	public void setIsCountinueRun(boolean flag){
		this.isCountinueRun=flag;
	}
	
	public boolean getIsCountinueRun(){
		return isCountinueRun;
	}
	
}





















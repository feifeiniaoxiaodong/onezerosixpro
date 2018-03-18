package com.cnc.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Properties;

import android.util.Log;

import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataDelayTime;
import com.cnc.domain.DataType;
import com.cnc.domain.GeneralData;
import com.cnc.net.service.Post;
import com.cnc.utils.JsonUtil;

public class Transmit_Test_offline implements Runnable{

	
	boolean login = false;    	
//	private String path ="http://192.168.1.102:8080/DataPush/rec";//������������·��    	
	private String path ="http://47.94.200.41:8080/DataPush/rec";//������������·��    	
//  private String path ="http://111.198.20.237:7070/DataInputServiceTest/pushData";//������������·��
	String resultofPost = "ac"; 
	String resultofExcep = "ero";
    	
	String res = null;
	private final String tag = "hnctest";
	
//	@Override
	public void run1() {
		
		DataAlarm dataalarm=new DataAlarm();
		dataalarm.setId("20171030");
		dataalarm.setNo("NO0001");
		dataalarm.setF((byte)1);
		dataalarm.setTime(new Date().toString());
		dataalarm.setCtt("������Ϣ����ֹͣ��");
		
//		private String 	CncId;	//����ID
//		private String 	TerminalId; //�ɼ��ն�ID
//		private int     DataType ;  //��������
//		private long    NumOfMsg ;  // �˴η�����Ϣ������
//		private long    NumOfMsgUnsent ;  //δ���͵ı�����������
//		private long    DelayTime;  // ��ʱʱ�䣬��λ ms
//		private long    PackageSize; //���ݰ���С���������ݣ�����λ KB
//		private double  speed ;     //����
//		private String  TimeStamp; //ʱ���
		
		DataDelayTime  delaytime=new DataDelayTime("NO0001" ,"ANDROID002",
				3,12,200,300,1024,23,"20171129 17:20:38");
		
		while(true){
			
			//�����ݴ������
			GeneralData generalData = new GeneralData();
//			generalData.setDid(DataType.DataAlarm); 		//������Ϣ
//			generalData.setDt(JsonUtil.object2Json(dataalarm));
			
			//�����ݴ������
//			GeneralData generalData = new GeneralData();
			generalData.setDid(DataType.DataDelay); 		//��ʱʱ��
			generalData.setDt(JsonUtil.object2Json(delaytime));
			
			Log.i(tag,delaytime.toString());

//			try {
				res = Post.sendData(path, JsonUtil.object2Json(generalData));
//			} catch (SocketTimeoutException e1) {
//				e1.printStackTrace();
//			}
			//�ж��Ƿ��ͳɹ�						
			if(resultofPost.equals(res))
			{
//				dbService.deleteAlarmData(dataAlarmPlus.id);//������Ϣ�ɹ��󣬴����ݿ���ɾ��������Ϣ
				Log.i(tag, "������ʱ��Ϣ�ɹ�");
			}
			else if(resultofExcep.equals(res)) //����ʧ��
//				login = false;			//��Ҫ���µ�¼
				
				Log.i(tag, "������ʱ��Ϣʧ��");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	
	}
	
//	private static final String  pathResource="/com/cnc/resources/source.properties";
	@Override
	public void run() {
		
		InputStream in=null;
		Properties pro=new Properties();
		
		in=Transmit_Test_offline.class.getResourceAsStream(DataType.pathResource);
		
		try {
			pro.load(new InputStreamReader(in, "utf-8"));
			
			String path= pro.getProperty("pathaliyun");
			String path106=pro.getProperty("path106");
			
			System.out.println(path );
			System.out.println(path106 );
						
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}

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
//	private String path ="http://192.168.1.102:8080/DataPush/rec";//服务器的链接路径    	
	private String path ="http://47.94.200.41:8080/DataPush/rec";//服务器的链接路径    	
//  private String path ="http://111.198.20.237:7070/DataInputServiceTest/pushData";//服务器的链接路径
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
		dataalarm.setCtt("报警信息，轴停止！");
		
//		private String 	CncId;	//机床ID
//		private String 	TerminalId; //采集终端ID
//		private int     DataType ;  //数据类型
//		private long    NumOfMsg ;  // 此次发送信息的条数
//		private long    NumOfMsgUnsent ;  //未发送的本地数据条数
//		private long    DelayTime;  // 延时时间，单位 ms
//		private long    PackageSize; //数据包大小（有用数据），单位 KB
//		private double  speed ;     //速率
//		private String  TimeStamp; //时间戳
		
		DataDelayTime  delaytime=new DataDelayTime("NO0001" ,"ANDROID002",
				3,12,200,300,1024,23,"20171129 17:20:38");
		
		while(true){
			
			//将数据打包发送
			GeneralData generalData = new GeneralData();
//			generalData.setDid(DataType.DataAlarm); 		//报警信息
//			generalData.setDt(JsonUtil.object2Json(dataalarm));
			
			//将数据打包发送
//			GeneralData generalData = new GeneralData();
			generalData.setDid(DataType.DataDelay); 		//延时时间
			generalData.setDt(JsonUtil.object2Json(delaytime));
			
			Log.i(tag,delaytime.toString());

//			try {
				res = Post.sendData(path, JsonUtil.object2Json(generalData));
//			} catch (SocketTimeoutException e1) {
//				e1.printStackTrace();
//			}
			//判断是否发送成功						
			if(resultofPost.equals(res))
			{
//				dbService.deleteAlarmData(dataAlarmPlus.id);//发送信息成功后，从数据库中删除该条信息
				Log.i(tag, "发送延时信息成功");
			}
			else if(resultofExcep.equals(res)) //发送失败
//				login = false;			//需要重新登录
				
				Log.i(tag, "发送延时信息失败");
			
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

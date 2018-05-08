package com.cnc.hangtian.dnc.driver;

import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
//import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
//import java.net.InetSocketAddress;
import java.net.Socket;
//import java.net.SocketAddress;
import java.net.UnknownHostException;

import com.cnc.hangtian.domain.AlarmDataHangtian;
import com.cnc.hangtian.domain.AxisDataHangtian;
import com.cnc.hangtian.domain.SystemDataHangtian;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 航天数控DNC接口驱动
 * @author wei
 */
@SuppressLint("DefaultLocale") public class HangtianDNCDriver {
	private final String TAG="HANGTIANDNCDRIVER";
	private Socket socket=null;
	private DataInputStream in=null;
//	private DataOutputStream out=null;
	private OutputStreamWriter outwriter=null;
	
	private static final String charsetASCII = "US-ASCII";
	private static final String charsetGBK = "GBK";
	
	
	private String ipAddr="192.168.188.141";
	private int port =5565 ;
	
	public HangtianDNCDriver(String ipAddr, int port) {
		super();
		this.ipAddr = ipAddr;
		this.port = port;
	}
	
	/**
	 * 建立连接函数
	 * @return 0 成功 ；-1 失败
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public int buildConnectionAndCNC() throws Exception{
		int res=0;
		//1建立socket连接
		socket=new Socket(ipAddr,port);
		//2 测试是否能够连接CNC
		if(socket.isConnected()){
			Log.d(TAG, ipAddr+"连接成功");
		}else{
			return -1; //连接失败
		}
		socket.setTcpNoDelay(true);
		int buffersize=4*1024; //4k缓冲区
		//3设置输入输出缓冲区
		socket.setSendBufferSize(buffersize);
		socket.setReceiveBufferSize(buffersize);
		in=new DataInputStream(socket.getInputStream());
//		out=new DataOutputStream(socket.getOutputStream());
		outwriter =new OutputStreamWriter(socket.getOutputStream(), "gbk");
		
		//设置超时时间
		socket.setSoTimeout(5000);
		socket.setKeepAlive(true);
			
		return res;
	}
	
	/**
	 * 重新建立连接
	 * @return
	 * @throws Exception
	 */
	public int  rebuildConnectionAndCNC() throws Exception{
		//先关闭连接 
		closeConnection();
		//重新建立连接
		return buildConnectionAndCNC();
	}
	
	
	/**
	 * 查看当前与数控系统的连接状态
	 * @return
	 */
	public boolean connectionState(){
		boolean flag= false;
		if(socket!=null && !socket.isClosed()){
			flag= socket.isConnected();
		}		
		return flag;
	}
	
	/**
	 * 关闭与数控系统的连接
	 */
	public void closeConnection(){
		
		try {				
			if(outwriter!=null){
				outwriter.close();
				outwriter=null;	
			}
			if(in!=null){
				in.close();
				in=null;
			}	
			if(socket!=null){
				if(!socket.isClosed()) socket.close();
				socket=null;
			}
		
		} catch (IOException e) {				
			e.printStackTrace();
		}				
	}
	
	/**
	 * 航天数控
	 * 采集轴信息
	 * @return
	 */
	public AxisDataHangtian getAxisinfo(){
		String axisInfoString=null; 		//字符串格式的轴数据
		AxisDataHangtian axisDataHangtian=null; 
		char[] a = {0x55,0xAA,0x01,0x00};
		String s1 = String.valueOf(a[0]);		
		String s2 = String.valueOf(a[1]);
		String s3 = String.valueOf(a[2]);
		String s4 = String.valueOf(a[3]);
		String s = s1+s2+s3+s4;
		
		sendCommandToCNC(s); //发送数据采集指令
		
		try {
			Object obj=null;
			axisInfoString= receiveDataFromCNC(); //接收采集数据
			if(axisInfoString!=null && !"".equals(axisInfoString)){
				obj=parseAcquisitionDatastr(axisInfoString);
				//解析采集数据
				if( obj instanceof AxisDataHangtian){
					axisDataHangtian=(AxisDataHangtian)obj;
				}
			}else{
				Log.d(TAG,"读到的轴信息为空");
			}
			
		} catch (Exception e) {			
			e.printStackTrace();
		}				
		return axisDataHangtian;
	}
	
	/**
	 * 航天数控
	 * 采集报警信息
	 * @return
	 */
	public AlarmDataHangtian getAlarmInfo(){
		String alarmInfostring=null;
		AlarmDataHangtian alarmDataHangtian=null;
		char[] a = {0x55,0xAA,0x02,0x00};
		String s1 = String.valueOf(a[0]);
		String s2 = String.valueOf(a[1]);
		String s3 = String.valueOf(a[2]);
		String s4 = String.valueOf(a[3]);
		String s = s1+s2+s3+s4;
		
		sendCommandToCNC(s); //发送数据采集指令
		
		try {
			alarmInfostring= receiveDataFromCNC();
			Object obj=null;
			if(alarmInfostring!=null && !"".equals(alarmInfostring)){
				obj=parseAcquisitionDatastr(alarmInfostring);
				//解析采集数据
				if( obj instanceof AlarmDataHangtian){
					alarmDataHangtian=(AlarmDataHangtian)obj;
				}
			}else{
				Log.d(TAG,"读到的报警信息为空");
			}
						
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return alarmDataHangtian;
	}
	
	/**
	 * 航天数控
	 * 采集系统信息
	 * @return
	 */
	public SystemDataHangtian getSystemInfo(){
		SystemDataHangtian systemDataHangtian=null;
		String systemInfostring=null;
		char[] a = {0x55,0xAA,0x03,0x00};
		String s1 = String.valueOf(a[0]);
		String s2 = String.valueOf(a[1]);
		String s3 = String.valueOf(a[2]);
		String s4 = String.valueOf(a[3]);
		String s = s1+s2+s3+s4;
		
		sendCommandToCNC(s);
		
		try {
			systemInfostring=receiveDataFromCNC();
			Object obj=null;
			if(systemInfostring!=null && !"".equals(systemInfostring)){
				obj=parseAcquisitionDatastr(systemInfostring);
				if(obj instanceof SystemDataHangtian){
					systemDataHangtian=(SystemDataHangtian)obj;
				}
			}else{
				Log.d(TAG,"读到的系统信息为空");
			}			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		return systemDataHangtian;
	}
	
	
	/**
	 * 解析采集数据字符串为 数据对象
	 * @param infostr  采集字符串数据
	 * @return
	 */
	private Object parseAcquisitionDatastr(String infostr) throws Exception{
		
		BigInteger p2 = new BigInteger(infostr.substring(0, 2),16);
		int p1 = p2.intValue(); //数据类型
		switch(p1){
		case 1:  //轴信息
			AxisDataHangtian axisDataHangtian=new AxisDataHangtian();
			axisDataHangtian.setType(String.valueOf(p1));
			float []c_axis = new float[3];
			int q1 = Dao_string(infostr.substring(8, 16));
			int q2 = Dao_string(infostr.substring(16, 24));
			int q3 = Dao_string(infostr.substring(24, 32));
			c_axis[0] = (float)(q1/10000F);
			c_axis[1] = (float)(q2/10000F);
			c_axis[2] = (float)(q3/10000F);
			axisDataHangtian.setC_axis(c_axis);
			
			int q4 = Dao_string(infostr.substring(32, 40));
			int q5 = Dao_string(infostr.substring(40, 48));
			int q6 = Dao_string(infostr.substring(48, 56));
			float []a_axis_work = new float[3];
			a_axis_work[0] = (float)(q4/10000F);
			a_axis_work[1] = (float)(q5/10000F);
			a_axis_work[2] = (float)(q6/10000F);
			axisDataHangtian.setA_axis_work(a_axis_work);
			
			int q7 = Dao_string(infostr.substring(56, 64));
			int q8 = Dao_string(infostr.substring(64, 72));
			int q9 = Dao_string(infostr.substring(72, 80));
			float []a_axis_relative = new float[3];
			a_axis_relative[0] = (float)(q7/10000F);
			a_axis_relative[1] = (float)(q8/10000F);
			a_axis_relative[2] = (float)(q9/10000F);
			axisDataHangtian.setA_axis_relative(a_axis_relative);
			
			int q10 = Dao_string(infostr.substring(80, 88));
			int q11 = Dao_string(infostr.substring(88, 96));
			int q12 = Dao_string(infostr.substring(96, 104));
			float []a_axis_machine = new float[3];
			a_axis_machine[0] = (float)(q10/10000F);
			a_axis_machine[1] = (float)(q11/10000F);
			a_axis_machine[2] = (float)(q12/10000F);
			axisDataHangtian.setA_axis_machine(a_axis_machine);
			
			int q13 = Dao_string(infostr.substring(104, 112));
			int q14 = Dao_string(infostr.substring(112, 120));
			int q15 = Dao_string(infostr.substring(120, 128));
			float []a_axis_remainder = new float[3];
			a_axis_remainder[0] = (float)(q13/10000F);
			a_axis_remainder[1] = (float)(q14/10000F);
			a_axis_remainder[2] = (float)(q15/10000F);
			axisDataHangtian.setA_axis_remainder(a_axis_remainder);
			
			int q16 = Dao_string(infostr.substring(128, 136));
			int q17 = Dao_string(infostr.substring(136, 144));
			int q18 = Dao_string(infostr.substring(144, 152));
			long []a_f_value = new long[3];	
			a_f_value[0] = Math.abs(q16);
			a_f_value[1] = Math.abs(q17);
			a_f_value[2] = Math.abs(q18);
			axisDataHangtian.setA_f_value(a_f_value);
			
			int q19 = Dao_string(infostr.substring(152, 160));
			int q20 = Dao_string(infostr.substring(160, 168));
			axisDataHangtian.setC_s_value(q19);
			axisDataHangtian.setA_s_value(q20);
			
			String source = To_ASCII(infostr.substring(168, 215));
			axisDataHangtian.setTimestamp(source);
			
			return axisDataHangtian;
//			break;
		case 2://报警
			AlarmDataHangtian alarmDataHangtian=new AlarmDataHangtian();
			
			alarmDataHangtian.setType(String.valueOf(p1));
			alarmDataHangtian.setAlarmcode(Dao_string(infostr.substring(8, 16)));
			alarmDataHangtian.setAlarmtime_occur(To_ASCII(infostr.substring(16, 64)));
			alarmDataHangtian.setAlarmtime_remove(Dao_string(infostr.substring(64, 72)));
			alarmDataHangtian.setAlarmnum(To_ASCII(infostr.substring(72, 74)));
			alarmDataHangtian.setTimestamp(To_ASCII(infostr.substring(74, 128)));
			
			String s1 = infostr.substring(128, 384);
			int []alarmcode_array = new int[32];
			int j1 = 0;
			for(int i = 0;i<alarmcode_array.length;i++){
				alarmcode_array[i] = Dao_string(s1.substring(j1, j1+8));
				j1 = j1+8;
			}
			alarmDataHangtian.setAlarmcode_array(alarmcode_array);
			
			String s2 = infostr.substring(384, 1920);
			String []alarmtime_occur_array = new String[32];	
			int p = 0;
			for(int i = 0;i<alarmtime_occur_array.length;i++){
				alarmtime_occur_array[i] = To_ASCII(s2.substring(p, p+48));
				p  = p+48;
			}
			alarmDataHangtian.setAlarmtime_occur_array(alarmtime_occur_array);
			
			return alarmDataHangtian;
		case 3: ////系统
			SystemDataHangtian systemDataHangtian=new SystemDataHangtian();
			
			systemDataHangtian.setType(String.valueOf(p1));
			systemDataHangtian.setSystemid(To_ASCII(infostr.substring(2, 18)));
			systemDataHangtian.setSystemtype(To_ASCII(infostr.substring(18, 50)));
			systemDataHangtian.setSystemver(To_ASCII(infostr.substring(50, 82)));
			systemDataHangtian.setGgroup(To_ASCII(infostr.substring(82, 162)));
			systemDataHangtian.setPn(To_ASCII(infostr.substring(162, 192)));
			
			systemDataHangtian.setPd(Integer.valueOf(Dao_string(infostr.substring(192, 200))));
			systemDataHangtian.setPs(Integer.valueOf(Dao_string(infostr.substring(200, 208))));
			systemDataHangtian.setPl(Integer.valueOf(Dao_string(infostr.substring(208, 216))));
			
			systemDataHangtian.setS_loadcurrent(Dao_string(infostr.substring(216, 224)));
			long []axis_loadcurrent = new long[3];
			axis_loadcurrent[0] = Dao_string(infostr.substring(224, 232));
			axis_loadcurrent[1] = Dao_string(infostr.substring(232, 240));
			axis_loadcurrent[2] = Dao_string(infostr.substring(240, 248));
			systemDataHangtian.setAxis_loadcurrent(axis_loadcurrent);
			
			systemDataHangtian.setOntime(Dao_string(infostr.substring(248, 256)));
			systemDataHangtian.setRuntime(Dao_string(infostr.substring(256, 264)));
			systemDataHangtian.setTimestamp(To_ASCII(infostr.substring(264, 312)));
						
			return systemDataHangtian;			
		}
		
		return null;
	}
	
	/**
	 * 发送数据采集命令
	 * 使用“gbk”编码格式，命令才能被正确接收
	 * @param command
	 */
	private void sendCommandToCNC(String command){
		
		if(outwriter==null)  return ;
			
		try {			
			outwriter.write(command);
			outwriter.flush();			
		}catch (IOException e){
			e.printStackTrace();
		}		
	}
	
	/**
	 * 接收CNC数据
	 * @return
	 * @throws IOException
	 */
	private String receiveDataFromCNC() throws IOException{
		String serverString = "";
		String hex = null;
		byte[]buffer =new byte[1024*2];
		
		if(in==null) return null;
		int size=0;
		int currentIndex=0;
		
		try {
			//延时一段时间，防止读不到数据
			Thread.sleep(150);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while( in.available()>0){
			size = in.read(buffer);
			currentIndex+=size;
		}
				
		for(int i=0; i< currentIndex ;i++){
			hex = Integer.toHexString(buffer[i] & 0xFF);
			if(hex.length()==1){
				hex = '0' + hex;
			}
			serverString += hex.toUpperCase();
		}
		
		if(serverString != null){
			serverString=serverString.trim().toUpperCase();
		}
				
		return serverString;
	}

	//转换ASCII
	private String To_ASCII(String str){
		int j = 0;
		String ss = "";
		for(int i = 0;i<(str.length())/2;i++){
			String sa = str.substring(j, j+2);
			j+=2;
			int code ;  
		    code = Integer.parseInt(sa, 16);
		    char result = (char) code;  
		    ss  = ss+result;
		}		
//		ss=ss.substring(0, ss.indexOf(" ")); //add by wei 2018/4/24
		return ss.trim();
	}
	//倒置字符串
	private int Dao_string(String s){
		String ss  = "";
		String s1 = s.substring(6, 8);
		String s2 = s.substring(4, 6);
		String s3 = s.substring(2, 4);
		String s4 = s.substring(0, 2);
		ss = s1+s2+s3+s4;
		//有符号十六进制转成有符号十进制
		BigInteger i = new BigInteger(ss,16);
		int j = i.intValue();
		return j;
	}
	
	
	
	
	

}

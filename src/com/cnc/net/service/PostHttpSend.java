package com.cnc.net.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class PostHttpSend {
	final private String TAG="PostHttpSend";
	private final String encoding = "utf-8";//这个是必须，不然传输的数据会发生乱码
	private static Object synObj=new Object();
	private HttpURLConnection conn=null;
	
	private static PostHttpSend postHttpSend=null;	
	public static PostHttpSend getInstance(){
		if(postHttpSend==null){
			synchronized(synObj){
				if(postHttpSend==null){
					postHttpSend=new PostHttpSend();
				}
			}
		}
		return postHttpSend;
	}
	
	
	/**
	 * http协议是无状态协议，底层使用socket,不调用disconnect()，下次连接会重用共享的链接资源。
	 * @param path
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	private boolean openConnection(String path) throws Exception,IOException{
		boolean isConnected=false;
		
		conn = (HttpURLConnection) new URL(path).openConnection();    //建立http连接
		conn.setConnectTimeout(8000); //超时时间8s
		conn.setReadTimeout(20000);   //设置数据读取超时时间为20s 
		conn.setRequestMethod("POST"); //传输方式post
		conn.setDoOutput(true);		//允许对外输出数据，即http传输中的实际内容
		conn.setDoInput(true);   //允许读取数据		
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //
		
//		if(conn.)
		
		return true;
	}
	
	

}

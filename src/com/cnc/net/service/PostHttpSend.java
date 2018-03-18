package com.cnc.net.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class PostHttpSend {
	final private String TAG="PostHttpSend";
	private final String encoding = "utf-8";//����Ǳ��룬��Ȼ��������ݻᷢ������
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
	 * httpЭ������״̬Э�飬�ײ�ʹ��socket,������disconnect()���´����ӻ����ù����������Դ��
	 * @param path
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	private boolean openConnection(String path) throws Exception,IOException{
		boolean isConnected=false;
		
		conn = (HttpURLConnection) new URL(path).openConnection();    //����http����
		conn.setConnectTimeout(8000); //��ʱʱ��8s
		conn.setReadTimeout(20000);   //�������ݶ�ȡ��ʱʱ��Ϊ20s 
		conn.setRequestMethod("POST"); //���䷽ʽpost
		conn.setDoOutput(true);		//�������������ݣ���http�����е�ʵ������
		conn.setDoInput(true);   //�����ȡ����		
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //
		
//		if(conn.)
		
		return true;
	}
	
	

}

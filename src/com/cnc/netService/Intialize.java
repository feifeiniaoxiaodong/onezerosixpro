package com.cnc.netService;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import com.cnc.huazhong.HncAPI;

public class Intialize {
	
	static Intialize intialize=new Intialize();
	
	public static Intialize getInstance(){
		return intialize;
	}
	
	
	
	/*
	 * ��ʼ��,�������IP���г�ʼ��������Ŀ��IP
	 * ����ֵ���ڵ���0�����ʼ���ɹ��������ʼ��ʧ��
	 */
	public  int inial()
	{
		int result=-1;
    	String localIp = getLocalIpAddress();  //����IP��ַ
    	localIp = localIp.replaceAll("[\\t\\n\\r]", ";");
    	localIp = findIP(localIp);
    	if((localIp != null) && localIp.startsWith("192"))
    	{
			result= HncAPI.HNCNetInit(localIp, 10015);	//��ʼ��		
    	}
		return result;
	
	}
	
	public  int inial6(int port)
	{
		int result=-1;
    	String localIp = getLocalIpAddress();  //����IP��ַ
    	localIp = localIp.replaceAll("[\\t\\n\\r]", ";");
    	localIp = findIP(localIp);
    	if((localIp != null) && localIp.startsWith("192"))
    	{
			result= HncAPI.HNCNetInit(localIp, port);	//��ʼ��		
    	}
		return result;
	
	}
	
	//ȡ��192��ͷ�������ַ
	public String findIP(String localIp) {
        String[] ip;
        ip = localIp.split(";");
        if(ip.length <=1)  //���������ʱ��û����������ʱ���bug
        	return ip[0];
        else
        {
	        if(ip[0].startsWith("192"))
	        	return ip[0];
	        else
	        	return ip[1];	
        }
    }

	/**
	 * ��ȡ����IP��ַ
	 * @return
	 */
	private String getLocalIpAddress() {  
		String IP = null;  
        StringBuilder IPStringBuilder = new StringBuilder();  
        try {  
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();  //��ȡ����ӿ�
            while (networkInterfaceEnumeration.hasMoreElements()) {  
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();  
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();  //��ȡ����ӿڵ������ַ
                while (inetAddressEnumeration.hasMoreElements()) {  
                    InetAddress inetAddress = inetAddressEnumeration.nextElement();  
                    if (!inetAddress.isLoopbackAddress()&&   
                        !inetAddress.isLinkLocalAddress()&&   
                         inetAddress.isSiteLocalAddress()) {  
                         IPStringBuilder.append(inetAddress.getHostAddress().toString()+"\n");  
                    }  
                }  
            }  
        } catch (SocketException ex) {  
        	
        }   
        IP = IPStringBuilder.toString();  
        return IP;  
	}
}

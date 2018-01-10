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
	 * 初始化,对自身的IP进行初始化和连接目的IP
	 * 返回值大于等于0，则初始化成功，否则初始化失败
	 */
	public  int inial()
	{
		int result=-1;
    	String localIp = getLocalIpAddress();  //本地IP地址
    	localIp = localIp.replaceAll("[\\t\\n\\r]", ";");
    	localIp = findIP(localIp);
    	if((localIp != null) && localIp.startsWith("192"))
    	{
			result= HncAPI.HNCNetInit(localIp, 10015);	//初始化		
    	}
		return result;
	
	}
	
	public  int inial6(int port)
	{
		int result=-1;
    	String localIp = getLocalIpAddress();  //本地IP地址
    	localIp = localIp.replaceAll("[\\t\\n\\r]", ";");
    	localIp = findIP(localIp);
    	if((localIp != null) && localIp.startsWith("192"))
    	{
			result= HncAPI.HNCNetInit(localIp, port);	//初始化		
    	}
		return result;
	
	}
	
	//取以192开头的网络地址
	public String findIP(String localIp) {
        String[] ip;
        ip = localIp.split(";");
        if(ip.length <=1)  //解决开机的时候没有连接网线时候的bug
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
	 * 获取本地IP地址
	 * @return
	 */
	private String getLocalIpAddress() {  
		String IP = null;  
        StringBuilder IPStringBuilder = new StringBuilder();  
        try {  
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();  //获取网络接口
            while (networkInterfaceEnumeration.hasMoreElements()) {  
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();  
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();  //获取网络接口的网络地址
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

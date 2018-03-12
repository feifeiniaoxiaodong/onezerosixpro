package com.cnc.net.datasend;

public class HandleMsgTypeMcro {
	
	 	public static final int MSG_ISUCCESS = 0;//初始化成功
	    public static final int MSG_IFAILURE = 1;//初始化失败
	    public static final int MSG_LFAILURE = 2;//注册或者登陆失败
	    public static final int MSG_DFAILURE = 3;//获取信息失败
	    
	    public static final int MSG_ALRAM = 10;	//报警信息标识
	    public static final int MSG_REG = 11;	//注册信息
	    public static final int MSG_RUN = 12;	//运行信息
	    
	    public static final int MSG_DELAYTIME = 13;	//延时时间
	    public static final int MSG_COUNTRUN=14 ; //未发送的运行信息的条数
	    
	    public static final int MSG_TEST = 19;//测试用标签
	    
	    
	    //机床的IP地址路径
	    public static final String HUAZHONG1_1="192.168.188.112";
	    public static final String HUAZHONG1_2="192.168.188.114";
	    public static final String HUAZHONG1_3="192.168.188.113";
	    public static final String HUAZHONG1_4="192.168.188.111";
	    public static final String HUAZHONG1_5="192.168.188.110";
	    
	    
	    //服务器地址
	    public static final String  SERVICES_ALIYUN="pathaliyun";
	    
	    public static final String  SERVICES_LOCATION="pathlocation";
	    
	    public static final String  SERVICES_YANG="pathyang";
	    
	    
	    //view
	    public static final int   	HUAZHONG_UINO= 4001;
	    public static final int   	HUAZHONG_UIALARM= 4002;
	    public static final int   	GAOJING_UINO= 5001;
	    public static final int   	GAOJING_UIALARM= 5002;
	    public static final int   	GSK_UINO= 6001;
	    public static final int   	GSK_UIALARM= 6002;
	    
	    public static final int   	GSK02= 60022;
	    public static final int   	GSK03= 6003;
	    public static final int   	GSK04= 6004;
	    public static final int   	GSK05= 6005;
	    
	    
	
	    
}

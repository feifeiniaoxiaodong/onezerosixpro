package com.cnc.daqnew;

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
	    public static final String HUAZHONG1_1="cncip112";
	    public static final String HUAZHONG1_2="cncip114";
	    public static final String HUAZHONG1_3="cncip113";
	    public static final String HUAZHONG1_4="cncip111";
	    public static final String HUAZHONG1_5="cncip110";
	    
	    
	    //服务器地址
	    public static final String  SERVICES_ALIYUN="pathaliyun";
	    
	    public static final String  SERVICES_LOCATION="pathlocation";
	    
	    public static final String  SERVICES_YANG="pathyang";
	    

}

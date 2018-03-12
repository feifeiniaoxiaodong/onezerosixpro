package com.cnc.domain;

/**
 * 标准数据类型
 * @author wei
 *
 */
public class DataType {
	public static final byte  DataReg = 1;  //注册信息
	public static final byte  DataAlarm = 2; //报警信息
	public static final byte  DataRun = 3;//注册信息   运行信息
	public static final byte  DataLog = 4; //登录、登出信息
	
	public static final  byte  DataDelay=6; //延时时间,自己添加，标准里面没这个类型
	
	public static final String  pathResource="/com/cnc/resources/source.properties"; //服务器地址和机床IP地址常量

	
	
}




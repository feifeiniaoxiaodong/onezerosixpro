package com.cnc.gaojing.ndkapi;
/**
 * 沈阳高精读取信息命令编号
 * @author wei
 *
 */
public class GJApiNum {
//	public static final String  CNC＿ID  ="192.168.188.132";  //数控系统ＩＤ使用ＩＰ地址表示
	//version  information
	public static final int  SYSTEM_MODEL=2541 ; //系统型号,string
	public static final int  SOFTWARE_VER=2541; //软件版本号 ,可能是2542,string
	public static final int  HARDWARE_VER=2543; //硬件版本号,string
	public static final int  SYSTEMKERNEL_VER=2545;//系统内核版本号,string
	
	//running information 
	public static final int  SPINDLE_ACT_SPEED=2048; //主轴实际转速，double
	public static final int  SPINDLE_COM_SPEED=2046; //主轴指令转速，double
	public static final int  SPINDLE_LOAD_CURRENT=2562;//主轴负载电流，double 
	
	public static final int  FEEDAXIS_ACT_SPEED=2028; //进给轴实际速度,double
	
	public static final int  FEEDAXIS_ACT_SPEED_X=2580; //进给轴实际速度,x,double
	public static final int  FEEDAXIS_ACT_SPEED_Y=2581; //进给轴实际速度,y,double
	public static final int  FEEDAXIS_ACT_SPEED_Z=2582; //进给轴实际速度,z,double
	
	
	public static final int  FEEDAXIS_ACT_POSITION_X=2094;//进给轴实际位置,x,double
	public static final int  FEEDAXIS_ACT_POSITION_Y=2096;//进给轴实际位置,y,double
	public static final int  FEEDAXIS_ACT_POSITION_Z=2098;//进给轴实际位置,Z,double
		
	public static final int  FEEDAXIS_COM_POSITION_X=2082; //进给轴指令位置，x,double
	public static final int  FEEDAXIS_COM_POSITION_Y=2084; //进给轴指令位置，Y,double
	public static final int  FEEDAXIS_COM_POSITION_Z=2086; //进给轴指令位置，z,double
	
	public static final int  FEEDAXIS_LOAD_CURRENT_X=2570;//进给轴负载电流，x,double
	public static final int  FEEDAXIS_LOAD_CURRENT_Y=2571;//进给轴负载电流，y,double
	public static final int  FEEDAXIS_LOAD_CURRENT_Z=2572;//进给轴负载电流，z,double
	
	public static final int  PROGRAM_NAME=2152; //当前程序名,string
	public static final int  G_CODE_RUN_STAT=2003; //G代码运行状态,int
	public static final int  G_CODE_LINE_NUM=2013;//G代码行号,int
	public static final int  G_CODE_MODALITY=2561;//G代码模态,返回int32_t[20]的数组值
	
	
	//login information 
	public static final int  TOTAL_RUN_TIME=2539; //累计运行时间，double，上电时间
	public static final int  TOTAL_PROCESS_TIME=2532; //累计加工时间  ,double

}

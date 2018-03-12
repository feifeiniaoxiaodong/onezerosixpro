package com.cnc.huazhong;

public class HncAPI {

	
	/*static {  
        // 加载动态库  
        System.loadLibrary("hncnet");  
        System.loadLibrary("hzHncAPI");  
        
    }*/
/**************************************************************************************/
/*来自hnecnet.h*/

 //和网络连接相关
	/**
	 * HNC_NetInit,网络初始化
	 * @param ip 上位机IP
	 * @param port 上位机端口号
	 * @return 返回值为零则表示初始化成功
	 */
	public static native int HNCNetInit(String ip, int port);
	//HNC_NetConnect，连接机床,返回值为零则表示连接成功
	public static native int HNCNetConnect(String ip, int port);
	//HNC_NetIsConnect，测试目标机器是否已连接，返回值为零则表示已连接
	public static native int HNCNetIsConnect(int clientNo);
	//HNC_NetExit，退出连接
	public static native void HNCNetExit();
	//HNC_NetIsThreadStartup,获取网络线程开启状态；返回0：网络线程未开启；1：网络线程已开启
	public static native int HNCNetIsThreadStartup();
	//HNC_NetSetIpaddr，设置目标机器(机床)IP和端口号；返回0：设置成功
	public static native int HNCNetSetIpaddr(String ip, int port, int clientNo);
	
 //和文件传输相关
	//HNC_NetFileSend,向目标机器(机床)发送文件；返回0：发送成功
	public static native int HNCNetFileSend(String localName, String dstName, int clientNo);
	//HNC_NetFileGet,获取目标机器文件；返回0：获取成功
	public static native int HNCNetFileGet(String localNamme, String dstName, int clientNo);
	//HNC_NetFileCheck,比较上、下位机文件是否一致；返回0：成功
	public static native int HNCNetFileCheck(String localNamme, String dstName, int clientNo);
	//HNC_NetFileRemove,删除下位机（机床）中的文件；返回0：成功
	public static native int HNCNetFileRemove(String dstName, int clientNo);
	
/**************************************************************************************/
/*来自hncalarm.h
	* 摘    要：报警管理：报警统一编号和管理
	*           1) 报警类型共计9种，分别是系统、通道、轴、伺服、PLC、设备、语法、用户PLC、HMI；
	*           2) 报警级别共计2级，分别是报警和提示；
	*           3) 报警号共计9位，
	*              通道、语法：(1位报警类型)+(1位报警级别)+(3位通道号)+(4位报警内容编号)；
	*              轴、伺服  ：(1位报警类型)+(1位报警级别)+(3位轴号)  +(4位报警内容编号)；
	*              其它      ：(1位报警类型)+(1位报警级别)+(7位报警内容编号)；*/
 //和报警相关
//	报警错误类型
//	public class AlarmType
//	{
//		public static final int ALARM_SY = 0;		//值 0系统报警（System）
//		public static final int ALARM_CH = 1;		//	通道报警（Channel）
//		public static final int ALARM_AX = 2;		//	轴报警（Axis）
//		public static final int ALARM_SV = 3;		//	伺服报警（Servo）
//		public static final int ALARM_PC = 4;		//	PLC报警（PLC）
//		public static final int ALARM_DV = 5;		//	设备报警（Dev）
//		public static final int ALARM_PS = 6;		//	语法报警（Program Syntax）
//		public static final int ALARM_UP = 7;		//	用户PLC报警（User PLC）
//		public static final int ALARM_HM = 8;		//	HMI报警（HMI）
//		public static final int ALARM_TYPE_ALL = 9; 
//	}
	//	报警级别
//	public class AlarmLevel
//	{
//		public static final int ALARM_ERR = 0;	//错误（Error）
//		public static final int ALARM_MSG = 1;	//提示（Message）	
//		public static final int ALARM_LEVEL_ALL = 3;
//	}
	//HNC_AlarmRefresh,报警信息刷新,返回0：成功；非0：失败。
	public static native int HNCAlarmRefresh(int clientNo);
	//HNC_AlarmGetNum,获取当前报警的数目，如果返回值-1或小于零，则获取失败；否则获取到报警数
	public static native int HNCAlarmGetNum(int type, int level, int clientNo);//注意这个函数需要改写
	//HNC_AlarmGetHistoryNum,获取历史报警数，如果返回值-1或小于零，则获取失败；否则获取到历史报警数
	public static native int HNCAlarmGetHistoryNum(int clientNo);//注意这个函数需要改写
	//HNC_AlarmGetData,获取当前报警的数据，如果返回null，则获取失败；否则获取报警文本
	public static native String HNCAlarmGetDataDefault(int type, int level,int index);//重载，clientNo默认为0
	//返回值格式：alarmNo::alarmText,默认在前面添加报警号冒号之后是报警文本
	public static native String HNCAlarmGetData(int type, int level,int index, int clientNo);//注意这个函数需要改写

	//HNC_AlarmGetHistoryData，获取获取从index开始的最多50条报警历史数据
	public static native AlarmHisData[] HNCAlarmGetHistoryData(int index, int clientNo);//注意这个函数需要改写
	//HNC_AlarmSaveHistory,保存报警历史,返回0：成功；非0：失败。
	public static native int HNCAlarmSaveHistory(int clientNo);
	//HNC_AlarmClrHistory,清除报警历史,返回0：成功；非0：失败。
	public static native int HNCAlarmClrHistory(int clientNo);
	
/**************************************************************************************/	
//来自hncreg.h，和寄存器相关
//	public class HncRegType
//	{
//		public static final int REG_TYPE_X = 0; // X寄存器 Bit8
//		public static final int REG_TYPE_Y = 1; // Y寄存器 Bit8
//		public static final int REG_TYPE_F = 2; // F寄存器 Bit16
//		public static final int REG_TYPE_G = 3; // G寄存器 Bit16
//		public static final int REG_TYPE_R = 4; // R寄存器 Bit8
//		public static final int REG_TYPE_W = 5; // W寄存器 Bit16
//		public static final int REG_TYPE_D = 6; // D寄存器 Bit32
//		public static final int REG_TYPE_B = 7; // B寄存器 Bit32
//		public static final int REG_TYPE_P = 8; // P寄存器 Bit32
//		public static final int REG_TYPE_TOTAL = 9; 
//	}//以下所有和寄存器相关的函数中的形参type都是指寄存器类型
	// FG寄存器基地址
//	public class HncRegFGBaseType
//	{
//		public static final int REG_FG_SYS_BASE = 10;// 系统数据基址 {Get(Bit32)}
//		public static final int REG_FG_CHAN_BASE= 11;// 通道数据基址 {Get(Bit32)}
//		public static final int REG_FG_AXIS_BASE= 12;// 轴数据基址 {Get(Bit32)}
//		public static final int REG_FG_BASE_TYPE_TOTAL= 13;
//	}
	//HNC_RegGetValue,获取寄存器的值;如果返回值是-1，则获取失败；否则返回值就是对应寄存器的值
	//type：寄存器类型；enum HncRegType. index：寄存器组号；
	public static native int HNCRegGetValue(int type, int index, int clientNo);
	//HNC_RegSetValue,设置寄存器的值;如果返回值是-1，则设置失败，否则返回值就是对应寄存器的值；
	public static native int HNCRegSetValue(int type, int index, int value, int clientNo);
	//HNC_RegSetBit,设置寄存器数据的一位,bit：要给32位寄存器设置值得十进制值；返回0：成功；非0：失败
	public static native int HNCRegSetBit(int type, int index, int bit, int clientNo);
	//HNC_RegClrBit,清除寄存器数据的一位.index：寄存器组号；bit：寄存器位号；返回0：成功；非0：失败
	public static native int HNCRegClrBit(int type, int index, int bit, int clientNo);
	//HNC_RegGetNum,获取寄存器的总组数,如果返回值是-1，则获取失败；否则返回值就是对应类型寄存器的总组数
	public static native int HNCRegGetNum(int type, int clientNo);
	//HNC_RegGetFGBase,获取FG寄存器的基址;如果返回值是-1，则获取失败；否则返回值就是基址
	public static native int HNCRegGetFGBase(int type, int clientNo);
/**************************************************************************************/
//来自hncvar.h
	/* 说    明：返回值说明：
	*           0：成功；
	*           1：异步获取变量，未获取到数据；
	*           -1：函数与变量类型不匹配；
	*           -2：轴号超过限制；
	*           -3：通道号超过限制；
	*           -4：轴变量索引号超过限制；
	*           -5：通道变量索引号超过限制；
	*           -6：系统变量索引号超过限制；
	*           -7：变量位号超过限制；
	*           -8：变量值指针为空；
	*/	
//	public class HncVarType
//	{
//		public static final int VAR_TYPE_AXIS = 0;	  // 轴变量 {Get(Bit32), Set(Bit32)}
//		public static final int VAR_TYPE_CHANNEL = 1; // 通道变量 {Get(Bit32), Set(Bit32)}
//		public static final int VAR_TYPE_SYSTEM = 2;  // 系统变量 {Get(Bit32), Set(Bit32)}
//		public static final int VAR_TYPE_SYSTEM_F = 3;// 浮点类型的系统变量 {Get(fBit64), Set(fBit64)}
//		public static final int VAR_TYPE_TOTAL = 4;
//	}
	//HNC_VarGetValue,获取变量的值;如果返回值是-1，则获取失败；否则返回值就是变量的值
	//type：变量类型；enum HncVarType;no：轴号或者通道号；index：索引号；
	//no：轴变量填轴号；通道变量填通道号；系统变量填0；
	public static native int HNCVarGetValueInt(int type, int No, int index, int clientNo);
	public static native double HNCVarGetValueDouble(int type, int No, int index, int clientNo);
	//HNC_VarSetValue,设置变量的值;如果返回值是-1，则设置失败；否则返回值就是变量的值
	public static native int HNCVarSetValueDouble(int type, int No, int index, double value, int clientNo);
	public static native int HNCVarSetValueInt(int type, int No, int index, int value, int clientNo);
/**************************************************************************************/
//来自hncaxis.h
	//HNC_AxisGetValue,获取轴数据的值.如果返回值是-1，则获取失败；否则返回值就是轴数据的值
	public static native double HNCAxisGetValueDouble(int type, int axis, int clientNo);
	public static native int HNCAxisGetValueInt(int type, int axis, int clientNo);
	//获取系统数据的值;如果返回值是null，则获取失败；否则返回值就是轴数据的值
	//axis的值0,axis-X;1,axis-Y;2,axis-Z;3,axis-AX;
	public static native String HNCAxisGetValueString(int type, int axis, int clientNo);
	
/**************************************************************************************/
//来自hncsys.h
	//HNC_SystemGetValue,获取系统数据的值;如果返回值是-1，则获取失败；否则返回值就是轴数据的值
	public static native int HNCSystemGetValueInt(int HncSystemType, int clientNo);
	//HNC_SystemGetValue,获取系统数据的值;如果返回值是null，则获取失败；否则返回值就是轴数据的值
	public static native String HNCSystemGetValueString(int HncSystemType, int clientNo);

/**************************************************************************************/
//来自hncchan.h
	//HNC_ChannelGetValue,获取轴数据的值.如果返回值是-1，则获取失败；否则返回值就是轴数据的值
	public static native double HNCChannelGetValueDouble(int HncChannelType, int channel, int index,int clientNo);
	public static native int HNCChannelGetValueInt(int HncChannelType, int channel,int index, int clientNo);
	//获取系统数据的值;如果返回值是null，则获取失败；否则返回值就是轴数据的值
//	public static native String HNCChannelGetValueString(int type, int channel,int index, int clientNo);
	
	
}

package com.cnc.huazhong;

public class HncSystem {
	
	public static final int HNC_SYS_CHAN_NUM = 0;		// 获取系统通道数 {Get(Bit32)}
	public static final int HNC_SYS_MOVE_UNIT = 1;		// 长度分辨率 {Get(Bit32)}
	public static final int HNC_SYS_TURN_UNIT = 2;		// 角度分辨率 {Get(Bit32)}
	public static final int HNC_SYS_METRIC_DISP = 3;	// 公英制 {Get(Bit32)}
	public static final int HNC_SYS_SHOW_TIME = 4;		// 显示时间 {Get(Bit32)}
	public static final int HNC_SYS_POP_ALARM = 5;		// 报警自动显示 {Get(Bit32)}
	public static final int HNC_SYS_GRAPH_ERASE = 6;	// 图形自动擦除 {Get(Bit32)}
	public static final int HNC_SYS_MAC_TYPE = 7;		// 机床类型
	public static final int HNC_SYS_PREC = 8;			// 坐标系精度 {Get(Bit32)}
	public static final int HNC_SYS_F_PREC = 9;			// F精度 {Get(Bit32)}
	public static final int HNC_SYS_S_PREC = 10;		// S精度 {Get(Bit32)}
	public static final int HNC_SYS_NCK_VER = 11; 		// NCK版本 {Get(Bit8[32])}
	public static final int HNC_SYS_DRV_VER = 12; 		// DRV版本 {Get(Bit8[32])}
	public static final int HNC_SYS_PLC_VER = 13;		// PLC版本 {Get(Bit8[32])}
	public static final int HNC_SYS_CNC_VER = 14;		// CNC版本 {Get(Bit32) Set(Bit32)}
	public static final int HNC_SYS_MCP_KEY = 15;		// MCP面板钥匙开关 {Get(Bit32)}
	public static final int HNC_SYS_ACTIVE_CHAN = 16;	// 活动通道 {Get(Bit32) Set(Bit32)}
	public static final int HNC_SYS_REQUEST_CHAN = 17;	// 请求通道 {Get(Bit32)}
	public static final int HNC_SYS_MDI_CHAN = 18;		// MDI运行通道 {Get(Bit32)}
	public static final int HNC_SYS_REQUEST_CHAN_MASK=19;// 请求的通道屏蔽字 {Get(Bit32)}
	public static final int HNC_SYS_CHAN_MASK = 20;		// 通道屏蔽字 {Set(Bit32)}
	public static final int HNC_SYS_PLC_STOP = 21;		// plc停止 {Set(NULL)}
	public static final int HNC_SYS_POWEROFF_ACT = 22;	// 断电应答 {Set(NULL)}
	public static final int HNC_SYS_IS_HOLD_REDECODE=23;// 进给保持后是否重新解释 {Get(Bit32)}
	public static final int HNC_SYS_NC_VER = 24;        // NC版本 {Get(Bit8[32])}
	public static final int HNC_SYS_SN_NUM = 25;        // CF卡SN号 {Get(Bit8[32]) Set(Bit8[32])}
	public static final int HNC_SYS_MACHINE_TYPE = 26;	//机床型号 {Get(Bit8[48]) Set(Bit8[48])}
	public static final int HNC_SYS_MACHINE_INFO = 27;	//机床信息 {Get(Bit8[48]) Set(Bit8[48])}
	public static final int HNC_SYS_MACFAC_INFO = 28;	//机床厂信息 {Get(Bit8[48]) Set(Bit8[48])}
	public static final int HNC_SYS_USER_INFO = 29;		//用户信息 {Get(Bit8[48]) Set(Bit8[48])}
	public static final int HNC_SYS_MACHINE_NUM = 30;	//机床编号 {Get(Bit8[48]) Set(Bit8[48])}
	public static final int HNC_SYS_EXFACTORY_DATE = 31;//出厂时间 {Get(Bit8[32])}
	public static final int HNC_SYS_ACCESS_LEVEL = 32;	//权限等级 {Get(Bit32)}
	public static final int HNC_SYS_TOTAL = 33;


}

package com.cnc.huazhong;
//轴数据类型
//Get(Bit32)表示Get数据时void *为Bit32 *
public class HncAxis {
	
	public static final int HNC_AXIS_NAME = 0;		// 轴名 {Get(Bit8[PARAM_STR_LEN])}
	public static final int HNC_AXIS_TYPE = 1;		// 轴类型 {Get(Bit32)}
	public static final int HNC_AXIS_CHAN = 2;		// 获取通道号 {Get(Bit32)}
	public static final int HNC_AXIS_CHAN_INDEX =3;	// 获取在通道中的轴号 {Get(Bit32)}
	public static final int HNC_AXIS_CHAN_SINDEX=4;	// 获取在通道中的主轴号 {Get(Bit32)}
	public static final int HNC_AXIS_LEAD = 5;		// 获取引导轴 {Get(Bit32)}
	public static final int HNC_AXIS_ACT_POS = 6;	// 机床实际位置 {Get(Bit32)}
	public static final int HNC_AXIS_ACT_POS2 = 7;	// 机床实际位置2 {Get(Bit32)}
	public static final int HNC_AXIS_CMD_POS = 8;	// 机床指令位置 {Get(Bit32)}
	public static final int HNC_AXIS_ACT_POS_WCS=9;	// 工件实际位置 {Get(Bit32)}
	public static final int HNC_AXIS_CMD_POS_WCS=10;// 工件指令位置 {Get(Bit32)}
	public static final int HNC_AXIS_ACT_POS_RCS=11;// 相对实际位置 {Get(Bit32)}
	public static final int HNC_AXIS_CMD_POS_RCS=12;// 相对指令位置 {Get(Bit32)}
	public static final int HNC_AXIS_ACT_PULSE = 13;// 实际脉冲位置 {Get(Bit32)}
	public static final int HNC_AXIS_CMD_PULSE = 14;// 指令脉冲位置 {Get(Bit32)}
	public static final int HNC_AXIS_PROG_POS = 15;	// 编程位置 {Get(Bit32)}
	public static final int HNC_AXIS_ENC_CNTR = 16;	// 电机位置 {Get(Bit32)}
	public static final int HNC_AXIS_CMD_VEL = 17;	// 指令速度 {Get(Bit32)}
	public static final int HNC_AXIS_ACT_VEL = 18;	// 实际速度 {Get(fBit64)}
	public static final int HNC_AXIS_LEFT_TOGO = 19;// 剩余进给 {Get(Bit32)}
	public static final int HNC_AXIS_WCS_ZERO = 20;	// 工件零点 {Get(Bit32)}
	public static final int HNC_AXIS_WHEEl_OFF = 21;// 手轮中断偏移量 {Get(Bit32)}
	public static final int HNC_AXIS_FOLLOW_ERR =22;// 跟踪误差 {Get(Bit32)}
	public static final int HNC_AXIS_SYN_ERR = 23;	// 同步误差	{Get(Bit32)}
	public static final int HNC_AXIS_COMP = 24;		// 轴补偿值 {Get(Bit32)}
	public static final int HNC_AXIS_ZSW_DIST = 25;	// Z脉冲偏移 {Get(Bit32)}
	public static final int HNC_AXIS_REAL_ZERO = 26;// 相对零点 {Get(Bit32)}
	public static final int HNC_AXIS_MOTOR_REV = 27;// 电机转速 {Get(fBit64)}
	public static final int HNC_AXIS_DRIVE_CUR = 28;// 驱动单元电流 {Get(fBit64)}
	public static final int HNC_AXIS_LOAD_CUR = 29;	// 负载电流 {Get(fBit64)}
	public static final int HNC_AXIS_RATED_CUR = 30;// 额定电流 {Get(fBit64)}
	public static final int HNC_AXIS_IS_HOMEF = 31;	// 回零完成 {Get(Bit32)}
	public static final int HNC_AXIS_WAVE_FREQ = 32;// 波形频率 {Get(fBit64)}
	public static final int HNC_AXIS_DRIVE_VER = 33;// 伺服驱动版本 {Get(fBit64)}
	public static final int HNC_AXIS_MOTOR_TYPE =34;// 伺服类型 {Get(Bit32)}
	public static final int HNC_AXIS_MOTOR_TYPE_FLAG=35;// 伺服类型出错标志 {Get(Bit32)}
	public static final int HNC_AXIS_TOTAL = 36;
	
	
}









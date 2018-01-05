package com.cnc.huazhong;

public class HncChannel {
	
	
	public static final int HNC_CHAN_IS_EXIST = 0;		// 通道是否存在 {Get(Bit32)}
	public static final int HNC_CHAN_MAC_TYPE = 1;		// 通道的机床类型 {Get(Bit32)}
	public static final int HNC_CHAN_AXES_MASK = 2;		// 轴掩码 {Get(Bit32)}
	public static final int HNC_CHAN_AXES_MASK1 = 3;	// 轴掩码1 {Get(Bit32)}
	public static final int HNC_CHAN_NAME = 4;			// 通道名 {Get(Bit8[PARAM_STR_LEN])}
	public static final int HNC_CHAN_CMD_TYPE = 5;		// 读取当前G代码的标志 {Get(Bit32)}
	public static final int HNC_CHAN_CMD_FEEDRATE = 6;	// 指令进给速度 {Get(fBit64)}
	public static final int HNC_CHAN_ACT_FEEDRATE = 7;	// 实际进给速度 {Get(fBit64)}
	public static final int HNC_CHAN_PROG_FEEDRATE = 8;	// 编程指令速度 {Get(fBit64)}
	public static final int HNC_CHAN_FEED_OVERRIDE = 9;	// 进给修调 {Get(Bit32)}
	public static final int HNC_CHAN_RAPID_OVERRIDE =10;// 快移修调 {Get(Bit32)}
	public static final int HNC_CHAN_MCODE = 11;        // 通道的M指令 {Get(Bit32)}
	public static final int HNC_CHAN_TCODE = 12;		// 通道的T指令 {Get(Bit32)}
	public static final int HNC_CHAN_TOFFS = 13;		// 通道中的刀偏号 {Get(Bit32)}
	public static final int HNC_CHAN_TOOL_USE = 14;		// 当前刀具 {Get(Bit32)}
	public static final int HNC_CHAN_TOOL_RDY = 15;		// 准备好交换的刀具 {Get(Bit32)}
	public static final int HNC_CHAN_MODE = 16;			// 模式(返回值数据定义见下面) {Get(Bit32)}
	public static final int HNC_CHAN_IS_MDI = 17;		// MDI {Get(Bit32)}
	public static final int HNC_CHAN_CYCLE = 18;		// 循环启动 {Get(Bit32), Set(NULL)}
	public static final int HNC_CHAN_HOLD = 19;			// 进给保持 {Get(Bit32), Set(NULL)}
	public static final int HNC_CHAN_IS_PROGSEL = 20;	// 已选程序 {Get(Bit32)}
	public static final int HNC_CHAN_IS_PROGEND = 21;	// 程序运行完成 {Get(Bit32)}
	public static final int HNC_CHAN_IS_THREADING = 22;	// 螺纹加工 {Get(Bit32)}
	public static final int HNC_CHAN_IS_RIGID = 23;		// 刚性攻丝 {Get(Bit32)}
	public static final int HNC_CHAN_IS_REWINDED = 24;	// 重运行复位状态 {Get(Bit32)}
	public static final int HNC_CHAN_IS_ESTOP = 25;		// 急停 {Get(Bit32)}
	public static final int HNC_CHAN_IS_RESETTING = 26;	// 复位 {Get(Bit32)}
	public static final int HNC_CHAN_IS_RUNNING = 27;	// 运行中 {Get(Bit32)}
	public static final int HNC_CHAN_IS_HOMING = 28;	// 回零中 {Get(Bit32)}
	public static final int HNC_CHAN_IS_MOVING = 29;	// 轴移动中 {Get(Bit32)}
	public static final int HNC_CHAN_DIAMETER = 30;		// 直半径编程 {Get(Bit32)}
	public static final int HNC_CHAN_VERIFY = 31;		// 校验 {Get(Bit32), Set(Bit32)}
	public static final int HNC_CHAN_RUN_ROW = 32;		// 运行行 {Get(Bit32)}
	public static final int HNC_CHAN_DCD_ROW = 33;		// 译码行 {Get(Bit32)}
	public static final int HNC_CHAN_SEL_PROG = 34;		// 选择程序的编号 {Get(Bit32)}
	public static final int HNC_CHAN_RUN_PROG = 35;		// 运行程序的编号 {Get(Bit32)}
	public static final int HNC_CHAN_PART_CNTR = 36;	// 加工计数 {Get(Bit32), Set(Bit32)}
	public static final int HNC_CHAN_PART_STATI = 37;	// 工件总数 {Get(Bit32), Set(Bit32)}
	public static final int HNC_CHAN_HMI_RESET = 38;	// HMI复位 {Set(NULL)}
	public static final int HNC_CHAN_CHG_PROG = 39;		// 程序修改标志 {Set(NULL)}

	public static final int HNC_CHAN_PERIOD_TOTAL = 40;	// 【周期数据结束】，以下数据不作为周期数据上传

	public static final int HNC_CHAN_LAX = 41;			// 通道轴对应的逻辑轴号 {Get(Bit32)}
	public static final int HNC_CHAN_AXIS_NAME = 42;	// 编程轴名 {Get(Bit8[PARAM_STR_LEN])}
	public static final int HNC_CHAN_SPDL_NAME = 43;	// 编程主轴名 {Get(Bit8[PARAM_STR_LEN])}
	public static final int HNC_CHAN_MODAL = 44;		// 通道模态 共80组 {Get(Bit32)}
	public static final int HNC_CHAN_SPDL_LAX = 45;		// 通道主轴对应的逻辑轴号，动态 {Get(Bit32)}
	public static final int HNC_CHAN_SPDL_PARA_LAX = 46;// 通道主轴对应的逻辑轴号，静态 {Get(Bit32)}
	public static final int HNC_CHAN_CMD_SPDL_SPEED =47;// 主轴指令速度 {Get(fBit64)}
	public static final int HNC_CHAN_ACT_SPDL_SPEED =48;// 主轴实际速度 {Get(fBit64)}
	public static final int HNC_CHAN_SPDL_OVERRIDE = 49;// 主轴修调 {Get(Bit32)}
	public static final int HNC_CHAN_DO_HOLD = 50;		// 设置进给保持 
	public static final int HNC_CHAN_BP_POS = 51;		// 断点位置 {Get(Bit32)}
	public static final int HNC_CHAN_TOTAL = 52;


}

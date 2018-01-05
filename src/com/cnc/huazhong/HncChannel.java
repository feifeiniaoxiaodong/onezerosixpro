package com.cnc.huazhong;

public class HncChannel {
	
	
	public static final int HNC_CHAN_IS_EXIST = 0;		// ͨ���Ƿ���� {Get(Bit32)}
	public static final int HNC_CHAN_MAC_TYPE = 1;		// ͨ���Ļ������� {Get(Bit32)}
	public static final int HNC_CHAN_AXES_MASK = 2;		// ������ {Get(Bit32)}
	public static final int HNC_CHAN_AXES_MASK1 = 3;	// ������1 {Get(Bit32)}
	public static final int HNC_CHAN_NAME = 4;			// ͨ���� {Get(Bit8[PARAM_STR_LEN])}
	public static final int HNC_CHAN_CMD_TYPE = 5;		// ��ȡ��ǰG����ı�־ {Get(Bit32)}
	public static final int HNC_CHAN_CMD_FEEDRATE = 6;	// ָ������ٶ� {Get(fBit64)}
	public static final int HNC_CHAN_ACT_FEEDRATE = 7;	// ʵ�ʽ����ٶ� {Get(fBit64)}
	public static final int HNC_CHAN_PROG_FEEDRATE = 8;	// ���ָ���ٶ� {Get(fBit64)}
	public static final int HNC_CHAN_FEED_OVERRIDE = 9;	// �����޵� {Get(Bit32)}
	public static final int HNC_CHAN_RAPID_OVERRIDE =10;// �����޵� {Get(Bit32)}
	public static final int HNC_CHAN_MCODE = 11;        // ͨ����Mָ�� {Get(Bit32)}
	public static final int HNC_CHAN_TCODE = 12;		// ͨ����Tָ�� {Get(Bit32)}
	public static final int HNC_CHAN_TOFFS = 13;		// ͨ���еĵ�ƫ�� {Get(Bit32)}
	public static final int HNC_CHAN_TOOL_USE = 14;		// ��ǰ���� {Get(Bit32)}
	public static final int HNC_CHAN_TOOL_RDY = 15;		// ׼���ý����ĵ��� {Get(Bit32)}
	public static final int HNC_CHAN_MODE = 16;			// ģʽ(����ֵ���ݶ��������) {Get(Bit32)}
	public static final int HNC_CHAN_IS_MDI = 17;		// MDI {Get(Bit32)}
	public static final int HNC_CHAN_CYCLE = 18;		// ѭ������ {Get(Bit32), Set(NULL)}
	public static final int HNC_CHAN_HOLD = 19;			// �������� {Get(Bit32), Set(NULL)}
	public static final int HNC_CHAN_IS_PROGSEL = 20;	// ��ѡ���� {Get(Bit32)}
	public static final int HNC_CHAN_IS_PROGEND = 21;	// ����������� {Get(Bit32)}
	public static final int HNC_CHAN_IS_THREADING = 22;	// ���Ƽӹ� {Get(Bit32)}
	public static final int HNC_CHAN_IS_RIGID = 23;		// ���Թ�˿ {Get(Bit32)}
	public static final int HNC_CHAN_IS_REWINDED = 24;	// �����и�λ״̬ {Get(Bit32)}
	public static final int HNC_CHAN_IS_ESTOP = 25;		// ��ͣ {Get(Bit32)}
	public static final int HNC_CHAN_IS_RESETTING = 26;	// ��λ {Get(Bit32)}
	public static final int HNC_CHAN_IS_RUNNING = 27;	// ������ {Get(Bit32)}
	public static final int HNC_CHAN_IS_HOMING = 28;	// ������ {Get(Bit32)}
	public static final int HNC_CHAN_IS_MOVING = 29;	// ���ƶ��� {Get(Bit32)}
	public static final int HNC_CHAN_DIAMETER = 30;		// ֱ�뾶��� {Get(Bit32)}
	public static final int HNC_CHAN_VERIFY = 31;		// У�� {Get(Bit32), Set(Bit32)}
	public static final int HNC_CHAN_RUN_ROW = 32;		// ������ {Get(Bit32)}
	public static final int HNC_CHAN_DCD_ROW = 33;		// ������ {Get(Bit32)}
	public static final int HNC_CHAN_SEL_PROG = 34;		// ѡ�����ı�� {Get(Bit32)}
	public static final int HNC_CHAN_RUN_PROG = 35;		// ���г���ı�� {Get(Bit32)}
	public static final int HNC_CHAN_PART_CNTR = 36;	// �ӹ����� {Get(Bit32), Set(Bit32)}
	public static final int HNC_CHAN_PART_STATI = 37;	// �������� {Get(Bit32), Set(Bit32)}
	public static final int HNC_CHAN_HMI_RESET = 38;	// HMI��λ {Set(NULL)}
	public static final int HNC_CHAN_CHG_PROG = 39;		// �����޸ı�־ {Set(NULL)}

	public static final int HNC_CHAN_PERIOD_TOTAL = 40;	// ���������ݽ��������������ݲ���Ϊ���������ϴ�

	public static final int HNC_CHAN_LAX = 41;			// ͨ�����Ӧ���߼���� {Get(Bit32)}
	public static final int HNC_CHAN_AXIS_NAME = 42;	// ������� {Get(Bit8[PARAM_STR_LEN])}
	public static final int HNC_CHAN_SPDL_NAME = 43;	// ��������� {Get(Bit8[PARAM_STR_LEN])}
	public static final int HNC_CHAN_MODAL = 44;		// ͨ��ģ̬ ��80�� {Get(Bit32)}
	public static final int HNC_CHAN_SPDL_LAX = 45;		// ͨ�������Ӧ���߼���ţ���̬ {Get(Bit32)}
	public static final int HNC_CHAN_SPDL_PARA_LAX = 46;// ͨ�������Ӧ���߼���ţ���̬ {Get(Bit32)}
	public static final int HNC_CHAN_CMD_SPDL_SPEED =47;// ����ָ���ٶ� {Get(fBit64)}
	public static final int HNC_CHAN_ACT_SPDL_SPEED =48;// ����ʵ���ٶ� {Get(fBit64)}
	public static final int HNC_CHAN_SPDL_OVERRIDE = 49;// �����޵� {Get(Bit32)}
	public static final int HNC_CHAN_DO_HOLD = 50;		// ���ý������� 
	public static final int HNC_CHAN_BP_POS = 51;		// �ϵ�λ�� {Get(Bit32)}
	public static final int HNC_CHAN_TOTAL = 52;


}

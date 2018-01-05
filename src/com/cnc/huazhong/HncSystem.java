package com.cnc.huazhong;

public class HncSystem {
	
	public static final int HNC_SYS_CHAN_NUM = 0;		// ��ȡϵͳͨ���� {Get(Bit32)}
	public static final int HNC_SYS_MOVE_UNIT = 1;		// ���ȷֱ��� {Get(Bit32)}
	public static final int HNC_SYS_TURN_UNIT = 2;		// �Ƕȷֱ��� {Get(Bit32)}
	public static final int HNC_SYS_METRIC_DISP = 3;	// ��Ӣ�� {Get(Bit32)}
	public static final int HNC_SYS_SHOW_TIME = 4;		// ��ʾʱ�� {Get(Bit32)}
	public static final int HNC_SYS_POP_ALARM = 5;		// �����Զ���ʾ {Get(Bit32)}
	public static final int HNC_SYS_GRAPH_ERASE = 6;	// ͼ���Զ����� {Get(Bit32)}
	public static final int HNC_SYS_MAC_TYPE = 7;		// ��������
	public static final int HNC_SYS_PREC = 8;			// ����ϵ���� {Get(Bit32)}
	public static final int HNC_SYS_F_PREC = 9;			// F���� {Get(Bit32)}
	public static final int HNC_SYS_S_PREC = 10;		// S���� {Get(Bit32)}
	public static final int HNC_SYS_NCK_VER = 11; 		// NCK�汾 {Get(Bit8[32])}
	public static final int HNC_SYS_DRV_VER = 12; 		// DRV�汾 {Get(Bit8[32])}
	public static final int HNC_SYS_PLC_VER = 13;		// PLC�汾 {Get(Bit8[32])}
	public static final int HNC_SYS_CNC_VER = 14;		// CNC�汾 {Get(Bit32) Set(Bit32)}
	public static final int HNC_SYS_MCP_KEY = 15;		// MCP���Կ�׿��� {Get(Bit32)}
	public static final int HNC_SYS_ACTIVE_CHAN = 16;	// �ͨ�� {Get(Bit32) Set(Bit32)}
	public static final int HNC_SYS_REQUEST_CHAN = 17;	// ����ͨ�� {Get(Bit32)}
	public static final int HNC_SYS_MDI_CHAN = 18;		// MDI����ͨ�� {Get(Bit32)}
	public static final int HNC_SYS_REQUEST_CHAN_MASK=19;// �����ͨ�������� {Get(Bit32)}
	public static final int HNC_SYS_CHAN_MASK = 20;		// ͨ�������� {Set(Bit32)}
	public static final int HNC_SYS_PLC_STOP = 21;		// plcֹͣ {Set(NULL)}
	public static final int HNC_SYS_POWEROFF_ACT = 22;	// �ϵ�Ӧ�� {Set(NULL)}
	public static final int HNC_SYS_IS_HOLD_REDECODE=23;// �������ֺ��Ƿ����½��� {Get(Bit32)}
	public static final int HNC_SYS_NC_VER = 24;        // NC�汾 {Get(Bit8[32])}
	public static final int HNC_SYS_SN_NUM = 25;        // CF��SN�� {Get(Bit8[32]) Set(Bit8[32])}
	public static final int HNC_SYS_MACHINE_TYPE = 26;	//�����ͺ� {Get(Bit8[48]) Set(Bit8[48])}
	public static final int HNC_SYS_MACHINE_INFO = 27;	//������Ϣ {Get(Bit8[48]) Set(Bit8[48])}
	public static final int HNC_SYS_MACFAC_INFO = 28;	//��������Ϣ {Get(Bit8[48]) Set(Bit8[48])}
	public static final int HNC_SYS_USER_INFO = 29;		//�û���Ϣ {Get(Bit8[48]) Set(Bit8[48])}
	public static final int HNC_SYS_MACHINE_NUM = 30;	//������� {Get(Bit8[48]) Set(Bit8[48])}
	public static final int HNC_SYS_EXFACTORY_DATE = 31;//����ʱ�� {Get(Bit8[32])}
	public static final int HNC_SYS_ACCESS_LEVEL = 32;	//Ȩ�޵ȼ� {Get(Bit32)}
	public static final int HNC_SYS_TOTAL = 33;


}

package com.cnc.huazhong;

public class HncAPI {

	
	/*static {  
        // ���ض�̬��  
        System.loadLibrary("hncnet");  
        System.loadLibrary("hzHncAPI");  
        
    }*/
/**************************************************************************************/
/*����hnecnet.h*/

 //�������������
	/**
	 * HNC_NetInit,�����ʼ��
	 * @param ip ��λ��IP
	 * @param port ��λ���˿ں�
	 * @return ����ֵΪ�����ʾ��ʼ���ɹ�
	 */
	public static native int HNCNetInit(String ip, int port);
	//HNC_NetConnect�����ӻ���,����ֵΪ�����ʾ���ӳɹ�
	public static native int HNCNetConnect(String ip, int port);
	//HNC_NetIsConnect������Ŀ������Ƿ������ӣ�����ֵΪ�����ʾ������
	public static native int HNCNetIsConnect(int clientNo);
	//HNC_NetExit���˳�����
	public static native void HNCNetExit();
	//HNC_NetIsThreadStartup,��ȡ�����߳̿���״̬������0�������߳�δ������1�������߳��ѿ���
	public static native int HNCNetIsThreadStartup();
	//HNC_NetSetIpaddr������Ŀ�����(����)IP�Ͷ˿ںţ�����0�����óɹ�
	public static native int HNCNetSetIpaddr(String ip, int port, int clientNo);
	
 //���ļ��������
	//HNC_NetFileSend,��Ŀ�����(����)�����ļ�������0�����ͳɹ�
	public static native int HNCNetFileSend(String localName, String dstName, int clientNo);
	//HNC_NetFileGet,��ȡĿ������ļ�������0����ȡ�ɹ�
	public static native int HNCNetFileGet(String localNamme, String dstName, int clientNo);
	//HNC_NetFileCheck,�Ƚ��ϡ���λ���ļ��Ƿ�һ�£�����0���ɹ�
	public static native int HNCNetFileCheck(String localNamme, String dstName, int clientNo);
	//HNC_NetFileRemove,ɾ����λ�����������е��ļ�������0���ɹ�
	public static native int HNCNetFileRemove(String dstName, int clientNo);
	
/**************************************************************************************/
/*����hncalarm.h
	* ժ    Ҫ��������������ͳһ��ź͹���
	*           1) �������͹���9�֣��ֱ���ϵͳ��ͨ�����ᡢ�ŷ���PLC���豸���﷨���û�PLC��HMI��
	*           2) �������𹲼�2�����ֱ��Ǳ�������ʾ��
	*           3) �����Ź���9λ��
	*              ͨ�����﷨��(1λ��������)+(1λ��������)+(3λͨ����)+(4λ�������ݱ��)��
	*              �ᡢ�ŷ�  ��(1λ��������)+(1λ��������)+(3λ���)  +(4λ�������ݱ��)��
	*              ����      ��(1λ��������)+(1λ��������)+(7λ�������ݱ��)��*/
 //�ͱ������
//	������������
//	public class AlarmType
//	{
//		public static final int ALARM_SY = 0;		//ֵ 0ϵͳ������System��
//		public static final int ALARM_CH = 1;		//	ͨ��������Channel��
//		public static final int ALARM_AX = 2;		//	�ᱨ����Axis��
//		public static final int ALARM_SV = 3;		//	�ŷ�������Servo��
//		public static final int ALARM_PC = 4;		//	PLC������PLC��
//		public static final int ALARM_DV = 5;		//	�豸������Dev��
//		public static final int ALARM_PS = 6;		//	�﷨������Program Syntax��
//		public static final int ALARM_UP = 7;		//	�û�PLC������User PLC��
//		public static final int ALARM_HM = 8;		//	HMI������HMI��
//		public static final int ALARM_TYPE_ALL = 9; 
//	}
	//	��������
//	public class AlarmLevel
//	{
//		public static final int ALARM_ERR = 0;	//����Error��
//		public static final int ALARM_MSG = 1;	//��ʾ��Message��	
//		public static final int ALARM_LEVEL_ALL = 3;
//	}
	//HNC_AlarmRefresh,������Ϣˢ��,����0���ɹ�����0��ʧ�ܡ�
	public static native int HNCAlarmRefresh(int clientNo);
	//HNC_AlarmGetNum,��ȡ��ǰ��������Ŀ���������ֵ-1��С���㣬���ȡʧ�ܣ������ȡ��������
	public static native int HNCAlarmGetNum(int type, int level, int clientNo);//ע�����������Ҫ��д
	//HNC_AlarmGetHistoryNum,��ȡ��ʷ���������������ֵ-1��С���㣬���ȡʧ�ܣ������ȡ����ʷ������
	public static native int HNCAlarmGetHistoryNum(int clientNo);//ע�����������Ҫ��д
	//HNC_AlarmGetData,��ȡ��ǰ���������ݣ��������null�����ȡʧ�ܣ������ȡ�����ı�
	public static native String HNCAlarmGetDataDefault(int type, int level,int index);//���أ�clientNoĬ��Ϊ0
	//����ֵ��ʽ��alarmNo::alarmText,Ĭ����ǰ����ӱ�����ð��֮���Ǳ����ı�
	public static native String HNCAlarmGetData(int type, int level,int index, int clientNo);//ע�����������Ҫ��д

	//HNC_AlarmGetHistoryData����ȡ��ȡ��index��ʼ�����50��������ʷ����
	public static native AlarmHisData[] HNCAlarmGetHistoryData(int index, int clientNo);//ע�����������Ҫ��д
	//HNC_AlarmSaveHistory,���汨����ʷ,����0���ɹ�����0��ʧ�ܡ�
	public static native int HNCAlarmSaveHistory(int clientNo);
	//HNC_AlarmClrHistory,���������ʷ,����0���ɹ�����0��ʧ�ܡ�
	public static native int HNCAlarmClrHistory(int clientNo);
	
/**************************************************************************************/	
//����hncreg.h���ͼĴ������
//	public class HncRegType
//	{
//		public static final int REG_TYPE_X = 0; // X�Ĵ��� Bit8
//		public static final int REG_TYPE_Y = 1; // Y�Ĵ��� Bit8
//		public static final int REG_TYPE_F = 2; // F�Ĵ��� Bit16
//		public static final int REG_TYPE_G = 3; // G�Ĵ��� Bit16
//		public static final int REG_TYPE_R = 4; // R�Ĵ��� Bit8
//		public static final int REG_TYPE_W = 5; // W�Ĵ��� Bit16
//		public static final int REG_TYPE_D = 6; // D�Ĵ��� Bit32
//		public static final int REG_TYPE_B = 7; // B�Ĵ��� Bit32
//		public static final int REG_TYPE_P = 8; // P�Ĵ��� Bit32
//		public static final int REG_TYPE_TOTAL = 9; 
//	}//�������кͼĴ�����صĺ����е��β�type����ָ�Ĵ�������
	// FG�Ĵ�������ַ
//	public class HncRegFGBaseType
//	{
//		public static final int REG_FG_SYS_BASE = 10;// ϵͳ���ݻ�ַ {Get(Bit32)}
//		public static final int REG_FG_CHAN_BASE= 11;// ͨ�����ݻ�ַ {Get(Bit32)}
//		public static final int REG_FG_AXIS_BASE= 12;// �����ݻ�ַ {Get(Bit32)}
//		public static final int REG_FG_BASE_TYPE_TOTAL= 13;
//	}
	//HNC_RegGetValue,��ȡ�Ĵ�����ֵ;�������ֵ��-1�����ȡʧ�ܣ����򷵻�ֵ���Ƕ�Ӧ�Ĵ�����ֵ
	//type���Ĵ������ͣ�enum HncRegType. index���Ĵ�����ţ�
	public static native int HNCRegGetValue(int type, int index, int clientNo);
	//HNC_RegSetValue,���üĴ�����ֵ;�������ֵ��-1��������ʧ�ܣ����򷵻�ֵ���Ƕ�Ӧ�Ĵ�����ֵ��
	public static native int HNCRegSetValue(int type, int index, int value, int clientNo);
	//HNC_RegSetBit,���üĴ������ݵ�һλ,bit��Ҫ��32λ�Ĵ�������ֵ��ʮ����ֵ������0���ɹ�����0��ʧ��
	public static native int HNCRegSetBit(int type, int index, int bit, int clientNo);
	//HNC_RegClrBit,����Ĵ������ݵ�һλ.index���Ĵ�����ţ�bit���Ĵ���λ�ţ�����0���ɹ�����0��ʧ��
	public static native int HNCRegClrBit(int type, int index, int bit, int clientNo);
	//HNC_RegGetNum,��ȡ�Ĵ�����������,�������ֵ��-1�����ȡʧ�ܣ����򷵻�ֵ���Ƕ�Ӧ���ͼĴ�����������
	public static native int HNCRegGetNum(int type, int clientNo);
	//HNC_RegGetFGBase,��ȡFG�Ĵ����Ļ�ַ;�������ֵ��-1�����ȡʧ�ܣ����򷵻�ֵ���ǻ�ַ
	public static native int HNCRegGetFGBase(int type, int clientNo);
/**************************************************************************************/
//����hncvar.h
	/* ˵    ��������ֵ˵����
	*           0���ɹ���
	*           1���첽��ȡ������δ��ȡ�����ݣ�
	*           -1��������������Ͳ�ƥ�䣻
	*           -2����ų������ƣ�
	*           -3��ͨ���ų������ƣ�
	*           -4������������ų������ƣ�
	*           -5��ͨ�����������ų������ƣ�
	*           -6��ϵͳ���������ų������ƣ�
	*           -7������λ�ų������ƣ�
	*           -8������ֵָ��Ϊ�գ�
	*/	
//	public class HncVarType
//	{
//		public static final int VAR_TYPE_AXIS = 0;	  // ����� {Get(Bit32), Set(Bit32)}
//		public static final int VAR_TYPE_CHANNEL = 1; // ͨ������ {Get(Bit32), Set(Bit32)}
//		public static final int VAR_TYPE_SYSTEM = 2;  // ϵͳ���� {Get(Bit32), Set(Bit32)}
//		public static final int VAR_TYPE_SYSTEM_F = 3;// �������͵�ϵͳ���� {Get(fBit64), Set(fBit64)}
//		public static final int VAR_TYPE_TOTAL = 4;
//	}
	//HNC_VarGetValue,��ȡ������ֵ;�������ֵ��-1�����ȡʧ�ܣ����򷵻�ֵ���Ǳ�����ֵ
	//type���������ͣ�enum HncVarType;no����Ż���ͨ���ţ�index�������ţ�
	//no�����������ţ�ͨ��������ͨ���ţ�ϵͳ������0��
	public static native int HNCVarGetValueInt(int type, int No, int index, int clientNo);
	public static native double HNCVarGetValueDouble(int type, int No, int index, int clientNo);
	//HNC_VarSetValue,���ñ�����ֵ;�������ֵ��-1��������ʧ�ܣ����򷵻�ֵ���Ǳ�����ֵ
	public static native int HNCVarSetValueDouble(int type, int No, int index, double value, int clientNo);
	public static native int HNCVarSetValueInt(int type, int No, int index, int value, int clientNo);
/**************************************************************************************/
//����hncaxis.h
	//HNC_AxisGetValue,��ȡ�����ݵ�ֵ.�������ֵ��-1�����ȡʧ�ܣ����򷵻�ֵ���������ݵ�ֵ
	public static native double HNCAxisGetValueDouble(int type, int axis, int clientNo);
	public static native int HNCAxisGetValueInt(int type, int axis, int clientNo);
	//��ȡϵͳ���ݵ�ֵ;�������ֵ��null�����ȡʧ�ܣ����򷵻�ֵ���������ݵ�ֵ
	//axis��ֵ0,axis-X;1,axis-Y;2,axis-Z;3,axis-AX;
	public static native String HNCAxisGetValueString(int type, int axis, int clientNo);
	
/**************************************************************************************/
//����hncsys.h
	//HNC_SystemGetValue,��ȡϵͳ���ݵ�ֵ;�������ֵ��-1�����ȡʧ�ܣ����򷵻�ֵ���������ݵ�ֵ
	public static native int HNCSystemGetValueInt(int HncSystemType, int clientNo);
	//HNC_SystemGetValue,��ȡϵͳ���ݵ�ֵ;�������ֵ��null�����ȡʧ�ܣ����򷵻�ֵ���������ݵ�ֵ
	public static native String HNCSystemGetValueString(int HncSystemType, int clientNo);

/**************************************************************************************/
//����hncchan.h
	//HNC_ChannelGetValue,��ȡ�����ݵ�ֵ.�������ֵ��-1�����ȡʧ�ܣ����򷵻�ֵ���������ݵ�ֵ
	public static native double HNCChannelGetValueDouble(int HncChannelType, int channel, int index,int clientNo);
	public static native int HNCChannelGetValueInt(int HncChannelType, int channel,int index, int clientNo);
	//��ȡϵͳ���ݵ�ֵ;�������ֵ��null�����ȡʧ�ܣ����򷵻�ֵ���������ݵ�ֵ
//	public static native String HNCChannelGetValueString(int type, int channel,int index, int clientNo);
	
	
}

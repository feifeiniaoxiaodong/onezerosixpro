package com.cnc.gaojing.ndkapi;
/**
 * �����߾���ȡ��Ϣ������
 * @author wei
 *
 */
public class GJApiNum {
//	public static final String  CNC��ID  ="192.168.188.132";  //����ϵͳ�ɣ�ʹ�ãɣе�ַ��ʾ
	//version  information
	public static final int  SYSTEM_MODEL=2541 ; //ϵͳ�ͺ�,string
	public static final int  SOFTWARE_VER=2541; //����汾�� ,������2542,string
	public static final int  HARDWARE_VER=2543; //Ӳ���汾��,string
	public static final int  SYSTEMKERNEL_VER=2545;//ϵͳ�ں˰汾��,string
	
	//running information 
	public static final int  SPINDLE_ACT_SPEED=2048; //����ʵ��ת�٣�double
	public static final int  SPINDLE_COM_SPEED=2046; //����ָ��ת�٣�double
	public static final int  SPINDLE_LOAD_CURRENT=2562;//���Ḻ�ص�����double 
	
	public static final int  FEEDAXIS_ACT_SPEED=2028; //������ʵ���ٶ�,double
	
	public static final int  FEEDAXIS_ACT_SPEED_X=2580; //������ʵ���ٶ�,x,double
	public static final int  FEEDAXIS_ACT_SPEED_Y=2581; //������ʵ���ٶ�,y,double
	public static final int  FEEDAXIS_ACT_SPEED_Z=2582; //������ʵ���ٶ�,z,double
	
	
	public static final int  FEEDAXIS_ACT_POSITION_X=2094;//������ʵ��λ��,x,double
	public static final int  FEEDAXIS_ACT_POSITION_Y=2096;//������ʵ��λ��,y,double
	public static final int  FEEDAXIS_ACT_POSITION_Z=2098;//������ʵ��λ��,Z,double
		
	public static final int  FEEDAXIS_COM_POSITION_X=2082; //������ָ��λ�ã�x,double
	public static final int  FEEDAXIS_COM_POSITION_Y=2084; //������ָ��λ�ã�Y,double
	public static final int  FEEDAXIS_COM_POSITION_Z=2086; //������ָ��λ�ã�z,double
	
	public static final int  FEEDAXIS_LOAD_CURRENT_X=2570;//�����Ḻ�ص�����x,double
	public static final int  FEEDAXIS_LOAD_CURRENT_Y=2571;//�����Ḻ�ص�����y,double
	public static final int  FEEDAXIS_LOAD_CURRENT_Z=2572;//�����Ḻ�ص�����z,double
	
	public static final int  PROGRAM_NAME=2152; //��ǰ������,string
	public static final int  G_CODE_RUN_STAT=2003; //G��������״̬,int
	public static final int  G_CODE_LINE_NUM=2013;//G�����к�,int
	public static final int  G_CODE_MODALITY=2561;//G����ģ̬,����int32_t[20]������ֵ
	
	
	//login information 
	public static final int  TOTAL_RUN_TIME=2539; //�ۼ�����ʱ�䣬double���ϵ�ʱ��
	public static final int  TOTAL_PROCESS_TIME=2532; //�ۼƼӹ�ʱ��  ,double

}

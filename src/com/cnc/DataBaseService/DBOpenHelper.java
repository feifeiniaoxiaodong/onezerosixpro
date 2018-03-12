package com.cnc.databaseservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 
 * @author wei
 *
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	//��һ��ʹ�������ʱ���漰�����ݿ⣩���Զ��������ݿ�
	public DBOpenHelper(Context context) {
		//������������ʾcursor factory��null��ʾ����ϵͳĬ�ϵ��α깤�������һ�������ǰ汾�ţ�����Ϊ0
		super(context, "daq.db", null, 1);//"/data/data/<��>/databases/"
	}

	@Override
	public void onCreate(SQLiteDatabase db) {//�������ݿ�ÿһ�α�������ʱ����õ�
//		db.execSQL(getMacListCreationString());
		db.execSQL(getAlarmCreationString());//����������Ϣ��	
//		db.execSQL(getAxisInfoCreationString());//��������Ϣ��������Ϣ��
		db.execSQL(getRunInfoCreationString());//����������Ϣ��
	}
	/*
	 * ����������Ϣ���sql���
	 */
	private String getAlarmCreationString()
	{
		String str  = "CREATE TABLE HistoryAlarm"
				+ "(id integer primary key autoincrement, "				
				+ "no VARCHAR(15),"	
				+ "ctt varchar(20),"
				+ "time VARCHAR(25), "
				+ "f VARCHAR(2),"
				+ "SN_NUM VARCHAR(32))";
		return str;	
	}
	
	/*
	 * ����������Ϣ���sql���
	 */
	private String getRunInfoCreationString()
	{
		String str  = "CREATE TABLE RunInfo"
				+ "(id integer primary key autoincrement, "							
				
				+ "cas real,"
				+ "ccs real,"
				+ "aload real,"
				
				+ "aspd1 real,"				
				+ "aspd2 real,"
				+ "aspd3 real,"
				+ "aspd4 real,"
				+ "aspd5 real,"
				
				+ "apst1 real,"
				+ "apst2 real,"
				+ "apst3 real,"
				+ "apst4 real,"				
				+ "apst5 real,"
				
				+ "cpst1 real,"
				+ "cpst2 real,"
				+ "cpst3 real,"
				+ "cpst4 real,"				
				+ "cpst5 real,"
				
				+ "load1 real,"
				+ "load2 real,"
				+ "load3 real,"
				+ "load4 real,"				
				+ "load5 real,"
				
				+ "pd integer, "
				+ "pn VARCHAR(20),"
				+ "ps VARCHAR(20),"
				+ "pl integer, "
				+ "pm integer, "

				+ "SN_NUM VARCHAR(32),"
				+ "time VARCHAR(25))";
		return str;	
	}
	
	
//	/*
//	 * ��������Ϣ���sql���
//	 */
//	private String getAxisInfoCreationString()
//	{
//		String str  = "CREATE TABLE AxisInfo"
//				+ "(id integer primary key autoincrement, "	
//								
//				+ "HNC_AXIS_TYPE integer, "
//				
//				+ "HNC_AXIS_ACT_POS integer, "
//				+ "HNC_AXIS_ACT_POS2 integer, "
//				+ "HNC_AXIS_CMD_POS integer, "
//				+ "HNC_AXIS_ACT_POS_WCS integer, "
//				+ "HNC_AXIS_CMD_POS_WCS integer, "
//				+ "HNC_AXIS_ACT_POS_RCS integer, "
//				+ "HNC_AXIS_CMD_POS_RCS integer, "
//				+ "HNC_AXIS_ACT_PULSE integer, "
//				+ "HNC_AXIS_CMD_PULSE integer, "
//				+ "HNC_AXIS_PROG_POS integer, "
//				+ "HNC_AXIS_ENC_CNTR integer, "
//				+ "HNC_AXIS_CMD_VEL integer, "
//				
//				+ "HNC_AXIS_ACT_VEL real,"
//				+ "HNC_AXIS_MOTOR_REV real,"
//				+ "HNC_AXIS_DRIVE_CUR real,"
//				+ "HNC_AXIS_LOAD_CUR real,"				
//				+ "HNC_AXIS_RATED_CUR real,"
//				+ "SN_NUM VARCHAR(32),"
//				+ "TIME VARCHAR(25))";
//		return str;	
//	}
//	
//	/*
//	 * ����������Ϣ���sql���
//	 */
//	private String getSpindleDataCreationString()
//	{
//		String str  = "CREATE TABLE SpindleData"
//				+ "(id integer primary key autoincrement, "	
//								
//				+ "channel integer, "
//				
//				+ "progNO integer, "
//				+ "GcodeSign integer, "
//				+ "runLine integer, "
//				+ "compLine integer, "				
//				
//				+ "runVel real,"
//				+ "feedSpeed real,"
//				
//				+ "SN_NUM VARCHAR(32),"
//				+ "TIME VARCHAR(25))";
//		return str;	
//	}
	
//	private String getMacListCreationString()
//	{
//		String str  = "CREATE TABLE MachineList"
//				+ "(id integer primary key autoincrement, "	
//								
//				+ "NCK_VER VARCHAR(32), "
//				+ "DRV_VER VARCHAR(32), "
//				+ "PLC_VER VARCHAR(32), "
//				+ "CNC_VER VARCHAR(32), "
//				+ "NC_VER VARCHAR(32),	"
//				
//				+ "MACHINE_TYPE VARCHAR(48),"
//				+ "MACHINE_INFO VARCHAR(48),"
//				+ "MACHINE_NUM VARCHAR(48),"
//				+ "MACFAC_INFO VARCHAR(48),"
//				+ "EXFACTORY_DAT VARCHAR(32))";
//		return str;	
//	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//���ݿ�汾�����ʱ�򱻵���
		//db.execSQL("ALTER TABLE person ADD amount integer");
	}
	

}

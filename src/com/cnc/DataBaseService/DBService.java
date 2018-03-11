package com.cnc.DataBaseService;

import java.util.ArrayList;
import java.util.List;

import com.cnc.daq.MyApplication;
import com.cnc.domain.DataAlarm;
import com.cnc.domain.DataRun;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.content.Intent;

/**
 * 
 * @author wei
 *
 */
public class DBService{
	private static final String TAG="DBService...";
	private DBOpenHelper dbOpenHelper=null;
	
	private static DBService dbService=new DBService(MyApplication.getContext());
	
	public DBService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}
	
	//����ģʽ��ȡDBService����
	public static DBService  getInstanceDBService(){
	
//		if(dbService ==null){
//			synchronized (dbService) {
//				if(dbService ==null){
//					dbService=new DBService(MyApplication.getContext());
//				}				
//			}			
//}
		return dbService;		
	}
	
	
/********************************************************************************/
//����һ����¼
	/**
	 * ��Ӽ�¼
	 * @param alarmHistoryData
	 */
	public void saveAlarmData(DataAlarm dataAlarm){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into HistoryAlarm(no, ctt, time, f, SN_NUM) values(?,?,?,?,?)",
				new Object[]{
						dataAlarm.getNo(),
						dataAlarm.getCtt(),
						dataAlarm.getTime(),
						dataAlarm.getF(),
						dataAlarm.getId()});//Object[]Ϊ��������������ݸ�ֵ
 	}
	
	
	/*
	 * ��Ӽ�¼������������Ϣ
	 */
	public void saveRunData(DataRun dataRun){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into RunInfo(cas, ccs, aload,"
				+ "aspd1,aspd2,aspd3,aspd4,aspd5,"
				+ "apst1,apst2,apst3,apst4,apst5,"
				+ "cpst1,cpst2,cpst3,cpst4,cpst5,"
				+ "load1,load2,load3,load4,load5,"
				+ "pd,pn,ps,pl,pm,"
				+ "SN_NUM,time"
				+ ") "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[]{
						dataRun.getCas(),
						dataRun.getCcs(),
						dataRun.getAload(),
						
						dataRun.getAspd1(),
						dataRun.getAspd2(),
						dataRun.getAspd3(),
						dataRun.getAspd4(),
						dataRun.getAspd5(),
						
						dataRun.getApst1(),
						dataRun.getApst2(),
						dataRun.getApst3(),
						dataRun.getApst4(),
						dataRun.getApst5(),
						
						dataRun.getCpst1(),
						dataRun.getCpst2(),
						dataRun.getCpst3(),
						dataRun.getCpst4(),
						dataRun.getCpst5(),
						
						dataRun.getLoad1(),
						dataRun.getLoad2(),
						dataRun.getLoad3(),
						dataRun.getLoad4(),
						dataRun.getLoad5(),
						
						dataRun.getPd(),
						dataRun.getPn(),
						dataRun.getPs(),
						dataRun.getPl(),
						dataRun.getPm(),
						
						dataRun.getId(),
						dataRun.getTime()
						});//Object[]Ϊ��������������ݸ�ֵ
 	}
	
	
	/*
	 * ��Ӽ�¼ AxisInfo
	 */
	
/********************************************************************************/

	/**
	 * ɾ��һ����¼
	 * @param id ��¼ID
	 */
	public void deleteRunInfo(Integer id){
		if(0 != id)
		{
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			db.execSQL("delete from RunInfo where id=?", new Object[]{id});
		}
	}
	
	/**
	 * ɾ��������¼
	 * @param id ��¼ID
	 */
	public void deleteRunInfo(Integer id,Integer idEnd){
		if(0 != id)
		{
			if(id<idEnd){
				SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			db.execSQL("delete from RunInfo where id between ? and ?", 
					    new Object[] {id,idEnd}); //between �����߽�ֵ
			}else if(id.equals(idEnd)){  				
				deleteRunInfo( id);
			}else{
				Log.d(TAG," id>idEnd ������Ϣ������id�ŵ�˳�򲻶�");
			}			
		}
	}
	
	/**
	 * ɾ��һ��������Ϣ
	 * @param id
	 */
	public void deleteAlarmData(Integer id){
		if(0 != id)
		{
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			db.execSQL("delete from HistoryAlarm where id=?", 
					new Object[]{id});
		}
	}
	
	/**
	 * ɾ������������Ϣ
	 * @param id
	 */
	public void deleteAlarmData(Integer id,Integer idEnd){
		if(0 != id)
		{
			if(id<idEnd){
				SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
				db.execSQL("delete from HistoryAlarm where id between ? and ?", 
							new Object[]{id, idEnd});
			}else if(id==idEnd){
				deleteAlarmData(id);
			}else{
				Log.d(TAG," id>idEnd ������Ϣ������id�ŵ�˳�򲻶�");
			}			
		}
	}
/********************************************************************************/	

/*���������*******************************************************************************/
/*******************************************************************************/
	/**
	 * ��ȡ���ݿ��¼�еĵ�һ����������
	 * ��ȡ������Ϣ
	 * @return
	 */	
	public DataAlarmPlus findFirstRowAlarmDataP()
	{
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from HistoryAlarm order by id asc limit ?,?",
				new String[]{String.valueOf(0), String.valueOf(1)});
		DataAlarmPlus dataAlarmPlus = new DataAlarmPlus();		
		DataAlarm dataAlarm = new DataAlarm();
		
		if(cursor.moveToFirst()){			
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String alarmNO = cursor.getString(cursor.getColumnIndex("no"));
			String text = cursor.getString(cursor.getColumnIndex("ctt"));
			String timeBegin = cursor.getString(cursor.getColumnIndex("time"));
			int f = cursor.getInt(cursor.getColumnIndex("f"));
			String SNNum = cursor.getString(cursor.getColumnIndex("SN_NUM"));
			dataAlarm.setNo(alarmNO);
			dataAlarm.setCtt(text);
			dataAlarm.setTime(timeBegin);
			dataAlarm.setF((byte)f);
			dataAlarm.setId(SNNum);
			
			dataAlarmPlus.dataAlarm = dataAlarm;
			dataAlarmPlus.id = id;					
		}
		cursor.close();
		return dataAlarmPlus;
	}
	
	/**
	 * ��ȡ���ݿ��¼�е�ǰn����������
	 * ��ȡ������Ϣ
	 * @return
	 */	
	public DataAlarmPlus findNumRowAlarmDataP(int n){
		boolean isRecordFirstId=false;
		int count=0;
		List<DataAlarm> listAlarmInfo=new ArrayList<DataAlarm>();
		DataAlarmPlus dataAlarmPlus = new DataAlarmPlus();	
		
		if(n<1){
			Log.d(TAG,"Ҫȡ�ı�������С��1�����޷����");
			return null;
		}
		
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();		
		Cursor cursor = db.rawQuery("select * from HistoryAlarm order by id asc limit ?,?",
				new String[]{String.valueOf(0), String.valueOf(n)});
		
		if(cursor.moveToFirst()){
			do{
				DataAlarm dataAlarm = new DataAlarm();
				
				String alarmNO = cursor.getString(cursor.getColumnIndex("no"));
				String text = cursor.getString(cursor.getColumnIndex("ctt"));
				String timeBegin = cursor.getString(cursor.getColumnIndex("time"));
				int f = cursor.getInt(cursor.getColumnIndex("f"));
				String SNNum = cursor.getString(cursor.getColumnIndex("SN_NUM"));
				
				dataAlarm.setNo(alarmNO);
				dataAlarm.setCtt(text);
				dataAlarm.setTime(timeBegin);
				dataAlarm.setF((byte)f);
				dataAlarm.setId(SNNum);
				
				listAlarmInfo.add(dataAlarm);//���浽����
				
				if(!isRecordFirstId){
					dataAlarmPlus.setId(cursor.getInt(cursor.getColumnIndex("id")));
					isRecordFirstId=true;
				}
				
				if(++count==n){
					dataAlarmPlus.setIdEnd(cursor.getInt(cursor.getColumnIndex("id")));
				}
				
			}while(cursor.moveToNext());
		}
		
		dataAlarmPlus.setListDataAlarm(listAlarmInfo);
		cursor.close();
		return dataAlarmPlus;
	}
	
	
	/**
	 * ��ȡ��һ��������Ϣ
	 * @return
	 */			
	public DataRunPlus findFirstRowRunInfo()
	{
		DataRunPlus dataRunPlus = new DataRunPlus();
		
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from RunInfo order by id asc limit ?,?",
				new String[]{String.valueOf(0), String.valueOf(1)});
		DataRun dataRun = new DataRun();
		
		if(cursor.moveToFirst()){
			dataRun.setCas((float)cursor.getDouble(cursor.getColumnIndex("cas")));
			dataRun.setCcs((float)cursor.getDouble(cursor.getColumnIndex("ccs")));
			dataRun.setAload((float)cursor.getDouble(cursor.getColumnIndex("aload")));
			
			dataRun.setAspd1((float)cursor.getDouble(cursor.getColumnIndex("aspd1")));
			dataRun.setAspd2((float)cursor.getDouble(cursor.getColumnIndex("aspd2")));
			dataRun.setAspd3((float)cursor.getDouble(cursor.getColumnIndex("aspd3")));
			dataRun.setAspd4((float)cursor.getDouble(cursor.getColumnIndex("aspd4")));
			dataRun.setAspd5((float)cursor.getDouble(cursor.getColumnIndex("aspd5")));
			
			dataRun.setApst1((float)cursor.getDouble(cursor.getColumnIndex("apst1")));
			dataRun.setApst2((float)cursor.getDouble(cursor.getColumnIndex("apst2")));
			dataRun.setApst3((float)cursor.getDouble(cursor.getColumnIndex("apst3")));
			dataRun.setApst4((float)cursor.getDouble(cursor.getColumnIndex("apst4")));
			dataRun.setApst5((float)cursor.getDouble(cursor.getColumnIndex("apst5")));
			
			dataRun.setCpst1((float)cursor.getDouble(cursor.getColumnIndex("cpst1")));
			dataRun.setCpst2((float)cursor.getDouble(cursor.getColumnIndex("cpst2")));
			dataRun.setCpst3((float)cursor.getDouble(cursor.getColumnIndex("cpst3")));
			dataRun.setCpst4((float)cursor.getDouble(cursor.getColumnIndex("cpst4")));
			dataRun.setCpst5((float)cursor.getDouble(cursor.getColumnIndex("cpst5")));
			
			dataRun.setLoad1((float)cursor.getDouble(cursor.getColumnIndex("load1")));
			dataRun.setLoad2((float)cursor.getDouble(cursor.getColumnIndex("load2")));
			dataRun.setLoad3((float)cursor.getDouble(cursor.getColumnIndex("load3")));
			dataRun.setLoad4((float)cursor.getDouble(cursor.getColumnIndex("load4")));
			dataRun.setLoad5((float)cursor.getDouble(cursor.getColumnIndex("load5")));
			
			dataRun.setPd((short)cursor.getInt(cursor.getColumnIndex("pd")));
			dataRun.setPn(cursor.getString(cursor.getColumnIndex("pn")));
			dataRun.setPs(cursor.getString(cursor.getColumnIndex("ps")));
			dataRun.setPl((short)cursor.getInt(cursor.getColumnIndex("pl")));
			dataRun.setPm((short)cursor.getInt(cursor.getColumnIndex("pm")));
			
			dataRun.setId(cursor.getString(cursor.getColumnIndex("SN_NUM")));	
			dataRun.setTime(cursor.getString(cursor.getColumnIndex("time")));			
			
			dataRunPlus.id = cursor.getInt(cursor.getColumnIndex("id"));
			dataRunPlus.dataRun = dataRun;
		}
		cursor.close();
		
		return dataRunPlus;
	}
	
	/**
	 * �ӱ������ݿ���ȡ��ǰn��������Ϣ
	 * @param n:ȡ��n��������Ϣ
	 * @return
	 */
	public DataRunPlus findNumRowRunInfo(int n){
		boolean isRecordFirstId=false; //�Ƿ��¼����ʼid��
		int count =0;
		DataRunPlus dataRunPlus = new DataRunPlus();
		List<DataRun> listRunInfo=new ArrayList<DataRun>();
		
		if(n<1){
			Log.d(TAG,"Ҫȡ����������С��1�����޷����");
			return null;
		}
		
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from RunInfo order by id asc limit ?,?",
						new String[]{String.valueOf(0), String.valueOf(n)});
		if(cursor.moveToFirst()){
			do{
				DataRun dataRun = new DataRun();
				
				dataRun.setCas((float)cursor.getDouble(cursor.getColumnIndex("cas")));
				dataRun.setCcs((float)cursor.getDouble(cursor.getColumnIndex("ccs")));
				dataRun.setAload((float)cursor.getDouble(cursor.getColumnIndex("aload")));
				
				dataRun.setAspd1((float)cursor.getDouble(cursor.getColumnIndex("aspd1")));
				dataRun.setAspd2((float)cursor.getDouble(cursor.getColumnIndex("aspd2")));
				dataRun.setAspd3((float)cursor.getDouble(cursor.getColumnIndex("aspd3")));
				dataRun.setAspd4((float)cursor.getDouble(cursor.getColumnIndex("aspd4")));
				dataRun.setAspd5((float)cursor.getDouble(cursor.getColumnIndex("aspd5")));
				
				dataRun.setApst1((float)cursor.getDouble(cursor.getColumnIndex("apst1")));
				dataRun.setApst2((float)cursor.getDouble(cursor.getColumnIndex("apst2")));
				dataRun.setApst3((float)cursor.getDouble(cursor.getColumnIndex("apst3")));
				dataRun.setApst4((float)cursor.getDouble(cursor.getColumnIndex("apst4")));
				dataRun.setApst5((float)cursor.getDouble(cursor.getColumnIndex("apst5")));
				
				dataRun.setCpst1((float)cursor.getDouble(cursor.getColumnIndex("cpst1")));
				dataRun.setCpst2((float)cursor.getDouble(cursor.getColumnIndex("cpst2")));
				dataRun.setCpst3((float)cursor.getDouble(cursor.getColumnIndex("cpst3")));
				dataRun.setCpst4((float)cursor.getDouble(cursor.getColumnIndex("cpst4")));
				dataRun.setCpst5((float)cursor.getDouble(cursor.getColumnIndex("cpst5")));
				
				dataRun.setLoad1((float)cursor.getDouble(cursor.getColumnIndex("load1")));
				dataRun.setLoad2((float)cursor.getDouble(cursor.getColumnIndex("load2")));
				dataRun.setLoad3((float)cursor.getDouble(cursor.getColumnIndex("load3")));
				dataRun.setLoad4((float)cursor.getDouble(cursor.getColumnIndex("load4")));
				dataRun.setLoad5((float)cursor.getDouble(cursor.getColumnIndex("load5")));
				
				dataRun.setPd((short)cursor.getInt(cursor.getColumnIndex("pd")));
				dataRun.setPn(cursor.getString(cursor.getColumnIndex("pn")));
				dataRun.setPs(cursor.getString(cursor.getColumnIndex("ps")));
				dataRun.setPl((short)cursor.getInt(cursor.getColumnIndex("pl")));
				dataRun.setPm((short)cursor.getInt(cursor.getColumnIndex("pm")));
				
				dataRun.setId(cursor.getString(cursor.getColumnIndex("SN_NUM")));	
				dataRun.setTime(cursor.getString(cursor.getColumnIndex("time")));	
				
				listRunInfo.add(dataRun);
				
				if(!isRecordFirstId){
					dataRunPlus.setId(cursor.getInt(cursor.getColumnIndex("id")));//��¼��ʼ��id��
					isRecordFirstId=true;
				}
								
				if((++count)==n){
					dataRunPlus.setIdEnd(cursor.getInt(cursor.getColumnIndex("id")));//��¼ĩβ��id��
				}												
			}while(cursor.moveToNext());									
		}
		dataRunPlus.setListRunData(listRunInfo);
		cursor.close();
		return dataRunPlus;		
	}
	
/*******************************************************************************/	
	/**
	 * ��ȡ��¼����
	 * @return
	 */
	//��ȡ������Ϣ������
	public synchronized long getCountAlarmData(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from HistoryAlarm", null);
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		return result;
	}
	/**
	 * ��ȡ������Ϣ������
	 * @return
	 */
	public synchronized long  getCountRunInfo(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from RunInfo", null);
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		return result;
	}	
/*******************************************************************************/

}

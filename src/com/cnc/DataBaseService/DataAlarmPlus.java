package com.cnc.DataBaseService;

import java.util.List;

import com.cnc.domain.DataAlarm;

public class DataAlarmPlus {
	public int id;		//��¼���ݿ��е�idֵ�����ݿ��Զ����ɵģ�����ɾ������
	public DataAlarm dataAlarm;
	
	private int idEnd;
	private List<DataAlarm>  listDataAlarm;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DataAlarm getDataAlarm() {
		return dataAlarm;
	}
	public void setDataAlarm(DataAlarm dataAlarm) {
		this.dataAlarm = dataAlarm;
	}
	public int getIdEnd() {
		return idEnd;
	}
	public void setIdEnd(int idEnd) {
		this.idEnd = idEnd;
	}
	public List<DataAlarm> getListDataAlarm() {
		return listDataAlarm;
	}
	public void setListDataAlarm(List<DataAlarm> listDataAlarm) {
		this.listDataAlarm = listDataAlarm;
	}
	
	
}

package com.cnc.DataBaseService;

import java.util.List;

import com.cnc.domain.DataRun;

public class DataRunPlus {
	public int id;     //��¼���ݿ��е�idֵ�����ݿ��Զ����ɵģ�����ɾ������
	public DataRun dataRun;
	
	private int idEnd;   //��β��IDֵ��ɾ������ʹ��
	private List<DataRun> listRunData;		//һ�ζ�ȡ��������ʱ�����浽list��
		

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DataRun getDataRun() {
		return dataRun;
	}
	public void setDataRun(DataRun dataRun) {
		this.dataRun = dataRun;
	}
	
	
	public int getIdEnd() {
		return idEnd;
	}
	public void setIdEnd(int idEnd) {
		this.idEnd = idEnd;
	}
	public List<DataRun> getListRunData() {
		return listRunData;
	}
	public void setListRunData(List<DataRun> listRunData) {
		this.listRunData = listRunData;
	}
}

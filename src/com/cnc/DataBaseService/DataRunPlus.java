package com.cnc.DataBaseService;

import java.util.List;

import com.cnc.domain.DataRun;

public class DataRunPlus {
	public int id;     //记录数据库中的id值，数据库自动生成的，用来删除数据
	public DataRun dataRun;
	
	private int idEnd;   //结尾的ID值，删除数据使用
	private List<DataRun> listRunData;		//一次读取多条数据时，保存到list中
		

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

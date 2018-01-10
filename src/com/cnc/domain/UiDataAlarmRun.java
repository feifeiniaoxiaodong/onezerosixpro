package com.cnc.domain;

public class UiDataAlarmRun {
	
	String alarminfo;
	
	String runinfo;
	
	

	public UiDataAlarmRun() {
		super();
	}

	public UiDataAlarmRun(String alarminfo, String runinfo) {
		super();
		this.alarminfo ="Alarm"+ alarminfo;
		this.runinfo = "RunInfo"+runinfo;
	}

	public String getAlarminfo() {
		return alarminfo;
	}

	public void setAlarminfo(String alarminfo) {
		this.alarminfo = alarminfo;
	}

	public String getRuninfo() {
		return runinfo;
	}

	public void setRuninfo(String runinfo) {
		this.runinfo = runinfo;
	}

}

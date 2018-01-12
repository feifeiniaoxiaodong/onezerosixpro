package com.cnc.domain;

public class UiDataAlarmRun {
	
	String alarminfo;
	
	String runinfo;
	
	String threadlabel; //线程标记

	public UiDataAlarmRun() {
		super();
	}

	public UiDataAlarmRun(String alarminfo, String runinfo) {
		super();
		this.alarminfo ="Alarm: "+ alarminfo;
		this.runinfo = "RunInfo: "+runinfo;
	}
	
	public UiDataAlarmRun(String alarminfo, String runinfo,String label) {
		this(alarminfo,runinfo);
		this.threadlabel=label;
	}

	
	public String getThreadlabel() {
		return threadlabel;
	}

	public void setThreadlabel(String threadlabel) {
		this.threadlabel = threadlabel;
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

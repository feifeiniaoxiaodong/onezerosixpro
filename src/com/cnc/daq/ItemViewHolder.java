package com.cnc.daq;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ItemViewHolder {
	TextView  no,ip,
	 idcnc,
	 idandroid,
	 alarm,
	 runinfo;
	Spinner   spinner;
	Button    btstart ,btstop;
	
	public ItemViewHolder() {		
	}
	
	public ItemViewHolder(TextView no, TextView ip, TextView idcnc,
		TextView idandroid, TextView alarm, TextView runinfo,
		Spinner spinner, Button btstart, Button btstop) {
	
	this.no = no;
	this.ip = ip;
	this.idcnc = idcnc;
	this.idandroid = idandroid;
	this.alarm = alarm;
	this.runinfo = runinfo;
	this.spinner = spinner;
	this.btstart = btstart;
	this.btstop = btstop;
	}
	
	public TextView getIp() {
	return ip;
	}
	
	public TextView getNo() {
	return no;
	}
	
	public TextView getIdcnc() {
	return idcnc;
	}
	
	public TextView getIdandroid() {
	return idandroid;
	}
	
	public TextView getAlarm() {
	return alarm;
	}
	
	public TextView getRuninfo() {
	return runinfo;
	}
	
	public Spinner getSpinner() {
	return spinner;
	}
	
	public Button getBtstart() {
	return btstart;
	}
	
	public Button getBtstop() {
	return btstop;
	}
}

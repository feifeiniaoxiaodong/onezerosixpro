package com.cnc.domain;

public class UiDataNo {
	String no;
	String ip;
	String idcnc;
	String idandroid;
	
	
	
	public UiDataNo() {
		super();
	}
	
	public UiDataNo(String no, String ip, String idcnc, String idandroid) {
		super();
		this.no = no;
		this.ip = ip;
		this.idcnc = idcnc;
		this.idandroid = idandroid;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIdcnc() {
		return idcnc;
	}
	public void setIdcnc(String idcnc) {
		this.idcnc = idcnc;
	}
	public String getIdandroid() {
		return idandroid;
	}
	public void setIdandroid(String idandroid) {
		this.idandroid = idandroid;
	}
	

}

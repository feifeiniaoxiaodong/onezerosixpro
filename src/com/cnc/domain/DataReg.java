package com.cnc.domain;

/**
 * ��׼ע����Ϣ��
 * @author wei
 *
 */
public class DataReg {
	private String id;    //����ϵͳID����ʶΨһNC��    
    private String tp;    //����ϵͳ�ͺ�    
    private String ver;   //����ϵͳ�����ְ汾��    
    private String time;  //ʱ���
    
 
	public DataReg() {
		super();
	}
	public DataReg(String id, String tp, String ver, String time) {
		super();
		this.id = id;
		this.tp = tp;
		this.ver = ver;
		this.time = time;
	}
	
	
	@Override
	public String toString() {
		return "DataReg [id=" + id + ", tp=" + tp + ", ver=" + ver + ", time="
				+ time + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
    
	
	
}

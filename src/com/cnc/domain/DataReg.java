package com.cnc.domain;

/**
 * 标准注册信息类
 * @author wei
 *
 */
public class DataReg {
	private String id;    //数控系统ID（标识唯一NC）    
    private String tp;    //数控系统型号    
    private String ver;   //数控系统各部分版本号    
    private String time;  //时间戳
    
 
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

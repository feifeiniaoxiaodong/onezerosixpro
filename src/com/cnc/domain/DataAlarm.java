package com.cnc.domain;

/**
 * 标准报警信息
 * @author wei
 *
 */
public class DataAlarm {
	private String id;    //数控系统ID（标识唯一NC）    
    private byte f;  //报警时间标志位 0：表示报警发生时间 1：表示报警消除时间    
    private String no;     //报警代码    
    private String time;      //报警发生时间或者消除时间    
    private String ctt;  //报警文本
	
    
    
    public DataAlarm() {
		super();
	}
	public DataAlarm(String id, byte f, String no, String time, String ctt) {
		super();
		this.id = id;
		this.f = f;
		this.no = no;
		this.time = time;
		this.ctt = ctt;
	}
	
	
	@Override
	public String toString() {
		return "DataAlarm [id=" + id + ", f=" + f + ", no=" + no + ", time="
				+ time + ", ctt=" + ctt + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public byte getF() {
		return f;
	}
	public void setF(byte f) {
		this.f = f;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCtt() {
		return ctt;
	}
	public void setCtt(String ctt) {
		this.ctt = ctt;
	}
    
}

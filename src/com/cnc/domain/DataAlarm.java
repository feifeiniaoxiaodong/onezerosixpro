package com.cnc.domain;

/**
 * ��׼������Ϣ
 * @author wei
 *
 */
public class DataAlarm {
	private String id;    //����ϵͳID����ʶΨһNC��    
    private byte f;  //����ʱ���־λ 0����ʾ��������ʱ�� 1����ʾ��������ʱ��    
    private String no;     //��������    
    private String time;      //��������ʱ���������ʱ��    
    private String ctt;  //�����ı�
	
    
    
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

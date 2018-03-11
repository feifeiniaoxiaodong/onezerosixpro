package com.cnc.domain;


/**
 * ��ʱʱ�����ݶ���
 * @author wei
 *	
 */
public class DataDelayTime {
	
	private String 	cncId;	//����ID, �ն˷�����ʱʱ��ͻ���û��ϵ�� �����������Ϊ 4G��Ӫ��
	private String 	terminalid; //�ɼ��ն�ID
	private int     datatype ;  //��������
	private long    numofmsg ;  // �˴η�����Ϣ������
	private long    numofmsgunsent ;  //δ���͵ı�����������
	private long    delaytime;  // ��ʱʱ�䣬��λ ms
	private long    packagesize; //���ݰ���С���������ݣ�����λ KB
	private double  speed ;     //����  
	private String  ts; //ʱ���
	
	
	
	public DataDelayTime() {	}

	public DataDelayTime(String cncId, String terminalid, int datatype,
			long numofmsg, long numofmsgunsent, long delaytime,
			long packagesize, double speed, String ts) {
		
		this.cncId = cncId;
		this.terminalid = terminalid;
		this.datatype = datatype;
		this.numofmsg = numofmsg;
		this.numofmsgunsent = numofmsgunsent;
		this.delaytime = delaytime;
		this.packagesize = packagesize;
		this.speed = speed;
		this.ts = ts;
	}

	public String getCncId() {
		return cncId;
	}


	public void setCncId(String cncId) {
		this.cncId = cncId;
	}


	public String getTerminalid() {
		return terminalid;
	}


	public void setTerminalid(String terminalid) {
		this.terminalid = terminalid;
	}


	public int getDatatype() {
		return datatype;
	}


	public void setDatatype(int datatype) {
		this.datatype = datatype;
	}


	public long getNumofmsg() {
		return numofmsg;
	}


	public void setNumofmsg(long numofmsg) {
		this.numofmsg = numofmsg;
	}


	public long getNumofmsgunsent() {
		return numofmsgunsent;
	}


	public void setNumofmsgunsent(long numofmsgunsent) {
		this.numofmsgunsent = numofmsgunsent;
	}


	public long getDelaytime() {
		return delaytime;
	}


	public void setDelaytime(long delaytime) {
		this.delaytime = delaytime;
	}


	public long getPackagesize() {
		return packagesize;
	}


	public void setPackagesize(long packagesize) {
		this.packagesize = packagesize;
	}


	public double getSpeed() {
		return speed;
	}


	public void setSpeed(double speed) {
		this.speed = speed;
	}


	public String getTs() {
		return ts;
	}


	public void setTs(String ts) {
		this.ts = ts;
	}


	@Override
	public String toString() {
		return "DataDelayTime [cncId=" + cncId + ", terminalid=" + terminalid
				+ ", datatype=" + datatype + ", numofmsg=" + numofmsg
				+ ", numofmsgunsent=" + numofmsgunsent + ", delaytime="
				+ delaytime + ", packagesize=" + packagesize + ", speed="
				+ speed + ", ts=" + ts + "]";
	}
	

}

package bean;
/*
 * �ⲿͨ��
 * ϵͳ��Ϣ
 */
public class SYSTEM_INFO {

	private String type;        //���ؽṹ���ͺ�
	private String systemid;	//����ϵͳID
	private String systemtype;	//����ϵͳ�ͺ�
	private String systemver;	//NC�ܰ汾��
	private String ggroup;		//G����ģ̬
	private String pn;			//������
	private int pd;             //���г�����
	private int ps;				//��������״̬
	private int	pl;				//���д����к�
	private long s_loadcurrent;		//���Ḻ�ص���
	private long []axis_loadcurrent = new long[3];	//�����Ḻ�ص���
	private long ontime;			//����ϵͳ�ۼ�����ʱ��
	private long runtime;			//����ϵͳ�ۼƼӹ�ʱ��
	private String timestamp;		//ʱ���yyyy-mm-dd hh:mm:ss.zzz
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSystemid() {
		return systemid;
	}
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	public String getSystemtype() {
		return systemtype;
	}
	public void setSystemtype(String systemtype) {
		this.systemtype = systemtype;
	}
	public String getSystemver() {
		return systemver;
	}
	public void setSystemver(String systemver) {
		this.systemver = systemver;
	}
	public String getGgroup() {
		return ggroup;
	}
	public void setGgroup(String ggroup) {
		this.ggroup = ggroup;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public int getPd() {
		return pd;
	}
	public void setPd(int pd) {
		this.pd = pd;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public int getPl() {
		return pl;
	}
	public void setPl(int pl) {
		this.pl = pl;
	}
	public long getS_loadcurrent() {
		return s_loadcurrent;
	}
	public void setS_loadcurrent(long s_loadcurrent) {
		this.s_loadcurrent = s_loadcurrent;
	}
	public long[] getAxis_loadcurrent() {
		return axis_loadcurrent;
	}
	public void setAxis_loadcurrent(long[] axis_loadcurrent) {
		this.axis_loadcurrent = axis_loadcurrent;
	}
	public long getOntime() {
		return ontime;
	}
	public void setOntime(long ontime) {
		this.ontime = ontime;
	}
	public long getRuntime() {
		return runtime;
	}
	public void setRuntime(long runtime) {
		this.runtime = runtime;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}

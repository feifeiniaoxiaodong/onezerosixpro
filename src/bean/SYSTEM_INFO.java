package bean;
/*
 * 外部通信
 * 系统信息
 */
public class SYSTEM_INFO {

	private String type;        //数控结构体型号
	private String systemid;	//数控系统ID
	private String systemtype;	//数控系统型号
	private String systemver;	//NC总版本号
	private String ggroup;		//G代码模态
	private String pn;			//程序名
	private int pd;             //运行程序编号
	private int ps;				//代码运行状态
	private int	pl;				//运行代码行号
	private long s_loadcurrent;		//主轴负载电流
	private long []axis_loadcurrent = new long[3];	//进给轴负载电流
	private long ontime;			//数控系统累计运行时间
	private long runtime;			//数控系统累计加工时间
	private String timestamp;		//时间戳yyyy-mm-dd hh:mm:ss.zzz
	
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

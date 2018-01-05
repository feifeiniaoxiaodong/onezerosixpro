package com.cnc.domain;

//运行信息
public class DataRun {
	private String id;     //数控系统ID（标识唯一NC）    
    private float cas;     //主轴实际转速    
    private float ccs;     //主轴指令转速    
    private float aload;   //主轴负载电流    
    private float aspd1;   //轴1实际转速(一般为X轴)    
    private float aspd2;   //轴2实际转速(一般为Y轴)    
    private float aspd3;   //轴3实际转速(一般为Z轴)    
    private float aspd4;   //轴4实际转速(一般为A轴)    
    private float aspd5;   //轴5实际转速(一般为B轴)    
    private float apst1;   //轴1实际位置(一般为X轴)    
    private float apst2;   //轴2实际位置(一般为Y轴)    
    private float apst3;   //轴3实际位置(一般为Z轴)    
    private float apst4;   //轴4实际位置(一般为A轴)    
    private float apst5;   //轴5实际位置(一般为B轴)    
    private float cpst1;   //轴1指令位置(一般为X轴)    
    private float cpst2;   //轴2指令位置(一般为Y轴)    
    private float cpst3;   //轴3指令位置(一般为Z轴)    
    private float cpst4;   //轴4指令位置(一般为A轴)    
    private float cpst5;   //轴5指令位置(一般为B轴)    
    private float load1;   //增加负载电流的项目，轴1    
    private float load2;   //轴2负载电流    
    private float load3;   //轴3负载电流    
    private float load4;   //轴4负载电流    
    private float load5;   //轴5负载电流    
    private short pd;      //运行程序编号    
    private String pn;     //程序名    
    private String ps;     //代码运行状态    
    private int pl;  	   //运行行
    private short pm;      //通道模态    
    private String time;   //时间戳    	
    

	public DataRun() {
		super();
	}
	public DataRun(String id, float cas, float ccs, float aload, float aspd1,
			float aspd2, float aspd3, float aspd4, float aspd5, float apst1,
			float apst2, float apst3, float apst4, float apst5, float cpst1,
			float cpst2, float cpst3, float cpst4, float cpst5, float load1,
			float load2, float load3, float load4, float load5, short pd,
			String pn, String ps, int pl, short pm, String time) {
		super();
		this.id = id;
		this.cas = cas;
		this.ccs = ccs;
		this.aload = aload;
		this.aspd1 = aspd1;
		this.aspd2 = aspd2;
		this.aspd3 = aspd3;
		this.aspd4 = aspd4;
		this.aspd5 = aspd5;
		this.apst1 = apst1;
		this.apst2 = apst2;
		this.apst3 = apst3;
		this.apst4 = apst4;
		this.apst5 = apst5;
		this.cpst1 = cpst1;
		this.cpst2 = cpst2;
		this.cpst3 = cpst3;
		this.cpst4 = cpst4;
		this.cpst5 = cpst5;
		this.load1 = load1;
		this.load2 = load2;
		this.load3 = load3;
		this.load4 = load4;
		this.load5 = load5;
		this.pd = pd;
		this.pn = pn;
		this.ps = ps;
		this.pl = pl;
		this.pm = pm;
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public float getCas() {
		return cas;
	}
	public void setCas(float cas) {
		this.cas = cas;
	}
	public float getCcs() {
		return ccs;
	}
	public void setCcs(float ccs) {
		this.ccs = ccs;
	}
	public float getAload() {
		return aload;
	}
	public void setAload(float aload) {
		this.aload = aload;
	}
	public float getAspd1() {
		return aspd1;
	}
	public void setAspd1(float aspd1) {
		this.aspd1 = aspd1;
	}
	public float getAspd2() {
		return aspd2;
	}
	public void setAspd2(float aspd2) {
		this.aspd2 = aspd2;
	}
	public float getAspd3() {
		return aspd3;
	}
	public void setAspd3(float aspd3) {
		this.aspd3 = aspd3;
	}
	public float getAspd4() {
		return aspd4;
	}
	public void setAspd4(float aspd4) {
		this.aspd4 = aspd4;
	}
	public float getAspd5() {
		return aspd5;
	}
	public void setAspd5(float aspd5) {
		this.aspd5 = aspd5;
	}
	public float getApst1() {
		return apst1;
	}
	public void setApst1(float apst1) {
		this.apst1 = apst1;
	}
	public float getApst2() {
		return apst2;
	}
	public void setApst2(float apst2) {
		this.apst2 = apst2;
	}
	public float getApst3() {
		return apst3;
	}
	public void setApst3(float apst3) {
		this.apst3 = apst3;
	}
	public float getApst4() {
		return apst4;
	}
	public void setApst4(float apst4) {
		this.apst4 = apst4;
	}
	public float getApst5() {
		return apst5;
	}
	public void setApst5(float apst5) {
		this.apst5 = apst5;
	}
	public float getCpst1() {
		return cpst1;
	}
	public void setCpst1(float cpst1) {
		this.cpst1 = cpst1;
	}
	public float getCpst2() {
		return cpst2;
	}
	public void setCpst2(float cpst2) {
		this.cpst2 = cpst2;
	}
	public float getCpst3() {
		return cpst3;
	}
	public void setCpst3(float cpst3) {
		this.cpst3 = cpst3;
	}
	public float getCpst4() {
		return cpst4;
	}
	public void setCpst4(float cpst4) {
		this.cpst4 = cpst4;
	}
	public float getCpst5() {
		return cpst5;
	}
	public void setCpst5(float cpst5) {
		this.cpst5 = cpst5;
	}
	public float getLoad1() {
		return load1;
	}
	public void setLoad1(float load1) {
		this.load1 = load1;
	}
	public float getLoad2() {
		return load2;
	}
	public void setLoad2(float load2) {
		this.load2 = load2;
	}
	public float getLoad3() {
		return load3;
	}
	public void setLoad3(float load3) {
		this.load3 = load3;
	}
	public float getLoad4() {
		return load4;
	}
	public void setLoad4(float load4) {
		this.load4 = load4;
	}
	public float getLoad5() {
		return load5;
	}
	public void setLoad5(float load5) {
		this.load5 = load5;
	}
	public short getPd() {
		return pd;
	}
	public void setPd(short pd) {
		this.pd = pd;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getPs() {
		return ps;
	}
	public void setPs(String ps) {
		this.ps = ps;
	}
	public int getPl() {
		return pl;
	}
	public void setPl(int pl) {
		this.pl = pl;
	}
	public short getPm() {
		return pm;
	}
	public void setPm(short pm) {
		this.pm = pm;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "DataRun [id=" + id + ", cas=" + cas + ", ccs=" + ccs
				+ ", aload=" + aload + ", aspd1=" + aspd1 + ", aspd2=" + aspd2
				+ ", aspd3=" + aspd3 + ", aspd4=" + aspd4 + ", aspd5=" + aspd5
				+ ", apst1=" + apst1 + ", apst2=" + apst2 + ", apst3=" + apst3
				+ ", apst4=" + apst4 + ", apst5=" + apst5 + ", cpst1=" + cpst1
				+ ", cpst2=" + cpst2 + ", cpst3=" + cpst3 + ", cpst4=" + cpst4
				+ ", cpst5=" + cpst5 + ", load1=" + load1 + ", load2=" + load2
				+ ", load3=" + load3 + ", load4=" + load4 + ", load5=" + load5
				+ ", pd=" + pd + ", pn=" + pn + ", ps=" + ps + ", pl=" + pl
				+ ", pm=" + pm + ", time=" + time + "]";
	}
	
	
}

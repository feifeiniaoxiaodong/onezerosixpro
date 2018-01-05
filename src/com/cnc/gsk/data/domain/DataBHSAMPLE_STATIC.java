package com.cnc.gsk.data.domain;

import java.util.Arrays;

/**
 * Created by wei on 2017/12/8.
 */

public class DataBHSAMPLE_STATIC {
    /*
     float cas;		// 主轴实际速度
    float ccs;		//主轴指令速度
    float aload;	//主轴负载电流
    float aspd[NET_AXIS_NUM];// 进给轴实际速度
    float apst[NET_AXIS_NUM];// 进给轴指令位置
    float cpst[NET_AXIS_NUM];// 进给轴实际位置
    float load[NET_AXIS_NUM];// 进给轴负载电流
    char progname[20];		//当前程序名
    char runstatus[60];		// G代码运行状态
    char almhead[16][20];// 报警消除时间
    char almtime[16][38];// 报警发生时间
    char alminfor[16][70];// 报警信息内容
    int runtime;// 累计运行时间
    int ontime;// 累计加工时间
    int gclinenum;	// G代码行号
    short prognum;//G代码程序编号
    short gcmode;	// G代码模态
    short almflag;// 异常信号
    short reserved;
    //12+4*32+20+60+16*20+16*38+16*70+20=2288
     */
    float cas; //主轴实际速度
    float ccs;		//主轴指令速度
    float aload;	//主轴负载电流

    float aspd[];//=new double[Mcronum.NET_AXIS_NUM];// 进给轴实际速度
    float apst[];//=new double[Mcronum.NET_AXIS_NUM];// 进给轴指令位置
    float cpst[];//=new double[Mcronum.NET_AXIS_NUM];// 进给轴实际位置
    float load[];//=new double[Mcronum.NET_AXIS_NUM];// 进给轴负载电流
    String progname;		//当前程序名
    String runstatus;		// G代码运行状态

    String almhead[];// 报警编号
    String almtime[];// 报警发生时间，读不出来显示“20NULL”
    String alminfor[];// 报警信息内容,中文报警信息

    int runtime;// 累计运行时间
    int ontime; // 累计加工时间
    int gclinenum;	// G代码行号
    short prognum;// G代码程序编号
    short gcmode;	// G代码模态
    short almflag;// 异常信号
    short reserved;

    @Override
    public String toString() {
        return "DataBHSAMPLE_STATIC{" +
                "cas=" + cas +
                ", ccs=" + ccs +
                ", aload=" + aload +
                ", aspd=" + Arrays.toString(aspd) +
                ", apst=" + Arrays.toString(apst) +
                ", cpst=" + Arrays.toString(cpst) +
                ", load=" + Arrays.toString(load) +
                ", progname='" + progname + '\'' +
                ", runstatus='" + runstatus + '\'' +
                ", almhead=" + Arrays.toString(almhead) +
                ", almtime=" + Arrays.toString(almtime) +
                ", alminfor=" + Arrays.toString(alminfor) +
                ", runtime=" + runtime +
                ", ontime=" + ontime +
                ", gclinenum=" + gclinenum +
                ", prognum=" + prognum +
                ", gcmode=" + gcmode +
                ", almflag=" + almflag +
                ", reserved=" + reserved +
                '}';
    }

    public DataBHSAMPLE_STATIC(){}
    public DataBHSAMPLE_STATIC(float cas, float ccs, float aload, float[] aspd, float[] apst, float[] cpst, float[] load, String progname, String runstatus, String[] almhead, String[] almtime, String[] alminfor, int runtime, int ontime, int gclinenum, short prognum, short gcmode, short almflag, short reserved) {
        this.cas = cas;
        this.ccs = ccs;
        this.aload = aload;
        this.aspd = aspd;
        this.apst = apst;
        this.cpst = cpst;
        this.load = load;
        this.progname = progname;
        this.runstatus = runstatus;
        this.almhead = almhead;
        this.almtime = almtime;
        this.alminfor = alminfor;
        this.runtime = runtime;
        this.ontime = ontime;
        this.gclinenum = gclinenum;
        this.prognum = prognum;
        this.gcmode = gcmode;
        this.almflag = almflag;
        this.reserved = reserved;
    }

    public float getCcs() {
        return ccs;
    }

    public void setCcs(float ccs) {
        this.ccs = ccs;
    }

    public float getCas() {
        return cas;
    }

    public void setCas(float cas) {
        this.cas = cas;
    }

    public float getAload() {
        return aload;
    }

    public void setAload(float aload) {
        this.aload = aload;
    }

    public float[] getAspd() {
        return aspd;
    }

    public void setAspd(float[] aspd) {
        this.aspd = aspd;
    }

    public float[] getApst() {
        return apst;
    }

    public void setApst(float[] apst) {
        this.apst = apst;
    }

    public float[] getCpst() {
        return cpst;
    }

    public void setCpst(float[] cpst) {
        this.cpst = cpst;
    }

    public float[] getLoad() {
        return load;
    }

    public void setLoad(float[] load) {
        this.load = load;
    }

    public String getProgname() {
        return progname;
    }

    public void setProgname(String progname) {
        this.progname = progname;
    }

    public String getRunstatus() {
        return runstatus;
    }

    public void setRunstatus(String runstatus) {
        this.runstatus = runstatus;
    }

    public String[] getAlmhead() {
        return almhead;
    }

    public void setAlmhead(String[] almhead) {
        this.almhead = almhead;
    }

    public String[] getAlmtime() {
        return almtime;
    }

    public void setAlmtime(String[] almtime) {
        this.almtime = almtime;
    }

    public String[] getAlminfor() {
        return alminfor;
    }

    public void setAlminfor(String[] alminfor) {
        this.alminfor = alminfor;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getOntime() {
        return ontime;
    }

    public void setOntime(int ontime) {
        this.ontime = ontime;
    }

    public int getGclinenum() {
        return gclinenum;
    }

    public void setGclinenum(int gclinenum) {
        this.gclinenum = gclinenum;
    }

    public short getPrognum() {
        return prognum;
    }

    public void setPrognum(short prognum) {
        this.prognum = prognum;
    }

    public short getGcmode() {
        return gcmode;
    }

    public void setGcmode(short gcmode) {
        this.gcmode = gcmode;
    }

    public short getAlmflag() {
        return almflag;
    }

    public void setAlmflag(short almflag) {
        this.almflag = almflag;
    }

    public short getReserved() {
        return reserved;
    }

    public void setReserved(short reserved) {
        this.reserved = reserved;
    }
}

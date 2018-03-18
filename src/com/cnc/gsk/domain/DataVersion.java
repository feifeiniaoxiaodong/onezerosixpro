package com.cnc.gsk.domain;

import  android.util.Log;

/**
 * Created by wei on 2017/4/11.
 */

/**
 * 广数版本信息结构�?
 * @author wei
 *
 */
public class DataVersion {

    public String TAG=new String("JNITest");

    private String sysVersion;//系统版本
    private String armVersion;//应用版本
    private String dspVersion;//插补版本
    private String FPGAVersion;//位控版本
    private String plcfileName;//梯图文件�?

    private String hardVersion;    //硬件版本
    
    private String softWareNumber;//软件序号

    private String hardWareNumber;//硬件序号  保留

    
    
    public DataVersion() {
		super();
	}

	public DataVersion(String tAG, String sysVersion, String armVersion,
			String dspVersion, String fPGAVersion, String plcfileName,
			String hardVersion, String softWareNumber, String hardWareNumber) {
		super();
		TAG = tAG;
		this.sysVersion = sysVersion;
		this.armVersion = armVersion;
		this.dspVersion = dspVersion;
		FPGAVersion = fPGAVersion;
		this.plcfileName = plcfileName;
		this.hardVersion = hardVersion;
		this.softWareNumber = softWareNumber;
		this.hardWareNumber = hardWareNumber;
	}

	
	
	@Override
	public String toString() {
		return "DataVersion [TAG=" + TAG + ", sysVersion=" + sysVersion
				+ ", armVersion=" + armVersion + ", dspVersion=" + dspVersion
				+ ", FPGAVersion=" + FPGAVersion + ", plcfileName="
				+ plcfileName + ", hardVersion=" + hardVersion
				+ ", softWareNumber=" + softWareNumber + ", hardWareNumber="
				+ hardWareNumber + "]";
	}

	public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getArmVersion() {
        return armVersion;
    }

    public void setArmVersion(String armVersion) {
        this.armVersion = armVersion;
    }

    public String getDspVersion() {
        return dspVersion;
    }

    public void setDspVersion(String dspVersion) {
        this.dspVersion = dspVersion;
    }

    public String getFPGAVersion() {
        return FPGAVersion;
    }

    public void setFPGAVersion(String FPGAVersion) {
        this.FPGAVersion = FPGAVersion;
    }

    public String getPlcfileName() {
        return plcfileName;
    }

    public void setPlcfileName(String plcfileName) {
        this.plcfileName = plcfileName;
    }

    public String getHardVersion() {
        return hardVersion;
    }

    public void setHardVersion(String hardVersion) {
        this.hardVersion = hardVersion;
    }

    public String getSoftWareNumber() {
        return softWareNumber;
    }

    public void setSoftWareNumber(String softWareNumber) {
        this.softWareNumber = softWareNumber;
    }

    public String getHardWareNumber() {
        return hardWareNumber;
    }

    public void setHardWareNumber(String hardWareNumber) {
        this.hardWareNumber = hardWareNumber;
    }

    //打印该类的信�?
    public void loginfor(){

        Log.i("JNITest" ,"系统版本:"+this.getSysVersion());
        Log.i("JNITest" ,"应用版本:"+this.getArmVersion());
        Log.i("JNITest" ,"插补版本:"+this.getDspVersion());
        Log.i("JNITest" ,"位控版本:"+this.getFPGAVersion());
        Log.i("JNITest" ,"梯图文件�?:"+this.getPlcfileName());

        Log.i("JNITest","硬件版本:"+this.getHardVersion());       //保留
        //Log.i("JNITest","硬件序号:"+this.getHardWareNumber());   //保留,没有内容
        Log.i("JNITest","软件序号�?"+this.getSoftWareNumber());  //保留
    }

}

package com.cnc.hangtian.domain;

/**
 * �������ذ汾��Ϣ
 * @author wei
 *
 */
public class VersionHangtian {
	private String systemVersion; //NC�ܰ汾��
	private String systemType; //����ϵͳ�ͺ�
	
	public VersionHangtian() {
		
	}
	
	
	public VersionHangtian(String systemVersion, String systemType) {
		super();
		this.systemVersion = systemVersion;
		this.systemType = systemType;
	}


	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

}

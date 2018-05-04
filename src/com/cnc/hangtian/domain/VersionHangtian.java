package com.cnc.hangtian.domain;

/**
 * 航天数控版本信息
 * @author wei
 *
 */
public class VersionHangtian {
	private String systemVersion; //NC总版本号
	private String systemType; //数控系统型号
	
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

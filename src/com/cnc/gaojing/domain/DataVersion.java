package com.cnc.gaojing.domain;

/**
 * 沈阳高精版本信息
 * @author wei
 *
 */
public class DataVersion {
	private String  cnc_system_model=null;
	private String  software_number=null; 
	private String  hardware_number=null;
	private String  system_kernel_number=null; 
		
	public String getCnc_system_model() {
		return cnc_system_model;
	}
	public void setCnc_system_model(String cnc_system_model) {
		this.cnc_system_model = cnc_system_model;
	}
	public String getSoftware_number() {
		return software_number;
	}
	public void setSoftware_number(String software_number) {
		this.software_number = software_number;
	}
	public String getHardware_number() {
		return hardware_number;
	}
	public void setHardware_number(String hardware_number) {
		this.hardware_number = hardware_number;
	}
	public String getSystem_kernel_number() {
		return system_kernel_number;
	}
	public void setSystem_kernel_number(String system_kernel_number) {
		this.system_kernel_number = system_kernel_number;
	}

}

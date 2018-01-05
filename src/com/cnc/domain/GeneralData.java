package com.cnc.domain;


public class GeneralData {
//	did	数据内容
//	1	注册信息数据
//	2	报警信息数据
//	3	运行信息数据
//	4	登录/登出信息数据
	private byte did;
//具体数据：与数据类型ID相对应的详细数据信息，使用Json格式编码。
	private String dt;   
//	Json：
//	{
//		did : 1,
//		dt : “{XX:\”xxx\”,…}”
//	}
//
//	编码方式：utf-8。
	public byte getDid() {
		return did;
	}
	public void setDid(byte did) {
		this.did = did;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}

}

package com.cnc.domain;


public class GeneralData {
//	did	��������
//	1	ע����Ϣ����
//	2	������Ϣ����
//	3	������Ϣ����
//	4	��¼/�ǳ���Ϣ����
	private byte did;
//�������ݣ�����������ID���Ӧ����ϸ������Ϣ��ʹ��Json��ʽ���롣
	private String dt;   
//	Json��
//	{
//		did : 1,
//		dt : ��{XX:\��xxx\��,��}��
//	}
//
//	���뷽ʽ��utf-8��
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

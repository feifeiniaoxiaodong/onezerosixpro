package bean;
/*
 * �ⲿͨ��
 * ������Ϣ
 */
public class ALARM_INFO {

	private String type;                //���ؽṹ���ͺ�
	private int alarmcode;				//��������
	private String alarmtime_occur;		//��������ʱ��
	private long alarmtime_remove;		//��������ʱ��
	private String alarmnum;			//��������
	private String timestamp;			//ʱ���yyyy-mm-dd hh:mm:ss.zzz
	private int []alarmcode_array = new int[32];     //���ڵı���������
	private String []alarmtime_occur_array = new String[32];		//���ڵı������뷢��ʱ����
	private String [][]warn;  			//���� ˵��
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getAlarmcode() {
		return alarmcode;
	}
	public void setAlarmcode(int alarmcode) {
		this.alarmcode = alarmcode;
	}
	public String getAlarmtime_occur() {
		return alarmtime_occur;
	}
	public void setAlarmtime_occur(String alarmtime_occur) {
		this.alarmtime_occur = alarmtime_occur;
	}
	public long getAlarmtime_remove() {
		return alarmtime_remove;
	}
	public void setAlarmtime_remove(long alarmtime_remove) {
		this.alarmtime_remove = alarmtime_remove;
	}
	public String getAlarmnum() {
		return alarmnum;
	}
	public void setAlarmnum(String alarmnum) {
		this.alarmnum = alarmnum;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public int[] getAlarmcode_array() {
		return alarmcode_array;
	}
	public void setAlarmcode_array(int[] alarmcode_array) {
		this.alarmcode_array = alarmcode_array;
	}
	public String[] getAlarmtime_occur_array() {
		return alarmtime_occur_array;
	}
	public void setAlarmtime_occur_array(String[] alarmtime_occur_array) {
		this.alarmtime_occur_array = alarmtime_occur_array;
	}
	public String[][] getWarn() {
		return warn;
	}
	public void setWarn(String[][] warn) {
		this.warn = warn;
	}
	
	
}

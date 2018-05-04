package bean;
/*
 * 外部通信
 * 轴信息   108
 */
public class AXIS_INFO {

	private String type;                //数控结构体型号
	private float []c_axis = new float[3];				//进给轴指令位置   单位：mm
	private float []a_axis_work = new float[3];		//进给轴实际位置（工件坐标） 单位：mm
	private float []a_axis_relative = new float[3]; 	//进给轴实际位置（相对坐标） 单位：mm
	private float []a_axis_machine = new float[3]; 	//进给轴实际位置（机床坐标） 单位：mm
	private float []a_axis_remainder = new float[3];	//进给轴实际位置（剩余坐标） 单位：mm
	private long  []a_f_value = new long[3];			//进给轴实际速度   单位：转/分
	private long c_s_value;				//主轴指令速度   单位：转/分
	private long a_s_value;			    //主轴实际速度   单位：转/分
	private String timestamp;			//时间戳yyyy-mm-dd hh:mm:ss.zzz
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public float[] getC_axis() {
		return c_axis;
	}
	public void setC_axis(float[] c_axis) {
		this.c_axis = c_axis;
	}
	public float[] getA_axis_work() {
		return a_axis_work;
	}
	public void setA_axis_work(float[] a_axis_work) {
		this.a_axis_work = a_axis_work;
	}
	public float[] getA_axis_relative() {
		return a_axis_relative;
	}
	public void setA_axis_relative(float[] a_axis_relative) {
		this.a_axis_relative = a_axis_relative;
	}
	public float[] getA_axis_machine() {
		return a_axis_machine;
	}
	public void setA_axis_machine(float[] a_axis_machine) {
		this.a_axis_machine = a_axis_machine;
	}
	public float[] getA_axis_remainder() {
		return a_axis_remainder;
	}
	public void setA_axis_remainder(float[] a_axis_remainder) {
		this.a_axis_remainder = a_axis_remainder;
	}
	public long[] getA_f_value() {
		return a_f_value;
	}
	public void setA_f_value(long[] a_f_value) {
		this.a_f_value = a_f_value;
	}
	public long getC_s_value() {
		return c_s_value;
	}
	public void setC_s_value(long c_s_value) {
		this.c_s_value = c_s_value;
	}
	public long getA_s_value() {
		return a_s_value;
	}
	public void setA_s_value(long a_s_value) {
		this.a_s_value = a_s_value;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}

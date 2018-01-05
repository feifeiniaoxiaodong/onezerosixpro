package com.cnc.domain;

/**
 * 登录和登出信息类
 * @author wei
 *
 */
public class DataLog {
	
	private String id;    	//数控系统ID（标识唯一NC）    
    private long ontime;    //累计运行时间    
    private long runtime;   //累计加工时间    
    private String time;    //登录时间
    
    public DataLog() {		
	}
        
	public DataLog(String id, long ontime, long runtime, String time) {
		super();
		this.id = id;
		this.ontime = ontime;
		this.runtime = runtime;
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getOntime() {
		return ontime;
	}
	public void setOntime(long ontime) {
		this.ontime = ontime;
	}
	public long getRuntime() {
		return runtime;
	}
	public void setRuntime(long runtime) {
		this.runtime = runtime;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "DataLog [id=" + id + ", ontime=" + ontime + ", runtime="
				+ runtime + ", time=" + time + "]";
	}
    
	
}

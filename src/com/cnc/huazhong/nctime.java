package com.cnc.huazhong;

public class nctime {
	public int second;
	public int minute;
	public int hour;
	public int hsecond;// a hundredth of a second or a thousandth of a second 
	
	public int day;
	public int month;
	public int year;
	public int wday;// Day of week, [0,6] (Sunday = 0)

	public nctime() {
	}
	public nctime(int year, int month, int day, int wday, int hour, int minute, int second) 
	{
		this.year = year;
		this.month = month;
		this.day =day;
		this.wday = wday;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.hsecond = 0;
	}
	public nctime(int year, int month, int day, int wday, int hour, int minute, int second,int hsecond) 
	{
		this.year = year;
		this.month = month;
		this.day =day;
		this.wday = wday;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.hsecond = hsecond;
	}
}
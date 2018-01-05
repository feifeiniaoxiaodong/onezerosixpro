package com.cnc.huazhong;

public class HncVarType {
	
	public static final int VAR_TYPE_AXIS = 0;	  // 轴变量 {Get(Bit32), Set(Bit32)}
	public static final int VAR_TYPE_CHANNEL = 1; // 通道变量 {Get(Bit32), Set(Bit32)}
	public static final int VAR_TYPE_SYSTEM = 2;  // 系统变量 {Get(Bit32), Set(Bit32)}
	public static final int VAR_TYPE_SYSTEM_F = 3;// 浮点类型的系统变量 {Get(fBit64), Set(fBit64)}
	public static final int VAR_TYPE_TOTAL = 4;
	
}

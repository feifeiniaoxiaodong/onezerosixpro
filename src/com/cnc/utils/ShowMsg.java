package com.cnc.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

public class ShowMsg {
	private static final String Tag = "hnctest";
	
	public static void showLog(String msg)
	{
		Log.i(Tag, msg);
	}
	
	public static void showLog(int msg)
	{
		Log.i(Tag, String.valueOf(msg));
	}
	//Toast֪ͨ
	public static void showToastMsg(Activity act,String s)
	{
		Toast.makeText(act.getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * ��activity������ʾ�ַ���
	 * @param act
	 * @param s
	 */
	public static void showOnScreen(Activity act,String s)
	{
		new AlertDialog.Builder(act).setMessage(s).show();
	}	
	
	

}

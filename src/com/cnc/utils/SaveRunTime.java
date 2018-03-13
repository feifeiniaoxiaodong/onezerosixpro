package com.cnc.utils;

import com.cnc.daq.MainActivity;
import com.cnc.daq.MyApplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//���濪��ʱ�������ʱ�䵽����
public class SaveRunTime {
	
	private static SharedPreferences preff=PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());

	public static  void  saveOnTime(String key,long n){
		long pretime =preff.getLong(key, 0);
		
		SharedPreferences.Editor  editor =preff.edit();
		editor.putLong(key, n+pretime); //��ʱ������ۼ�
		editor.apply();		
	}
	
	public static Long getOnTime(String key){
		return  preff.getLong(key, 0);		
	}
}

package com.cnc.utils;

import com.cnc.daq.MainActivity;
import com.cnc.daq.MyApplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveRunTime {
	
	static SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());

	public static  void  saveOnTime(String key,long n){
		long pretime =pref.getLong(key, 0);
		
		SharedPreferences.Editor  editor =pref.edit();
		editor.putLong(key, n+pretime);
		editor.apply();		
	}
	
	public static Long getOnTime(String key){
		return  pref.getLong(key, 0);		
	}
	

}

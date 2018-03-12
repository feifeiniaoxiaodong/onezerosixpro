package com.cnc.daq;

import android.app.Application;
import android.content.Context;

/**
 * 获取全局Context
 * @author wei
 *
 */
public class MyApplication extends Application {
	
	 private static Context context=null;
	 
	     @Override
	     public void onCreate() {
	          
	          super.onCreate();
	          context=getApplicationContext();
	     }
	     //获取全局的context
	     public static Context getContext() {
	         return context;
	     }

}

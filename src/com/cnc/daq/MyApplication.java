package com.cnc.daq;

import android.app.Application;
import android.content.Context;

/**
 * ��ȡȫ��Context
 * @author wei
 *
 */
public class MyApplication extends Application {
	
	 private static Context context=null;
	 
	     @Override
	     public void onCreate() {
	          // TODO Auto-generated method stub
	          super.onCreate();
	          context=getApplicationContext();
	     }
	     //��ȡȫ�ֵ�context
	     public static Context getContext() {
	         return context;
	     }

}

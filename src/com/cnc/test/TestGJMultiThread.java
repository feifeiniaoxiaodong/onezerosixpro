package com.cnc.test;

import org.junit.Test;

import com.cnc.gaojing.GJDataCollectThread;

public class TestGJMultiThread {
	
	
	public static void test(){
		
	 new Thread(new Runnable(){
		 GJDataCollectThread gjDataCollectThread=null;
		@Override
		public void run() {
//			new Thread( new GJDataCollectThread("192.168.188.132")).start();
//			new Thread( new GJDataCollectThread("192.168.188.131")).start();	
		}
		
	}).start();

	}
	
	

}

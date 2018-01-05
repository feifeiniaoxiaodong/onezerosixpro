package com.cnc.daq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SplashActivity extends Activity {

	static {  
        // 加载华中动态库  
        System.loadLibrary("hncnet");  
        System.loadLibrary("hzHncAPI");  
        //加载广数动态库
        System.loadLibrary("gsknetw-lib");     
    }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashactivity);
		
		new Thread(new Runnable(){
			
			
			@Override
			public void run() {	
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
					
				Intent intent=new Intent(SplashActivity.this , DaqActivity.class );
				startActivity(intent);
				SplashActivity.this.finish();
		
				
			} //end run()

		}).start();

	}
}








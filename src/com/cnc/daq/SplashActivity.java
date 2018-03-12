package com.cnc.daq;

import com.cnc.mainservice.DelMsgServie;
import com.cnc.net.datasend.DataTransmitThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * ������������
 * ����ʼ�������ض�̬�⣬��������
 * @author wei
 *
 */
public class SplashActivity extends Activity {
	final String TAG="SplashActivity";

	static {  
        // ���ػ��ж�̬��  
        System.loadLibrary("hncnet");  
        System.loadLibrary("hzHncAPI");  
        //���ع�����̬��
        System.loadLibrary("gsknetw-lib");     
    }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashactivity);
		//��������
		Intent intentservice=new Intent(SplashActivity.this ,DelMsgServie.class);
		startService(intentservice);
		
		//����������ʱ��֮�������
		new Thread(new Runnable(){
			@Override
			public void run() {	
				try {
					Thread.sleep(1000*5);
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
				//�������	
//				Intent intent=new Intent(SplashActivity.this , DaqActivity.class );
				Intent intent=new Intent(SplashActivity.this , MainActivity.class );
				startActivity(intent);
				SplashActivity.this.finish();
						
			} //end run()
		}).start();

	}
}








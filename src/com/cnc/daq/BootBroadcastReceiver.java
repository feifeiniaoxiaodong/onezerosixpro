package com.cnc.daq;

import com.cnc.daqnew.DataService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * �������㲥
 * ������������
 * @author wei
 *
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		
//		Intent service = new Intent(context, daqService.class);//��ʽ/��ʽ
//		service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startService(service);//Intent�������(Service)
		
		/*//�ݲ�������������
		Intent service = new Intent(context, DataService.class);//��ʽ/��ʽ
		context.startService(service);//Intent�������(Service)
*/	
		Intent splashactivity = new Intent(context, SplashActivity.class);//��ʽ/��ʽ
		splashactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(splashactivity);
	}
	
	
}

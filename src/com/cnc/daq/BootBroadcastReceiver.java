package com.cnc.daq;

import com.cnc.daqnew.DataService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 处理开机广播
 * 开启启动服务
 * @author wei
 *
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		
//		Intent service = new Intent(context, daqService.class);//显式/隐式
//		service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startService(service);//Intent激活组件(Service)
		
		/*//暂不开启开机启动
		Intent service = new Intent(context, DataService.class);//显式/隐式
		context.startService(service);//Intent激活组件(Service)
*/	
		Intent splashactivity = new Intent(context, SplashActivity.class);//显式/隐式
		splashactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(splashactivity);
	}
	
	
}

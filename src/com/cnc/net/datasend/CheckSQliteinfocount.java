package com.cnc.net.datasend;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.cnc.broadcast.BroadcastAction;
import com.cnc.broadcast.BroadcastType;
import com.cnc.daq.DaqData;
import com.cnc.daq.MyApplication;
import com.cnc.db.service.DBService;

/**
 * 专门开启一个线程检查本地数据库中运行信息条数
 * @author wei
 *
 */
public class CheckSQliteinfocount implements Runnable{

	
	@Override
	public void run() {
		
		while(true){
			
			long n=DBService.getInstanceDBService().getCountRunInfo();
			DaqData.setCacheinfocount(n);
			
			sendBroadCast(BroadcastAction.SendThread_PARAMALL,
					BroadcastType.MSGLOCAL,"本地缓存数据:"+n+"条");						
			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		
		}		
	}
	
	
	//发送本地广播消息	
		private void sendBroadCast(String action,String ... args){
			Intent intent =new Intent();
			intent.setAction(action);
			if(args.length%2 !=0 ) return ;
			for(int i=0;i<args.length;i+=2){
				intent.putExtra(args[i], args[i+1]);
			}
			LocalBroadcastManager.getInstance(MyApplication.getContext())
								 .sendBroadcast(intent);	
		}

}

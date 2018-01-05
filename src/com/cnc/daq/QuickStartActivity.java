package com.cnc.daq;

import java.util.ArrayList;
import java.util.List;

import ui.tabview.library.TabView;
import ui.tabview.library.TabViewChild;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuickStartActivity extends FragmentActivity {
	TabView tabView;
	
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		
		super.onCreate(arg0);
		setContentView(R.layout.quickstartactivity);
		
		tabView=(TabView) findViewById(R.id.tabView);		
		List<TabViewChild> tabViewChildList=new ArrayList<>();
		
		TabViewChild tabViewchild01=new TabViewChild(R.drawable.tab01_sel, R.drawable.tab01_unsel,"��ҳ",FragmentMainPage.newInstance("��ҳ"));
		TabViewChild tabViewchild02=new TabViewChild(R.drawable.tab02_sel, R.drawable.tab02_unsel,"����",FragmentMainPage.newInstance("����"));
		tabViewChildList.add(tabViewchild01);
		tabViewChildList.add(tabViewchild02);
		
		tabView.setTabViewDefaultPosition(0);
		tabView.setTabViewChild(tabViewChildList, getSupportFragmentManager());		
		tabView.setOnTabChildClickListener(new  TabView.OnTabChildClickListener() {			
			@Override
			public void onTabChildClick(int position, ImageView imageView,
					TextView textView) {
				Toast.makeText(getApplicationContext(), "position:"+position, Toast.LENGTH_SHORT).show();
			}
		});
		
		//����ϵͳ�Դ�������
		ActionBar actionbar=getActionBar();
		if(actionbar!=null){
			actionbar.hide();
		}
		
	}
	

}




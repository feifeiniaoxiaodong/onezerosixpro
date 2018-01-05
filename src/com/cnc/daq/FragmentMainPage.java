package com.cnc.daq;


import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentMainPage extends Fragment implements View.OnClickListener {
	  TextView textView;
	  
	  public static FragmentMainPage newInstance(String text){
		FragmentMainPage fragmentmainpage=new FragmentMainPage();
		Bundle bundle=new Bundle();
		bundle.putString("text",text);		
		fragmentmainpage.setArguments(bundle);		
		return fragmentmainpage;
	 }

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.fragmentmain, container, false);
		
		textView=(TextView)view.findViewById(R.id.textview);
		textView.setOnClickListener(this);
		textView.setText(getArguments().getString("text"));		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.textview:
			textView.setText("first page!");
			break;
		default:{}		
		}
		
	}
	

}

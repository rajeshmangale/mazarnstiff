package com.rns.tiffeat.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;


public class TabActivity extends Fragment
{
	private static final String TAG = "TabHostActivity";
	TabHost tabHost;


	

		
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.activity_tab,
				container, false);

		
		
		//tabHost = (TabHost)view.findViewById(R.id.tabhost);
		// Get TabHost Refference
		tabHost = new FragmentTabHost(getActivity());
		//tabHost.setup(getActivity(), getChildFragmentManager(),R.id.container_body);

		// Set TabChangeListener called when tab changed
		// tabHost.setOnTabChangedListener(this);

		TabHost.TabSpec spec;
		Intent intent;


		/************* TAB1 ************/
		// Create  Intents to launch an Activity for the tab (to be reused)
		tabHost.getTabWidget().setDividerDrawable(null);
		intent = new Intent();


		spec = tabHost.newTabSpec("Afternoon").setIndicator("LUNCH")
				.setContent(intent);

		//Add intent to tab
		tabHost.addTab(spec);

		/************* TAB2 ************/
		//intent = new Intent().setClass(this, ScheduledUser.class);

		spec = tabHost.newTabSpec("Night").setIndicator("DINNER")
				.setContent(intent);  
		tabHost.addTab(spec);

		// Set Tab1 as Default tab   
		tabHost.getTabWidget().setCurrentTab(0);
		tabHost.getTabWidget().setDividerDrawable(null);
		return view;

	}
	
	
	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	    tabHost = null;
	}
}

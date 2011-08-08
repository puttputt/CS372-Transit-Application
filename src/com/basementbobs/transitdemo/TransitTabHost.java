package com.basementbobs.transitdemo;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TransitTabHost extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabhost);

	    TabHost tabHost = getTabHost();
	    TabHost.TabSpec spec;
	    Intent intent;

	    intent = new Intent().setClass(this, RoutePlannerActivity.class);	    
	    spec = tabHost.newTabSpec("map").setIndicator("Route Planner", null)
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, TransitMapActivity.class);
	    spec = tabHost.newTabSpec("map").setIndicator("Live Map", null)
        	.setContent(intent);
	    tabHost.addTab(spec);


	    tabHost.setCurrentTab(0);
	    
        // set each tab's height
        for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
        	tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 45;
        }
	}
	
}
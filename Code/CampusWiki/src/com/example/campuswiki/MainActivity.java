package com.example.campuswiki;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.preference.PreferenceManager;

public class MainActivity extends Activity {
	final int RQS_GooglePlayServices = 1;
	
	boolean showTut=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyPagerAdapter adapter = new MyPagerAdapter(this);
        ViewPager myPager = (ViewPager) findViewById(R.id.home_pannels_pager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);
        
        Intent intent = new Intent();
		intent.setClass(this, Demo1.class);				
		//startActivity(intent);
		
		SharedPreferences setNoti = PreferenceManager.getDefaultSharedPreferences(this);
		// SharedPref tutorial
		showTut = setNoti.getBoolean("tutorial", true);
		showTut=true;
		if (showTut == true) {
			showActivityOverlay(this);
		}

		
		//showDetails(init_id);
          
    }
    
    @Override
    public void onBackPressed() {
    		ViewPager myPager = (ViewPager) findViewById(R.id.home_pannels_pager);
    		if(myPager.getCurrentItem()==0){
    			super.onBackPressed(); 
    		}else
    			myPager.setCurrentItem(0);
    }

	private void showActivityOverlay(final Activity mContext) {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.activity_demo1);
		LinearLayout layout = (LinearLayout) dialog
		.findViewById(R.id.llOverlay_activity);
		layout.setBackgroundColor(Color.TRANSPARENT);
		layout.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View arg0) {
		// Get SharedPrefs
			PreferenceManager.setDefaultValues(mContext, R.xml.prefs, true);
			SharedPreferences setNoti = PreferenceManager
			.getDefaultSharedPreferences(mContext);
			setNoti.edit().putBoolean("tutorial", false).commit();
			showTut = false;
			dialog.dismiss();

		}

		});

		dialog.show();

		}
    
    
}

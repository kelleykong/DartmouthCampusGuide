package com.example.campuswiki;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.example.campuswiki.MyPagerAdapter.ActivityEntriesCursorAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;



public class MapActivity extends Activity 
	implements OnMapClickListener, OnMapLongClickListener, OnMarkerClickListener{
	private PlacesDataSource datasource;
	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;
	
	boolean showTut=true;
	
	Location myLocation;
	TextView tvLocInfo;
	
	boolean markerClicked;
	PolylineOptions rectOptions;
	Polyline polyline;
	
	//Location Array List
	ArrayList<LatLng> lllist;
	
	//K-means
	ArrayList<Cluster> result;
	int k;
	double KMEANS_ERROR=0.001;

	static final LatLng COVENTRY = new LatLng(43.704441, -72.288693);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		//tvLocInfo = (TextView)findViewById(R.id.locinfo);
		
		FragmentManager myFragmentManager = getFragmentManager();
		MapFragment myMapFragment 
			= (MapFragment)myFragmentManager.findFragmentById(R.id.map);
		myMap = myMapFragment.getMap();
		
		myMap.setMyLocationEnabled(true);
		
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		myMap.setOnMapClickListener(this);
		myMap.setOnMapLongClickListener(this);
		myMap.setOnMarkerClickListener(this);
		
		
		/*LocationManager locationManager;
	    String svcName= Context.LOCATION_SERVICE;
	    locationManager = (LocationManager)getSystemService(svcName);

	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setSpeedRequired(false);
	    criteria.setCostAllowed(true);
	    String provider = locationManager.getBestProvider(criteria, true);

	    Location l = locationManager.getLastKnownLocation(provider);
	    
	    LatLng latlng=fromLocationToLatLng(l);
		*/
	
		//Move the camera instantly to the best city in the world! with a zoom of 15.
		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(COVENTRY, 5));
		myMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null); 
		
		//myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,
		//		17));

		markerClicked = false;
		
		lllist=new ArrayList<LatLng>();
		result=new ArrayList<Cluster>();
		
		k=4;
		
		SharedPreferences setNoti = PreferenceManager.getDefaultSharedPreferences(this);
		// SharedPref tutorial
		showTut = setNoti.getBoolean("tutorial", true);
		showTut=true;
		if (showTut == true) {
			showActivityOverlay(this);
		}
		
		
		
	
		
	}
	
	private void showActivityOverlay(final Activity mContext) {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.activity_demo2);
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
	
	
	public class Cluster{
		LatLng center;
		ArrayList<LatLng> members;
		double distance;
		public Cluster(){
			members=new ArrayList<LatLng>();
			distance=0;
		}
		
		
	}
	
	public static LatLng fromLocationToLatLng(Location location){
		return new LatLng(location.getLatitude(), location.getLongitude());
		
	}
	
	public double computeDistance(LatLng a,LatLng b){
		return 
				Math.sqrt((a.latitude-b.latitude)*(a.latitude-b.latitude)+(a.longitude-b.longitude)*(a.longitude-b.longitude));
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		
		if (resultCode == ConnectionResult.SUCCESS){
			//Toast.makeText(getApplicationContext(), 
			//		"isGooglePlayServicesAvailable SUCCESS", 
			//		Toast.LENGTH_LONG).show();
		}else{
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
		}
		
		
		datasource = PlacesDataSource.getInstance(this);
		Cursor cursor = datasource.createCursor(new String[] {
				MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME,
				MySQLiteHelper.COLUMN_LATLNG}, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			byte[] byteTrack = cursor.getBlob(2);
			if(byteTrack!= null && byteTrack.length >0){
				
		    		ByteBuffer byteBuffer = ByteBuffer.wrap(byteTrack);  		  		
		    		myMap.addMarker(new MarkerOptions().position(new LatLng(byteBuffer.getDouble(),byteBuffer.getDouble())).icon(BitmapDescriptorFactory.defaultMarker(
		   			     BitmapDescriptorFactory.HUE_GREEN)));
			}
			
			
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
	}

	@Override
	public void onMapClick(LatLng point) {
	/*	tvLocInfo.setText(point.toString());
		myMap.animateCamera(CameraUpdateFactory.newLatLng(point));
		markerClicked = false;*/
	}
	
	public void onClearClick(View v){
		myMap.clear();
		lllist.clear();
	}
	
	public void onGenerateClick(View v){
		result.clear();
		//Init
		for(int i=0;i<k;i++){
			Cluster c= new Cluster();
			c.center=lllist.get(i);
			result.add(c);
		}
		//Iteration
		double error=Double.MAX_VALUE;
		//while(error>KMEANS_ERROR){
		for(int m=0;m<10;m++){
			error=0;
			//Clear original members
			for(Cluster c:result){
				c.members.clear();
				c.distance=0;
			}
			
			//Classify
			for(LatLng l:lllist){//For every 
				double local_error=Double.MAX_VALUE;
				int min_idx=0;
				int idx=0;
				for(Cluster c:result){
					if(computeDistance(c.center,l)<local_error){
						min_idx=idx;
						local_error=computeDistance(c.center,l);
					}
					idx++;
				}
				result.get(min_idx).members.add(l);
				result.get(min_idx).distance=Math.max(result.get(min_idx).distance, local_error);
				error+=local_error;
				Log.d("GPS","The total error is"+error);
			}
			
			Log.d("GPS","The total error is"+error);
			
			//re-compute the centers
			for(Cluster c:result){
				int size=c.members.size();
				double sum_lng=0;
				double sum_lat=0;
				for(LatLng l:c.members){
					sum_lng+=l.longitude;
					sum_lat+=l.latitude;
				}
				c.center= new LatLng(sum_lat/(double)size, sum_lng/(double)size);
			}
			
		}
		
		for(Cluster c:result){
			// Instantiates a new CircleOptions object and defines the center and radius
			CircleOptions circleOptions = new CircleOptions().center(c.center).radius(100000*c.distance).strokeColor(Color.GRAY).strokeWidth(3); // In meters
	
			// Get back the mutable Circle
			myMap.addCircle(circleOptions);
		}
		
		   double lat1 = 40.74560;
		   double lon1 = -73.94622000000001;
		   double lat2 = 46.59122000000001;
		   double lon2 = -112.004230;

		   String url = "http://maps.googleapis.com/maps/api/directions/json?";
		   List<NameValuePair> params = new LinkedList<NameValuePair>();
		   params.add(new BasicNameValuePair("origin", lat1 + "," + lon1));
		   params.add(new BasicNameValuePair("destination", lat2 + "," + lon2));
		   params.add(new BasicNameValuePair("sensor", "false"));
		   String paramString = URLEncodedUtils.format(params, "utf-8");
		   url += paramString;
		   HttpGet get = new HttpGet(url);
		
	}

	@Override
	public void onMapLongClick(LatLng point) {
		/*tvLocInfo.setText("New marker added@" + point.toString());
		MarkerOptions mk=new MarkerOptions().position(point).title(point.toString());
		myMap.addMarker(mk);
		
		lllist.add(point);
		markerClicked = false;*/
		Intent intent = new Intent();
		intent.putExtra("lat",point.latitude);
		intent.putExtra("lng", point.longitude);
		intent.setClass(this, ManualInputPlace.class);
		startActivity(intent);
		
		
		
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		LatLng latlng = marker.getPosition();
		List<Place> places = datasource.getAllEntries();
		for(Place place:places){
			if(place == null || place.getLatLng() == null) continue;
			if(Math.abs(place.getLatLng().latitude - latlng.latitude) <= 0.0001 
					&& Math.abs(place.getLatLng().longitude - latlng.longitude) <0.0001){
				MyPagerAdapter.showDetails(place.getId());
				ViewPager myPager = (ViewPager) MyPagerAdapter.mContext.findViewById(R.id.home_pannels_pager);
				myPager.setCurrentItem(1);
				finish();
			}
		}
		
		
		
		/*if(markerClicked){
			
			if(polyline != null){
 				polyline.remove();
				polyline = null;
			}
			
			rectOptions.add(marker.getPosition());
			rectOptions.color(Color.RED);
			polyline = myMap.addPolyline(rectOptions);
		}else{
			if(polyline != null){
				polyline.remove();
				polyline = null;
			}
			
			rectOptions = new PolylineOptions().add(marker.getPosition());
			markerClicked = true;
		}*/
		
		return true;
	}

}

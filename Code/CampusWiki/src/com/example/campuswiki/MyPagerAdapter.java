package com.example.campuswiki;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MyPagerAdapter extends PagerAdapter implements AdapterView.OnItemClickListener {
	
	static private Place place;
	static private PlacesDataSource datasource;
	public static  Activity mContext;
	private  Button btnAdd;
	private  Button btnMap;
	private  Button btnSync;
	private  Button btnUpload;
	
	public ArrayList<String> ids;

	static private EditText name;
	static private ImageView imageview;
	static private EditText ranking;
	static private EditText desc;	
	static private MediaPlayer myPlayer;
	static private Button  plays;
	public static ActivityEntriesCursorAdapter mAdapter;
	
	static private GoogleMap myMap;
	static public Marker whereAmI;
	static final LatLng COVENTRY = new LatLng(43.704441, -72.288693);
	
    public MyPagerAdapter(Activity mContext){
    	this.mContext = mContext;
    	ids=new ArrayList<String>();
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    	StrictMode.setThreadPolicy(policy); 
    }
	
    // State number of pages
    public int getCount() {
        return 2;
    }

    // Set each screen's content
    @Override
    public Object instantiateItem(View container, int position) {
    	View view = null;
    	LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(position == 0){
        		view = inflater.inflate(R.layout.list, null);       		
        }
        if(position == 1){
        		view = inflater.inflate(R.layout.detail, null);
            
        }
        
        //View view2 = inflater.inflate(R.layout.demo, null);
        ((ViewPager) container).addView(view, 0);
        //((ViewPager) container).addView(view2, 1);
		return view;	

        
        /*Context context = container.getContext();
        LinearLayout layout = new LinearLayout(context);
        // Add elements
        TextView textItem = new TextView(context);

        buttonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.phone");
               // myFancyMethod(v);
            }
        });


        switch (position) {
        case 0:
           textItem.setText("First Screen");
           break;
        case 1:
           textItem.setText("Second Screen");
           break;
        case 2:
            textItem.setText("Third Screen");
            break;


        case 3:
        	textItem.setText("Fourth Screen");
        break;
        case 4:
        	textItem.setText("Fifth Screen");
        break;
    }
        
    layout.addView(textItem);
    ((ViewPager) container).addView(layout, 0); // This is the line I added
    return layout;*/
}
    
    public void finishUpdate (ViewGroup container) {
		datasource = PlacesDataSource.getInstance(mContext);
		Cursor cursor = datasource.createCursor(new String[] {
				MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME,
				MySQLiteHelper.COLUMN_RANKING,MySQLiteHelper.COLUMN_BITMAP}, null);
		mAdapter = new ActivityEntriesCursorAdapter(
				mContext, cursor);
		ListView lv = (ListView)mContext.findViewById(R.id.list);
		if(lv!=null){
			lv.setAdapter(mAdapter);
			lv.setOnItemClickListener(this);
		}
		
		
		
		btnAdd = (Button) mContext.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, ManualInputPlace.class);				
				mContext.startActivity(intent);
			}
		});
		
		
		btnMap = (Button) mContext.findViewById(R.id.btnMap);
		btnMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				
				intent.setClass(mContext, MapActivity.class);				
				mContext.startActivity(intent);
			}
		});
		
		btnSync = (Button) mContext.findViewById(R.id.btnSync);
		btnSync.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				refreshPostHistory();
			}
		});
		
		/*btnUpload = (Button) mContext.findViewById(R.id.btnUpload);
		btnUpload.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				upload();
			}
		});*/
    }
    
    public void upload(){
    	
    }
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    		showDetails(id);
    		ViewPager myPager = (ViewPager) mContext.findViewById(R.id.home_pannels_pager);
    		myPager.setCurrentItem(1);
    }
    
	public  static void showDetails(long id){
		place = datasource.getEntry(id);
		if(place.getName() == null) return;

		name = (EditText) mContext.findViewById(R.id.name);
		name.setText(place.getName());
		
		//ranking = (EditText)mContext.findViewById(R.id.ranking);
		//ranking.setText(String.valueOf(place.getRanking()));

		desc = (EditText) mContext.findViewById(R.id.desc);

		desc.setText(place.getDesc());
		
		FragmentManager myFragmentManager = mContext.getFragmentManager();
		MapFragment myMapFragment 
			= (MapFragment)myFragmentManager.findFragmentById(R.id.map_show);
		myMap = myMapFragment.getMap();
		
		myMap.setMyLocationEnabled(true);
		
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		//Move the camera instantly to the best city in the world! with a zoom of 15.
		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(COVENTRY, 5));
		myMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		myMap.clear();
		if(place.getLatLng()!=null)
			whereAmI=myMap.addMarker(new MarkerOptions().position(place.getLatLng()).icon(BitmapDescriptorFactory.defaultMarker(
			     BitmapDescriptorFactory.HUE_GREEN)));
		
		RatingBar ratingBar1=(RatingBar)mContext.findViewById(R.id.ratingBar1);
		ratingBar1.setRating(place.getRanking());
		
		imageview = (ImageView)mContext.findViewById(R.id.bitmap);
		imageview.setClickable(true); 
		String bitmapurl =  place.getBitmap();
		try{
			URL url = new URL(place.getBitmap());
			Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			imageview.setImageBitmap(bmp);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		imageview.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        
		    	Dialog dialog = new Dialog(mContext);
		    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    	dialog.setContentView(R.layout.thumb);

		    	ImageView image = (ImageView) dialog.findViewById(R.id.thumbnail);

		    	// !!! Do here setBackground() instead of setImageDrawable() !!! //
		    	try{
					//FileInputStream fis = new FileInputStream(place.getBitmap());
					//bitmap=BitmapFactory.decodeStream(fis);
					URL url = new URL(place.getBitmap());
					Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
					//imageview.setImageBitmap(bmp);
					image.setImageBitmap(bmp);
				}catch(Exception e){
					e.printStackTrace();
				}
		    
		    	// Without this line there is a very small border around the image (1px)
		    	// In my opinion it looks much better without it, so the choice is up to you.
		    	dialog.getWindow().setBackgroundDrawable(null);
		    	dialog.show();
		        
		    }
		});
		
		
		new Thread(new Runnable() {
            public void run() {
                 /*runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("uploading started.....");
                        }
                    });*/                      
               
                 //uploadFile(voicePath);
                 //ArrayList<String> temp = new ArrayList<String>(Arrays.asList(voicePath.split("/")));
                 //voicePath_postfix=temp.get(temp.size()-1);
            	try{
        			
        			//FileInputStream fis = new FileInputStream(place.getBitmap());
        			
        			//fis=(FileInputStream) new URL("http://google.com/").openStream();
        			Log.d("The pics path", place.getBitmap());
        			//URL url = new URL();
        			
        			//InputStream is = (InputStream) new URL("www.cs.dartmouth.edu/~tyun/cs65/uploads/tmp_1401219063813.jpg").getContent();
        			
        			/*String image_url = "http://www.cs.dartmouth.edu/~tyun/cs65/uploads/tmp_1401219063813.jpg";
        			/*URL url = new URL(urlStr);
        			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        			url = uri.toURL();
        			
        			InputStream is = (InputStream) url.getContent();
        			
        			
        			Bitmap bmp = BitmapFactory.decodeStream(is);
        			imageview.setImageBitmap(bmp);*/
        			
        			// Imageview to show
        	        //ImageView image = (ImageView) findViewById(R.id.image);
        	         
        	        // Image url
        	        //String image_url = "http://api.androidhive.info/images/sample.jpg";
        	         
        	        // ImageLoader class instance
        	       // ImageLoader imgLoader = new ImageLoader(mContext);
        	         
        	        // whenever you want to load an image from url
        	        // call DisplayImage function
        	        // url - image url to load
        	        // loader - loader image, will be displayed before getting image
        	        // image - ImageView 
        	       // imgLoader.DisplayImage(image_url, 0, imageview);
        	        
        			String bitmapurl =  place.getBitmap();
        			URL url = new URL(bitmapurl);
        			Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        		//	imageview.setImageBitmap(bmp);
        			
        			//imageview.setImageBitmap(BitmapFactory.decodeStream(fis));
        		}catch(Exception e){
        			e.printStackTrace();
        		}
                                          
            }
          }).start();

		
		if(place.getVoice() != null){ 
			//EditText filename = (EditText)mContext.findViewById(R.id.voice_file);
			//filename.setText(place.getVoice().substring(place.getVoice().lastIndexOf('/')+1));
			plays = (Button) mContext.findViewById(R.id.voice_play);
			//Button stop = (Button) getActivity().findViewById(R.id.voice_stop);
			myPlayer = new MediaPlayer();
			
			MediaController mcontroller;
			mcontroller=(MediaController)mContext.findViewById(R.id.mediaController1);
			
			
			
			plays.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						myPlayer.reset();
						Log.d("CampusGuide",place.getVoice());
						myPlayer.setDataSource(place.getVoice());
						if (plays.getText().toString().equalsIgnoreCase("play")) {

							myPlayer.prepare();
							myPlayer.start();
							plays.setText("stop");
						} else if(plays.getText().toString().equalsIgnoreCase("stop")){
								myPlayer.stop();
								plays.setText("play");								
							
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	
@Override
public void destroyItem(View arg0, int arg1, Object arg2) {
    ((ViewPager) arg0).removeView((View) arg2);
}
@Override
public boolean isViewFromObject(View arg0, Object arg1) {
    return arg0 == ((View) arg1);
}
@Override
public Parcelable saveState() {
    return null;
}

public void onRefreshClicked(View v) throws StreamCorruptedException, ClassNotFoundException, IOException {
	Log.d("Lab6", "The function being called in the on onRefreshClicked");
	
	
	//Get a list of all IDs and whether they should be delete
	refreshPostHistory();
	
}

private void refreshPostHistory() {
	new AsyncTask<Void, Void, String>() {

		@Override
		protected String doInBackground(Void... arg0) {
			String url = mContext.getString(R.string.server_addr)
					+ "/get_history.do";
			String res = "";
			Map<String, Object> params = new HashMap<String, Object>();
			try {
				res = ServerUtilities.post(url, params);
				//Log.d("Lab6",res);
				
				
				
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			/*if (!res.equals("")) {
				Log.d("Lab6",res);
				ArrayList<String> temp = new ArrayList<String>(Arrays.asList(res.split("\n")));
				ids.clear();
				for(String s:temp)
					ids.add(s);
			}*/

			return res;
		}

		@Override
		protected void onPostExecute(String res) {
			ids.clear();
			JSONObject jsonObj = null;
			if (!res.equals("")) {
				//Log.d("Lab6",res);
				ArrayList<String> temp = new ArrayList<String>(Arrays.asList(res.split("\n")));
				for(String s:temp)
					ids.add(s);
				
				datasource.deleteAllEntries();
			
				try{
						JSONArray array = new JSONArray(res);

			       	   int size = array.length();
			        	 
			   		
		        	   for(int i=0;i<size;i++){
		        		   Place myplace = new Place();
		        		   jsonObj = array.getJSONObject(i);
		        		   //place.setId(jsonObj.getLong(MySQLiteHelper.COLUMN_ID));
		        		   myplace.setName(jsonObj.getString(MySQLiteHelper.COLUMN_NAME));
		        		   myplace.setLatLng(new LatLng(jsonObj.getDouble(MySQLiteHelper.COLUMN_LAT),jsonObj.getDouble(MySQLiteHelper.COLUMN_LNG)));
		        		   if(jsonObj.has(MySQLiteHelper.COLUMN_RANKING)){
		        			   myplace.setRanking(jsonObj.getInt(MySQLiteHelper.COLUMN_RANKING));
		        		   }
		        		   
		        		   if(jsonObj.has(MySQLiteHelper.COLUMN_DESC)){
		        			   myplace.setDesc((String)jsonObj.get(MySQLiteHelper.COLUMN_DESC));
		        		   }
		        		   if(jsonObj.has(MySQLiteHelper.COLUMN_BITMAP)){
		        			   myplace.setBitmap(jsonObj.getString(MySQLiteHelper.COLUMN_BITMAP));
		        		   }
		        		   if(jsonObj.has(MySQLiteHelper.COLUMN_VOICE)){
		        			   myplace.setVoice( jsonObj.getString(MySQLiteHelper.COLUMN_VOICE));
		        		   }
		        		   
			     	
		        		   
		        		   datasource.createEntry(myplace);
			     	
		        	   }
			     	//name = jsonObj.getString(PostEntity.FIELD_NAME_NAME);
			        }catch(Exception e){
			        	e.printStackTrace();
			        	
			        }
				mAdapter.notifyDataSetChanged();
				
			}
				
			/*try {
					//Log.d("Lab6","This function being called");
					//updateEntries();
				} catch (StreamCorruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			*/
		}

	}.execute();
}


public void updateEntries_not_using() throws StreamCorruptedException, ClassNotFoundException, IOException{
	
	//datasource = new PlacesDataSource(this);
	//datasource.open();
	List<Place> values = datasource.getAllEntries();
	
	Log.d("Lab6","The size of the IDS is "+ids.size());
	
	//Loop the List to Delete local entry
	/*for(int i=0;i<ids.size();i++){
		ArrayList<String> temp=new ArrayList<String>(Arrays.asList(ids.get(i).split(":")));
		 if(temp.get(2).equals("True")){
			for(Place he:values){
				if(he.getId()==Long.parseLong(temp.get(0)))
					datasource.deleteEntry(he);
			}
		} 
		//else
		{
			boolean exist_at_local=false;
			for(Place he:values){
				if(he.getId()==Long.parseLong(temp.get(0))){
					exist_at_local=true;
					break;
				}
			}
			if(exist_at_local==false){
				//Send Remote Delete
				Log.d("Lab6","The history is"+temp.get(0));
				deletePostHistory(temp.get(0));
			}
		}
		//datasource.close();
	} */
	
	//Add Local list which is not at remote
	//SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	//String regid_comprare = sharedPref.getString(PROPERTY_REG_ID, "");
	//S//tring my_unit_preference = sharedPref.getString("unit_preference", "");
	
	for(Place he:values){
		boolean exist=false;
		Log.d("Lab6","Start Compare");
		for(int i=0;i<ids.size();i++){
			ArrayList<String> temp=new ArrayList<String>(Arrays.asList(ids.get(i).split(":")));
			//Log.d("Lab6","Left ID is "+he.getId()+" right ID is "+Long.parseLong(temp.get(0)));
			if(he.getId()==Long.parseLong(temp.get(0))){
				exist=true;
				break;
			}
		}
		if(exist==false){
			Log.d("Lab6","We are adding an entry");
			//DecimalFormat df = new DecimalFormat("#.##");
			//String distance=df.format(he.getDistance(my_unit_preference));
			String avg_speed="";
			String climb="";
			//if(my_unit_preference.equals("Imperial"))
			//	distance=distance+" Miles";
			//else
			//	distance=distance+" Kilometers";
			
			
			/*if(my_unit_preference.equals("Imperial")){
				avg_speed=df.format(he.getAvgSpeed()*MS_TO_MPH)+" mi/h";
				climb=df.format(he.getClimb()/1000*MILE_TO_KM)+" Miles";
			}
			else{
				avg_speed=df.format(he.getAvgSpeed()*MS_TO_KMPH)+" km/h";
				climb=df.format(he.getClimb()/1000)+" Kilometers";
				
			}*/
			
			 
		}
		
	}	
	
	//Delete Remote
}

public void updateEntries() throws StreamCorruptedException, ClassNotFoundException, IOException{
	
	//datasource = new PlacesDataSource(m);
	//datasource.open();
	//List<Place> values = datasource.getAllEntries();
	
	Log.d("Lab6","The size of the IDS is "+ids.size());
	
	//Loop the List to Delete local entry
	for(int i=0;i<ids.size();i++){
		ArrayList<String> temp=new ArrayList<String>(Arrays.asList(ids.get(i).split(":")));
		//if(temp.get(2).equals("True")){
		Place lplace = new Place();
		lplace.setName(temp.get(0));
		lplace.setDesc(temp.get(1));
		lplace.setRanking(Integer.parseInt(temp.get(2)));
		datasource.createEntry(lplace);
			
	}
	//datasource.close();
	
	
}


public class ActivityEntriesCursorAdapter extends CursorAdapter {

	private LayoutInflater mInflater;

	public ActivityEntriesCursorAdapter(Context context, Cursor c) {
		super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
		mInflater = LayoutInflater.from(context);
	}

	// Override the BindView function to set our data which means,
	// take the data from the cursor and put it into views.
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView tv1 = (TextView) view.findViewById(R.id.label5);
		//TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
		RatingBar rb=(RatingBar) view.findViewById(R.id.ratingBar5);
		ImageView iv = (ImageView)view.findViewById(R.id.bitmapicon);
		String part1 = cursor.getString(1);
		String part2 = cursor.getString(2);
		tv1.setText(part1);
		rb.setRating(Integer.parseInt(part2));	
		
		try{
			String bitmapurl = cursor.getString(3);
			URL url = new URL(bitmapurl);
			Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			iv.setImageBitmap(bmp);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	// When the view will be created for first time,
	// we need to tell the adapters, how each item will look
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		//return mInflater.inflate(android.R.layout.two_line_list_item, null);
		return mInflater.inflate(R.layout.row, null);
	}
	}
}




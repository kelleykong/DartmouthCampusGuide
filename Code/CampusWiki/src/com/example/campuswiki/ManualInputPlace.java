package com.example.campuswiki;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;




public class ManualInputPlace extends Activity implements OnMapClickListener,OnClickListener, OnMapLongClickListener {

	String upLoadServerUri = null;
	
	String upLoadRoot = "http://www.cs.dartmouth.edu/~tyun/cs65/uploads/";
	String abspath_postfix ;
	String voicePath_postfix;
	
	boolean already_start=false;
	
    
    /**********  File Path *************/
    final String uploadFilePath = "/storage/emulated/0/";
    final String uploadFileName = "myRunProfile.jpg";
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
	
	public static final int LIST_ITEM_ID_NAME = 0;
	public static final int LIST_ITEM_ID_RANKING = 1;
	public static final int LIST_ITEM_ID_DESC = 2;
	public static final int LIST_ITEM_ID_LATLNG = 3;
	public static final int LIST_ITEM_ID_BITMAP = 4;
	public static final int LIST_ITEM_ID_VOICE = 5;
	
	public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
	public static final int REQUEST_CODE_TAKE_FROM_ALBUM = 1;
	public static final int REQUEST_CODE_CROP_PHOTO = 2;
	
	private static final String IMAGE_UNSPECIFIED = "image/*";
	private Calendar mDateAndTime = Calendar.getInstance();
	private Place place;
	private PlacesDataSource datasource;
	private Uri mImageCaptureUri;
	private Bitmap mImageCapture;
	//private ImageView mImageView;
	private boolean isTakenFromCamera;
	private String abspath;
	private AlertDialog aler = null;
	private String voicePath;
	private File saveFilePath;
	private MediaRecorder myRecorder;
	private ImageButton start;
	private ImageButton stop;
	private ImageButton btnGetLocation;
	private GoogleMap myMap;
	private MarkerOptions markerOptions;
	private Marker marker;
	private LatLng definedLatlng;
	private LocationManager locationManager;
	static final LatLng COVENTRY = new LatLng(43.704441, -72.288693);
	
	boolean showTut=true;
	
	GalleryImageAdapter galleryAdapter;
	LatLng clickLocation;
	Gallery gallery;
	
	private ArrayList<String> pathList;
	
	private void showActivityOverlay(final Activity mContext) {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.activity_demo3);
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
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual_input);
		start = (ImageButton) findViewById(R.id.record_start);
		stop = (ImageButton) findViewById(R.id.record_stop);
		btnGetLocation = (ImageButton) findViewById(R.id.btnGetLocation);
		
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		
		datasource = PlacesDataSource.getInstance(this);
		place = new Place();
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		
		Intent intent = getIntent();
		if(intent.hasExtra("lat") && intent.hasExtra("lng")){
			definedLatlng = new LatLng(intent.getDoubleExtra("lat",0.0), intent.getDoubleExtra("lng",0.0));
		}
		//mImageView = (ImageView) findViewById(R.id.mi_bitmap);
		
		FragmentManager myFragmentManager = getFragmentManager();
		MapFragment myMapFragment 
			= (MapFragment)myFragmentManager.findFragmentById(R.id.map_small);
		myMap = myMapFragment.getMap();
		if(definedLatlng != null){
			updateWithNewLocation(definedLatlng);
			btnGetLocation.setEnabled(false);
		}else{
			myMap.setMyLocationEnabled(true);
		}
		
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		//Move the camera instantly to the best city in the world! with a zoom of 15.
		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(COVENTRY, 5));
		myMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		
		pathList = new ArrayList<String>();
		
	    gallery = (Gallery) findViewById(R.id.gallery1);
        //selectedImage=(ImageView)findViewById(R.id.imageView1);
        gallery.setSpacing(1);
        galleryAdapter=new GalleryImageAdapter(this); 
        gallery.setAdapter(galleryAdapter);
        
        
        SharedPreferences setNoti = PreferenceManager.getDefaultSharedPreferences(this);
		// SharedPref tutorial
		showTut = setNoti.getBoolean("tutorial", true);
		showTut=true;
		if (showTut == true) {
			showActivityOverlay(this);
		}
        
		
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
	    updateWithNewLocation(l);
	    
	    locationManager.requestLocationUpdates(provider, 2000, 10,
                locationListener);*/
		
		//updateWithNewLocation(myMap.getMyLocation());
	}
	@Override
	public void onResume(){
		super.onResume();
		//locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 1000, 0,
          //      locationListener);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		//locationManager.removeUpdates(locationListener);
	}
	
	private final LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	      updateWithNewLocation(fromLocationToLatLng(location));
	    }

	    public void onProviderDisabled(String provider) {}
	    public void onProviderEnabled(String provider) {}
	    public void onStatusChanged(String provider, int status, 
	                                Bundle extras) {}
	  };
	
	public static LatLng fromLocationToLatLng(Location location){
			return new LatLng(location.getLatitude(), location.getLongitude());
			
	}
	
	private void updateWithNewLocation(LatLng location) {
		EditText myLocationText;
	    myLocationText = (EditText)findViewById(R.id.location_name);
	      
	    //String latLongString = "No location found";
	    String addressString = "";
	    
	    if (location != null) {
	      // Update the map location.
	      
	      LatLng latlng=location;
	     
	      //double lat = location.getLatitude();
	      //double lng = location.getLongitude();
	      //latLongString = "Lat:" + lat + "\nLong:" + lng;
	      if(marker != null){
	    	  marker.remove();
	      }
	      marker = myMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(
	   			     BitmapDescriptorFactory.HUE_GREEN)));
	      double latitude = location.latitude;
	      double longitude = location.longitude;
	      Geocoder gc = new Geocoder(this, Locale.getDefault());

	      if (!Geocoder.isPresent())
	        addressString = "No geocoder available";
	      else {
	        try {
	          List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
	          StringBuilder sb = new StringBuilder();
	          if (addresses.size() > 0) {
	            Address address = addresses.get(0);

	            //for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
	             if(address.getMaxAddressLineIndex()>=1)
	            		sb.append(address.getAddressLine(0));
	             //else if(address.getMaxAddressLineIndex()==2)
	            	 //sb.append(address.getAddressLine(1));

	            //sb.append(address.getLocality()).append("\n");
	            //sb.append(address.getPostalCode()).append("\n");
	            //sb.append(address.getCountryName());
	          }
	          addressString = sb.toString();
	        } catch (IOException e) {
	          Log.d("WHEREAMI", "IO Exception", e);
	        }
	      }
	    }
	      
	    myLocationText.setText(addressString);
	  }

	public void onGetLocationClick(View v){
		/*if(clickLocation!=null)
			updateWithNewLocation(clickLocation);
		else if(definedLatlng ==null){	*/	
			updateWithNewLocation(fromLocationToLatLng(myMap.getMyLocation()));						
		//}
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.record_start:
			already_start=true;
									try {
										voicePath = Environment.getExternalStorageDirectory()
												.getCanonicalPath().toString()
												+ "/"
												
												+ new SimpleDateFormat(
														"yyyyMMddHHmmss").format(System
														.currentTimeMillis())
												+ ".mp3";
										saveFilePath = new File(voicePath);
										saveFilePath.createNewFile();
										
										myRecorder = new MediaRecorder();
										
										myRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
										
										myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
										
										myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
										
										myRecorder.setOutputFile(voicePath);
										
										myRecorder.prepare();
										
										myRecorder.start();
										//start.setText("recording。。");
										start.setEnabled(false);
										aler.dismiss();
										
								
									} catch (Exception e) {
										e.printStackTrace();
									}

							
			break;
		case R.id.record_stop:
			if(already_start==false)
				break;
			
			if (saveFilePath != null && saveFilePath.exists()  ) {
				already_start=false;
				myRecorder.stop();
				myRecorder.reset();
				myRecorder.release();
				
				new AlertDialog.Builder(this)
						.setTitle("save it?")
						.setPositiveButton("save", null)
						.setNegativeButton("cancel",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										saveFilePath.delete();
									}
								}).show();

			}
			//start.setText("start");
			start.setEnabled(true);
			
			dialog = ProgressDialog.show(ManualInputPlace.this, "", "Uploading file...", true);
			upLoadServerUri = "http://www.cs.dartmouth.edu/~tyun/cs65/UploadToServer.php";
	        
	        new Thread(new Runnable() {
	                public void run() {
	                     runOnUiThread(new Runnable() {
	                            public void run() {
	                                //messageText.setText("uploading started.....");
	                            }
	                        });                      
	                   
	                     uploadFile(voicePath);
	                     ArrayList<String> temp = new ArrayList<String>(Arrays.asList(voicePath.split("/")));
	                     voicePath_postfix=temp.get(temp.size()-1);
	                     
	                                              
	                }
	              }).start();
			
		default:
			break;
		}

	}
	

	/*protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		int dialogId = 0;

		// Show dialog based on the position clicked
		switch (position) {
		case LIST_ITEM_ID_NAME:
			dialogId = MyDialogFragment.DIALOG_ID_MANUAL_INPUT_NAME;
			break;
		case LIST_ITEM_ID_RANKING:
			dialogId = MyDialogFragment.DIALOG_ID_MANUAL_INPUT_RANKING;
			break;
		case LIST_ITEM_ID_DESC:
			dialogId = MyDialogFragment.DIALOG_ID_MANUAL_INPUT_DESC;
			break;
		default:
			dialogId = MyDialogFragment.DIALOG_ID_ERROR;
		}
		displayDialog(dialogId);
	}*/
	
	public void onDetailClicked(View v){
		int dialogId = 0;
		dialogId = MyDialogFragment.DIALOG_ID_MANUAL_INPUT_DESC;
		displayDialog(dialogId);
	}

	public void onTakePhotoClicked(View v) {
		// changing the profile image, show the dialog asking the user
		// to choose between taking a picture
		// Go to MyRunsDialogFragment for details.
		displayDialog(MyDialogFragment.DIALOG_ID_PHOTO_PICKER);
	}

	public void onNameSet(String name) {
		// save comment into sqlite
		place.setName(name);
	}

	public void onRankingSet(int ranking) {
		// save comment into sqlite
		place.setRanking(ranking);
	}
	
	public void onDescSet(String desc) {
		// save comment into sqlite
		place.setDesc(desc);
	}
	
	
	
	// Handle data after activity returns.
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode != RESULT_OK)
				return;

			switch (requestCode) {
			case REQUEST_CODE_TAKE_FROM_CAMERA:
				// Send image taken from camera for cropping
				cropImage();
				break;
			case REQUEST_CODE_TAKE_FROM_ALBUM:
				// Send image taken from album for cropping
				mImageCaptureUri = data.getData();
				//abspath = mImageCaptureUri.getPath();
				cropImage();
				break;

			case REQUEST_CODE_CROP_PHOTO:
				// Update image view after image crop
				Bundle extras = data.getExtras();
				// Set the picture image in UI
				if (extras != null) {
					//mImageView
					//		.setImageBitmap((Bitmap) extras.getParcelable("data"));
					mImageCapture = (Bitmap) extras.getParcelable("data");
					
					// mImageCapture = (Bitmap) extras.getParcelable("data");
				}
				
				

				// Delete temporary image taken by camera after crop.
				if (isTakenFromCamera) {
					File f = new File(mImageCaptureUri.getPath());
					if (f.exists()) {
						f.delete();
					}
				}
				try{
					FileOutputStream stream = new FileOutputStream(abspath);
					mImageCapture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				pathList.add(abspath);
				ArrayList<String> temp = new ArrayList<String>(Arrays.asList(abspath.split("/")));
				abspath_postfix=temp.get(temp.size()-1);
				
				dialog = ProgressDialog.show(ManualInputPlace.this, "", "Uploading file...", true);
				upLoadServerUri = "http://www.cs.dartmouth.edu/~tyun/cs65/UploadToServer.php";
		        
		        new Thread(new Runnable() {
		                public void run() {
		                     runOnUiThread(new Runnable() {
		                            public void run() {
		                                //messageText.setText("uploading started.....");
		                            }
		                        });                      
		                   
		                     uploadFile(abspath);
		                                              
		                }
		              }).start();  
				
				galleryAdapter.addImage(abspath);
				galleryAdapter.notifyDataSetChanged();
				
				

				break;
			}
		}
		
		
			
		

		// ******* Photo picker dialog related functions ************//

		public void displayDialog(int id) {
			DialogFragment fragment = MyDialogFragment.newInstance(id);
			fragment.show(getFragmentManager(),
					getString(R.string.dialog_fragment_tag_photo_picker));
		}

		public void onPhotoPickerItemSelected(int item) {
			Intent intent;
			abspath = Environment.getExternalStorageDirectory() + "/tmp_"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg";

			switch (item) {

			case MyDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:
				// Take photo from camera�? // Construct an intent with action
				// MediaStore.ACTION_IMAGE_CAPTURE
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// Construct temporary image path and name to save the taken
				// photo
				
				mImageCaptureUri = Uri.fromFile(new File(abspath));
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
						mImageCaptureUri);
				intent.putExtra("return-data", true);
				try {
					// Start a camera capturing activity
					// REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
					// defined to identify the activity in onActivityResult()
					// when it returns
					startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
				}
				isTakenFromCamera = true;
				break;

			case MyDialogFragment.ID_PHOTO_PICKER_FROM_ALBUM:

				// Start a photo selection activity
				// REQUEST_CODE_TAKE_FROM_ALBUM is an integer tag
				// defined to identify the activity in onActivityResult()
				// when it returns

				Intent intentFromAlbum = new Intent((String) null, null);
				intentFromAlbum.setType("image/*");
				intentFromAlbum.setAction(Intent.ACTION_PICK);
				startActivityForResult(intentFromAlbum,
						REQUEST_CODE_TAKE_FROM_ALBUM);
				break;

			default:
				return;
			}

		}
	
	
		// Crop and resize the image for profile
		private void cropImage() {
			// Use existing crop activity.
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mImageCaptureUri, IMAGE_UNSPECIFIED);

			// Specify image size
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 300);

			// Specify aspect ratio, 1:1
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);
			// REQUEST_CODE_CROP_PHOTO is an integer tag you defined to
			// identify the activity in onActivityResult() when it returns
			startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
		}
		
		
		public int uploadFile(String sourceFileUri) {
	           
	           
	          String fileName = sourceFileUri;
	  
	          HttpURLConnection conn = null;
	          DataOutputStream dos = null;  
	          String lineEnd = "\r\n";
	          String twoHyphens = "--";
	          String boundary = "*****";
	          int bytesRead, bytesAvailable, bufferSize;
	          byte[] buffer;
	          int maxBufferSize = 1 * 1024 * 1024; 
	          File sourceFile = new File(sourceFileUri); 
	           
	          if (!sourceFile.isFile()) {
	               
	               dialog.dismiss(); 
	                
	               Log.e("uploadFile", "Source File not exist :"
	                                   +uploadFilePath + "" + uploadFileName);
	                
	               runOnUiThread(new Runnable() {
	                   public void run() {
	                       //messageText.setText("Source File not exist :"
	                       //        +uploadFilePath + "" + uploadFileName);
	                   }
	               }); 
	                
	               return 0;
	            
	          }
	          else
	          {
	               try { 
	                    
	                     // open a URL connection to the Servlet
	                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
	                   URL url = new URL(upLoadServerUri);
	                    
	                   // Open a HTTP  connection to  the URL
	                   conn = (HttpURLConnection) url.openConnection(); 
	                   conn.setDoInput(true); // Allow Inputs
	                   conn.setDoOutput(true); // Allow Outputs
	                   conn.setUseCaches(false); // Don't use a Cached Copy
	                   conn.setRequestMethod("POST");
	                   conn.setRequestProperty("Connection", "Keep-Alive");
	                   conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	                   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	                   conn.setRequestProperty("uploaded_file", fileName); 
	                    
	                   dos = new DataOutputStream(conn.getOutputStream());
	          
	                   dos.writeBytes(twoHyphens + boundary + lineEnd); 
	                   dos.writeBytes("Content-Disposition: form-data; name="+"uploaded_file"+";filename="
	                                             + fileName +"" + lineEnd);
	                    
	                   dos.writeBytes(lineEnd);
	          
	                   // create a buffer of  maximum size
	                   bytesAvailable = fileInputStream.available(); 
	          
	                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                   buffer = new byte[bufferSize];
	          
	                   // read file and write it into form...
	                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
	                      
	                   while (bytesRead > 0) {
	                        
	                     dos.write(buffer, 0, bufferSize);
	                     bytesAvailable = fileInputStream.available();
	                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
	                      
	                    }
	          
	                   // send multipart form data necesssary after file data...
	                   dos.writeBytes(lineEnd);
	                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	          
	                   // Responses from the server (code and message)
	                   serverResponseCode = conn.getResponseCode();
	                   String serverResponseMessage = conn.getResponseMessage();
	                     
	                   Log.i("uploadFile", "HTTP Response is : "
	                           + serverResponseMessage + ": " + serverResponseCode);
	                    
	                   if(serverResponseCode == 200){
	                        
	                       runOnUiThread(new Runnable() {
	                            public void run() {
	                                 
	                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
	                                              +" http://www.androidexample.com/media/uploads/"
	                                              +uploadFileName;
	                                 
	                                //messageText.setText(msg);
	                                //Toast.makeText(MainActivity.this, "File Upload Complete.", 
	                                //             Toast.LENGTH_SHORT).show();
	                            }
	                        });                
	                   }    
	                    
	                   //close the streams //
	                   fileInputStream.close();
	                   dos.flush();
	                   dos.close();
	                     
	              } catch (MalformedURLException ex) {
	                   
	                  dialog.dismiss();  
	                  ex.printStackTrace();
	                   
	                  runOnUiThread(new Runnable() {
	                      public void run() {
	                          //messageText.setText("MalformedURLException Exception : check script url.");
	                          //Toast.makeText(MainActivity.this, "MalformedURLException", 
	                           //                                   Toast.LENGTH_SHORT).show();
	                      }
	                  });
	                   
	                  Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
	              } catch (Exception e) {
	                   
	                  dialog.dismiss();  
	                  e.printStackTrace();
	                   
	                  runOnUiThread(new Runnable() {
	                      public void run() {
	                          //messageText.setText("Got Exception : see logcat ");
	                          //Toast.makeText(MainActivity.this, "Got Exception : see logcat ", 
	                          //        Toast.LENGTH_SHORT).show();
	                      }
	                  });
	                  Log.e("Upload file to server Exception", "Exception : "
	                                                   + e.getMessage(), e);  
	              }
	              dialog.dismiss();       
	              return serverResponseCode; 
	               
	           } // End else block 
	         } 


		
	// "Save" button is clicked
	public void onSaveClicked(View v) {
		// Pop up a toast
		 
		
		
		
		//Toast.makeText(this, "New entry saved", Toast.LENGTH_SHORT).show();
		RatingBar ratingBar1=(RatingBar)findViewById(R.id.ratingBar2);
		place.setRanking((int)ratingBar1.getRating());
		EditText edit=(EditText)findViewById(R.id.location_name);
		place.setName(edit.getText().toString());
		if(definedLatlng !=null){
			place.setLatLng(definedLatlng);
		}else{
			place.setLatLng(fromLocationToLatLng(myMap.getMyLocation()));
		}
		
		
		
		try{
			//hanlde bitmap
			//FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
			/*String path = Environment.getExternalStorageDirectory()
					.getCanonicalPath().toString()
					+ "/DCIM/";
			FileInputStream fis = new FileInputStream(path+"test.jpg");
			entry.setBitmap(BitmapFactory.decodeStream(fis));*/
			
			//String path = mImageCaptureUri.getPath();
			place.setBitmap(upLoadRoot+abspath_postfix);
		
			
		//handle voice
		 /*path = Environment.getExternalStorageDirectory()
				.getCanonicalPath().toString()
				+ "/XIONGRECORDERS/test.amr";*/
		place.setVoice(upLoadRoot+voicePath_postfix);
	
		
		
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Place newPlace = datasource.createEntry(place);
		
		
		MyPagerAdapter.mAdapter.notifyDataSetChanged();
		
		//upload to server
		JSONObject obj = new JSONObject();
		//obj.put(MainActivity.PROPERTY_REG_ID, MainActivity.regid);
		try{
		
			
			obj.put(MySQLiteHelper.COLUMN_ID, newPlace.getId());
			obj.put(MySQLiteHelper.COLUMN_NAME, newPlace.getName());
			
			obj.put(MySQLiteHelper.COLUMN_LAT, newPlace.getLatLng().latitude);
			obj.put(MySQLiteHelper.COLUMN_LNG, newPlace.getLatLng().longitude);
			
			obj.put(MySQLiteHelper.COLUMN_BITMAP, newPlace.getBitmap());
			obj.put(MySQLiteHelper.COLUMN_RANKING, newPlace.getRanking());
			obj.put(MySQLiteHelper.COLUMN_DESC, newPlace.getDesc());
			obj.put(MySQLiteHelper.COLUMN_VOICE, newPlace.getVoice());
			String out = obj.toString();
			syncExercise(out);		
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		
		
		
		
		// Close the activity
		finish();
	}
	
	private void syncExercise(String data) {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				String url = getString(R.string.server_addr) + "/post.do";
				String res = "";						
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("data", arg0[0]);
				try {
					res = ServerUtilities.post(url, params,"application/json");
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				return res;
			}

			@Override
			protected void onPostExecute(String res) {
				//mPostText.setText("");
				//refreshPostHistory();
			}

		}.execute(data);
	}
	
	public void searchLocation(View v){
		Geocoder coder = new Geocoder(this);
		List<Address> address;
		EditText et=(EditText)findViewById(R.id.location_name);
		String str=et.getText().toString();
		Log.d("Wiki",str);
		
		/*clickLocation=getLatLong(getLocationInfo(str));
		
		if(clickLocation!=null){
			
			MarkerOptions mk=new MarkerOptions().position(clickLocation).title(clickLocation.toString());
			myMap.addMarker(mk);
		}*/

		
	}
	
	public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

        address = address.replaceAll(" ","%20");    

        HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }
	
	public static LatLng getLatLong(JSONObject jsonObject) {

        try {

            double longitute = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");

            double latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");
            
            return new LatLng(latitude, longitute);

        } catch (JSONException e) {
            return null;

        }

        
    }

	// "Cancel" button is clicked
	public void onCancelClicked(View v) {
		// Pop up a toast, discard the input and close the activity directly
		//Toast.makeText(this, "New entry canceled", Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		MarkerOptions mk=new MarkerOptions().position(point).title(point.toString());
		myMap.addMarker(mk);
		clickLocation=point;
		updateWithNewLocation(clickLocation);
		
	}
}

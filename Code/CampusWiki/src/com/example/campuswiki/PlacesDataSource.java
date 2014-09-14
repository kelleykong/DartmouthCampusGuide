package com.example.campuswiki;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;

public class PlacesDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private static PlacesDataSource datasource;

	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_LATLNG,
			MySQLiteHelper.COLUMN_BITMAP, MySQLiteHelper.COLUMN_RANKING,
			MySQLiteHelper.COLUMN_DESC, MySQLiteHelper.COLUMN_VOICE,};

	private static final String TAG = "PLACES";

	private PlacesDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public static PlacesDataSource getInstance(Context context) {
		if (datasource == null) {
			datasource = new PlacesDataSource(context);
			datasource.open();
		}

		return datasource;
	}

	public Cursor createCursor(String[] cols, String args) {
		return database.query(MySQLiteHelper.TABLE_PLACES, cols, args, null,
				null, null, null);

	}

	public Place createEntry(Place place) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, place.getName());
		
		if(place.getLatLng() != null){			
			LatLng latlng = place.getLatLng();
			ByteBuffer byteBuffer = ByteBuffer.allocate( 16);
			
			byteBuffer.putDouble(latlng.latitude);
			byteBuffer.putDouble(latlng.longitude);
					
			values.put(MySQLiteHelper.COLUMN_LATLNG,byteBuffer.array());
		}
		
		
		
		values.put(MySQLiteHelper.COLUMN_BITMAP, place.getBitmap());
		values.put(MySQLiteHelper.COLUMN_RANKING, place.getRanking());
		values.put(MySQLiteHelper.COLUMN_DESC, place.getDesc());
		values.put(MySQLiteHelper.COLUMN_VOICE, place.getVoice());
	
		
		/*
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		entry.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		values.put(MySQLiteHelper.COLUMN_BITMAP,byteArray);
		*/
		
		
		
		long insertId = database.insert(MySQLiteHelper.TABLE_PLACES, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACES,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Place newEntry = cursorToEntry(cursor);

		// Log the comment stored
		Log.d(TAG, "entry = " + cursorToEntry(cursor).toString()
				+ " insert ID = " + insertId);

		cursor.close();
		return newEntry;
	}

	public void deleteEntry(Place place) {
		long id = place.getId();
		Log.d(TAG, "delete entry = " + id);
		System.out.println("Entry deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_PLACES, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public void deleteAllEntries() {
		List<Place> places = getAllEntries();
		for(Place place:places){
			deleteEntry(place);
		}
	}
	
	public List<Place> getAllEntries() {
		List<Place> places = new ArrayList<Place>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACES,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Place place = cursorToEntry(cursor);
			Log.d(TAG, "get entry = " + cursorToEntry(cursor).toString());
			places.add(place);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return places;
	}


	public Place getEntry(long _id) {
		Place place = new Place();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACES,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + _id, null, null,
				null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			place = cursorToEntry(cursor);
			Log.d(TAG, "get entry = " + cursorToEntry(cursor).toString());
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return place;
	}

	private Place cursorToEntry(Cursor cursor) {
		Place place = new Place();
		place.setId(cursor.getLong(0));
		place.setName(cursor.getString(1));
		
		byte[] byteTrack = cursor.getBlob(2);
        if(byteTrack != null){
    		ByteBuffer byteBuffer = ByteBuffer.wrap(byteTrack);  		
    		place.setLatLng(new LatLng(byteBuffer.getDouble(),byteBuffer.getDouble()));    		
        }
		
		place.setBitmap(cursor.getString(3));
		place.setRanking(cursor.getInt(4));
		place.setDesc(cursor.getString(5));
		place.setVoice(cursor.getString(6));
		
		
		/*
		byte[] byteArray = cursor.getBlob(12);
		Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		entry.setBitmap(bmp);
		*/
		return place;
	}
}
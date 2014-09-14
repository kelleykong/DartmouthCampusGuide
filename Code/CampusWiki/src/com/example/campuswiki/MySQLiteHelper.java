package com.example.campuswiki;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_PLACES = "places";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LATLNG = "latlng";

	public static final String COLUMN_BITMAP = "bitmap";
	public static final String COLUMN_RANKING = "ranking";
	public static final String COLUMN_DESC = "desc";
//	public static final String COLUMN_IMGS = "imgs";
	public static final String COLUMN_VOICE = "voice";

	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LNG = "lng";
	
	private static final String DATABASE_NAME = "places.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_PLACES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text, " + COLUMN_LATLNG 
			+ " blob, " + COLUMN_BITMAP + " text, " + COLUMN_RANKING
			+ " integer, " + COLUMN_DESC
			+ " text, " + COLUMN_VOICE
			+ " text );";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
		onCreate(db);
	}

}
package com.example.campuswiki;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.model.LatLng;
import android.graphics.Bitmap;


public class Place {
	private long id;
	private String name;
	private LatLng latLng;
	private String bitmap ="";
	private int ranking =1;
	private String desc ="";
	private String voice = ""; 
	private ArrayList<String> bitmaps;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public LatLng getLatLng() {
		return latLng;
	}
	
	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}
	
	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getBitmap() {
		return bitmap;
	}

	public void setBitmap(String bitmap) {
		this.bitmap = bitmap;
	}
	
	public ArrayList<String> getBitmaps() {
		return bitmaps;
	}

	public void setBitmaps(ArrayList<String> bitmaps) {
		this.bitmaps = bitmaps;
	}
	
	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}
}

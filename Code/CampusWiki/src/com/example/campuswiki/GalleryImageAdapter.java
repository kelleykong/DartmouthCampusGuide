package com.example.campuswiki;

import java.io.FileInputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryImageAdapter extends BaseAdapter 
{
    private Context mContext;

    private ArrayList<String> mImageIds;

    public GalleryImageAdapter(Context context) 
    {
    		mImageIds = new ArrayList<String>();
        mContext = context;
    }

    public int getCount() {
        return mImageIds.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public void addImage(String imagePath){
    		mImageIds.add(imagePath);
    }


    // Override this method according to your need
    @SuppressWarnings("deprecation")
	public View getView(int index, View view, ViewGroup viewGroup) 
    {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        //i.setImageResource(mImageIds.at(index));
        
        try{
			FileInputStream fis = new FileInputStream(mImageIds.get(index));
			i.setImageBitmap(BitmapFactory.decodeStream(fis));
		}catch(Exception e){
			e.printStackTrace();
		}
        
        i.setLayoutParams(new Gallery.LayoutParams(350, 350));
    
        i.setScaleType(ImageView.ScaleType.FIT_XY);

        return i;
    }
}

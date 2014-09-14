package com.example.campuswiki;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
	 
	public class SplashScreen extends Activity {
	 
	    // Splash screen timer
	    private static int SPLASH_TIME_OUT = 2000;
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_splash_screen);
	 
	        new Handler().postDelayed(new Runnable() {
	 
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	                Intent i = new Intent(SplashScreen.this, MainActivity.class);
	                startActivity(i);
	                
	                // close this activity
	                finish();
	                
	                overridePendingTransition(R.xml.activityfadein,
	                        R.xml.splashfadeout);
	                
	            }
	        }, SPLASH_TIME_OUT);
	    }	 
}

	



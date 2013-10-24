package com.example.weeblyweatherapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	protected static final String MAIN = "main";
	protected static final String CURRENT = "current";
	protected static final String LOCATIONS = "locations";
	private static final String DEFAULT_LOCATION = "San Francisco";

	private SharedPreferences mSharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSharedPreferences = getSharedPreferences(MAIN, MODE_PRIVATE);
		String location = mSharedPreferences.getString(CURRENT, null);
		if(location == null) {
			// If no locations have been saved, default is San Francisco
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			location = DEFAULT_LOCATION;
			editor.putString(CURRENT, location);
			// TODO: change following line to just location
			editor.putString(LOCATIONS, "San Francisco,Philadelphia,Los Angeles,Sydney");
			editor.commit();
		}
		TextView locationTextView = (TextView) findViewById(R.id.current_location);
		locationTextView.setText(location);
	}
	
	// TODO: Either use options menu or delete it
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onClickLocations(View view) {
		Intent intent = new Intent(this, LocationsActivity.class);
		startActivity(intent);
	}
	
}

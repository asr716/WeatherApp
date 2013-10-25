package com.example.weeblyweatherapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	protected static final String MAIN = "main";
	protected static final String CURRENT = "current";
	protected static final String LOCATIONS = "locations";
	private static final String DEFAULT_LOCATION = "San Francisco";

	private SharedPreferences mSharedPreferences;
	private CurrentWeather mCurrentWeather;
	private Forecast mForecast;
	
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
		new CallAPI().execute("weather", "http://api.wunderground.com/api/70fcf8d041fb9d01/conditions/q/CA/San_Francisco.json");
		new CallAPI().execute("forecast", "http://api.wunderground.com/api/70fcf8d041fb9d01/forecast/q/CA/San_Francisco.json");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, LocationsActivity.class);
		startActivity(intent);
		return true;
	}
	
	private void setWeather() {
		TextView tempTextView = (TextView) findViewById(R.id.temperature);
		tempTextView.setText("" + mCurrentWeather.getTemp_f());
	}
	
	private void setForecast() {
		
	}
	
	private class CallAPI extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {
			String s = "";
			String json = "";
			try {
				URL url = new URL(args[1]);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				BufferedReader rd = new BufferedReader(new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream())));
				while ((s = rd.readLine()) != null) {
					json = json + s;
					Log.i("", s);
				}
				if (args[0].equals("weather")) {
					mCurrentWeather = new CurrentWeather(json);
					Log.i("", mCurrentWeather.toString());
				} else {
					mForecast = new Forecast(json);
					Log.i("", mForecast.toString());
				}
			} catch(IOException e) {
				// TODO: Handle exception
				return null;
			}
			
			return args[0];
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				if (result.equals("weather")) {
					setWeather();
				} else {
					setForecast();
				}
			}
		}
	}
}

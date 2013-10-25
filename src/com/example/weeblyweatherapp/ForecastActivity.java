package com.example.weeblyweatherapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ForecastActivity extends Activity {
	private final static int NUM_DAY_FORECAST = 10;
	private final static String ITEM_DATE = "item_date";
	private final static String ITEM_HIGH = "item_high";
	private final static String ITEM_LOW = "item_low";
	private final static String[] ITEMS = {ITEM_DATE, ITEM_HIGH, ITEM_LOW};
	private final static int[] ITEM_IDS = {R.id.date, R.id.high, R.id.low};
	
	private SharedPreferences mSharedPreferences;
	private ListView mForecastListView;
	private List<Map<String, ?>> mForecastList;
	private Forecast mForecast;
	
	private Map<String, ?> createItem(Day day) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_DATE, day.getDate());
		item.put(ITEM_HIGH, getResources().getString(R.string.high) + " " + day.getHigh().replaceAll("\"", ""));
		item.put(ITEM_LOW, getResources().getString(R.string.low) + " " + day.getLow().replaceAll("\"", ""));
		return item;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forecast);
		
		mSharedPreferences = getSharedPreferences(MainActivity.MAIN, MODE_PRIVATE);
		mForecastListView = (ListView) findViewById(R.id.forecast_list);
		mForecastList = new ArrayList<Map<String, ?>>();
		
		String location = mSharedPreferences.getString(MainActivity.CURRENT, null);
		new ForecastAPI().execute("http://api.wunderground.com/api/70fcf8d041fb9d01/forecast10day/q/"
				+ LocationHelper.convertToURLFormat(location) + ".json");
	}
	
	private void setForecast() {
		for (Day day: mForecast.getDays()) {
			mForecastList.add(createItem(day));
		}
		mForecastListView.setAdapter(new SimpleAdapter(this, mForecastList, R.layout.forecast_item, ITEMS, ITEM_IDS));
	}
	
	private class ForecastAPI extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {
			String s = "";
			String json = "";
			try {
				URL url = new URL(args[0]);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				BufferedReader rd = new BufferedReader(new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream())));
				while ((s = rd.readLine()) != null) {
					json = json + s;
					Log.i("", s);
				}
				mForecast = new Forecast(json, NUM_DAY_FORECAST);
				Log.i("", mForecast.toString());
			} catch(IOException e) {
				// TODO: Handle exception
				return null;
			}
			
			return args[0];
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				setForecast();
			}
		}
	}
}

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
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class AddLocationActivity extends Activity {
	private final static String ITEM_LOCATION = "item_location";
	private final static String[] ITEMS = {ITEM_LOCATION};
	private final static int[] ITEM_IDS = {R.id.location};
	
	private Context mContext;
	private SharedPreferences mSharedPreferences;
	private ListView mLocationsListView;
	private List<Map<String, ?>> mLocationsList;
	private Locations mLocations;
	private EditText mEditText;

	private Map<String, ?> createItem(String location) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_LOCATION, location);
		return item;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_location);

		mContext = this;
		mSharedPreferences = getSharedPreferences(MainActivity.MAIN, MODE_PRIVATE);
		mLocationsListView = (ListView) findViewById(R.id.autocomplete_list);
		mLocationsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String loc = ((String) mLocationsList.get(position).get(ITEM_LOCATION)).trim();
				String locations = mSharedPreferences.getString(MainActivity.LOCATIONS, null);
				if (locations != null && !locations.contains(loc)) {
					// If the selected location is not already saved, add it
					SharedPreferences.Editor editor = mSharedPreferences.edit();
					if (locations.length() > 0) {
						editor.putString(MainActivity.LOCATIONS, locations + ":" + loc);
					} else {
						editor.putString(MainActivity.LOCATIONS, loc);
					}
					editor.commit();
					// TODO: Do not hard code string in following line
					Toast.makeText(getApplicationContext(), "Added " + loc + " to saved locations", Toast.LENGTH_LONG).show();
					finish();
				}
			}
		});
		mEditText = (EditText) findViewById(R.id.new_location);
		mEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE); 
					inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					String location = v.getText().toString();
					new GeoLookupAPI().execute("http://api.wunderground.com/api/70fcf8d041fb9d01/geolookup/q/"
							+ LocationHelper.convertToURLFormat(location) + ".json");
					return true;
				}
				return false;
			}
		});
	}

	private void setLocations() {
		mLocationsList = new ArrayList<Map<String, ?>>();
		for (String loc: mLocations.getLocations()) {
			mLocationsList.add(createItem(loc));
		}
		mLocationsListView.setAdapter(new SimpleAdapter(this, mLocationsList, R.layout.location_item, ITEMS, ITEM_IDS));
	}

	private class GeoLookupAPI extends AsyncTask<String, Void, String> {

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
				mLocations = new Locations(json);
				Log.i("", mLocations.toString());
			} catch(IOException e) {
				// TODO: Handle exception
				return null;
			}
			
			return args[0];
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				setLocations();
			}
		}
	}
}

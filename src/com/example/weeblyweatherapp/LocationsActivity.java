package com.example.weeblyweatherapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class LocationsActivity extends Activity {
	private final static String ITEM_LOCATION = "item_location";
	private final static String[] ITEMS = {ITEM_LOCATION};
	private final static int[] ITEM_IDS = {R.id.location};
	
	private Context mContext;
	private SharedPreferences mSharedPreferences;
	private ListView mLocationsListView;
	private List<Map<String, ?>> mLocationsList;
	
	private Map<String, ?> createItem(String location) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_LOCATION, location);
		return item;
	}

	private void createLocationsList() {
		mLocationsList = new ArrayList<Map<String, ?>>();
		String csvLocations = mSharedPreferences.getString(MainActivity.LOCATIONS, null);
		if (csvLocations != null) {
			String[] locations = csvLocations.split(":");
			for (String location: locations) {
				mLocationsList.add(createItem(location));
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);
		
		mContext = this;
		mSharedPreferences = getSharedPreferences(MainActivity.MAIN, MODE_PRIVATE);
		mLocationsListView = (ListView) findViewById(R.id.location_list);
		
		// Set current location on click
		mLocationsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String location = (String) mLocationsList.get(position).get(ITEM_LOCATION);
				SharedPreferences.Editor editor = mSharedPreferences.edit();
				editor.putString(MainActivity.CURRENT, location);
				editor.commit();
				Toast.makeText(getApplicationContext(), "Set " + location + " as current location", Toast.LENGTH_LONG).show();
				finish();
			}
		});
		
		// Delete location on long click
		mLocationsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final String location = (String) mLocationsList.get(position).get(ITEM_LOCATION);
				AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(mContext);
				deleteDialogBuilder.setTitle(getResources().getString(R.string.delete_location_title));
				deleteDialogBuilder.setMessage(getResources().getString(R.string.delete_location_message));
				deleteDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						deleteLocation(location);
						onResume();
						dialog.dismiss();
					}
				});
				deleteDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog deleteDialog = deleteDialogBuilder.create();
				deleteDialog.show();
				return true;
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		createLocationsList();
		mLocationsListView.setAdapter(new SimpleAdapter(this, mLocationsList, R.layout.location_item, ITEMS, ITEM_IDS));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_locations, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, AddLocationActivity.class);
		startActivityForResult(intent, 1);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			finish();
		}
	}

	private void deleteLocation(String location) {
		String csvLocations = mSharedPreferences.getString(MainActivity.LOCATIONS, null);
		int index = csvLocations.indexOf(location);
		csvLocations = csvLocations.replace(location, "");
		if (csvLocations.length() > 0) {
			if (csvLocations.charAt(0) == ':') {
				// Delete the colon if it is at the beginning of the string
				csvLocations = csvLocations.substring(1, csvLocations.length());
			} else if (csvLocations.charAt(csvLocations.length() - 1) == ':') {
				// Delete the colon if at the end of the string
				csvLocations = csvLocations.substring(0, csvLocations.length() - 1);
			} else {
				// Delete the colon from the middle of the string
				csvLocations = csvLocations.substring(0, index) + csvLocations.substring(index + 1, csvLocations.length());
			}
		}
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		if (location.equals(mSharedPreferences.getString(MainActivity.CURRENT, null))) {
			// If current location was deleted, make current location the first saved location
			String firstLocation = null;
			int endIndex = csvLocations.indexOf(":");
			if (csvLocations.length() == 0) {
				firstLocation = null;
				csvLocations = null;
			} else if (endIndex == -1) {
				firstLocation = csvLocations;
			} else {
				firstLocation = csvLocations.substring(0, endIndex);
			}
			editor.putString(MainActivity.CURRENT, firstLocation);
		}
		editor.putString(MainActivity.LOCATIONS, csvLocations);
		editor.commit();
	}
}

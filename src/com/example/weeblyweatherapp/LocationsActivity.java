package com.example.weeblyweatherapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SimpleAdapter;
import android.app.Activity;

public class LocationsActivity extends Activity {
	private final static String ITEM_LOCATION = "item_location";
	private final static String[] ITEMS = {ITEM_LOCATION};
	private final static int[] ITEM_IDS = {R.id.location};
	
	private ListView mLocationsListView;
	private List<Map<String, ?>> mLocationsList;
	
	private Map<String, ?> createItem(String location) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_LOCATION, location);
		return item;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);
		
		mLocationsListView = (ListView) findViewById(R.id.location_list);
		mLocationsList = new ArrayList<Map<String, ?>>();
		
		// Set current location on click
		mLocationsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO: Set to current location on click
			}
		});
		
		// Delete location on long click
		mLocationsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO: Delete from list on long click
				return true;
			}
		});
		
		// TODO: Add saved locations to mLocationsList
		mLocationsList.add(createItem("San Francisco"));
		mLocationsList.add(createItem("Philadelphia"));
		
		mLocationsListView.setAdapter(new SimpleAdapter(this, mLocationsList, R.layout.location_item, ITEMS, ITEM_IDS));
	}
}

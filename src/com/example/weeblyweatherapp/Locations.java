package com.example.weeblyweatherapp;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Locations {
	private List<String> locations;
	
	public Locations(String json) {
		locations = new ArrayList<String>();
		JsonElement jelement = new JsonParser().parse(json);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonObject loc = jobject.getAsJsonObject("location");
		if (loc != null) {
			// Only 1 location returned
			addLoc(loc);
		} else {
			// An array of locations returned
			jobject = jobject.getAsJsonObject("response");
			JsonArray jarray = jobject.getAsJsonArray("results");
			if (jarray != null) {
				for (JsonElement e: jarray) {
					addLoc(e.getAsJsonObject());
				}
			}
		}
	}
	
	private void addLoc(JsonObject loc) {
		String city = loc.get("city").toString().replaceAll("\"", "");
		String country = loc.get("country_name").toString().replaceAll("\"", "");
		if ("USA".equals(country)) {
			// US city
			String state = loc.get("state").toString().replaceAll("\"", "");;
			locations.add(city + ", " + state);
		} else {
			// International city
			locations.add(city + ", " + country);
		}
	}
	
	public List<String> getLocations() {
		return locations;
	}
	
	public String toString() {
		return locations.toString();
	}
}

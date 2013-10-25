package com.example.weeblyweatherapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Forecast {
	private Day[] days;

	public Forecast(String json, int numDays) {
		days = new Day[numDays];
		JsonElement jelement = new JsonParser().parse(json);
		JsonObject jobject = jelement.getAsJsonObject();
		jobject = jobject.getAsJsonObject("forecast");
		jobject = jobject.getAsJsonObject("simpleforecast");
		JsonArray jarray = jobject.getAsJsonArray("forecastday");
		for (int i = 0; i < numDays; i++) {
			Day day = new Day();
			JsonObject date = jarray.get(i).getAsJsonObject();
			JsonObject high = date.getAsJsonObject("high");
			day.setHigh(high.get("fahrenheit").toString());
			JsonObject low = date.getAsJsonObject("low");
			day.setLow(low.get("fahrenheit").toString());
			date = date.getAsJsonObject("date");
			day.setDate(date.get("month") + "/" + date.get("day"));
			days[i] = day;
		}
	}
	
	public Day[] getDays() {
		return days;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Day day: days) {
			sb.append(day.getDate() + " (high: " + day.getHigh() + ", low: " + day.getLow() + ") ");
		}
		return sb.toString();
	}
}

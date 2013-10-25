package com.example.weeblyweatherapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CurrentWeather {
	private String station_id;
	private String observation_time;
	private String observation_time_rfc822;
	private String observation_epoch;
	private String local_time_rfc822;
	private String local_epoch;
	private String local_tz_short;
	private String local_tz_long;
	private String local_tz_offset;
	private String weather;
	private String temperature_string;
	private float temp_f;
	private float temp_c;
	private String relative_humidity;
	private String wind_string;
	private String wind_dir;
	private int wind_degrees;
	private float wind_mph;
	private int wind_gust_mph;
	private float wind_kph;
	private int wind_gust_kph;
	private String pressure_mb;
	private String pressure_in;
	private String pressure_trend;
	private String dewpoint_string;
	private int dewpoint_f;
	private int dewpoint_c;
	private String heat_index_string;
	private String heat_index_f;
	private String heat_index_c;
	private String windchill_string;
	private String windchill_f;
	private String windchill_c;
	private String feelslike_string;
	private String feelslike_f;
	private String feelslike_c;
	private String visibility_mi;
	private String visibility_km;
	private String solarradiation;
	private String UV;
	private String precip_1hr_string;
	private String precip_1hr_in;
	private String precip_1hr_metric;
	private String precip_today_string;
	private String precip_today_in;
	private String precip_today_metric;
	private String icon;
	private String icon_url;
	private String forecast_url;
	private String history_url;
	private String ob_url;
	
	public CurrentWeather(String json) {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(json);
		JsonObject jobject = jelement.getAsJsonObject();
		jobject = jobject.getAsJsonObject("current_observation");
		temp_f = gson.fromJson(jobject.getAsJsonPrimitive("temp_f"), float.class);
		temp_c = gson.fromJson(jobject.getAsJsonPrimitive("temp_c"), float.class);
	}
	
	public void setStation_id (String station_id) {
		this.station_id = station_id;
	}
	
	public void setObservation_time (String observation_time) {
		this.observation_time = observation_time;
	}
	
	public void setObservation_time_rfc822 (String observation_time_rfc822) {
		this.observation_time_rfc822 = observation_time_rfc822;
	}
	
	public void setTemp_f (float temp_f) {
		this.temp_f = temp_f;
	}
	
	public float getTemp_f () {
		return temp_f;
	}
	
	public void setTemp_c (float temp_c) {
		this.temp_c = temp_c;
	}
	
	public float getTemp_c () {
		return temp_c;
	}
	
	@Override
	public String toString() {
		return "WeatherConditions, F: " + temp_f + ", C:" + temp_c;
	}
}

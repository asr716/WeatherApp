package com.example.weeblyweatherapp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CurrentWeather {
	private float temp_f;
	
	public CurrentWeather(String json) {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(json);
		JsonObject jobject = jelement.getAsJsonObject();
		jobject = jobject.getAsJsonObject("current_observation");
		temp_f = gson.fromJson(jobject.getAsJsonPrimitive("temp_f"), float.class);
	}
	
	public void setTemp_f (float temp_f) {
		this.temp_f = temp_f;
	}
	
	public float getTemp_f () {
		return temp_f;
	}
	
	@Override
	public String toString() {
		return "WeatherConditions, F: " + temp_f;
	}
}

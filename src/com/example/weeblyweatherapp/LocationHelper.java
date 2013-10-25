package com.example.weeblyweatherapp;

public class LocationHelper {

	public static String convertToURLFormat(String location) {
		StringBuffer sb = new StringBuffer();
		int index = location.indexOf(',');
		if (index != -1 && index + 1 < location.length()) {
			sb.append(location.substring(index + 1).trim() + "/");
			sb.append(location.substring(0, index).trim());
			location = sb.toString();
		}
		return location;
	}
}

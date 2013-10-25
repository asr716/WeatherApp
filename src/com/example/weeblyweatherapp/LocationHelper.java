package com.example.weeblyweatherapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LocationHelper {

	public static String convertToURLFormat(String location) {
		StringBuffer sb = new StringBuffer();
		int index = location.indexOf(',');
		if (index != -1 && index + 1 < location.length()) {
			try {
				sb.append(URLEncoder.encode(location.substring(index + 1).trim(), "utf-8") + "/");
				sb.append(URLEncoder.encode(location.substring(0, index).trim(), "utf-8"));
			} catch (UnsupportedEncodingException e) {}
			location = sb.toString();
		}
		return location;
	}
}

package com.rns.tiffeat.mobile.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;

import com.rns.tiffeat.web.bo.domain.Meal;
import com.rns.tiffeat.web.bo.domain.Vendor;

import android.util.Log;

public class VendorServerUtils implements AndroidConstants {

	private static String result = "";

	public static String getVendorForArea(String pinCode) 
	{

		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put(PIN_CODE,pinCode);
		Log.d(MYTAG, "Result of vendor search :" + result);
		result = CoreServerUtils.serverCall(GET_VENDORS_FOR_AREA, uriVariables,HttpMethod.POST).getBody();
		Log.d(MYTAG, "Result of vendor search :" + result);
		return result;
	}

	public static String createVendorImageUrl(Vendor vendor) 
	{
		String url = ROOT_URL + "downloadVendorImageAndroid?vendor=" + vendor.getEmail();
		Log.d(MYTAG, "URL is :" + url);
		return url;
	}
	
	public static String createMealImageUrl(Meal meal) 
	{
		CustomerServerUtils.removeCircularReferences(meal);
		String url = ROOT_URL + "downloadImageAndroid?vendor=" + meal.getId();
		Log.d(MYTAG, "URL is :" + url);
		return url;
	}
	
	
}



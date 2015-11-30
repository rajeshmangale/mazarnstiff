package com.rns.tiffeat.mobile.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import com.google.gson.Gson;

public class CoreServerUtils implements AndroidConstants{

	private static String GET_VENDORS_URL = GET_AREAS;

	//	public static String[] vendors = {""};

	//	public static String[] getAllVendors() 
	//	{
	//		Log.d("asas","raaju csuti1");
	//		List<String> vendorNames = retrieveVendorNames();
	//		if(CollectionUtils.isEmpty(vendorNames)) 
	//		{
	//			return vendors;
	//		}
	//		vendors = new String[vendorNames.size()];
	//		vendors = vendorNames.toArray(vendors);
	//		return vendors;
	//	}


	public static List<String> areaNames = new ArrayList<String>();

	public static List<String> retrieveVendorAreaNames() 
	{
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		ResponseEntity<String> responseEntity = serverCall(GET_VENDORS_URL,uriVariables,HttpMethod.POST);
		areaNames = new Gson().fromJson(responseEntity.getBody(), List.class);
		return areaNames;
	}

	public static ResponseEntity<String> serverCall(String url,Map<String, Object> uriVariables,HttpMethod method) {
		HttpHeaders requestHeaders = new HttpHeaders();
		//requestHeaders.setContentType(new MediaType("text", "xml"));
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, requestEntity,String.class,uriVariables);
		Log.d("Response Received ..", responseEntity.getBody());
		return responseEntity;
	}

}

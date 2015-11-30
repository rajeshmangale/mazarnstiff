package com.rns.tiffeat.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.web.bo.domain.Customer;

public class CustomerUtils implements AndroidConstants{


	public static void storeCurrentCustomer(Context context, Customer customer) {
		SharedPreferences prefs = context.getSharedPreferences(CUSTOMER_SHARED_CONTEXT, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(CUSTOMER_OBJECT, new Gson().toJson(customer));
		//editor.putBoolean(LOGGED_IN, true);
		editor.commit();
		Log.d(MYTAG, "Saved the Customer!!");
	}

	public static Customer getCurrentCustomer(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(CUSTOMER_SHARED_CONTEXT, Context.MODE_PRIVATE);
		String customerJson = prefs.getString(CUSTOMER_OBJECT, null);
		Customer customer = new Customer();
		if(customerJson != null) {
			customer = new Gson().fromJson(customerJson, Customer.class);
			Log.d(MYTAG, "Got the Customer!! "+customer);
		}
		return customer;
	}

	public static void logout(Context context) 
	{
		SharedPreferences prefs = context.getSharedPreferences(CUSTOMER_SHARED_CONTEXT, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(CUSTOMER_OBJECT, null);
		editor.commit();
		Log.d(MYTAG, "Logged Out!");
	}

	public static void clearFragmentStack(FragmentManager fragmentManager) 
	{


//		for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {    
//			fragmentManager.popBackStack();
		
	fragmentManager.popBackStack(null,fragmentManager.POP_BACK_STACK_INCLUSIVE);	
		//}
		
	}

	public static void nextFragment(Fragment fragment,FragmentManager fragmentManager,boolean addToStack) {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(addToStack)
			fragmentTransaction.addToBackStack(fragment.getClass().getName()).replace(R.id.container_body, fragment);
		else
			fragmentTransaction.replace(R.id.container_body, fragment);
		fragmentTransaction.commit();
	}

}

package com.rns.tiffeat.mobile.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.Meal;
import com.rns.tiffeat.web.bo.domain.Vendor;

public class CustomerServerUtils implements AndroidConstants {



	private static String result = "";

	public static String getVendorForArea(String pinCode) 
	{

		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put(PIN_CODE,pinCode);
		result = CoreServerUtils.serverCall(GET_VENDORS_FOR_AREA, uriVariables,HttpMethod.POST).getBody();
		Log.d(MYTAG, "Result of vendor search :" + result);
		return result;
	}

	public static String getMealsForVendor(Vendor vendor) 
	{

		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put(VENDOR_OBJECT,new Gson().toJson( vendor));
		Log.d(MYTAG, "Result of meal :" + result);
		result = CoreServerUtils.serverCall(GET_MEALS_FOR_VENDORS, uriVariables,HttpMethod.POST).getBody();
		Log.d(MYTAG, "Result of meal :" + result);
		return result;
	}

	public static String customerRegistration(Customer customer) 
	{

		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		removeCircularReferences(customer);
		uriVariables.put(CUSTOMER_OBJECT,new Gson().toJson( customer));
		Log.d(MYTAG, "Result of customer :" + result);
		result = CoreServerUtils.serverCall(CUSTOMER_REGISTRATION, uriVariables,HttpMethod.POST).getBody();
		Log.d(MYTAG, "Result of customer :" + result);
		return result;
	}


	public static String customerLogin(Customer customer) 
	{
		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		removeCircularReferences(customer);
		uriVariables.put(CUSTOMER_OBJECT,new Gson().toJson( customer));
		Log.d(MYTAG, "Result of customer :" + result);

		result = CoreServerUtils.serverCall(CUSTOMER_LOGIN_URL, uriVariables,HttpMethod.POST).getBody();
		Log.d(MYTAG, "Result of customer :" + result);
		return result;
	}

	public static String quickOrder(CustomerOrder customerOrderObject) 
	{

		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		Type type = new TypeToken<CustomerOrder>() {}.getType();
		removeCircularReferences(customerOrderObject);
		uriVariables.put(CUSTOMER_ORDER_OBJECT,new Gson().toJson(customerOrderObject,type));
		result = CoreServerUtils.serverCall(CUSTOMER_QUICK_ORDER_URL, uriVariables,HttpMethod.POST).getBody();
		return result;

	}

	public static String scheduledOrder(List<CustomerOrder> customerOrderObjects) 
	{
		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		Type type = new TypeToken<List<CustomerOrder>>() {}.getType();
		for(CustomerOrder customerOrderObject:customerOrderObjects) {
			removeCircularReferences(customerOrderObject);
		}
		uriVariables.put(CUSTOMER_ORDER_OBJECT,new Gson().toJson(customerOrderObjects,type));
		result = CoreServerUtils.serverCall(CUSTOMER_SCHEDULED_ORDER_URL, uriVariables,HttpMethod.POST).getBody();
		return result;

	}

	public static String customerGetMealAvailable(CustomerOrder customerOrderObject) 
	{

		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		Type type = new TypeToken<CustomerOrder>() {}.getType();

		removeCircularReferences(customerOrderObject);
		uriVariables.put(CUSTOMER_ORDER_OBJECT,new Gson().toJson( customerOrderObject,type));
		result = CoreServerUtils.serverCall(CUSTOMER_GETMEAL_URL, uriVariables,HttpMethod.POST).getBody();
		Log.d(MYTAG, "Result of Get Vailable Meals :" + result);
		return result;
	}

	public static Customer getCurrentCustomer(Customer customer) {
		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		removeCircularReferences(customer);
		uriVariables.put(CUSTOMER_OBJECT,new Gson().toJson(customer));
		result = CoreServerUtils.serverCall(GET_CURRENT_CUSTOMER_URL, uriVariables,HttpMethod.POST).getBody();
		Log.d(MYTAG, "Got the latest customer :" + result);
		return new Gson().fromJson(result, Customer.class);
	}


	public static void removeCircularReferences(CustomerOrder customerOrderObject) {
		if(customerOrderObject == null || customerOrderObject.getMeal() == null || customerOrderObject.getCustomer() == null) {
			return;
		}
		if(customerOrderObject.getMeal().getVendor() != null) {
			customerOrderObject.getMeal().getVendor().setMeals(null);	// -------
		}
		customerOrderObject.getCustomer().setPreviousOrders(null);
		customerOrderObject.getCustomer().setQuickOrders(null);
		customerOrderObject.getCustomer().setScheduledOrder(null);
	}



	public static void removeCircularReferences(Customer customerObject) {
		if(customerObject == null) {
			return;
		}

		removeCircularReferncesCustomerOrders(customerObject.getScheduledOrder());
		removeCircularReferncesCustomerOrders(customerObject.getPreviousOrders());
		removeCircularReferncesCustomerOrders(customerObject.getQuickOrders());
	}

	public static void removeCircularReferncesCustomerOrders(List<CustomerOrder> orders) {
		if(CollectionUtils.isEmpty(orders)) {
			return;
		}
		for(CustomerOrder order:orders) {
			order.setCustomer(null);
		}
	}

	public static String validateQuickOrder(CustomerOrder customerOrder) {
		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put(CUSTOMER_ORDER_OBJECT,new Gson().toJson(customerOrder));
		result = CoreServerUtils.serverCall(VALIDATE_CUSTOMER_URL, uriVariables,HttpMethod.POST).getBody();
		return result;
	}

	public static String changeOrder(CustomerOrder customerOrder) {
		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		removeCircularReferences(customerOrder);
		uriVariables.put(CUSTOMER_ORDER_OBJECT,new Gson().toJson(customerOrder));
		result = CoreServerUtils.serverCall(CHANGE_ORDER_URL, uriVariables,HttpMethod.POST).getBody();
		return result;
	}


	public static String cancelScheduleOrder(CustomerOrder customerOrder) {
		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		removeCircularReferences(customerOrder);
		uriVariables.put(CUSTOMER_ORDER_OBJECT,new Gson().toJson(customerOrder));
		result = CoreServerUtils.serverCall(CANCEL_ORDER_URL, uriVariables,HttpMethod.POST).getBody();
		return result;
	}

	public static String addToWallet(Customer currentCustomer) {
		final Map<String, Object> uriVariables = new HashMap<String, Object>();
		removeCircularReferences(currentCustomer);
		uriVariables.put(CUSTOMER_OBJECT,new Gson().toJson(currentCustomer));

		result = CoreServerUtils.serverCall(ADD_TO_WALLET_URL, uriVariables,HttpMethod.POST).getBody();
		return result;
	}

	public static void removeCircularReferences(Meal meal) 
	{
		if (meal== null) 
		{
			return;
		}
		if (meal.getVendor() == null) {
			return;
		}

		//meal.getVendor().setMeals(null);
		if (meal.getDinnerMenu() == null) {
			return;
		}

		if (meal.getLunchMenu()== null) {
			return;
		}

		meal.getLunchMenu().setMeal(null);
		meal.getDinnerMenu().setMeal(null);
	}

}

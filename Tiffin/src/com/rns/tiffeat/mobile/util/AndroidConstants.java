package com.rns.tiffeat.mobile.util;


public interface AndroidConstants {

	String GOOGLE_PROJECT_ID = " "; 					
	String RESPONSE_INVALID = "INVALID";
	String RESPONSE_OK = "OK";
	String VENDOR_OBJECT = "vendor";
	String TAG = "tiffEAT";
	String ROOT_URL="http://192.168.0.3:8080/tiffeat-web/";
	//String ROOT_URL="http://www.tiffeat.com/";
	//String ROOT_URL="http://www.itrmitra.com/";
	String CUSTOMER_OBJECT = "customer";	
	String CUSTOMER_ORDER_OBJECT = "customerOrderObject";
	String CUSTOMERORDER_OBJECT = "customerOrder";
	String MEAL_OBJECT = "meal";

	String GET_VENDORS_FOR_AREA=ROOT_URL+"getVendorsForAreaAndroid?pinCode={pinCode}";
	String GET_MEALS_FOR_VENDORS=ROOT_URL+"getVendorMealsAndroid?vendor={vendor}";
	String CUSTOMER_REGISTRATION=ROOT_URL+"registerCustomerAndroid?customer={customer}";
	String CUSTOMER_QUICK_ORDER_URL=ROOT_URL+"quickOrderAndroid?customerOrder={customerOrderObject}";
	String CUSTOMER_SCHEDULED_ORDER_URL=ROOT_URL+"scheduledOrderAndroid?customerOrder={customerOrderObject}";
	String GET_AREAS=ROOT_URL+"getAreasAndroid.htm";
	String CUSTOMER_GETMEAL_URL=ROOT_URL+"getAvailableMealTypeAndroid?customerOrder={customerOrderObject}";
	String GET_CURRENT_CUSTOMER_URL = ROOT_URL  + "getCurrentCustomerAndroid?customer={customer}";
	String CUSTOMER_LOGIN_URL = ROOT_URL + "loginCustomerAndroid?customer={customer}";
	String PAYMENT_URL=ROOT_URL + "paymentAndroid?customerOrder={customerOrderObject}";
	String VALIDATE_CUSTOMER_URL=ROOT_URL + "validateQuickOrderAndroid?customerOrder={customerOrderObject}";
	String ADD_TO_WALLET_URL = ROOT_URL + "addToWalletAndroid?customer={customer}";
	String CANCEL_ORDER_URL=ROOT_URL + "cancelOrderAndroid?customerOrder={customerOrderObject}";
	String CHANGE_ORDER_URL=ROOT_URL + "changeOrderAndroid?customerOrder={customerOrderObject}";

	String CUSTOMER_SHARED_CONTEXT = "customerShared";
	String PIN_CODE = "pinCode";
	String REG_ID = "regId";
	String USER_PREFERENCES = "userPreferences";
	String LOGGED_IN = "loggedIn";
	String MYTAG="tiffeat-android";
	String FONT="Roboto-Regular.ttf";

}

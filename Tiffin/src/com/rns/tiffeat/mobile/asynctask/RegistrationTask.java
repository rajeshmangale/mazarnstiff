package com.rns.tiffeat.mobile.asynctask;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rns.tiffeat.mobile.QuickOrderFragment;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.ScheduledOrderFragment;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerServerUtils;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.MealFormat;
import com.rns.tiffeat.web.bo.domain.MealType;

public class RegistrationTask extends AsyncTask<String ,String , String> implements AndroidConstants
{

	private FragmentActivity mregistration;
	private ProgressDialog progressdialog;
	private CustomerOrder customerOrder;
	private String result1;
	private List<MealType> mealTypeList;
	private Customer customer;
	public RegistrationTask(FragmentActivity contxt,CustomerOrder customerOrder) {
		mregistration=contxt;
		this.customerOrder=customerOrder;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressdialog = new ProgressDialog(mregistration);
		progressdialog.setMessage("Please Wait...");
		progressdialog.setTitle("Download Data ");
		progressdialog.setCancelable(false);
		progressdialog.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String doInBackground(String... args) 
	{

		String resultRegistration =  CustomerServerUtils.customerRegistration(customerOrder.getCustomer());
		try 
		{
			customer = new Gson().fromJson(resultRegistration, Customer.class);
			CustomerUtils.storeCurrentCustomer(mregistration, customer);
			
		} 
		catch (Exception e) 
		{					// com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING
			return resultRegistration;
		}
		
		result1 =  CustomerServerUtils.customerGetMealAvailable(customerOrder);		// ResourceAccessException
		
		Type typeMap=new TypeToken<Map<String,Object>>(){}.getType();

		Map<String, Object> customerorderavail = new HashMap<String, Object>();

		customerorderavail=new Gson().fromJson(result1, typeMap);

		mealTypeList = (List<MealType>) customerorderavail.get("mealType");
		String customerOrderString = (String) customerorderavail.get("customerOrder");
		//customerOrder=(String) customerorderavail.get("customerOrder");
		
		customerOrder = new Gson().fromJson(customerOrderString, CustomerOrder.class);
		
		String mealtp=mealTypeList.toString();
		
		if(mealtp.contains("LUNCH") && mealtp.contains("DINNER"))
			customerOrder.setMealType(MealType.BOTH);	
		else if(mealtp.contains("LUNCH"))
			customerOrder.setMealType(MealType.LUNCH);
		else if(mealtp.contains("DINNER"))
			customerOrder.setMealType(MealType.DINNER);
		
		return resultRegistration; 

	}


	@Override
	protected void onPostExecute(String result) 
	{
		super.onPostExecute(result);
		progressdialog.dismiss();
		if(customer == null) 
		{
			Toast.makeText(mregistration, "Registration failed due to :" + result, Toast.LENGTH_LONG).show();
			return;
		}
		customerOrder.setCustomer(customer);
		nextActivity(); 
	}

	private void nextActivity() {

		String customerOrderobj=new Gson().toJson(customerOrder, CustomerOrder.class);

		Fragment fragment = null;		
		if(customerOrder.getMealFormat().equals(MealFormat.QUICK))
			fragment = new QuickOrderFragment(customerOrder);
		else if(customerOrder.getMealFormat().equals(MealFormat.SCHEDULED))
			fragment = new ScheduledOrderFragment(customerOrder);	
		//fragment = new QuickOrderFragment(customerOrder);

		Bundle bundle = new Bundle();

		bundle.putString("MyObject", customerOrderobj);

		fragment.setArguments(bundle);

//		FragmentManager fragmentManager = mregistration.getSupportFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		fragmentTransaction.replace(R.id.container_body, fragment);
//		fragmentTransaction.commit();
		
		
		CustomerUtils.nextFragment(fragment,mregistration.getSupportFragmentManager(),true);

	}

}
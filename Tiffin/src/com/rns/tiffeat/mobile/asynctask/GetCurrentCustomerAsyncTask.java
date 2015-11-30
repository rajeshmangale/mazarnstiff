package com.rns.tiffeat.mobile.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.DrawerActivity;
import com.rns.tiffeat.mobile.QuickOrderHomeScreen;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerServerUtils;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.web.bo.domain.Customer;

public class GetCurrentCustomerAsyncTask extends AsyncTask<String, String, Customer> {
	
	private QuickOrderHomeScreen quickOrderHome;
	private Context context;
	private ProgressDialog progressDialog;
	
	public GetCurrentCustomerAsyncTask(Context context,QuickOrderHomeScreen quickHome) {
		this.quickOrderHome = quickHome;
		this.context = context;
	}

	@Override
	protected Customer doInBackground(String... arg0) {
		Customer currentCustomer = CustomerUtils.getCurrentCustomer(context);
		Customer latestCustomer = currentCustomer;
		try {
			latestCustomer = CustomerServerUtils.getCurrentCustomer(currentCustomer);
		} catch (Exception e) {
		}
		return latestCustomer;
	}
	
	@Override
	protected void onPostExecute(Customer result) {
		super.onPostExecute(result);
		if(progressDialog!=null) {
			progressDialog.dismiss();
		}
		CustomerUtils.storeCurrentCustomer(context, result);
		if(quickOrderHome == null) {
			nextActivity(result);
		}
		else {
			quickOrderHome.setCustomer(result);
			quickOrderHome.prepareScreen();
		}
	}
	

	private void nextActivity(Customer customer) {
		Intent i = new Intent(context,DrawerActivity.class);
		i.putExtra(AndroidConstants.CUSTOMER_OBJECT, new Gson().toJson(customer));
		context.startActivity(i);
	
	}

}

package com.rns.tiffeat.mobile.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.ListOfMeals;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.Vendor;

public class GetMealsForVendorAsynctask extends AsyncTask<String, String, String> implements AndroidConstants{

	private FragmentActivity activity;
	private ListOfMeals listofmeals;
	private Vendor vendorobj;
	private CustomerOrder customerOrder;
	private	ProgressDialog progressDialog ;

	public GetMealsForVendorAsynctask(FragmentActivity myactivity, Vendor vendor, CustomerOrder customerOrder) {
		activity=myactivity;
		vendorobj=vendor;
		this.customerOrder=customerOrder;
	}


	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(activity);
		progressDialog.setTitle("Download Meals ");
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {

		String result= CustomerServerUtils.getMealsForVendor(vendorobj);

		return result;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		vendorobj = new Gson().fromJson(result, Vendor.class);

		progressDialog.dismiss();

		Fragment fobj=new ListOfMeals(vendorobj,customerOrder);
		activity.getSupportFragmentManager()
		.beginTransaction()
		.addToBackStack(fobj.getClass().getName())
		.add(R.id.container_body, new ListOfMeals(vendorobj,customerOrder),
				"" + fobj).commit();


	}

}

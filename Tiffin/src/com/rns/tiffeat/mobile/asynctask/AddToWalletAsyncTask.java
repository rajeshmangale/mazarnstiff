package com.rns.tiffeat.mobile.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.ScheduledUser;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerServerUtils;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.web.bo.domain.Customer;

public class AddToWalletAsyncTask extends AsyncTask<String ,String , String> implements AndroidConstants{

	FragmentActivity activity;
	Customer currentCustomer;
	private ProgressDialog progressDialog;
	
	
	public AddToWalletAsyncTask(FragmentActivity activity, Customer customer) {
		this.activity = activity;
		this.currentCustomer = customer;
	}


	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(activity);
		progressDialog.setTitle("Scheduled order");
		progressDialog.setMessage("Preparing...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	
	@Override
	protected String doInBackground(String... arg0) {
		return CustomerServerUtils.addToWallet(currentCustomer);
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		Log.d(MYTAG, "After add to wallet ..:" + result);
		nextActivity();
	}
	
	private void nextActivity() 
	{
		
		Fragment fobj=new ScheduledUser(currentCustomer,false);
		CustomerUtils.nextFragment(fobj, activity.getSupportFragmentManager(), false);
//		activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_body, fobj,
//				"" + fobj).commit();
	}
	

}

package com.rns.tiffeat.mobile.asynctask;



import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.rns.tiffeat.mobile.FirstTimeUse;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.util.CoreServerUtils;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;

public class GetNewOrderAreaAsynctask extends AsyncTask<String, String, String> {

	//private static String url_login = "192.168.1.11:8080/tiffeat-web/getAreasAndroid";

	FragmentActivity mneworder;
	CustomerOrder customerOrder;
	ProgressDialog pd ;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new ProgressDialog(mneworder);
		pd.setTitle("Download Vendors ");
		pd.setMessage("Loading...");
		pd.setCancelable(false);
		pd.show();
	}


	public GetNewOrderAreaAsynctask(FragmentActivity  splashScreen, CustomerOrder customerOrder2) 
	{

		mneworder=splashScreen;
		customerOrder=customerOrder2;

	}


	@Override
	protected String doInBackground(String... arg) {

		CoreServerUtils.retrieveVendorAreaNames();
		return "Hello";
	}

	protected void onPostExecute(String result) {
		Toast.makeText(mneworder, result, Toast.LENGTH_LONG).show();
		pd.dismiss();
		
		Fragment fragment = null;
		fragment = new FirstTimeUse(customerOrder);
//		FragmentManager fragmentManager = mneworder.getSupportFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		fragmentTransaction.replace(R.id.container_body, fragment);
//		fragmentTransaction.commit();
		
		CustomerUtils.nextFragment(fragment,mneworder.getSupportFragmentManager() ,true);


	};

}

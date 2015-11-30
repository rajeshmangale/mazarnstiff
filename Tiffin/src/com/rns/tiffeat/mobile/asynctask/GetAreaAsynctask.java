package com.rns.tiffeat.mobile.asynctask;



import com.rns.tiffeat.mobile.DrawerActivity;
import com.rns.tiffeat.mobile.util.CoreServerUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GetAreaAsynctask extends AsyncTask<String, String, String> {


	Activity mSplashScreen;
	String action;

	ProgressDialog pd ;

	public GetAreaAsynctask(Activity splashScreen) 
	{

		mSplashScreen=splashScreen;

	}



	@Override
	protected String doInBackground(String... arg) {

		CoreServerUtils.retrieveVendorAreaNames();
		return "Hello";
	}

	protected void onPostExecute(String result) {
		Toast.makeText(mSplashScreen, result, Toast.LENGTH_LONG).show();
		Intent i = new Intent(mSplashScreen,DrawerActivity.class);

		mSplashScreen.startActivity(i);
		mSplashScreen.finish();
	};

}


package com.rns.tiffeat.mobile;



import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rns.tiffeat.mobile.asynctask.GetAreaAsynctask;
import com.rns.tiffeat.mobile.asynctask.GetCurrentCustomerAsyncTask;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.Customer;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SplashScreen extends AppCompatActivity 
{

	private Dialog networkDialog;
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		if(!Validation.isNetworkAvailable(context))
		{

			//			final Dialog dialog = new Dialog(context);
			//			dialog.setContentView(R.layout.networkconnection);
			//			dialog.setTitle("No Internet Connection");
			//
			//			Button dialogButton = (Button) dialog.findViewById(R.id.check_network_button);
			//			// if button is clicked, close the custom dialog
			//			dialogButton.setOnClickListener(new OnClickListener() 
			//			{
			//				@Override
			//				public void onClick(View v) 
			//				{
			//					Intent i = new Intent(android.provider.Settings.ACTION_SETTINGS);
			//					startActivity(i);
			//					dialog.dismiss();
			//				}
			//			});
			//
			//
			//
			//
			//
			//			dialog.show();

			Validation.checknetwork(context);

		}
		else
		{


			//			this.runOnUiThread(new Runnable() {
			//
			//				@Override
			//				public void run() 
			//				{
			//					AsyncTaskCall();
			//	//				finish();
			//
			//				}
			//			});

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							AsyncTaskCall(); 
							//add your code here
						}
					}, 1000);
					// finish();
				}


			});
			//finish();
		}


	}


	//	private boolean isNetworkAvailable(Context context2)
	//	{
	//		ConnectivityManager obj=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
	//		NetworkInfo objInfo=obj.getActiveNetworkInfo();
	//		return objInfo!=null&&objInfo.isConnected();
	//	}
	public void AsyncTaskCall()
	{
		Customer customer = CustomerUtils.getCurrentCustomer(getApplicationContext());
		if(TextUtils.isEmpty(customer.getEmail())) 
		{
			new GetAreaAsynctask(this).execute();

		}
		else 
		{
			new GetCurrentCustomerAsyncTask(this,null).execute();
		}

	}





}

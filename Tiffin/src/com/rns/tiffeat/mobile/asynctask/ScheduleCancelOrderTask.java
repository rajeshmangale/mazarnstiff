package com.rns.tiffeat.mobile.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerServerUtils;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;

public class ScheduleCancelOrderTask extends AsyncTask<String ,String , String> implements AndroidConstants
{

	private FragmentActivity mscheduleorder;
	private ProgressDialog progressdialog;
	private CustomerOrder customerOrder;
	private Button cancel,switchordr,menu;
	private String result1;
	private Customer customer;	private TextView status,tiffmenu;
	
	
	public ScheduleCancelOrderTask(FragmentActivity contxt,CustomerOrder customerOrder, Button switchorder, Button cancelorder, 
			TextView tiffstatus, Button viewmenu, TextView tiffmenu) {
		mscheduleorder=contxt;
		this.customerOrder=customerOrder;
		switchordr=switchorder;
		cancel=cancelorder;
		menu=viewmenu;
		status=tiffstatus;
		this.tiffmenu=tiffmenu;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressdialog = new ProgressDialog(mscheduleorder);
		progressdialog.setMessage("Please Wait...");
		progressdialog.setTitle("Download Data ");
		progressdialog.setCancelable(false);
		progressdialog.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String doInBackground(String... args) 
	{

		result1=  CustomerServerUtils.cancelScheduleOrder(customerOrder);

		return result1; 

	}


	@Override
	protected void onPostExecute(String result) 
	{
		super.onPostExecute(result);
		progressdialog.dismiss();

		String result1=new Gson().fromJson(result, String.class);

		if(result1.equals("OK"))
		{
			Toast.makeText(mscheduleorder, "Cancel Order Successful !! " , Toast.LENGTH_LONG).show();
			switchordr.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
			menu.setVisibility(View.GONE);
			status.setText("Your Order has been CANCELLED");
			
			tiffmenu.setText("");
		}
		else 
			Toast.makeText(mscheduleorder, "Cancel failed due to :" + result, Toast.LENGTH_LONG).show();
		//nextActivity()
	}

	private void nextActivity() {

		String customerOrderobj=new Gson().toJson(customerOrder, CustomerOrder.class);

		Fragment fragment = null;		
		//fragment = new QuickOrderFragment(customerOrder);

		Bundle bundle = new Bundle();

		bundle.putString("MyObject", customerOrderobj);

		fragment.setArguments(bundle);

		FragmentManager fragmentManager = mscheduleorder.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.container_body, fragment);
		fragmentTransaction.commit();

	}

}
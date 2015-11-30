package com.rns.tiffeat.mobile.asynctask;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.DrawerActivity;
import com.rns.tiffeat.mobile.QuickOrderHomeScreen;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.ScheduledUser;
import com.rns.tiffeat.mobile.ShowMenuFragment;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerServerUtils;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;

public class GetMealMenuAsyncTask extends AsyncTask<String, String, Customer> {

	private String quickOrderHome,scheduledUser;
	private FragmentActivity context;
	private Customer customer;
	private CustomerOrder customerOrder;
	private ProgressDialog progressDialog;

	public GetMealMenuAsyncTask(FragmentActivity context, String quickHome,String scheduledUser, CustomerOrder customerOrder2) {
		this.quickOrderHome = quickHome;
		customer=new Customer();
		this.scheduledUser=scheduledUser;
		this.customerOrder=customerOrder2;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setIndeterminate(true);
		progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.progress_dialog_anim));

		progressDialog.setTitle("Contacting your vendor");
		progressDialog.setMessage("Getting today's menu..");
		progressDialog.setCancelable(false);
		progressDialog.show();
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
		progressDialog.dismiss();

		List<CustomerOrder> Orders=new ArrayList<CustomerOrder>();
		Fragment fragment = null;



		if(quickOrderHome != null)
		{

			Orders.addAll(result.getQuickOrders());
			for(int i=0 ; i< Orders.size(); i++)
			{
				if(Orders.get(i).getId()==customerOrder.getId())
				{
					customer=customerOrder.getCustomer();
					customerOrder=Orders.get(i);
					customerOrder.setCustomer(customer);
					break;
				}
			}

			fragment = new ShowMenuFragment(customerOrder);	

		}
		else if (scheduledUser!= null) {
			Orders.addAll(result.getScheduledOrder());

			for(int i=0 ; i< Orders.size(); i++)
			{
				if(Orders.get(i).getId()==customerOrder.getId())
				{
					customer=customerOrder.getCustomer();
					customerOrder=Orders.get(i);
					customerOrder.setCustomer(customer);
					break;
				}
			}


			fragment = new ShowMenuFragment(customerOrder);
		} 

		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.container_body, fragment);
		fragmentTransaction.commit();
	}

}
package com.rns.tiffeat.mobile.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.QuickOrderHomeScreen;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerServerUtils;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;


public class QuickOrderAsyncTask extends AsyncTask<String ,String , String> implements AndroidConstants
{

	private FragmentActivity proceedtopay;
	private ProgressDialog progressdialog;
	private CustomerOrder customerOrder;
	private Customer currentCustomer;
	public QuickOrderAsyncTask(FragmentActivity contxt,CustomerOrder customerOrder) {
		proceedtopay=contxt;
		this.customerOrder=customerOrder;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressdialog = new ProgressDialog(proceedtopay);
		progressdialog.setMessage("Please Wait...");
		progressdialog.setTitle("Download Data ");
		progressdialog.setCancelable(false);
		progressdialog.show();
	}

	@Override
	protected String doInBackground(String... args) 
	{

		String result =  CustomerServerUtils.quickOrder(customerOrder);
		//String result =  CustomerServerUtils.validateQuickOrder(customerOrder);
		result=new Gson().fromJson(result,String.class);
		if("OK".equals(result)) 
		{
			currentCustomer = CustomerServerUtils.getCurrentCustomer(customerOrder.getCustomer());
			CustomerUtils.storeCurrentCustomer(proceedtopay, currentCustomer);
		}
		return result; 

	}


	@Override
	protected void onPostExecute(String result) 
	{
		super.onPostExecute(result);
		progressdialog.dismiss();
		try {
			if("OK".equals(result))
			{
				Toast.makeText(proceedtopay, "Order Successfull! ",Toast.LENGTH_SHORT).show();
				if(currentCustomer != null) 
				{
					nextActivity();
				}
			}
			else 
			{
				Toast.makeText(proceedtopay, "Order Failed due to : " + result,Toast.LENGTH_SHORT).show();
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(proceedtopay, " Request failed ", Toast.LENGTH_LONG).show();
		}

	}

	private void nextActivity() 
	{

		Fragment fragment=new QuickOrderHomeScreen(currentCustomer);
//		FragmentManager supportFragmentManager = proceedtopay.getSupportFragmentManager();
//		FragmentTransaction transaction = supportFragmentManager.beginTransaction();
//		transaction.addToBackStack(proceedtopay.getClass().getName());
//		transaction.replace(R.id.container_body, fobj,"" + fobj).commit();
		
		CustomerUtils.nextFragment(fragment, proceedtopay.getSupportFragmentManager(),false);
	}

}
package com.rns.tiffeat.mobile.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.FirstTimeUse;
import com.rns.tiffeat.mobile.QuickOrderHomeScreen;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.ScheduledUser;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerServerUtils;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.Meal;
import com.rns.tiffeat.web.bo.domain.MealFormat;
import com.rns.tiffeat.web.bo.domain.MealStatus;

public class ScheduleChangeOrderTask extends AsyncTask<String, String, String>
		implements AndroidConstants {

	private FragmentActivity mchangeorder;
	private ProgressDialog progressdialog;
	private CustomerOrder customerOrder;
	private String result1;
	private Meal objmMeal, scheduleMeal;

	public Meal getScheduleMeal() {
		return scheduleMeal;
	}

	public void setScheduleMeal(Meal scheduleMeal) {
		this.scheduleMeal = scheduleMeal;
	}

	public ScheduleChangeOrderTask(FragmentActivity contxt,
			CustomerOrder customerOrder, Meal objmeal) {
		mchangeorder = contxt;
		this.customerOrder = customerOrder;
		this.objmMeal = objmeal;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressdialog = new ProgressDialog(mchangeorder);
		progressdialog.setMessage("Please Wait...");
		progressdialog.setTitle("Download Data ");
		progressdialog.setCancelable(false);
		progressdialog.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String doInBackground(String... args) {
		this.setScheduleMeal(customerOrder.getMeal());
		customerOrder.setMeal(objmMeal);
		result1 = CustomerServerUtils.changeOrder(customerOrder);

		return result1;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressdialog.dismiss();
		// if(customer == null)
		// {
		// Toast.makeText(mregistration, "Registration failed due to :" +
		// result, Toast.LENGTH_LONG).show();
		// return;
		// }
		String result1 = new Gson().fromJson(result, String.class);

		if (!result1.equals("OK")) {
			Toast.makeText(mchangeorder, "You can't order this meal!!",
					Toast.LENGTH_LONG).show();
			homeActivity();
		} else if (result1.equals("OK")) {
			Toast.makeText(mchangeorder, "Change Order Successful !! ",
					Toast.LENGTH_LONG).show();
			nextActivity();
		}

		// else
		// {
		// Toast.makeText(mchangeorder, "Order Change failed due to :" + result,
		// Toast.LENGTH_LONG).show();
		// customerOrder.setMeal(this.getScheduleMeal());
		// homeActivity();
		// }
	}

	private void homeActivity() {

		// Fragment fragment = null;
		// fragment = new ScheduledUser(customerOrder.getCustomer(),true);
		//
		// FragmentManager fragmentManager =
		// mchangeorder.getSupportFragmentManager();
		// FragmentTransaction fragmentTransaction =
		// fragmentManager.beginTransaction();
		// fragmentTransaction.replace(R.id.container_body, fragment);
		// fragmentTransaction.commit();

		Fragment fragment = null;
		fragment = new FirstTimeUse(customerOrder);

		// FragmentManager fragmentManager =
		// mchangeorder.getSupportFragmentManager();
		// FragmentTransaction fragmentTransaction =
		// fragmentManager.beginTransaction();
		// fragmentTransaction.replace(R.id.container_body, fragment);
		// fragmentTransaction.commit();

		CustomerUtils.nextFragment(fragment,
				mchangeorder.getSupportFragmentManager(), false);
	}

	private void nextActivity() {

		Customer customer = CustomerUtils.getCurrentCustomer(mchangeorder);

		Fragment fragment = null;
		fragment = new ScheduledUser(customer, true);

		// FragmentManager fragmentManager =
		// mchangeorder.getSupportFragmentManager();
		// FragmentTransaction fragmentTransaction =
		// fragmentManager.beginTransaction();
		// fragmentTransaction.replace(R.id.container_body, fragment);
		// fragmentTransaction.commit();

		CustomerUtils.nextFragment(fragment,
				mchangeorder.getSupportFragmentManager(), false);

	}

}
package com.rns.tiffeat.mobile.asynctask;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rns.tiffeat.mobile.PaymentGatewayFragment;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerServerUtils;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.MealType;
import com.rns.tiffeat.web.bo.domain.PaymentType;

public class ValidateQuickOrderAsyncTask extends AsyncTask<String ,String , String> implements AndroidConstants {

	private FragmentActivity previousActivity;
	private ProgressDialog progressdialog;
	private CustomerOrder customerOrder;
	private String validationResult;

	public ValidateQuickOrderAsyncTask(FragmentActivity activity, CustomerOrder customerOrder) {
		this.previousActivity = activity;
		this.customerOrder = customerOrder;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressdialog = new ProgressDialog(previousActivity);
		progressdialog.setMessage("Please Wait...");
		progressdialog.setTitle("Validating order");
		progressdialog.setCancelable(false);
		progressdialog.show();
	}

	@Override
	protected String doInBackground(String... arg0) {
		String result =  CustomerServerUtils.validateQuickOrder(customerOrder);
		//result=new Gson().fromJson(result,String.class);
		return result; 
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Type typeMap=new TypeToken<Map<String,Object>>(){}.getType();
		Map<String, Object> validateOrderMap = new HashMap<String, Object>();
		validateOrderMap=new Gson().fromJson(result, typeMap);
		validationResult = (String) validateOrderMap.get("result");
		List<MealType> mealTypeList = (List<MealType>) validateOrderMap.get("mealType");
		String customerOrderString = (String) validateOrderMap.get("customerOrder");
		customerOrder = new Gson().fromJson(customerOrderString, CustomerOrder.class);
		if("OK".equals(validationResult)) {
			nextActivity();
		}
		progressdialog.dismiss();
	}

	private void nextActivity() {

		if(customerOrder.getPaymentType().equals(PaymentType.NETBANKING))
		{
			Fragment fragment=new PaymentGatewayFragment(customerOrder);
//			previousActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new PaymentGatewayFragment(customerOrder),
//		
//					"" + fobj).commit();
			CustomerUtils.nextFragment(fragment,previousActivity.getSupportFragmentManager() , false);
		}
		
		else if(customerOrder.getPaymentType().equals(PaymentType.CASH))
		{
			new QuickOrderAsyncTask(previousActivity, customerOrder).execute("");
		
		}

		
	}


}

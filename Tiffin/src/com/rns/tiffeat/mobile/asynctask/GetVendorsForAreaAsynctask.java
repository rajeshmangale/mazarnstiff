package com.rns.tiffeat.mobile.asynctask;



import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rns.tiffeat.mobile.FirstTimeUse;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.Validation;
import com.rns.tiffeat.mobile.adapter.FirstTimeUsedAdapter;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerServerUtils;
import com.rns.tiffeat.mobile.util.GetMealsForVendorAsynctask;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.Vendor;

public class GetVendorsForAreaAsynctask extends AsyncTask<String, String, String>  implements AndroidConstants{

	private FragmentActivity myactivity;
	private	ProgressDialog progressDialog ;
	private ListView list;
	private List<Vendor> vendors;
	private GetMealsForVendorAsynctask getMealsForVendorAsynctask;
	private FirstTimeUse firstTimeUse;
	private CustomerOrder customerOrder;

	public GetVendorsForAreaAsynctask(FragmentActivity activity, ListView listview, FirstTimeUse firstTimeUse, CustomerOrder customerOrder) {

		this.firstTimeUse=firstTimeUse;
		myactivity=activity;
		list=listview;

		this.customerOrder=customerOrder;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(myactivity);
		progressDialog.setIndeterminate(true);
		progressDialog.setIndeterminateDrawable(myactivity.getResources().getDrawable(R.anim.progress_dialog_anim));
		progressDialog.setTitle("Download Vendors ");
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.show();



	}

	public List<Vendor> getVendors() {
		return vendors;
	}

	public void setVendors(List<Vendor> vendors) {
		this.vendors = vendors;
	}

	@Override
	protected String doInBackground(String... params) {

		String result= CustomerServerUtils.getVendorForArea(params[0]);
		return result;

	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		Type typelist=new TypeToken<ArrayList<Vendor>>(){}.getType();
		vendors = new Gson().fromJson(result, typelist);
		progressDialog.dismiss();
		prepareVendorListAdapter();
	}


	public void prepareVendorListAdapter() {
		FirstTimeUsedAdapter Adapter = new FirstTimeUsedAdapter (myactivity,R.layout.activity_first_time_used_adapter, getVendors());
		list.setVerticalFadingEdgeEnabled(true);
		list.setAdapter(Adapter);

		list.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{

				if(!Validation.isNetworkAvailable(myactivity))
				{
					Validation.checknetwork(myactivity);
				}
				else
				{	
					Vendor vendor = (Vendor) parent.getAdapter().getItem(position);

					getMealsForVendorAsynctask=new GetMealsForVendorAsynctask(myactivity,vendor,customerOrder);
					getMealsForVendorAsynctask.execute();				
				}
			}

		});
	}


}

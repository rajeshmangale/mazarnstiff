package com.rns.tiffeat.mobile;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rns.tiffeat.mobile.adapter.ListOfMealAdapter;
import com.rns.tiffeat.mobile.asynctask.ScheduleChangeOrderTask;
import com.rns.tiffeat.mobile.asynctask.ScheduledOrderAsyncTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.Meal;
import com.rns.tiffeat.web.bo.domain.MealFormat;
import com.rns.tiffeat.web.bo.domain.MealType;
import com.rns.tiffeat.web.bo.domain.Vendor;

public class ListOfMeals extends Fragment implements AndroidConstants {

	private ListView listview;
	private Vendor vendor;
	private List<Meal> meallist;
	private Meal objmeal;
	private Meal objmeal2;
	private TextView vendorname;
	private CustomerOrder customerOrder;

	public ListOfMeals(Vendor vendorobj, CustomerOrder customerOrder) {
		vendor = vendorobj;
		this.customerOrder = customerOrder;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_list_of_meals,
				container, false);

		objmeal = new Meal();
		objmeal2 = new Meal();

		if (!isNetworkAvailable()) {
			showdialog();
		} else {

			vendorname = (TextView) view
					.findViewById(R.id.list_of_meals_vendor_name_textView);
			objmeal.setVendor(vendor);

			vendorname.setText(vendor.getName() + "  offers : ");
			objmeal2 = objmeal;
			listview = (ListView) view
					.findViewById(R.id.list_of_meals_listView);
			ListOfMealAdapter Adapter = new ListOfMealAdapter(getActivity(),
					R.layout.activity_first_time_used_adapter, objmeal
							.getVendor().getMeals(), objmeal, customerOrder);
			listview.setAdapter(Adapter);

			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					if (!isNetworkAvailable()) {
						showdialog();
					}

					Vendor vendor = objmeal2.getVendor();
					meallist = objmeal2.getVendor().getMeals();
					objmeal2 = meallist.get(position);
					objmeal2.setVendor(vendor);

					Fragment fragment = null;

					if (customerOrder != null
							&& customerOrder.getMealFormat() != null) {
						if (customerOrder.getMealFormat().equals(
								MealFormat.SCHEDULED)) {

							if (customerOrder.getId() == 0) {
								Fragment fragment1 = null;
								fragment1 = new ScheduledOrderFragment(
										customerOrder);
								// FragmentManager fragmentManager =
								// getFragmentManager();
								// FragmentTransaction fragmentTransaction =
								// fragmentManager.beginTransaction();
								// fragmentTransaction.replace(R.id.container_body,
								// fragment1);
								// fragmentTransaction.commit();
								CustomerUtils.nextFragment(fragment1,
										getFragmentManager(), true);

							} else

								new ScheduleChangeOrderTask(getActivity(),
										customerOrder, objmeal2).execute();

						}
					} else {
						fragment = new SelectType(objmeal2, customerOrder);
						CustomerUtils.nextFragment(fragment,
								getFragmentManager(), true);

						// FragmentManager fragmentManager =
						// getFragmentManager();
						// FragmentTransaction fragmentTransaction =
						// fragmentManager.beginTransaction();
						// fragmentTransaction.replace(R.id.container_body,
						// fragment);
						// fragmentTransaction.commit();

					}

				}
			});

			view.setFocusableInTouchMode(true);
			view.requestFocus();
			view.setOnKeyListener(new View.OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub

					if (keyCode == KeyEvent.KEYCODE_BACK
							&& event.getAction() == KeyEvent.ACTION_UP) {
						getFragmentManager().popBackStack(null,
								FragmentManager.POP_BACK_STACK_INCLUSIVE);
						return true;
					} else
						return false;
				}
			});

		}
		return view;

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager obj = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo objInfo = obj.getActiveNetworkInfo();
		return objInfo != null && objInfo.isConnected();
	}

	void showdialog() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.networkconnection);
		dialog.setTitle("No Internet Connection");

		Button dialogButton = (Button) dialog
				.findViewById(R.id.check_network_button);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(android.provider.Settings.ACTION_SETTINGS);
				startActivity(i);
				dialog.dismiss();
			}
		});
		dialog.show();

	}

}

package com.rns.tiffeat.mobile;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.asynctask.ExistingUserAsyncTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.Meal;
import com.rns.tiffeat.web.bo.domain.MealFormat;
import com.rns.tiffeat.web.bo.domain.MealType;

public class SelectType extends Fragment implements AndroidConstants 
{

	private ImageView scheduled,quick;
	private Meal mealobj;
	private View view;
	private CustomerOrder customerOrder;
	private List<MealType> mealTypeList;
	private String result1;

	public SelectType(Meal objmeal) {

		mealobj=objmeal;

	}

	public SelectType(Meal objmeal, CustomerOrder customerOrder2) {
		mealobj=objmeal;
		customerOrder=customerOrder2;
	}


	public SelectType(CustomerOrder customerOrder2) 
	{
		customerOrder=customerOrder2;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);


	}	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.activity_select_type,
				container, false);

		if(!isNetworkAvailable())
		{
			showdialog();
		}
		else
		{

			initialise();


			quick.setOnClickListener(new OnClickListener() 
			{

				@Override
				public void onClick(View v) 
				{
					if(!isNetworkAvailable())
					{
						showdialog();
					}
					
					try{

						if(customerOrder.getCustomer().getName().toString()==null)
						{

						}
						else if(customerOrder.getMeal()==null)
						{
							customerOrder.setMeal(mealobj);
							customerOrder.setArea(mealobj.getVendor().getPinCode());

						}

					} catch (Exception e) {

						customerOrder=new CustomerOrder();
						customerOrder.setMeal(mealobj);
						customerOrder.setArea(mealobj.getVendor().getPinCode());

					}

					customerOrder.setMealFormat(MealFormat.QUICK);
					nextActivity();
				}

			});


			scheduled.setOnClickListener(new OnClickListener() 
			{

				@Override
				public void onClick(View v) 
				{

					if(!isNetworkAvailable())
					{
						showdialog();
					}
					try{

						if(customerOrder.getCustomer().getName().toString()==null)
						{

						}
						else if(customerOrder.getMeal()==null)
						{
							customerOrder.setMeal(mealobj);
							customerOrder.setArea(mealobj.getVendor().getPinCode());

						}


					} catch (Exception e) {

						customerOrder=new CustomerOrder();
						customerOrder.setMeal(mealobj);
						customerOrder.setArea(mealobj.getVendor().getPinCode());

					}

					customerOrder.setMealFormat(MealFormat.SCHEDULED);
					nextActivity();
				}


			});
		}
		return view;


	}

	private void initialise() {

		scheduled = (ImageView)view.findViewById(R.id.select_type_scheduled_imageView);
		quick = (ImageView)view.findViewById(R.id.select_type_quick_imageView);

		Bundle b=this.getArguments();
		if(b!=null)
		{
			String objmeal=b.getString("MymealObject");
			mealobj = new Gson().fromJson(objmeal, Meal.class);
		}

	}

	private void nextActivity() 
	{

		//String customerOrderobj=new Gson().toJson(customerOrder, CustomerOrder.class);

		Fragment fragment = null;		

		try{


			if(customerOrder.getCustomer().getName().toString()!=null)
			{

				new ExistingUserAsyncTask(getActivity(), customerOrder).execute();

			}

			else
			{
				fragment = new FirstTimeUse(customerOrder);
//				FragmentManager fragmentManager = getFragmentManager();
//				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//				fragmentTransaction.replace(R.id.container_body, fragment);
//				fragmentTransaction.commit();
				
				CustomerUtils.nextFragment(fragment, getFragmentManager(),false);
			}


		}
		catch (Exception e) 
		{
			fragment = new LoginFragment(customerOrder);
//			FragmentManager fragmentManager = getFragmentManager();
//			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			fragmentTransaction.replace(R.id.container_body, fragment);
//			fragmentTransaction.commit();
			CustomerUtils.nextFragment(fragment, getFragmentManager(),false);
			
		}

	}
	
	void showdialog()
	{
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.networkconnection);
		dialog.setTitle("No Internet Connection");

		Button dialogButton = (Button) dialog.findViewById(R.id.check_network_button);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(android.provider.Settings.ACTION_SETTINGS);
				startActivity(i);
				dialog.dismiss();
			}
		});
		dialog.show();

	}

	private boolean isNetworkAvailable()
	{
		ConnectivityManager obj=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo objInfo=obj.getActiveNetworkInfo();
		return objInfo!=null&&objInfo.isConnected();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
	    super.onActivityCreated(savedInstanceState);

	    FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), FONT);
	    fontChanger.replaceFonts((ViewGroup) this.getView());
	}
}

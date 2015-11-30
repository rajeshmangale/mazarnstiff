package com.rns.tiffeat.mobile;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.asynctask.GetAreaAsynctask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.Customer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class DrawerActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener,AndroidConstants 
{

	private static String TAG = DrawerActivity.class.getSimpleName();

	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;
	private Customer customer;
	private Boolean exit = false;

	@Override
	public void onBackPressed() 
	{
//		if (exit) {
//			finish(); // finish activity
//		} else {
//			Toast.makeText(this, "Press Back again to Exit.",
//					Toast.LENGTH_SHORT).show();
//			exit = true;
//			new Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					exit = false;
//				}
//			}, 3 * 1000);
//
//		}
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		int backcount = fragmentManager.getBackStackEntryCount();
		if (backcount>1)
		{
			super.onBackPressed();
			
		}
		else
			finish();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//getSupportActionBar().hide();
		setContentView(R.layout.activity_drawer);

		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		if(getIntent().getExtras()!=null)
		{
			String customerJson = (String) getIntent().getExtras().get(AndroidConstants.CUSTOMER_OBJECT);
			customer = new Gson().fromJson(customerJson, Customer.class);
		}
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);


		drawerFragment = (FragmentDrawer)
				getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragment.setDrawerListener(this);

		//customer = CustomerUtils.getCurrentCustomer(getApplicationContext());
		displayView(0);
	}

	public void onDrawerItemSelected(View view, int position) 
	{
		displayView(position);
	}

	@SuppressWarnings("unused")
	private void displayView(int position) {
		Fragment fragment = null;
		String title = getString(R.string.app_name);

		hideSoftKeyboard() ;		

		switch (position) {
		case 0:

			if(customer==null || TextUtils.isEmpty(customer.getEmail())) {
				fragment = new FirstTimeUse();
				title = getString(R.string.title_activity_first_time_use);
			}
			else if(customer.getScheduledOrder().size()>0)
			{
				fragment = new ScheduledUser(customer,false);
				title = "Home";
				FragmentManager fragmentManager = getSupportFragmentManager();
				CustomerUtils.clearFragmentStack(fragmentManager);
			}
			else if(customer.getQuickOrders().size()>0)
			{
				fragment = new QuickOrderHomeScreen(customer);
				title = "Home";
				FragmentManager fragmentManager = getSupportFragmentManager();
				CustomerUtils.clearFragmentStack(fragmentManager);
			}
			break;
		case 1:

			if(customer!=null)
			{
				if(customer.getQuickOrders().size()>0)
				{
					fragment = new QuickOrderHomeScreen(customer);
					title = "Home";
					FragmentManager fragmentManager = getSupportFragmentManager();
					CustomerUtils.clearFragmentStack(fragmentManager);
				}
			}else
			{
				Toast.makeText(getApplicationContext(), "Sorry You dont have order ", Toast.LENGTH_LONG).show();
				fragment = new FirstTimeUse();
			}
			//fragment = new ScheduledUser();
			//			title = getString(R.string.title_vendors);
			break;

		case 2:
			fragment = new TermsFragment();
			title = getString(R.string.nav_item_terms);
			break;

		case 3:
			fragment = new AboutUsFragment();
			title = getString(R.string.nav_item_terms);
			break;

		case 4:

			fragment = new  ContactusFragment();
			title = getString(R.string.nav_item_contactus);
			break;


		case 5:
			if(customer!=null)
			{

				CustomerUtils.logout(this);
				new GetAreaAsynctask(this).execute();
			}
			else{	
				Toast.makeText(getApplicationContext(), " You Are not Logged In  ", Toast.LENGTH_LONG).show();
				fragment = new FirstTimeUse();
			}
			break;	

		default:
			break;
		}

		if (fragment != null ) {
			title=" Tiffeat ";
			getSupportActionBar().setTitle(title);
			if(isFragmentToBeAddedToBackStack(fragment) ){
				CustomerUtils.nextFragment(fragment, getSupportFragmentManager(),false);
			}
			else{
				CustomerUtils.nextFragment(fragment, getSupportFragmentManager(),true);
			}


		}	

		
		
	}

	private boolean isFragmentToBeAddedToBackStack(Fragment fragment) {
		return fragment instanceof TermsFragment || fragment instanceof ContactusFragment || fragment instanceof AboutUsFragment;
	}
	public void setContentView(View view)
	{
		super.setContentView(view);

		FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), FONT);
		fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
	}

	public void hideSoftKeyboard() {
		if(getCurrentFocus()!=null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

}
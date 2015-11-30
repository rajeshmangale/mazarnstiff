package com.rns.tiffeat.mobile.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rns.tiffeat.mobile.QuickOrderHomeScreen;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.SelectType;
import com.rns.tiffeat.mobile.ShowMenuFragment;
import com.rns.tiffeat.mobile.asynctask.GetCurrentCustomerAsyncTask;
import com.rns.tiffeat.mobile.asynctask.GetMealMenuAsyncTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;

public class PreviousOrderListAdapter extends ArrayAdapter<CustomerOrder> implements AndroidConstants
{
	private FragmentActivity activity;
	private List<CustomerOrder> previousOrders;
	private Customer customer;
	private CustomerOrder customerOrder;
	private ViewHolder holder;
	private QuickOrderHomeScreen quickHome;
	String todaydate,orderdate;

	public void setQuickHome(QuickOrderHomeScreen quickHome) {
		this.quickHome = quickHome;
	}

	public ViewHolder getHolder() {
		return holder;
	}

	public class ViewHolder 
	{
		TextView title,tiffintype,price,date;
		ImageView foodimage;
		TextView repeatorderButton;
		boolean viewMenuClicked;

		public void setViewMenuClicked(boolean viewMenuClicked) {
			this.viewMenuClicked = viewMenuClicked;
		}

		public boolean isViewMenuClicked() {
			return viewMenuClicked;
		}

	}


	public PreviousOrderListAdapter(FragmentActivity activity,
			int activityQuickorderListAdapter,
			List<CustomerOrder> previousOrders, Customer customer) 
	{
		super(activity,activityQuickorderListAdapter,previousOrders);
		this.previousOrders=new ArrayList<CustomerOrder>();
		this.activity = activity;
		this.customer=customer;
		this.previousOrders.addAll(previousOrders);
		Collections.reverse(this.previousOrders);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		FontChangeCrawler fontChanger = new FontChangeCrawler(activity.getAssets(), FONT);
		
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.activity_previousorder_list_adapter, null);

			holder = new ViewHolder();
			fontChanger.replaceFonts((ViewGroup)convertView);
			holder.title = (TextView)convertView.findViewById(R.id.previousorder_list_adapter_name_textView);
			holder.tiffintype = (TextView)convertView.findViewById(R.id.previousorder_list_adapter_type_textView);
			holder.date = (TextView)convertView.findViewById(R.id.previousorder_list_adapter_date_textView);
			holder.foodimage=(ImageView)convertView.findViewById(R.id.previousorder_list_adapter_imageview);
			holder.repeatorderButton = (TextView)convertView.findViewById(R.id.previousorder_list_adapter_repeatorder_button);

			convertView.setTag(holder);

		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		customerOrder= previousOrders.get(position);

		holder.title.setText(customerOrder.getMeal().getTitle());
		holder.tiffintype.setText(customerOrder.getMealType().toString());

		DateFormat dt = new SimpleDateFormat("MM-dd-yyyy");

		orderdate  = dt.format(customerOrder.getDate());

		holder.date.setText(""+orderdate);

		if (holder.foodimage != null) {

			//	new ImageDownloaderTask(getActivity(),holder.foodimage).execute(objmeal.getImage());
		}


		holder.date.setText(""+orderdate);

		holder.repeatorderButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				customerOrder.setCustomer(customer);

				repeatActivity(customerOrder);

			}
		});

		return convertView;

	}


	private void repeatActivity(CustomerOrder customerOrder2) 
	{

		Fragment fragment = null;		
		fragment = new SelectType(customerOrder2);

		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.container_body, fragment);
		fragmentTransaction.commit();
	}
}
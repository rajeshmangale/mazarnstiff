package com.rns.tiffeat.mobile.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rns.tiffeat.mobile.QuickOrderHomeScreen;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.asynctask.GetMealMenuAsyncTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;

public class QuickOrderListAdapter extends ArrayAdapter<CustomerOrder> implements AndroidConstants
{
	private FragmentActivity activity;
	private List<CustomerOrder> quickOrders;
	private Customer customer;
	private CustomerOrder customerOrder ;
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
		TextView title,tiffintype,price,status,date;
		ImageView foodimage;
		TextView viewmenuButton;
		boolean viewMenuClicked;

		public void setViewMenuClicked(boolean viewMenuClicked) {
			this.viewMenuClicked = viewMenuClicked;
		}

		public boolean isViewMenuClicked() {
			return viewMenuClicked;
		}

	}


	public QuickOrderListAdapter(FragmentActivity activity,
			int activityQuickorderListAdapter,
			List<CustomerOrder> quickOrders, Customer customer) 
	{
		super(activity,activityQuickorderListAdapter,quickOrders);
		this.quickOrders=new ArrayList<CustomerOrder>();
		this.activity = activity;
		this.customer=customer;
		this.quickOrders.addAll(quickOrders);
		Collections.reverse(this.quickOrders);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		FontChangeCrawler fontChanger = new FontChangeCrawler(activity.getAssets(), FONT);
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.activity_quickorder_list_adapter, null);

			holder = new ViewHolder();
			fontChanger.replaceFonts((ViewGroup)convertView);
			holder.title = (TextView)convertView.findViewById(R.id.quickorder_list_adapter_name_textView);
			holder.tiffintype = (TextView)convertView.findViewById(R.id.quickorder_list_adapter_type_textView);
			holder.date = (TextView)convertView.findViewById(R.id.quickorder_list_adapter_date_textView);
			holder.foodimage=(ImageView)convertView.findViewById(R.id.quickorder_list_adapter_imageview);
			holder.status=(TextView)convertView.findViewById(R.id.quickorder_list_adapter_status_textView);
			//	holder.price = (TextView)convertView.findViewById(R.id.quickorderlist_tiffin_price_textView);
			holder.viewmenuButton = (TextView)convertView.findViewById(R.id.quickorder_list_adapter_viewmenu_button);

			convertView.setTag(holder);

		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		customerOrder= quickOrders.get(position);

		holder.title.setText(customerOrder.getMeal().getTitle());
		holder.tiffintype.setText(customerOrder.getMealType().toString());


		if (holder.foodimage != null) {

			//	new ImageDownloaderTask(getActivity(),holder.foodimage).execute(objmeal.getImage());
		}


		if(customerOrder.getStatus()!=null)
		{
			try 
			{
				if(customerOrder.getContent()!= null)
				{
					if(compareDate(customerOrder.getDate())== true)
					{		
						holder.status.setText(customerOrder.getMeal().toString());
					}
					else
						holder.status.setText("Menu is not Updated yet!!");
				}

			} 
			catch (ParseException e) 
			{

				holder.status.setText("Menu is not Updated yet!!");
			}
		}
		else
			holder.status.setText("PLEASE TRY AGAIN");

		DateFormat dt = new SimpleDateFormat("MM-dd-yyyy");
		holder.date.setText(dt.format(customerOrder.getDate()));

		holder.viewmenuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				ShowMenu(customerOrder);

			}
		});

		return convertView;

	}

	private boolean compareDate(Date date) throws ParseException 
	{
		DateFormat dt = new SimpleDateFormat("MM-dd-yyyy");
		Date currentdate = new Date();

		orderdate  = dt.format(date);
		todaydate = dt.format(currentdate); 

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Date d1 = sdf.parse(orderdate);
		Date d2 = sdf.parse(todaydate);

		if(d2.compareTo(d1)==0)
			return true;
		else
			return false;

	}

	//	private void repeatActivity(CustomerOrder customerOrder2) 
	//	{
	//
	//		Fragment fragment = null;		
	//		fragment = new SelectType(customerOrder2);
	//
	//		FragmentManager fragmentManager = activity.getSupportFragmentManager();
	//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	//		fragmentTransaction.replace(R.id.container_body, fragment);
	//		fragmentTransaction.commit();
	//	}
	//
	private void ShowMenu(CustomerOrder customerOrder2) 
	{
		customerOrder.setCustomer(customer);
		new GetMealMenuAsyncTask(activity,"QuickOrder",null,customerOrder2).execute();
	}

}
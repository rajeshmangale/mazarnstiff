package com.rns.tiffeat.mobile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;







import com.rns.tiffeat.mobile.adapter.PreviousOrderListAdapter;
import com.rns.tiffeat.mobile.adapter.QuickOrderListAdapter;
import com.rns.tiffeat.mobile.asynctask.GetAreaAsynctask;
import com.rns.tiffeat.mobile.asynctask.GetCurrentCustomerAsyncTask;
import com.rns.tiffeat.mobile.asynctask.GetMealMenuAsyncTask;
import com.rns.tiffeat.mobile.asynctask.GetNewOrderAreaAsynctask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;

public class QuickOrderHomeScreen extends Fragment implements AndroidConstants 
{
	private ListView todaylistview,previouslistview;
	private Customer customer;
	private GetCurrentCustomerAsyncTask getCurrentCustomerAsyncTask;
	private Button neworder,previousorder;
	private CustomerOrder customerOrder;
	private TextView welcomeText,previousText;
	private QuickOrderListAdapter quickOrdersAdapter;
	private PreviousOrderListAdapter previousOrderAdapter;
	private View view;	
	private LinearLayout linearLayout;
	private int flag=0;

	public QuickOrderHomeScreen(Customer currentCustomer) {
		this.customer = currentCustomer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{

		view = inflater.inflate(R.layout.fragment_quick_order_main_homescreen,container, false);

		customerOrder=new CustomerOrder();

		if(!isNetworkAvailable())
		{
			showdialog();
		}
		else
		{
			initialise();

			neworder.setOnClickListener(new OnClickListener() 
			{


				@Override
				public void onClick(View arg0) 
				{
					if(!isNetworkAvailable())
					{
						showdialog();
					}
					else
					{
						customerOrder.setCustomer(customer);
						newActivity(customerOrder);
					}
				}
			});

			previousorder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) 
				{
					if(!isNetworkAvailable())
					{
						showdialog();
					}
					if(customer.getPreviousOrders().size()==0)
					{
						previousText.setText("You Don't Have Any Previous Order ");
					}
					if(flag==0)
						repeatorderActivity(customer);
					else
					{
						flag=0;
						linearLayout.setVisibility(View.GONE);
						previousorder.setText("Show Previous Orders");
						welcomeText.setVisibility(View.VISIBLE);
					}
				}
			});
			prepareScreen();
		}
		return view;
	}

	private void initialise() 
	{
		todaylistview = (ListView)view.findViewById(R.id.quick_order_homescreen_today_order_listView);
		neworder = (Button)view.findViewById(R.id.quick_order_homescreen_neworder_button);
		welcomeText = (TextView) view.findViewById(R.id.quick_order_homescreen_textView);
		previousText=  (TextView) view.findViewById(R.id.quick_order_homescreen_previousorder_textView);

		quickOrdersAdapter = new QuickOrderListAdapter(getActivity(),R.layout.activity_quickorder_list_adapter,customer.getQuickOrders(),customer); 
		previousOrderAdapter=new PreviousOrderListAdapter(getActivity(),R.layout.activity_previousorder_list_adapter , customer.getPreviousOrders(), customer);

		previousorder=(Button) view.findViewById(R.id.quick_order_homescreen_previousorder_button);
		linearLayout=(LinearLayout) view.findViewById(R.id.quick_order_homescreen_linearlayout);
		previouslistview = (ListView)view.findViewById(R.id.quick_order_homescreen_previous_order_listView);

	}

	public void prepareScreen() {
		welcomeText.setText("Welcome " + customer.getName());
		quickOrdersAdapter.setQuickHome(this);
		previousOrderAdapter.setQuickHome(this);

		previousOrderAdapter.clear();
		quickOrdersAdapter.clear();

		quickOrdersAdapter.addAll(customer.getQuickOrders());
		previousOrderAdapter.addAll(customer.getPreviousOrders());

		todaylistview.setAdapter(quickOrdersAdapter);
		previouslistview.setAdapter(previousOrderAdapter);

		//		todaylistview.addHeaderView(neworder);
		//		todaylistview.addFooterView(previousorder);
		//		
		//		if(customer.getPreviousOrders()==null || customer.getPreviousOrders().size()==0)
		//			prevorder.setText("You Have No Previous Order");

	}

	private void newActivity(CustomerOrder customerOrder2) 
	{
		new GetNewOrderAreaAsynctask(getActivity(),customerOrder2).execute();
	}

	@Override
	public void onResume() {
		super.onResume();
		new GetCurrentCustomerAsyncTask(getActivity(),this).execute();
	}


	private boolean isNetworkAvailable()
	{
		ConnectivityManager obj=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo objInfo=obj.getActiveNetworkInfo();
		return objInfo!=null&&objInfo.isConnected();
	}

	private void ShowMenu(CustomerOrder customerOrder2) 
	{
		customerOrder2.setCustomer(customer);
		new GetMealMenuAsyncTask(getActivity(),"QuickOrder",null,customerOrder2).execute();
	}

	private void repeatorderActivity(Customer customer2) {

		previousorder.setText("View Today's Order");
		linearLayout.setVisibility(View.VISIBLE);
		flag=1;
		welcomeText.setVisibility(View.GONE);
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), FONT);
		fontChanger.replaceFonts((ViewGroup) this.getView());
	}
}

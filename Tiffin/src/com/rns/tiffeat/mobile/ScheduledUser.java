package com.rns.tiffeat.mobile;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rns.tiffeat.mobile.asynctask.GetMealMenuAsyncTask;
import com.rns.tiffeat.mobile.asynctask.GetNewOrderAreaAsynctask;
import com.rns.tiffeat.mobile.asynctask.ScheduleCancelOrderTask;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.Meal;
import com.rns.tiffeat.web.bo.domain.MealFormat;
import com.rns.tiffeat.web.bo.domain.MealStatus;
import com.rns.tiffeat.web.bo.domain.MealType;
import com.rns.tiffeat.web.bo.domain.OrderStatus;

public class ScheduledUser extends Fragment {

	private Button cancelorder, switchorder, viewmenu, edit, card2cancelorder,card2switchorder, card2viewmenu;
	private Customer customer;
	private View view;
	private TextView tiffmenu, tiffstatus, tiffschedulefrom, tiffintextview;
	private TextView tiffcard2menu, tiffcard2status, tiffcard2schedulefrom,
	tiffcard2textview;
	private CustomerOrder customerOrder;
	private EditText balanceEditText;
	private BigDecimal b1, b2 = BigDecimal.ZERO;
	List<CustomerOrder> schedulCustomerOrders;
	private boolean showAddToWalletDialog;
	private Dialog addToWalletDialog, networkDialog;
	private CardView card1,card2,card3;
	private int arraySize = 0,flagcheck=0;

	public ScheduledUser(Customer currentCustomer, boolean showAddToWallet) {
		this.customer = currentCustomer;
		this.showAddToWalletDialog = showAddToWallet;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		FragmentManager fragmentManager = getFragmentManager();
		CustomerUtils.clearFragmentStack(fragmentManager); 
		orderValidation();

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_scheduled_user, container,
				false);

		if (!isNetworkAvailable()) {
			showdialog();
		}
		// if(OrderStatus.CANCELLED.equals(customerOrder.getStatus()))
		// {
		// switchorder.setVisibility(View.GONE);
		// viewmenu.setVisibility(View.GONE);
		// cancelorder.setText("CANCELLED");
		//
		//
		// }
		else {
			initialise();

			if(flagcheck==1)
			{
				if(arraySize==1)
				{
					Toast.makeText(getActivity(), "Your Order is cancelled!!",
							Toast.LENGTH_SHORT).show();
					cancelorder.setVisibility(View.GONE);
					viewmenu.setVisibility(View.GONE);
					switchorder.setVisibility(View.GONE);
					//tiffintextview.setText("Your Tiffin is Cancelled");
				}
				else
				{
					Toast.makeText(getActivity(), "Your Order is cancelled!!",
							Toast.LENGTH_SHORT).show();
					card2cancelorder.setVisibility(View.GONE);
					card2viewmenu.setVisibility(View.GONE);
					card2switchorder.setVisibility(View.GONE);
				}
			}

			b1 = customer.getBalance();
			schedulCustomerOrders.addAll(customer.getScheduledOrder());

			for (int i = 0; i < schedulCustomerOrders.size(); i++) {
				CustomerOrder cOrder = new CustomerOrder();
				cOrder = schedulCustomerOrders.get(i);
				b2 = b2.add(cOrder.getMeal().getPrice());
			}
			try {

				if (b1.compareTo(b2) <= 0) {
					showwalletdialogbox();
				}

			} catch (Exception e) {
				// Toast.makeText(getActivity(), "Try Again",
				// Toast.LENGTH_LONG).show();
			}

			cancelorder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) 
				{


					if (!isNetworkAvailable()) 
					{
						showdialog();
					}

					alertbox(customerOrder.getMealStatus(),customerOrder,1);
				}

			});

			switchorder.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isNetworkAvailable()) {
						showdialog();
					} else {
						customerOrder = customer.getScheduledOrder().get(0);
						customerOrder.setCustomer(customer);
						nextActivity();
					}
				}
			});

			viewmenu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isNetworkAvailable()) {
						showdialog();
					} 
					else 
					{
						customerOrder = customer.getScheduledOrder().get(0);
						customerOrder.setCustomer(customer);
						ShowMenu(customerOrder);
					}
				}
			});

			edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CustomerOrder co = new CustomerOrder();
					co = customer.getScheduledOrder().get(arraySize-1);
					co.setMealFormat(MealFormat.SCHEDULED);
					co.setCustomer(customer);
					co.setId(0);
					if (co.getMealType().equals(MealType.DINNER)) 					
					{
						co.setMealType(MealType.LUNCH);

					} else
						co.setMealType(MealType.DINNER);
					// co.getCustomer().setOrderInProcess(co);
					newOrder(co);
				}

			});


			card2cancelorder.setOnClickListener(new OnClickListener() 
			{

				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					alertbox(customerOrder.getMealStatus(),customerOrder,2);
				}
			});

			card2switchorder.setOnClickListener(new OnClickListener() 
			{

				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub

					customerOrder = customer.getScheduledOrder().get(1);
					customerOrder.setCustomer(customer);
					nextActivity();
				}
			});

			card2viewmenu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) 
				{
					arraySize = 2;
					if (!isNetworkAvailable()) {
						showdialog();
					} 
					else 
					{

						customerOrder = customer.getScheduledOrder().get(1);
						customerOrder.setCustomer(customer);
						ShowMenu(customerOrder);
					}
				}
			});

		}
		return view;
	}

	private void newOrder(CustomerOrder customerOrder) {
		// TODO Auto-generated method stub
		new GetNewOrderAreaAsynctask(getActivity(), customerOrder).execute();

	}

	private void showwalletdialogbox() {
		customerOrder = new CustomerOrder();

		addToWalletDialog = new Dialog(getActivity());
		addToWalletDialog.setContentView(R.layout.activity_add_to_wallet);
		addToWalletDialog.setTitle("Add money to Wallet");
		addToWalletDialog.setCancelable(false);
		Button dialogAddAmt = (Button) addToWalletDialog
				.findViewById(R.id.add_amount_button);
		Button dialogAddLater = (Button) addToWalletDialog
				.findViewById(R.id.add_later_button);

		dialogAddAmt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addToWalletDialog.dismiss();
				customerOrder = new CustomerOrder();
				customerOrder.setMealFormat(MealFormat.SCHEDULED);
				if (balanceEditText.getText().toString().equals("")
						|| balanceEditText.getText().toString().length() == 0) {
					Toast.makeText(getActivity(), "Invalid amount!",
							Toast.LENGTH_LONG);
					return;
				}
				customer.setBalance(new BigDecimal(balanceEditText.getText()
						.toString()));
				customerOrder.setCustomer(customer);
				customerOrder.setMeal(new Meal());
				Fragment fobj = new PaymentGatewayFragment(customerOrder);
				getFragmentManager()
				.beginTransaction()
				.replace(R.id.container_body,
						new PaymentGatewayFragment(customerOrder),
						"" + fobj).commit();
			}

		});
		dialogAddLater.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				addToWalletDialog.dismiss();
			}
		});

		balanceEditText = (EditText) addToWalletDialog
				.findViewById(R.id.add_amount_edittext);
		addToWalletDialog.show();

	}

	private void alertbox(final MealStatus mealStatus,CustomerOrder customerOrder2, final int i)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				R.style.AppCompatAlertDialogStyle);
		builder.setTitle("Order Status");
		builder.setMessage("Are you sure you want to cancel your order?.");

		builder.setNegativeButton("No", null);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{

				// cancelorder.setVisibility(View.GONE);

				customerOrder.setCustomer(customer);
				if(i==1)
				{
					Toast.makeText(getActivity(), "1 !!",	Toast.LENGTH_SHORT).show();
					new ScheduleCancelOrderTask(getActivity(), customerOrder,
							switchorder, cancelorder, tiffstatus, viewmenu,
							tiffmenu).execute();
				}
				else if(i==2)
				{
					Toast.makeText(getActivity(), "2 !!",	Toast.LENGTH_SHORT).show();
					new ScheduleCancelOrderTask(getActivity(), customerOrder,
							card2switchorder,card2cancelorder, tiffcard2status,card2viewmenu,
							tiffcard2menu).execute();

				}
			}
		});

		builder.show();
	}

	private void initialise() {

		cancelorder = (Button) view
				.findViewById(R.id.scheduled_user_homescreen_cancel_button);
		switchorder = (Button) view
				.findViewById(R.id.scheduled_user_homescreen_switch_button);
		tiffschedulefrom = (TextView) view
				.findViewById(R.id.scheduled_user_homescreen_scheduled_from_textView);
		viewmenu = (Button) view
				.findViewById(R.id.scheduled_order_homescreen_viewmenu_button);
		tiffmenu = (TextView) view
				.findViewById(R.id.scheduled_user_homescreen_todays_menu_textView);
		tiffstatus = (TextView) view
				.findViewById(R.id.scheduled_user_homescreen_status_textView);
		edit = (Button) view
				.findViewById(R.id.scheduled_user_homescreen_edit_button);
		tiffintextview = (TextView) view
				.findViewById(R.id.scheduled_user_homescreen_status_textView);
		tiffcard2menu = (TextView) view
				.findViewById(R.id.scheduled_user_homescreen_card2_todays_menu_textView);
		tiffcard2status = (TextView) view
				.findViewById(R.id.scheduled_user_homescreen_card2_status_textView);
		tiffcard2schedulefrom = (TextView) view
				.findViewById(R.id.scheduled_user_homescreen_card2_scheduled_from_textView);
		// tiffcard2textview = (TextView)view.findViewById(R.id.sc)
		card2cancelorder = (Button) view
				.findViewById(R.id.scheduled_user_homescreen_card2_cancel_button);
		card2switchorder = (Button) view
				.findViewById(R.id.scheduled_user_homescreen_card2_switch_button);
		card2viewmenu = (Button) view
				.findViewById(R.id.scheduled_order_homescreen_card2_viewmenu_button);
		card2 = (CardView)view.findViewById(R.id.scheduled_user_homescreen_card2_cardView2);


		card3 = (CardView)view.findViewById(R.id.scheduled_user_homescreen_card2_cardView3);
		schedulCustomerOrders = new ArrayList<CustomerOrder>();
		gatherData();

	}

	private void gatherData() 
	{CustomerOrder customerOrder1 = new CustomerOrder();
	arraySize = customer.getScheduledOrder().size();
	if (arraySize == 1)
	{
		card3.setVisibility(View.GONE);
		card2.setVisibility(View.GONE);
		showCard1(customerOrder1);
	}
	else
	{
		showCard2(customerOrder1);
		showCard1(customerOrder1);
	}
	}

	private void showCard1(CustomerOrder customerOrder1) 
	{

		//CustomerOrder customerOrder1 = new CustomerOrder();

		customerOrder1 = customer.getScheduledOrder().get(0);
		tiffmenu.setText("Todays " + customerOrder1.getMealType() + " Menu of "
				+ customerOrder1.getMeal().getVendor().getName() + " :");
		if (customerOrder1.getMealStatus() == null
				|| MealStatus.PREPARE
				.equals(customerOrder1.getStatus()))
			tiffstatus.setText("Your " + customerOrder1.getMealType()
					+ " is yet to be cooked. You can cancel your order.");
		else
			tiffstatus.setText("Your " + customerOrder1.getMealType()
					+ " is in "
					+ customerOrder1.getMealStatus().toString()
					+ " stage.");
		if (!MealStatus.PREPARE.equals(customerOrder1.getMealStatus())) {
			switchorder.setVisibility(View.GONE);

			cancelorder.setVisibility(View.GONE);
		}

		if (MealType.DINNER.equals(customerOrder1.getMealStatus()))
			edit.setText("Order Lunch");
		else if (customerOrder1.getMealType().equals(MealType.LUNCH))
			edit.setText("Order Dinner");

		DateFormat dt = new SimpleDateFormat("MM-dd-yyyy");

		tiffschedulefrom.setText("Tiffin Scheduled From: "
				+ dt.format(customerOrder1.getDate()));
		//card2.setVisibility(View.VISIBLE);
	}

	private void showCard2(CustomerOrder customerOrder12) 
	{

		card3.setVisibility(View.VISIBLE);
		card2.setVisibility(View.VISIBLE);
		edit.setVisibility(View.GONE);
		//CustomerOrder customerOrder1 = new CustomerOrder();
		customerOrder12 = customer.getScheduledOrder().get(1);

		tiffcard2menu.setText("Todays " + customerOrder12.getMealType()
				+ " Menu of " + customerOrder12.getMeal().getVendor().getName()
				+ " :");
		if (customerOrder12.getStatus() == null
				|| MealStatus.PREPARE
				.equals(customerOrder12.getStatus()))
			tiffcard2status.setText("Your " + customerOrder12.getMealType()
					+ " is yet to be cooked. You can cancel your order.");
		else
			tiffcard2status.setText("Your " + customerOrder12.getMealType()
					+ " is in "
					+ customerOrder12.getMeal().toString()
					+ " stage.");

		if (!MealStatus.PREPARE.equals( customerOrder12.getMealStatus())) {
			card2switchorder.setVisibility(View.GONE);

			card2cancelorder.setVisibility(View.GONE);
		}

		//		if (customerOrder1.getMealType().equals(MealType.DINNER))
		//			edit.setText("Order Lunch");
		//		else if (customerOrder1.getMealType().equals(MealType.LUNCH))
		//			edit.setText("Order Dinner");

		DateFormat dt = new SimpleDateFormat("MM-dd-yyyy");

		tiffcard2schedulefrom.setText("Tiffin Scheduled From: "
				+ dt.format(customerOrder12.getDate()));


	}

	private boolean isNetworkAvailable() {
		ConnectivityManager obj = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo objInfo = obj.getActiveNetworkInfo();
		return objInfo != null && objInfo.isConnected();
	}

	private void nextActivity() {

		Fragment fragment = null;
		fragment = new FirstTimeUse(customerOrder);
		CustomerUtils.nextFragment(fragment, getFragmentManager(), true);
//		FragmentManager fragmentManager = getFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager
//				.beginTransaction();
//		fragmentTransaction.replace(R.id.container_body, fragment);
//		fragmentTransaction.commit();
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

	private void ShowMenu(CustomerOrder customerOrder2) {
		new GetMealMenuAsyncTask(getActivity(), null, "Scheduled",
				customerOrder2).execute();
	}

	private void orderValidation() {
		// TODO Auto-generated method stub

		// customerOrder= customer.getScheduledOrder().get(arraySize-1);
		// schedulCustomerOrders.addAll(customer.getScheduledOrder());
		if (customer.getScheduledOrder() == null
				|| customer.getScheduledOrder().size() == 0) {
			return;
		}

		for (CustomerOrder cOrder : customer.getScheduledOrder()) {
			if (cOrder.getStatus() == null) {
				continue;
			} else if (OrderStatus.CANCELLED.equals(cOrder.getStatus())) {

				flagcheck=1;

			} else if (OrderStatus.ORDERED.equals(cOrder.getStatus())) {
				return;
				// Toast.makeText(getActivity(),"You successfully ordered your tiffin!!",Toast.LENGTH_SHORT).show();
			}

			else if (OrderStatus.DELIVERED.equals(cOrder.getStatus())) {
				Toast.makeText(getActivity(),
						"Your Tiffin had been delivered. Please rate us!!",
						Toast.LENGTH_SHORT).show();
			}

			else if (OrderStatus.PAYABLE.equals(cOrder.getStatus())) {
				Toast.makeText(
						getActivity(),
						"You won't recieve todays tiffin due to insufficient balance.",
						Toast.LENGTH_SHORT).show();
			}

			else if (OrderStatus.NA.equals(cOrder.getStatus())) {
				Toast.makeText(
						getActivity(),
						"Your Order is not available for today.You orderd for "
								+ cOrder.getDate().toString(),
								Toast.LENGTH_SHORT).show();
			}

		}

	}

}
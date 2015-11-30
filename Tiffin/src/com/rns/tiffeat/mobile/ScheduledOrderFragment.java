package com.rns.tiffeat.mobile;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rns.tiffeat.mobile.asynctask.ScheduledOrderAsyncTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.MealType;

public class ScheduledOrderFragment extends Fragment implements OnClickListener,AndroidConstants
{

	private RadioButton lunch,dinner,both; 
	private EditText lunchaddr;
	private CheckBox sameaddr;
	private CustomerOrder customerOrder; 
	private TextView tiffindesc,name,emailid,phone,amount,wallet;
	private View rootView;
	private Button proceed;
	private List<MealType> mealtypelist;

	public ScheduledOrderFragment(CustomerOrder customerOrder) 
	{
		this.customerOrder=customerOrder;

	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_scheduled_order, container, false);

		if(!isNetworkAvailable())
		{
			Toast.makeText(getActivity(), "Check Your Internet Connection ",Toast.LENGTH_LONG).show();
		}
		else
		{


			initialise();

			Log.d(MYTAG, "oncreate ");

			lunch.setOnClickListener(this);
			dinner.setOnClickListener(this);
			both.setOnClickListener(this);
			proceed.setOnClickListener(this);
			/*sameaddr.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (((CheckBox) v).isChecked()) {
						dinneraddr.setText(""+lunchaddr.getText().toString());

					}
					else
					{
						dinneraddr.setText("");
					}
				}
			});*/
		}
		return rootView;
	}

	private void initialise( ) {


		lunch=(RadioButton) rootView.findViewById(R.id.scheduled_order_radioButton_lunch);
		dinner=(RadioButton) rootView.findViewById(R.id.scheduled_order_radioButton_dinner);
		both=(RadioButton) rootView.findViewById(R.id.scheduled_order_radioButton_both);

		lunchaddr=(EditText) rootView.findViewById(R.id.scheduled_order_editText_LunchAddress);
		//dinneraddr=(EditText) rootView.findViewById(R.id.scheduled_order_editText_DinnerAddress);

		sameaddr=(CheckBox) rootView.findViewById(R.id.scheduled_order_checkBox_sameasabove);

		tiffindesc=(TextView) rootView.findViewById(R.id.scheduled_order_editText_TiffinName);
		name=(TextView) rootView.findViewById(R.id.scheduled_order_editText_Name);
		emailid=(TextView) rootView.findViewById(R.id.scheduled_order_editText_Email);
		phone=(TextView) rootView.findViewById(R.id.scheduled_order_editText_Phoneno);
		amount=(TextView) rootView.findViewById(R.id.scheduled_order_editText_Price);
		proceed=(Button) rootView.findViewById(R.id.scheduled_order_proceed_button);
		wallet=(TextView) rootView.findViewById(R.id.scheduled_order_textview_Wallet);

		//String object=getActivity().getIntent().getExtras().getString("CustomerOrder");
		//customerOrder = new Gson().fromJson(object, CustomerOrder.class);

		customerData();
	}

	private void customerData() {

		tiffindesc.setText(customerOrder.getMeal().getTitle());
		name.setText(customerOrder.getCustomer().getName());
		emailid.setText(customerOrder.getCustomer().getEmail());;
		phone.setText(customerOrder.getCustomer().getPhone());;
		//amount.setText(customerOrder.getMeal().getPrice());

		if(customerOrder.getCustomer().getBalance()==null)
			wallet.setText(" 0 ");
		else
			wallet.setText(customerOrder.getCustomer().getBalance().toString()); 


		if(MealType.BOTH.equals(customerOrder.getMealType()))
		{
			both.setVisibility(View.VISIBLE);
			lunch.setVisibility(View.VISIBLE);
			dinner.setVisibility(View.VISIBLE);
		}
		else if(MealType.LUNCH.equals(customerOrder.getMealType()))
		{
			lunchaddr.setVisibility(View.VISIBLE);
			lunch.setVisibility(View.VISIBLE);
		}
		else if(MealType.DINNER.equals(customerOrder.getMealType()))
		{
			lunchaddr.setVisibility(View.VISIBLE);
			dinner.setVisibility(View.VISIBLE);
			lunchaddr.setHint("Dinner Address");
		}

	}


	@Override
	public void onClick(View view) {

		switch (view.getId()) 
		{
		case R.id.scheduled_order_radioButton_lunch:
			dinner.setChecked(false);
			both.setChecked(false);
			//dinneraddr.setVisibility(View.GONE);
			lunchaddr.setVisibility(View.VISIBLE);
			//sameaddr.setVisibility(View.GONE);

			break;

		case R.id.scheduled_order_radioButton_dinner:
			lunch.setChecked(false);
			both.setChecked(false);
			lunchaddr.setVisibility(View.VISIBLE);
			lunchaddr.setHint("Dinner Address");

			//sameaddr.setVisibility(View.GONE);

			//dinneraddr.setVisibility(View.VISIBLE);
			break;

		case R.id.scheduled_order_radioButton_both:
			dinner.setChecked(false);
			lunch.setChecked(false);

			lunchaddr.setVisibility(View.VISIBLE);
			lunchaddr.setHint("Enter Address");
			//sameaddr.setVisibility(View.VISIBLE);

			break;

		case R.id.scheduled_order_proceed_button:

			if(lunchaddr.getText().toString().equals("") )
				Toast.makeText(getActivity(), " Donot Leave Empty Field ", Toast.LENGTH_SHORT).show();
			else if(lunchaddr.getText().toString().length() <= 8  )
				Toast.makeText(getActivity(), " Enter Valid Address ", Toast.LENGTH_SHORT).show();
			else {
				new ScheduledOrderAsyncTask(prepareCustomerOrders(),getActivity()).execute();
			}
			break;

		default:
			break;
		}

	}

	private List<CustomerOrder> prepareCustomerOrders() {
		List<CustomerOrder> scheduledOrders = new ArrayList<CustomerOrder>();
		customerOrder.setAddress(lunchaddr.getText().toString());
		if(lunch!=null && lunch.isChecked()) {
			customerOrder.setMealType(MealType.LUNCH);
		}
		else if(dinner!=null && dinner.isChecked()) {
			customerOrder.setMealType(MealType.DINNER);
		}
		else if(both!=null && both.isChecked()) {
			CustomerOrder scheduledOrder = new CustomerOrder();
			scheduledOrder.setAddress(customerOrder.getAddress());
			scheduledOrder.setArea(customerOrder.getArea());
			customerOrder.setMealType(MealType.LUNCH);
			scheduledOrder.setMealType(MealType.DINNER);
			scheduledOrder.setMeal(customerOrder.getMeal());
			scheduledOrder.setCustomer(customerOrder.getCustomer());
			scheduledOrder.setDate(customerOrder.getDate());
			scheduledOrders.add(scheduledOrder);
		}
		scheduledOrders.add(customerOrder);
		return scheduledOrders;
	}


	private boolean isNetworkAvailable()
	{
		ConnectivityManager obj=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo objInfo=obj.getActiveNetworkInfo();
		return objInfo!=null&&objInfo.isConnected();
	}
}

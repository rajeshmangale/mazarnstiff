package com.rns.tiffeat.mobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rns.tiffeat.mobile.asynctask.ValidateQuickOrderAsyncTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.MealType;
import com.rns.tiffeat.web.bo.domain.PaymentType;

public class QuickOrderFragment extends Fragment implements OnClickListener,AndroidConstants{

	private RadioButton lunch,dinner,codpayment,onlinepayment; 
	private EditText lunchaddr;

	private Button proceed;
	private String lunchadress,dinneradress,bothaddress;
	private CustomerOrder customerOrder; 
	private TextView tiffindesc,name,emailid,phone,amount;
	private String customervendorname,customertiffindesc,customername,customeremailid,customerphone,customeramount;
	private View rootView;
	Context context;


	public QuickOrderFragment(CustomerOrder customerOrder) {
		this.customerOrder=customerOrder;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_quick_order, container, false);

		initialise();

		lunch.setOnClickListener(this);
		dinner.setOnClickListener(this);
		proceed.setOnClickListener(this);
		codpayment.setOnClickListener(this);
		onlinepayment.setOnClickListener(this);

		return rootView;

	}
	private void initialise( ) {


		lunch=(RadioButton) rootView.findViewById(R.id.quick_order_screen_radioButton_lunch);
		dinner=(RadioButton) rootView.findViewById(R.id.quick_order_screen_radioButton_dinner);
		codpayment=(RadioButton) rootView.findViewById(R.id.quick_order_screen_radioButton_cashondelivery);
		onlinepayment=(RadioButton) rootView.findViewById(R.id.quick_order_screen_radioButton_onlinepayment);

		lunchaddr=(EditText) rootView.findViewById(R.id.quick_order_screen_editText_LunchAddress);
		tiffindesc=(EditText) rootView.findViewById(R.id.quick_order_screen_editText_TiffinName);
		name=(EditText) rootView.findViewById(R.id.quick_order_screen_editText_Name);
		emailid=(EditText) rootView.findViewById(R.id.quick_order_screen_editText_Email);
		phone=(EditText) rootView.findViewById(R.id.quick_order_screen_editText_Phoneno);
		amount=(EditText) rootView.findViewById(R.id.quick_order_screen_editText_Price);
		proceed=(Button) rootView.findViewById(R.id.quick_order_screen_proceed_button);

		//String object=getActivity().getIntent().getExtras().getString("CustomerOrder");
		//customerOrder = new Gson().fromJson(object, CustomerOrder.class);

		customerData();
	}

	private void customerData() {

		tiffindesc.setText(customerOrder.getMeal().getTitle());
		name.setText(customerOrder.getCustomer().getName());
		emailid.setText(customerOrder.getCustomer().getEmail());;
		phone.setText(customerOrder.getCustomer().getPhone());
		amount.setText(customerOrder.getMeal().getPrice().toString());

		if(customerOrder.getMealType().equals(MealType.BOTH))
		{
			lunch.setVisibility(View.VISIBLE);
			dinner.setVisibility(View.VISIBLE);
		}
		else if(customerOrder.getMealType().equals(MealType.LUNCH))
		{
			lunch.setVisibility(View.VISIBLE);
		}
		else if(customerOrder.getMealType().equals(MealType.DINNER))
		{
			dinner.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void onClick(View view) {

		switch (view.getId()) 
		{
		case R.id.quick_order_screen_radioButton_lunch:
			dinner.setChecked(false);

			lunchaddr.setVisibility(View.VISIBLE);
			lunchaddr.setHint("Lunch Address");

			break;

		case R.id.quick_order_screen_radioButton_dinner:
			lunch.setChecked(false);

			lunchaddr.setVisibility(View.VISIBLE);
			lunchaddr.setHint("Dinner Address");
			break;

		case R.id.quick_order_screen_proceed_button:

			InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(),0);

			if(lunchaddr.getText().toString().equals("") )
				Toast.makeText(getActivity(), " Do not Leave Empty Field ", Toast.LENGTH_SHORT).show();
			else if(lunchaddr.getText().toString().length() <= 8 )
				Toast.makeText(getActivity(), " Enter Valid Address ", Toast.LENGTH_SHORT).show();
			else if(codpayment.isChecked()==false && onlinepayment.isChecked()==false)
				Toast.makeText(getActivity(), " Select A Payment Method ", Toast.LENGTH_SHORT).show();
			else if(dinner.isChecked()==false && lunch.isChecked()==false)
				Toast.makeText(getActivity(), " Select Address ", Toast.LENGTH_SHORT).show();
			else {
				prepareCustomerOrder();
				new ValidateQuickOrderAsyncTask(getActivity(), customerOrder).execute();
			}
			break;

		case R.id.quick_order_screen_radioButton_cashondelivery:
			onlinepayment.setChecked(false);

			customerOrder.setPaymentType(PaymentType.CASH);
			break;

		case R.id.quick_order_screen_radioButton_onlinepayment:
			codpayment.setChecked(false);

			customerOrder.setPaymentType(PaymentType.NETBANKING);
			break;

		default:
			break;
		}

	}

	private void prepareCustomerOrder() 
	{
		customerOrder.setMealType(MealType.LUNCH);
		if(dinner.isChecked()) {
			customerOrder.setMealType(MealType.DINNER);
		}

		customerOrder.setAddress(lunchaddr.getText().toString());

		//customerOrder.setPaymentType(PaymentType.CASH);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), FONT);
		fontChanger.replaceFonts((ViewGroup) this.getView());
	}
}

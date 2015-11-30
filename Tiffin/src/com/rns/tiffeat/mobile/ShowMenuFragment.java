package com.rns.tiffeat.mobile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.MealFormat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ShowMenuFragment extends Fragment implements AndroidConstants{

	private Toolbar mToolbar;
	private Fragment fragment = null;
	private Button alertbtn;
	private TextView roti,sabji,rice,salad,extra,price;
	private View rootView;
	private CustomerOrder customerOrder;
	private String todaydate,orderdate;
	//	String d1 = customerOrder.getDate().toString();
	//	SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy");
	//	String today=   getToday("MM-dd-yyyy");
	//	Date date1,date2 ;
	//	public ShowMenuFragment() 
	//	{
	//
	//	}

	public ShowMenuFragment(CustomerOrder customerOrder2) 
	{
		customerOrder = new CustomerOrder();
		this.customerOrder=customerOrder2;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{

		rootView = inflater.inflate(R.layout.show_menu_fragment, container, false);

		initialise();
		if(customerOrder.getContent()!=null)
			try {
				if(compareDate(customerOrder.getDate())== true)
				{
					sabji.setText(customerOrder.getContent().getMainItem().toString());
					roti.setText(customerOrder.getContent().getSubItem1().toString());
					rice.setText(customerOrder.getContent().getSubItem2().toString());
					salad.setText(customerOrder.getContent().getSubItem3().toString());
					extra.setText(customerOrder.getContent().getSubItem4().toString());
				}
				else
				{
					sabji.setText("Menu not available yet..");
					roti.setText("");
					rice.setText("");
					salad.setText("");
					extra.setText("");

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}


		if(customerOrder == null || customerOrder.getContent() == null) 
		{
			sabji.setText("Menu not available yet..");
		}


		if(customerOrder.getMeal()!=null && customerOrder.getMeal().getPrice()!=null) 
		{
			price.setText(customerOrder.getMeal().getPrice().toString());
		}

		alertbtn.setOnClickListener(new OnClickListener() 
		{


			@Override
			public void onClick(View arg0) 
			{

				Fragment fragment = null;		
				if(customerOrder.getMealFormat().equals(MealFormat.QUICK))
					fragment = new QuickOrderHomeScreen(customerOrder.getCustomer());
				else
					fragment = new ScheduledUser(customerOrder.getCustomer(),true);

				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.container_body, fragment);
				fragmentTransaction.commit();


			}
		} );

		return rootView;
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

	public static String getToday(String format)
	{
		Date date = new Date();
		return new SimpleDateFormat(format).format(date);
	}
	private void initialise() 
	{
		roti=(TextView) rootView.findViewById(R.id.roti_status_tv);
		sabji=(TextView) rootView.findViewById(R.id.sabji_status_tv);
		rice=(TextView) rootView.findViewById(R.id.rice_status_tv);
		salad=(TextView) rootView.findViewById(R.id.salad_status_tv);
		extra=(TextView) rootView.findViewById(R.id.extra_status_tv);
		price=(TextView) rootView.findViewById(R.id.price_status_tv);

		mToolbar = (Toolbar) rootView.findViewById(R.id.tool_bar);
		alertbtn = (Button)rootView.findViewById(R.id.menu_done_button);

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
	    super.onActivityCreated(savedInstanceState);

	    FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), FONT);
	    fontChanger.replaceFonts((ViewGroup) this.getView());
	}
}

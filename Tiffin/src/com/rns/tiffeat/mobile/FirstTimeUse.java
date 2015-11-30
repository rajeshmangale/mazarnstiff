package com.rns.tiffeat.mobile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rns.tiffeat.mobile.adapter.AutocompleteTvAdapter;
import com.rns.tiffeat.mobile.asynctask.GetVendorsForAreaAsynctask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CoreServerUtils;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.MealFormat;

public class FirstTimeUse extends Fragment implements AndroidConstants
{

	private ListView listview;
	private View view;
	private Button searchvendor;
	private GetVendorsForAreaAsynctask getVendorsForAreaAsynctask;
	private String area;
	private CustomerOrder customerOrder;
	private AutoCompleteTextView actvAreas;	
	private TextView text,text1;
	private Typeface font ;

	public FirstTimeUse(CustomerOrder customerOrder) {
		this.customerOrder=customerOrder;
	}

	public FirstTimeUse() {
	}

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{

		view = inflater.inflate(R.layout.activity_first_time_use,
				container, false);

		if(!isNetworkAvailable())
		{
			showdialog();
		}
		else
		{

			initialise();

			if(customerOrder!=null && customerOrder.getMealFormat()!=null)
				if(customerOrder.getMealFormat().equals(MealFormat.SCHEDULED) && customerOrder.getId()!=0)
				{
					changeScheduleOrder();
				}

			searchvendor.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) 
				{

					if(!isNetworkAvailable())
					{
						showdialog();
					}
					else
					{	
						InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(),0);

						String area= actvAreas.getText().toString();
						if(area.length()>0 && area!="") {
							text.setVisibility(View.VISIBLE);
							getVendorsForAreaAsynctask = new GetVendorsForAreaAsynctask(getActivity(),listview,FirstTimeUse.this,customerOrder);
							getVendorsForAreaAsynctask.execute(area);

						} else
							Toast.makeText(getActivity(), "Please Enter Area ", Toast.LENGTH_SHORT).show();

					}
				}
			});
		}
		return view;

	}

	private void changeScheduleOrder()
	{

		actvAreas.setText(customerOrder.getArea());
		actvAreas.setEnabled(false);
		//searchvendor.setVisibility(View.GONE);

		getVendorsForAreaAsynctask = new GetVendorsForAreaAsynctask(getActivity(),listview,FirstTimeUse.this,customerOrder);
		getVendorsForAreaAsynctask.execute(area);

	}

	private void initialise() {

		
		actvAreas = (AutoCompleteTextView)view.findViewById(R.id.first_time_use_area_autoCompleteTextView);
		listview = (ListView)view.findViewById(R.id.first_time_used_listView);
		searchvendor=(Button) view.findViewById(R.id.first_time_use_search_button);

		text=(TextView) view.findViewById(R.id.first_time_area_textView);
		text1=(TextView) view.findViewById(R.id.first_time_heading_textView);
		
		
		getAreaName();

	}


	private void getAreaName() {
		AutocompleteTvAdapter adapter = new AutocompleteTvAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, CoreServerUtils.areaNames, FONT);
		//AutocompleteTvAdapter adapter = new AutocompleteTvAdapter(getActivity(),android.R.layout.simple_list_item_1,);
		actvAreas.setThreshold(1);
		actvAreas.setAdapter(adapter);

	}

	private boolean isNetworkAvailable()
	{
		ConnectivityManager obj=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo objInfo=obj.getActiveNetworkInfo();
		return objInfo!=null&&objInfo.isConnected();
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

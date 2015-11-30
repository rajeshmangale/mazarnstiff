package com.rns.tiffeat.mobile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;








import com.rns.tiffeat.mobile.asynctask.LoginAsyncTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.Customer;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.Meal;

public class LoginFragment extends Fragment implements AndroidConstants
{

	private Button submit;
	private TextView newuser;
	private View view;
	private Customer customer;
	private EditText email,password;
	private CustomerOrder customerOrder;

	public LoginFragment(CustomerOrder customerOrder2) {
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
		view = inflater.inflate(R.layout.activity_login,
				container, false);

		if(!isNetworkAvailable())
		{
			showdialog();
		}
		else
		{

			initialise();

			submit.setOnClickListener(new OnClickListener() 
			{

				@Override
				public void onClick(View v) 
				{
					InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(),0);
					
					if(!isNetworkAvailable())
					{
						showdialog();
					}
					if(email.getText().toString().length()>0 && password.getText().toString().length()>0)
					{

						
						customer.setEmail(email.getText().toString());
						customer.setPassword(password.getText().toString());
						customerOrder.setCustomer(customer);
						new LoginAsyncTask(getActivity(),customerOrder).execute();
					}
					else
						Toast.makeText(getActivity(), " Enter Valid Credentials ", Toast.LENGTH_SHORT).show();
				}
			});

			newuser.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Fragment fragment = null;
					fragment = new UserRegistration(customerOrder);
					CustomerUtils.nextFragment(fragment,getFragmentManager(),false);
					
//					FragmentManager fragmentManager = getFragmentManager();
//					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//					fragmentTransaction.replace(R.id.container_body, fragment);
//					fragmentTransaction.commit();


				}
			});
		}
		return view;

	}

	private void initialise() {
		customer=new Customer();
		submit = (Button)view.findViewById(R.id.login_submit_button);
		newuser=(TextView) view.findViewById(R.id.login_newuser_button);
		email=(EditText) view.findViewById(R.id.login_editText_email);
		password=(EditText) view.findViewById(R.id.login_editText_Password);
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

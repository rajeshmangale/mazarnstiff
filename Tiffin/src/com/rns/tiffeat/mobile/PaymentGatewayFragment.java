package com.rns.tiffeat.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.asynctask.AddToWalletAsyncTask;
import com.rns.tiffeat.mobile.asynctask.QuickOrderAsyncTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.MealFormat;

public class PaymentGatewayFragment extends Fragment implements AndroidConstants{

	private WebView paymentscreen;
	private View rootView;
	private CustomerOrder customerOrder;

	public  PaymentGatewayFragment(CustomerOrder customerOrder2) {
		this.customerOrder=customerOrder2;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_payuscreen, container, false);

		paymentscreen=(WebView) rootView.findViewById(R.id.payment_screen_webview);


		String url=new Gson().toJson(customerOrder); 		

		WebSettings webSettings = paymentscreen.getSettings();
		webSettings.setJavaScriptEnabled(true);
		WebViewClient webViewClient = new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) 
			{
				if(url!=null && url.contains("paymentAndroidResult.htm")) 
				{
					String[] urls = url.split("=");
					if(urls.length > 0 && "success".equals(urls[1]))
					{
						if(MealFormat.QUICK.equals(customerOrder.getMealFormat())) 
						{
							new QuickOrderAsyncTask(getActivity(), customerOrder).execute("");
						}
						else
						{
							new AddToWalletAsyncTask(getActivity(), customerOrder.getCustomer()).execute("");
						}
						return true;
					}
					else 
					{
						Log.d(MYTAG, "Payment failed!");
						return true;

					}
				}
				return false;
			}

		};
		paymentscreen.setWebViewClient(webViewClient);  

		paymentscreen.loadUrl(ROOT_URL + "paymentAndroid?customerOrder="+url);



		return rootView;
	}

}

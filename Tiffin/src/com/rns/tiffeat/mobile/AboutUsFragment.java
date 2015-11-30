package com.rns.tiffeat.mobile;

import com.rns.tiffeat.mobile.asynctask.AddToWalletAsyncTask;
import com.rns.tiffeat.mobile.asynctask.QuickOrderAsyncTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.CustomerUtils;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.MealFormat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class AboutUsFragment extends Fragment implements AndroidConstants {


	private WebView aboutweb;

	public AboutUsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_aboutus, container, false);

		aboutweb=(WebView) rootView.findViewById(R.id.aboutus_webview);

		WebSettings webSettings = aboutweb.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//setSupportActionBar(mToolbar);


		aboutweb.loadUrl("file:///android_asset/aboutUs.html");


		WebViewClient webViewClient = new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) 
			{
				if(url!=null && url.contains("tiffeat.com")) 
				{
					nextActivity();
					return true;

				}
				return false;
			}

		};
		aboutweb.setWebViewClient(webViewClient);  


		return rootView;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), FONT);
		fontChanger.replaceFonts((ViewGroup) this.getView());
	}

	private void nextActivity() 
	{

		Fragment fragment = null;		
		fragment = new FirstTimeUse();

//		FragmentManager fragmentManager = getFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		fragmentTransaction.replace(R.id.container_body, fragment);
//		fragmentTransaction.commit();
		
		CustomerUtils.nextFragment(fragment,getFragmentManager(),false);
	}

}

package com.rns.tiffeat.mobile;

import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class TermsFragment extends Fragment implements AndroidConstants
{

	private Toolbar mToolbar;
	private Fragment fragment = null;
	private WebView termsweb;

	public TermsFragment() {
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

		View rootView = inflater.inflate(R.layout.fragment_terms, container, false);
		mToolbar = (Toolbar) rootView.findViewById(R.id.tool_bar);

		termsweb=(WebView) rootView.findViewById(R.id.termsandcondition_webview);

		WebSettings webSettings = termsweb.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//setSupportActionBar(mToolbar);


		termsweb.loadUrl("file:///android_asset/terrms.html");
		
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), FONT);
		fontChanger.replaceFonts((ViewGroup) this.getView());
	}

}

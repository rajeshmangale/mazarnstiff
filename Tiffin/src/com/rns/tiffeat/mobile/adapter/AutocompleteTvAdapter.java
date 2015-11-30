package com.rns.tiffeat.mobile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AutocompleteTvAdapter extends ArrayAdapter<String> {

	private FragmentActivity activity;
	private int layout;
	private final Typeface tf;



	public AutocompleteTvAdapter(FragmentActivity activity,
			int simpleDropdownItem1line, List<String> areaNames, String fONT) {

		super(activity, simpleDropdownItem1line, areaNames);
		this.activity = activity;
		this.layout = simpleDropdownItem1line;
		tf = Typeface.createFromAsset(activity.getAssets(), fONT);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(layout, parent, false);

		TextView suggestion = (TextView) rowView.findViewById(R.id.text1);
		suggestion.setText(getItem(position).toString());
		suggestion.setTypeface(tf);


		return rowView;
	}
}

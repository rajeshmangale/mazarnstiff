package com.rns.tiffeat.mobile.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.asynctask.ImageDownloaderTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.Vendor;

public class FirstTimeUsedAdapter  extends ArrayAdapter<Vendor> implements AndroidConstants {


	private ArrayList<Vendor> vendors;
	private Activity activity; 

	public class ViewHolder {
		TextView name,mealtype,tiffinused;
		ImageView foodimage;
		RatingBar ratingbar;

		public ImageView getFoodimage() {
			return foodimage;
		}

		public void setFoodimage(ImageView foodimage) {
			this.foodimage = foodimage;
		}
	}

	public FirstTimeUsedAdapter(FragmentActivity activity,
			int activityFirstTimeUsedAdapter, List<Vendor> list) 
	{

		super(activity,activityFirstTimeUsedAdapter,list);

		this.vendors = new ArrayList<Vendor>();
		this.vendors.addAll(list);
		this.activity=activity;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		FontChangeCrawler fontChanger = new FontChangeCrawler(activity.getAssets(), FONT);
		
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.activity_first_time_used_adapter, null);

			holder = new ViewHolder();
		    fontChanger.replaceFonts((ViewGroup)convertView);
			holder.name = (TextView)convertView.findViewById(R.id.first_time_tiffinname_textView);
			holder.mealtype = (TextView)convertView.findViewById(R.id.first_time_tiffin_type_textView);
			holder.foodimage=(ImageView)convertView.findViewById(R.id.first_time_food1_imageView);
			ImageView vendorImageView = (ImageView)convertView.findViewById(R.id.first_time_food1_imageView);
			holder.foodimage= vendorImageView;
			new ImageDownloaderTask(holder,vendorImageView,getContext()).execute(this.getItem(position));
			holder.tiffinused=(TextView)convertView.findViewById(R.id.first_time_count_textView);
			holder.ratingbar=(RatingBar)convertView.findViewById(R.id.first_time_ratingBar1);
			convertView.setTag(holder);


		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}


		Vendor objmeal = vendors.get(position);

		holder.name.setText(objmeal.getName());

		holder.mealtype.setText("VEG");



		holder.tiffinused.setText("100");
		if(objmeal.getRating()==null)
		{
			objmeal.setRating(new BigDecimal(0));
		}

		holder.ratingbar.setRating(objmeal.getRating().floatValue());

		return convertView;

	}

}




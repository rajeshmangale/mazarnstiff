package com.rns.tiffeat.mobile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.SelectType;
import com.rns.tiffeat.mobile.UserRegistration;
import com.rns.tiffeat.mobile.asynctask.ImageDownloaderTask;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.FontChangeCrawler;
import com.rns.tiffeat.web.bo.domain.CustomerOrder;
import com.rns.tiffeat.web.bo.domain.Meal;
import com.rns.tiffeat.web.bo.domain.Vendor;

public class ListOfMealAdapter extends ArrayAdapter<Meal> implements AndroidConstants{


	private FragmentActivity activity;
	private List<Meal> meallist;
	private Meal objmeal;
	private CustomerOrder customerOrder;

	public class ViewHolder 
	{

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



	public ListOfMealAdapter(FragmentActivity activity,
			int activityFirstTimeUsedAdapter, List<com.rns.tiffeat.web.bo.domain.Meal> meallist, Meal objmeal, CustomerOrder customerOrder) {

		super(activity,activityFirstTimeUsedAdapter,meallist);
		this.meallist=new ArrayList<Meal>();
		this.activity=activity;		
		this.objmeal=objmeal;
		this.meallist.addAll(meallist);
		this.customerOrder=customerOrder;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		FontChangeCrawler fontChanger = new FontChangeCrawler(activity.getAssets(), FONT);

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.activity_list_of_meals_adapter, null);

			holder = new ViewHolder();
			fontChanger.replaceFonts((ViewGroup)convertView);
			holder.name = (TextView)convertView.findViewById(R.id.list_of_meals_veg_tiffin_textView);
			holder.mealtype = (TextView)convertView.findViewById(R.id.list_of_meals_veg_tiffin_meal_textView);
			holder.foodimage=(ImageView)convertView.findViewById(R.id.list_of_meals_food1_imageView);
			ImageView mealImageView = (ImageView)convertView.findViewById(R.id.list_of_meals_food1_imageView);
			holder.foodimage= mealImageView;
			//new ImageDownloaderTask(holder,mealImageView,getContext()).execute(this.getItem(position));
			holder.tiffinused=(TextView)convertView.findViewById(R.id.list_of_meals_count_textView1);

			convertView.setTag(holder);

		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		Vendor vendor = objmeal.getVendor();
		objmeal = meallist.get(position);
		objmeal.setVendor(vendor);
		holder.name.setText(objmeal.getTitle().toString());
		holder.mealtype.setText(objmeal.getDescription());

		//		
		//		if (holder.foodimage != null) 
		//		{
		//
		//				new ImageDownloaderTask(this,holder.foodimage).execute(objmeal.getImage());
		//		}


		holder.tiffinused.setText(""+objmeal.getPrice());

		//		holder.order.setOnClickListener(new OnClickListener() 
		//		{
		//
		//			@Override
		//			public void onClick(View v) {
		//
		//				Log.d(MYTAG, objmeal.toString());
		//				//String customerOrderobj=new Gson().toJson(objmeal, Meal.class);
		//
		//				Fragment fragment = null;		
		//
		//				fragment = new SelectType(objmeal,customerOrder);
		//
		//				/*Bundle bundle = new Bundle();
		//				//bundle.putString("MymealObject", customerOrderobj);
		//				fragment.setArguments(bundle);
		//				 */
		//				FragmentManager fragmentManager = activity.getSupportFragmentManager();
		//				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		//				fragmentTransaction.replace(R.id.container_body, fragment);
		//				fragmentTransaction.commit();
		//
		//
		//			}
		//		});

		return convertView;

	}

}





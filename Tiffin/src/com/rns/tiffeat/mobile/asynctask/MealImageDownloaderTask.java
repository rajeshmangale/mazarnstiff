package com.rns.tiffeat.mobile.asynctask;

import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.rns.tiffeat.mobile.R;
import com.rns.tiffeat.mobile.adapter.ListOfMealAdapter;
import com.rns.tiffeat.mobile.adapter.ListOfMealAdapter.ViewHolder;
import com.rns.tiffeat.mobile.util.AndroidConstants;
import com.rns.tiffeat.mobile.util.UserUtils;
import com.rns.tiffeat.mobile.util.VendorServerUtils;
import com.rns.tiffeat.web.bo.domain.Meal;

public class MealImageDownloaderTask extends AsyncTask<Meal, Void, Bitmap> {

	private Context mcont;
	private ImageView imageView;
	private ListOfMealAdapter adapter;
	private ListOfMealAdapter adapterml;
	private Meal meal;
	private ViewHolder holder;

	public MealImageDownloaderTask(ViewHolder holder, ImageView vendorImageView,Context context) 
	{
		this.holder = holder;
		this.imageView = vendorImageView;
		this.mcont = context;
	}

	public MealImageDownloaderTask(ViewHolder holder) {
		this.holder = holder;
	}

	

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}


	@Override
	protected Bitmap doInBackground(Meal... arg) {

		Bitmap bitmap = null;
		try 
		{
			bitmap = setimage(arg[0]);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.meal = arg[0];
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		//Toast.makeText(mcont, "Downloaded Image!!", Toast.LENGTH_LONG);
		if(result==null)
			imageView.setImageResource(R.drawable.food5);
		else{
			imageView.setImageBitmap(result);
			//UserUtils.scaleImage(imageView, mcont);
			UserUtils.scaleImage(imageView, result);
		}
		holder.setFoodimage(imageView);


		Log.d(AndroidConstants.MYTAG, "Downloaded the Image ..");
	}

	/*@Override
	protected void onPostExecute(String result) {

		int i=0;

		if(bitmap!=null)
		{
		iv1.setImageBitmap(bitmap);
		}
		else
		{
			iv1.setImageResource(R.drawable.food5);
		}
	}*/



	private Bitmap setimage(Meal meal) throws MalformedURLException, IOException {

		Bitmap bitmap=null;
		//input = new java.net.URL(VendorServerUtils.createVendorImageUrl(vendor)).openStream();
		bitmap = UserUtils.getBitmapFromURL(VendorServerUtils.createMealImageUrl(meal));

		return bitmap;
	}




}

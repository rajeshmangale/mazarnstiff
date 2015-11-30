package com.rns.tiffeat.mobile.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class UserUtils implements AndroidConstants
{

	public static Bitmap getBitmapFromURL(String urlString) 
	{
		try 
		{
			URL url = new URL(urlString );
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	//	public static Bitmap decodeInputstrem(InputStream is, Context context, int defaultDrawable ) 
	//	{
	//		
	//		 try {
	//	             is = (InputStream) new URL(url).getContent();
	//	            try 
	//	            {
	//	                return BitmapFactory.decodeStream(is);
	//	            } finally 
	//	            {
	//	                is.close();                    
	//	            }
	//	        } 
	//		 catch (Exception e) 
	//		 	{
	//	            return BitmapFactory.decodeResource(context.getResources(), defaultDrawable);
	//	        }
	//	    }
	//		try {
	//			is = new BufferedInputStream(is);
	//			BitmapFactory.Options o = new BitmapFactory.Options();
	//			o.inScaled = false;
	//			o.inJustDecodeBounds = true;
	//			BitmapFactory.decodeStream(is, null, o);
	//			is.mark(is.available());
	//			is.reset();
	//			// Find the correct scale value. It should be the power of 2.
	//			final int REQUIRED_SIZE = 70; // This is the max size of the bitmap
	//			// in kilobytes
	//			int width_tmp = o.outWidth, height_tmp = o.outHeight;
	//			int scale = 1;
	//			while (true) {
	//				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
	//					break;
	//				width_tmp /= 2;
	//				height_tmp /= 2;
	//				scale *= 2;
	//			}
	//
	//			// Decode with inSampleSize
	//			BitmapFactory.Options o2 = new BitmapFactory.Options();
	//			o2.inSampleSize = scale;
	//			Bitmap bitmap = BitmapFactory.decodeStream(is, null, o2);
	//			is.close();
	//			return bitmap;
	//		} catch (IOException e) 
	//		{
	//			e.printStackTrace();
	//			try 
	//			{
	//				Bitmap bmp = BitmapFactory.decodeStream(is);
	//				is.close();
	//				return bmp;
	//			} 
	//			catch (IOException e1) 
	//			{
	//				e1.printStackTrace();
	//				return BitmapFactory.decodeResource(context.getResources(), defaultDrawable);
	//			}
	//		}
	//	}

	public static InputStream openHttpConnection(String urlString) throws IOException {
		/*
		 * InputStream in = null; int response = -1;
		 * 
		 * URL url = new URL(urlString); URLConnection conn =
		 * url.openConnection();
		 * 
		 * if (!(conn instanceof HttpURLConnection)) throw new
		 * IOException("Not an HTTP connection");
		 * 
		 * try { HttpURLConnection httpConn = (HttpURLConnection) conn;
		 * httpConn.setAllowUserInteraction(false);
		 * httpConn.setInstanceFollowRedirects(true);
		 * httpConn.setRequestMethod("GET"); httpConn.connect();
		 * 
		 * response = httpConn.getResponseCode(); if (response ==
		 * HttpURLConnection.HTTP_OK) { in = httpConn.getInputStream(); } }
		 * catch (Exception ex) { throw new IOException("Error connecting"); }
		 */
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlString);
		HttpResponse response = httpClient.execute(request);
		return response.getEntity().getContent();
		// return in;
	}

	public static void scaleImage(ImageView view, Context context) {
		// Get the ImageView and its bitmap
		Drawable drawing = view.getDrawable();
		if (drawing == null) {
			return; // Checking for null & return, as suggested in comments
		}
		Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

		// Get current dimensions AND the desired bounding box
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int bounding = dpToPx(250, context);
		Log.i("Test", "original width = " + Integer.toString(width));
		Log.i("Test", "original height = " + Integer.toString(height));
		Log.i("Test", "bounding = " + Integer.toString(bounding));

		// Determine how much to scale: the dimension requiring less scaling is
		// closer to the its side. This way the image always stays inside your
		// bounding box AND either x/y axis touches it.
		float xScale = ((float) bounding) / width;
		float yScale = ((float) bounding) / height;
		float scale = (xScale <= yScale) ? xScale : yScale;
		Log.i("Test", "xScale = " + Float.toString(xScale));
		Log.i("Test", "yScale = " + Float.toString(yScale));
		Log.i("Test", "scale = " + Float.toString(scale));

		// Create a matrix for the scaling and add the scaling data
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		// Create a new bitmap and convert it to a format understood by the
		// ImageView
		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		width = scaledBitmap.getWidth(); // re-use
		height = scaledBitmap.getHeight(); // re-use
		BitmapDrawable result = new BitmapDrawable(scaledBitmap);
		Log.i("Test", "scaled width = " + Integer.toString(width));
		Log.i("Test", "scaled height = " + Integer.toString(height));

		// Apply the scaled bitmap
		view.setImageDrawable(result);

		// Now change ImageView's dimensions to match the scaled image
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
		params.width = width;
		params.height = height;
		view.setLayoutParams(params);

		Log.i("Test", "done");
	}

	private static int dpToPx(int dp, Context context) {
		float density = context.getResources().getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	public static void scaleImage(ImageView mImageView,Bitmap bitmap) {
		int intendedWidth = mImageView.getWidth();

		if(bitmap==null)
			return;

		// Gets the downloaded image dimensions
		int originalWidth = bitmap.getWidth();
		int originalHeight = bitmap.getHeight();

		// Calculates the new dimensions
		float scale = (float) intendedWidth / originalWidth;
		int newHeight = (int) Math.round(originalHeight * scale);

		// Resizes mImageView. Change "FrameLayout" to whatever layout
		// mImageView is located in.
		mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		mImageView.getLayoutParams().width = intendedWidth;
		mImageView.getLayoutParams().height = newHeight;
	}

}

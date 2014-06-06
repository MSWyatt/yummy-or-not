package com.androidproject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;


public class MainActivity extends Activity {

ImageView imgFavorite;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   Log.w("Debug", "Camera starting up.");
	   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	   startActivityForResult(intent, 0);
   }
	   @Override
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  Log.w("Debug", "Picture taken.");
		  Bitmap bp = (Bitmap) data.getExtras().get("data");
	      String saved = storeImage(bp, "dinner.jpg");
	      Log.w("Debug", "Calling asynchronous task with bitmap of dimensions " + bp.getWidth()+"x" + bp.getHeight());
	      new Uploader().execute(bp);
	      
	   }
	   private String storeImage(Bitmap image, String filename){
		   File path = Environment.getExternalStorageDirectory();
		   String fullpath = path + "/" + filename;
		   try{
			   
			   FileOutputStream fos = new FileOutputStream(fullpath);
			   BufferedOutputStream bos = new BufferedOutputStream(fos);
			   image.compress(CompressFormat.JPEG, 100, bos);
			   bos.flush();
			   bos.close();
			} catch (FileNotFoundException e) {
				Log.w("Debug", "Error saving image file: " + e.getMessage());
				return null;
			} catch (IOException e) {
				Log.w("Debug", "Error saving image file: " + e.getMessage());
				return null;
			}
		  Log.w("Debug", "File saved in " + fullpath + "."); 
		  return fullpath;
	   }   
	   public class Uploader extends AsyncTask<Bitmap, Integer, Boolean>
	   {
		   
		   @Override
		    protected Boolean doInBackground(Bitmap... params) {
				  try {
					   // Our initial request to get BlobURL
					   HttpClient client = new DefaultHttpClient();
					   HttpGet httpget = new HttpGet("http://yummyornot.appspot.com/Upload");
					   Log.w("Debug", "Attempting to get Http Response.");
					   HttpResponse response = client.execute(httpget);
					   Log.w("Debug", "Response received.");
					   HttpEntity urlEntity = response.getEntity();
					   String responseString = EntityUtils.toString(urlEntity, "UTF-8");
					   Log.w("Debug", "BlobURL received: " + responseString);
					   
					   // Our upload
					   HttpPost httppost = new HttpPost(responseString);
					   Bitmap image = params[0];
					   ByteArrayOutputStream baos = new ByteArrayOutputStream();
					   image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					   byte[] bytes = baos.toByteArray();
					   String photo = Base64.encodeToString(bytes, 0);
					   MultipartEntityBuilder builder = MultipartEntityBuilder.create();
					   builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					   builder.addBinaryBody("bytes", bytes);
					   Log.w("Debug", "photo contents = " + photo);
					   builder.addTextBody("photo", photo);
					   httppost.setEntity(builder.build());
					   Log.w("Debug", "Multipart Entity Builder created. Sending to BlobURL.");

					   HttpResponse res = client.execute(httppost);
					   Log.w("Debug", "Sent to BlobURL.");
					   urlEntity = res.getEntity();
					   String confirm = EntityUtils.toString(urlEntity, "UTF-8");
					   Log.w("Debug", "Response from ImgUpload Servlet: " + confirm);
					   
					  }
					  catch (Exception e){
						  Log.w("Debug", "Exception caught:" + e);
						  return false;
					  }
					  return true;
				   } 
			   

	   }
}
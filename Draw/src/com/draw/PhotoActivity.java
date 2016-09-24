package com.draw;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;


public class PhotoActivity extends Activity {

	private static final int ACTION_TAKE_PHOTO = 1;



	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private static final String LOG = "PhotoActivity";
	private ImageView mImageView;
	private Bitmap mImageBitmap;

	

	private String mCurrentPhotoPath;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

	
	
	
	
	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.app_name);
	}

	
	
	
	
	private File getAlbumDir() {
		File storageDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
			Log.i(LOG, storageDir.getAbsolutePath());
			if (storageDir != null) {
				if (! storageDir.mkdirs() || ! storageDir.isDirectory()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}
		return storageDir;
	}
	
	
	
	
	
	@SuppressLint("SimpleDateFormat") 
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}
	
	
	
	
	
	private File setUpPhotoFile() throws IOException {
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();	
		return f;
	}

	
	
	
	
	private void setPic() {
		

		
		SharedPreferences settings = getSharedPreferences(MainActivity.OPTIONS, 0);
		SharedPreferences.Editor editor = settings.edit();
	    editor.putString(MainActivity.PHOTO_LOC, mCurrentPhotoPath);
	    editor.commit();
		
		
	}
	
	private void goDraw(){
    	Intent intent = new Intent(this, DrawActivity.class);
		startActivity(intent);
		
	}
	

	
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}

	
	
	
	
	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) {
		case ACTION_TAKE_PHOTO:
			File f = null;
		
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
			break;

		default:
			break;			
		} // switch

		startActivityForResult(takePictureIntent, actionCode);
	}



	
	private void handleBigCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			
			setPic();
			galleryAddPic();
			goDraw();
			
			mCurrentPhotoPath = null;
		}

	}




@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_photo);

	//mImageView = (ImageView) findViewById(R.id.imageView1);
	mImageBitmap = null;
	


	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
		Log.i(LOG, "Froyo");
		mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
	} else {
		Log.i(LOG, "Base");
		mAlbumStorageDirFactory = new BaseAlbumDirFactory();
	}
	
	dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
}




@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
			handleBigCameraPhoto();
	
	}


//Some lifecycle callbacks so that the image can survive orientation change
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(
				savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? 
						ImageView.VISIBLE : ImageView.INVISIBLE
		);
		
	}



}

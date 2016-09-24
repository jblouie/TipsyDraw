package com.draw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class DrawActivity extends Activity implements SensorEventListener{
	
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	String mCurrentPhotoPath;

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	
	private static String LOGTAG = "MAIN";
	private String PhotoPath = "noPhoto";
	
	ImageView mImageView;
	
	public Bitmap.Config BIT_CONFIG;
	public  Bitmap BITMAP;
	public Canvas CANVAS = new Canvas();
	public Paint PAINT = new Paint();
	
	private boolean SensorActive = false; 
	private boolean clicktwo = false;
	private boolean FirstDraw = true;
	
	private int[] origin = {0,0};
	private int[] position = {0,0};
	private int displayX;
	private int displayY;
	private SensorManager manager;
	final private int[] ori = {0,0};
	final private int[] pos = {0,0};
	private Sensor sensor; 
	
	private static int Scale = 10;
	public int Stroke = 8;
	
	
	int nibSize;
	int paintColor;
	String colorName;
	ImageView nib;
	final public String SEC = "SecondActivity";
	
	float mPreviousX;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);
		getPrefs();
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			Log.i(LOGTAG, "Froyo");
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			Log.i(LOGTAG, "Base");
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}

		
		SensorActive = false; 
		clicktwo = false;
		FirstDraw = true;
		
		origin = ori;
		position = pos;
		
		Log.d(SEC, "onCreate");
		if (Build.VERSION.SDK_INT < 16) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}else{
			View decorView = getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(uiOptions);
			android.app.ActionBar actionBar = getActionBar();
			actionBar.hide();
		}
		Log.d(SEC, "After if: ");
		
		// Get the dimensions of the screen
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
    	displayX = size.x;
    	displayY = size.y;
		
		if(!PhotoPath.equals("noPhoto")){
			Log.d(SEC, "Get Photo Bitmap.. ");
			BITMAP = getPhoto();
			
			BIT_CONFIG = Bitmap.Config.ARGB_8888;
			BITMAP = BITMAP.copy(BIT_CONFIG, true);
			
		}else{
			
			int width = displayX;
	    	int height = displayY;
	    	
			Log.i(LOGTAG, "Bitmap dims: "+ width+","+ height);
		
			BIT_CONFIG = Bitmap.Config.ARGB_8888;
			BITMAP = Bitmap.createBitmap(width, height, BIT_CONFIG);
			
		}
		
		Log.d(SEC, "Have Bitmap.. ");
		
		
		CANVAS.setBitmap(BITMAP);
		
		Log.d(SEC, "Set Bitmap.. ");
		
		PAINT.setColor(paintColor);
		PAINT.setStrokeWidth(nibSize);
		PAINT.setStyle(Paint.Style.STROKE);
		PAINT.setStrokeJoin(Paint.Join.ROUND);
		PAINT.setStrokeCap(Paint.Cap.ROUND);
		
	    manager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    sensor = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		
	    

	   
		/*** DEBUG ***/
		int canvasWidth = CANVAS.getWidth();
    	int canvasHeigth = CANVAS.getHeight();
    	Log.i(LOGTAG, "Canvas dims: "+ canvasWidth+","+ canvasHeigth);
    	
	}
	@SuppressLint("NewApi")
	@Override
	protected void onResume(){
		super.onResume();
		getPrefs();
		Log.d(SEC, "onResume??");
		
		Log.i(SEC, "Pre-Manager");
		manager.registerListener(this, sensor,SensorManager.SENSOR_DELAY_GAME);
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
		Log.i(SEC, "Pre-BITMAP");
		iv.setImageBitmap(BITMAP);
		Log.i(SEC, "Post-BITMAP"); 
		
		iv.setOnTouchListener(new OnTouchListener(){
		        @Override
		        public boolean onTouch(View v, MotionEvent event){
		        	
		        	
		        int x = (int)event.getX();
		        int y = (int)event.getY();
		        //int pixel = BITMAP.getPixel(x,y);
		        
		        if(clicktwo == false){
       				//designate the designated spot as the origin 
		    		origin[0]=x;
       				origin[1]=y;
       				Log.i(LOGTAG, "position changed:"+x+","+y);
       				clicktwo = true;
       			}else{
       				switch (event.getAction()){ 
		       
		       		case MotionEvent.ACTION_DOWN:
		       			//Turn on the line drawer
	       				Log.i(LOGTAG, "Second Click + Held, SensorActive = true");
	       				if(SensorActive == false) mPreviousX = x;
	       				SensorActive = true; 	
			       		break;
			       		
		       		case MotionEvent.ACTION_UP:
		       			Log.i(LOGTAG, "Second Click released, SensorActive = false");
		       			//Turn off the line drawer
		       			SensorActive = false;
			    	   break;
			    	
		       		case MotionEvent.ACTION_MOVE:  
		       			float dx = x - mPreviousX;
		       			if (Math.abs(dx)>300){
		       				Context context = getApplicationContext();
		       				CharSequence text = "Drawing Saved! :)";
		       				int duration = Toast.LENGTH_SHORT;
		       				
		       				
		       				final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		       				toast.show();
		       				
		       				saveDraw();
		       			}
			    	   
       				}
       			}
		       return true;
		       }
		});
				
				
	}

	@Override
	public void onBackPressed(){
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
	}
	public void saveDraw() {
		boolean hasFile = true;
		try{
			File file = new File(PhotoPath);
			Log.i(LOGTAG, "PhotoPath: " + PhotoPath);
			BITMAP.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
		}catch (FileNotFoundException fnf){
			hasFile = false;
			Log.i(LOGTAG, "FILE NOT FOUND EXCEPTION");
		}
		if(!hasFile){
			try {
				Log.i(LOGTAG, "ABOUT TO CREATE FILE");
				File file = setUpPhotoFile();
				galleryAddPic();
				Log.i(LOGTAG, "FILE CREATED: " + file.getAbsolutePath());
				BITMAP.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
	
	}
	
	/*** DEBUG ***/
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
	    int width=iv.getWidth();
	    int height=iv.getHeight();
	    Log.i(LOGTAG, "image dims: "+width+","+height);
	}
	
	
	@Override
	protected void onPause(){
		super.onPause();
		manager.unregisterListener(this);
		clicktwo = false;
		SensorActive = false;
		FirstDraw = true;
		
	}
	
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void onSensorChanged(SensorEvent event) {
		float mag = (float) Math.sqrt(event.values[0]*event.values[0]+
					event.values[1]*event.values[1]);
		
			int y = Math.round(event.values[0]*Scale)*-1;
		    int x = Math.round(event.values[1]*Scale)*-1; 
		    int[] newpos = {position[0]+x, position[1]+y};
		       
		    if(SensorActive && mag>0.2){
		    	
		    	CANVAS.drawLine(position[0], position[1], newpos[0], newpos[1], PAINT);
		    	Log.i(LOGTAG, "drawing to: "+newpos[0]+","+newpos[1]);
		    	ImageView iv = (ImageView) findViewById(R.id.imageView1);
		    	iv.setImageBitmap(BITMAP);
		    	FirstDraw = false;
		    }
		    
		    if(FirstDraw) {
		    	position = origin;
		    	Log.i(LOGTAG, "onSensorChanged: position changed:"+position[0]+","+position[1]);
		    }else{
		    	position = newpos;
		    }
		    
		  
		    
	}
	
	//public void saveFile{
		
	//}
	
	private static int exifToDegrees(int exifOrientation) {        
	    if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; } 
	    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; } 
	    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }            
	    return 0;    
	 }
	
	public void getPrefs(){
		
		Log.d(SEC, "About to load preferences");
		SharedPreferences settings = getSharedPreferences(MainActivity.OPTIONS, 0);
		Log.d(SEC, "Loading paintColor");
		paintColor = settings.getInt(MainActivity.COLOR_CHOICE, 0);
		Log.d(SEC, "Paint color is " + paintColor);
		Log.d(SEC, "Loading colorName");
		colorName = settings.getString(MainActivity.COLOR_NAME, "");
		Log.d(SEC, "Color name is " + colorName);
		Log.d(SEC, "Loading nibSize");
		nibSize = settings.getInt(MainActivity.NIB_CHOICE, 0);
		Log.d(SEC, "Nib size is " + nibSize);
		PhotoPath = settings.getString(MainActivity.PHOTO_LOC, "");
		
		

		//GradientDrawable sd;
	    //nib = (ImageView) findViewById(R.id.markerPreview);
	    //sd = (GradientDrawable) nib.getDrawable();
	    //sd.setSize(nibSize, nibSize);
	    //sd.setColor(paintColor);
	    //sd.invalidateSelf();
		Log.d(SEC, "All done with getPrefs!");
	}

	private Bitmap getPhoto() {
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		//int targetW = mImageView.getWidth();
		//int targetH = mImageView.getHeight();
		
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		if( !PhotoPath.equals("noPhoto")){
			imageView.setBackgroundColor(Color.BLACK);
		}
		
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int targetW = size.x;
		int targetH = size.y;
		
		
		Log.d(SEC, "PhotoPath: " + PhotoPath);
		
		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(PhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
	    
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}
		scaleFactor = 2;

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;
		
		int rotation = 0;
		
		int orientation = 0;
		//try {
			//ExifInterface exif = new ExifInterface(PhotoPath);
			//rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
		//int rotationInDegrees = exifToDegrees(rotation);
	
		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(PhotoPath, bmOptions);
		
		int widthB = bitmap.getWidth();
		int heightB = bitmap.getHeight();
		int newWidth = targetW;
		int newHeight = targetH;
		
		Matrix matrix = new Matrix();
		
		
		float scaleWidth = ((float) newWidth) / widthB;
		float scaleHeight = ((float) newHeight) / heightB;
		

		if (widthB > heightB) {
			//matrix.postScale(scaleWidth, scaleHeight);
			matrix.postRotate(270);
			//rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, targetW, targetH, matrix, true);
		}
		
		//if( rotatedBitmap != null){
		//	bitmap = rotatedBitmap;
		//}
		
		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								bitmap.getWidth(), bitmap.getHeight(), matrix, true );
		
		bitmap = rotatedBitmap;
		
		/* Associate the Bitmap to the ImageView */
		//mImageView = (ImageView) findViewById(R.id.imageView1);
		//mImageView.setImageBitmap(bitmap);

		//mImageView.setVisibility(View.VISIBLE);
		
		return bitmap;
		
		
		
	}


	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.app_name);
	}

	
	
	
	
	private File getAlbumDir() {
		File storageDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
			Log.i(LOGTAG, storageDir.getAbsolutePath());
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

	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}



}

//Initial Page
//
/* Needs:
 * Selects color
 *  -color options
 *  -button designs
 *  -stores chosen color in preferences
 *  
 * Selects size
 *  -size options
 *  -stores chosen size in preferences
 *  
 * A line to demonstrate the line size & color? 
 *  -maybe the app name at top can be a line drawing that 
 *   changes line width/color according to preferences
 *  
 *  ^^Options on action bar??
 *  
 *  Draw on photo button
 *  
 *  Draw on blank button
 *   -press and hold for background color options??
 *  
 * Overall Design?
 * 
 * 
*/


package com.draw;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	static final public String LOG = "MainActivity";
	static final public String OPTIONS = "ColorSize";
	static final public String COLOR_CHOICE = "paintColor";
	static final public String COLOR_NAME = "colorName";
	static final public String NIB_CHOICE = "nibSize";
	static final public String PHOTO_LOC = "photodirectorypath";
	static private String noPhoto = "noPhoto";
	int nibSize = 10;
	int nibSmall = 20;
	int nibMed = 30;
	int nibLarge = 50;
	int nibXL = 80;
	int nibArea = 10;
	int paintColor = Color.rgb(0,0,0);
	ImageView nib;
	String colorName = "black";
	private TextView textViewColor;
	private TextView textViewSize;
	boolean noPrefs;

	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		if (Build.VERSION.SDK_INT < 16) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}else{
			View decorView = getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(uiOptions);
			android.app.ActionBar actionBar = getActionBar();
			actionBar.hide();
		}
        
        noPrefs = true;
        
	    nibSize = 18;
	    paintColor = Color.BLACK;
	    colorName = "none";
    	
		SharedPreferences settings = getSharedPreferences(OPTIONS, 0);
		SharedPreferences.Editor editor = settings.edit();
	    editor.putInt(COLOR_CHOICE, paintColor);
	    editor.putString(COLOR_NAME, colorName);
	    editor.putInt(NIB_CHOICE, nibSize);
	    editor.putString(PHOTO_LOC, "noPhoto");
	    editor.commit();
	    
	    updateView();
	    
        /*
        Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
        nibArea = (int)(Math.pow((width / 15), 2) * 3.14159);
        nibSmall = (int)Math.sqrt(nibArea / 3.14159);
        nibMed = (int)Math.sqrt((nibArea*2) / 3.14159);
        nibLarge = (int)Math.sqrt((nibArea*3) / 3.14159);
        nibXL = (int)Math.sqrt((nibArea*4) / 3.14159);
        
        Log.i(LOG, "onCreate Pre-redraw");
        // Adjusts the small size nib
        GradientDrawable s0;
	    nib = (ImageView) findViewById(R.id.small);
	    Log.i(LOG, "Nib assigned");
	    s0 = (GradientDrawable) nib.getDrawable();
	    s0.mutate();
	    Log.i(LOG, "Try the size...");
	    s0.setSize(nibSmall, nibSmall);
	    s0.invalidateSelf();
	    Log.i(LOG, "One done!");
	    // Adjusts the medium size nib
	    GradientDrawable s1;
	    nib = (ImageView) findViewById(R.id.medium);
	    s1 = (GradientDrawable) nib.getDrawable();
	    s1.mutate();
	    s1.setSize(nibMed, nibMed);
	    s1.invalidateSelf();
	    // Adjusts the large size nib
	    GradientDrawable s2;
	    nib = (ImageView) findViewById(R.id.large);
	    s2 = (GradientDrawable) nib.getDrawable();
	    s2.mutate();
	    s2.setSize(nibLarge, nibLarge);
	    s2.invalidateSelf();
	    // Adjusts the large size nib
	    GradientDrawable s3;
	    nib = (ImageView) findViewById(R.id.XL);
	    s3 = (GradientDrawable) nib.getDrawable();
	    s3.mutate();
	    s3.setSize(nibSmall, nibSmall);
	    s3.invalidateSelf();
	    */
	    
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	SharedPreferences settings = getSharedPreferences(MainActivity.OPTIONS, 0);
		paintColor = settings.getInt(COLOR_CHOICE, 0);
		nibSize = settings.getInt(NIB_CHOICE, 0);
		
		
		/*
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
        nibArea = (int)(Math.pow((width / 15), 2) * 3.14159);
        nibSmall = (int)Math.sqrt(nibArea / 3.14159);
        nibMed = (int)Math.sqrt((nibArea*2) / 3.14159);
        nibLarge = (int)Math.sqrt((nibArea*3) / 3.14159);
        nibXL = (int)Math.sqrt((nibArea*4) / 3.14159);
		
        Log.i(LOG, "onCreate Pre-redraw");
        // Adjusts the small size nib
        GradientDrawable s0;
	    nib = (ImageView) findViewById(R.id.small);
	    Log.i(LOG, "Nib assigned");
	    s0 = (GradientDrawable) nib.getDrawable();
	    s0.mutate();
	    Log.i(LOG, "Try the size...");
	    s0.setSize(nibSmall, nibSmall);
	    s0.invalidateSelf();
	    Log.i(LOG, "One done!");
	    // Adjusts the medium size nib
	    GradientDrawable s1;
	    nib = (ImageView) findViewById(R.id.medium);
	    s1 = (GradientDrawable) nib.getDrawable();
	    s1.mutate();
	    s1.setSize(nibMed, nibMed);
	    s1.invalidateSelf();
	    // Adjusts the large size nib
	    GradientDrawable s2;
	    nib = (ImageView) findViewById(R.id.large);
	    s2 = (GradientDrawable) nib.getDrawable();
	    s2.mutate();
	    s2.setSize(nibLarge, nibLarge);
	    s2.invalidateSelf();
	    // Adjusts the large size nib
	    GradientDrawable s3;
	    nib = (ImageView) findViewById(R.id.XL);
	    s3 = (GradientDrawable) nib.getDrawable();
	    s3.mutate();
	    s3.setSize(nibSmall, nibSmall);
	    s3.invalidateSelf();
	    */
		updateView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy(){
    	Log.i(LOG, "onDestroy");
    	
	    
    	super.onDestroy();
    }
    
    public void goDraw(View v){
    	
    	if(noPrefs){
    		SharedPreferences settings = getSharedPreferences(OPTIONS, 0);
    		SharedPreferences.Editor editor = settings.edit();
    	    editor.putInt(COLOR_CHOICE, Color.rgb(241, 181, 33));
    	    editor.putString(COLOR_NAME, "GOSLUGS!");
    	    editor.putInt(NIB_CHOICE, 150);
    	    editor.commit();
    		
    	}
    	
    	Intent intent = new Intent(this, DrawActivity.class);
		startActivity(intent);
    }
    
    public void goDrawPic(View v){
    	
    	if(noPrefs){
    		SharedPreferences settings = getSharedPreferences(OPTIONS, 0);
    		SharedPreferences.Editor editor = settings.edit();
    	    editor.putInt(COLOR_CHOICE, Color.rgb(241, 181, 33));
    	    editor.putString(COLOR_NAME, "GOSLUGS!");
    	    editor.putInt(NIB_CHOICE, 150);
    	    editor.commit();
    		
    	}
    	
    	Intent intent = new Intent(this, PhotoActivity.class);
		startActivity(intent);
    }
    
    public void updateView(){
    	//textViewColor = (TextView) findViewById(R.id.textView2);
    	//textViewSize = (TextView) findViewById(R.id.textView1);
    	
    	//textViewColor.setTextColor(paintColor);
    	//textViewSize.setTextSize(nibSize/2);
    	
    	if(colorName == "purple"){

            GradientDrawable s0;
    	    nib = (ImageView) findViewById(R.id.black);
    	    s0 = (GradientDrawable) nib.getDrawable();
    	    s0.mutate();
    	    s0.setStroke(20, Color.BLACK);
    	    s0.invalidateSelf();

    	    GradientDrawable s1;
    	    nib = (ImageView) findViewById(R.id.green);
    	    s1 = (GradientDrawable) nib.getDrawable();
    	    s1.mutate();
    	    s1.setStroke(20, Color.BLACK);
    	    s1.invalidateSelf();

    	    GradientDrawable s2;
    	    nib = (ImageView) findViewById(R.id.purple);
    	    s2 = (GradientDrawable) nib.getDrawable();
    	    s2.mutate();
    	    s2.setStroke(20, Color.RED);
    	    s2.invalidateSelf();

    	    GradientDrawable s3;
    	    nib = (ImageView) findViewById(R.id.orange);
    	    s3 = (GradientDrawable) nib.getDrawable();
    	    s3.mutate();
    	    s3.setStroke(20, Color.BLACK);
    	    s3.invalidateSelf();
    	}
    	else if(colorName == "black") {
            GradientDrawable s0;
    	    nib = (ImageView) findViewById(R.id.black);
    	    s0 = (GradientDrawable) nib.getDrawable();
    	    s0.mutate();
    	    s0.setStroke(20, Color.RED);
    	    s0.invalidateSelf();

    	    GradientDrawable s1;
    	    nib = (ImageView) findViewById(R.id.green);
    	    s1 = (GradientDrawable) nib.getDrawable();
    	    s1.mutate();
    	    s1.setStroke(20, Color.BLACK);
    	    s1.invalidateSelf();

    	    GradientDrawable s2;
    	    nib = (ImageView) findViewById(R.id.purple);
    	    s2 = (GradientDrawable) nib.getDrawable();
    	    s2.mutate();
    	    s2.setStroke(20, Color.BLACK);
    	    s2.invalidateSelf();

    	    GradientDrawable s3;
    	    nib = (ImageView) findViewById(R.id.orange);
    	    s3 = (GradientDrawable) nib.getDrawable();
    	    s3.mutate();
    	    s3.setStroke(20, Color.BLACK);
    	    s3.invalidateSelf();	
    	}else if(colorName == "green"){
            GradientDrawable s0;
    	    nib = (ImageView) findViewById(R.id.black);
    	    s0 = (GradientDrawable) nib.getDrawable();
    	    s0.mutate();
    	    s0.setStroke(20, Color.BLACK);
    	    s0.invalidateSelf();

    	    GradientDrawable s1;
    	    nib = (ImageView) findViewById(R.id.green);
    	    s1 = (GradientDrawable) nib.getDrawable();
    	    s1.mutate();
    	    s1.setStroke(20, Color.RED);
    	    s1.invalidateSelf();

    	    GradientDrawable s2;
    	    nib = (ImageView) findViewById(R.id.purple);
    	    s2 = (GradientDrawable) nib.getDrawable();
    	    s2.mutate();
    	    s2.setStroke(20, Color.BLACK);
    	    s2.invalidateSelf();

    	    GradientDrawable s3;
    	    nib = (ImageView) findViewById(R.id.orange);
    	    s3 = (GradientDrawable) nib.getDrawable();
    	    s3.mutate();
    	    s3.setStroke(20, Color.BLACK);
    	    s3.invalidateSelf();
    	}else if(colorName == "orange"){
            GradientDrawable s0;
    	    nib = (ImageView) findViewById(R.id.black);
    	    s0 = (GradientDrawable) nib.getDrawable();
    	    s0.mutate();
    	    s0.setStroke(20, Color.BLACK);
    	    s0.invalidateSelf();

    	    GradientDrawable s1;
    	    nib = (ImageView) findViewById(R.id.green);
    	    s1 = (GradientDrawable) nib.getDrawable();
    	    s1.mutate();
    	    s1.setStroke(20, Color.BLACK);
    	    s1.invalidateSelf();

    	    GradientDrawable s2;
    	    nib = (ImageView) findViewById(R.id.purple);
    	    s2 = (GradientDrawable) nib.getDrawable();
    	    s2.mutate();
    	    s2.setStroke(20, Color.BLACK);
    	    s2.invalidateSelf();

    	    GradientDrawable s3;
    	    nib = (ImageView) findViewById(R.id.orange);
    	    s3 = (GradientDrawable) nib.getDrawable();
    	    s3.mutate();
    	    s3.setStroke(20, Color.RED);
    	    s3.invalidateSelf();
    	}else{
            GradientDrawable s0;
    	    nib = (ImageView) findViewById(R.id.black);
    	    s0 = (GradientDrawable) nib.getDrawable();
    	    s0.mutate();
    	    s0.setStroke(20, Color.BLACK);
    	    s0.invalidateSelf();

    	    GradientDrawable s1;
    	    nib = (ImageView) findViewById(R.id.green);
    	    s1 = (GradientDrawable) nib.getDrawable();
    	    s1.mutate();
    	    s1.setStroke(20, Color.BLACK);
    	    s1.invalidateSelf();

    	    GradientDrawable s2;
    	    nib = (ImageView) findViewById(R.id.purple);
    	    s2 = (GradientDrawable) nib.getDrawable();
    	    s2.mutate();
    	    s2.setStroke(20, Color.BLACK);
    	    s2.invalidateSelf();

    	    GradientDrawable s3;
    	    nib = (ImageView) findViewById(R.id.orange);
    	    s3 = (GradientDrawable) nib.getDrawable();
    	    s3.mutate();
    	    s3.setStroke(20, Color.BLACK);
    	    s3.invalidateSelf();
    	}
    	
    	if(nibSize == nibSmall){

            GradientDrawable s0;
    	    nib = (ImageView) findViewById(R.id.small);
    	    s0 = (GradientDrawable) nib.getDrawable();
    	    s0.mutate();
    	    s0.setStroke(20, Color.RED);
    	    s0.invalidateSelf();

    	    GradientDrawable s1;
    	    nib = (ImageView) findViewById(R.id.medium);
    	    s1 = (GradientDrawable) nib.getDrawable();
    	    s1.mutate();
    	    s1.setStroke(20, Color.BLACK);
    	    s1.invalidateSelf();

    	    GradientDrawable s2;
    	    nib = (ImageView) findViewById(R.id.large);
    	    s2 = (GradientDrawable) nib.getDrawable();
    	    s2.mutate();
    	    s2.setStroke(20, Color.BLACK);
    	    s2.invalidateSelf();

    	    GradientDrawable s3;
    	    nib = (ImageView) findViewById(R.id.XL);
    	    s3 = (GradientDrawable) nib.getDrawable();
    	    s3.mutate();
    	    s3.setStroke(20, Color.BLACK);
    	    s3.invalidateSelf();
    	}
    	else if(nibSize == nibMed) {
            GradientDrawable s0;
    	    nib = (ImageView) findViewById(R.id.small);
    	    s0 = (GradientDrawable) nib.getDrawable();
    	    s0.mutate();
    	    s0.setStroke(20, Color.BLACK);
    	    s0.invalidateSelf();

    	    GradientDrawable s1;
    	    nib = (ImageView) findViewById(R.id.medium);
    	    s1 = (GradientDrawable) nib.getDrawable();
    	    s1.mutate();
    	    s1.setStroke(20, Color.RED);
    	    s1.invalidateSelf();

    	    GradientDrawable s2;
    	    nib = (ImageView) findViewById(R.id.large);
    	    s2 = (GradientDrawable) nib.getDrawable();
    	    s2.mutate();
    	    s2.setStroke(20, Color.BLACK);
    	    s2.invalidateSelf();

    	    GradientDrawable s3;
    	    nib = (ImageView) findViewById(R.id.XL);
    	    s3 = (GradientDrawable) nib.getDrawable();
    	    s3.mutate();
    	    s3.setStroke(20, Color.BLACK);
    	    s3.invalidateSelf();	
    	}else if(nibSize == nibLarge){
            GradientDrawable s0;
    	    nib = (ImageView) findViewById(R.id.small);
    	    s0 = (GradientDrawable) nib.getDrawable();
    	    s0.mutate();
    	    s0.setStroke(20, Color.BLACK);
    	    s0.invalidateSelf();

    	    GradientDrawable s1;
    	    nib = (ImageView) findViewById(R.id.medium);
    	    s1 = (GradientDrawable) nib.getDrawable();
    	    s1.mutate();
    	    s1.setStroke(20, Color.BLACK);
    	    s1.invalidateSelf();

    	    GradientDrawable s2;
    	    nib = (ImageView) findViewById(R.id.large);
    	    s2 = (GradientDrawable) nib.getDrawable();
    	    s2.mutate();
    	    s2.setStroke(20, Color.RED);
    	    s2.invalidateSelf();

    	    GradientDrawable s3;
    	    nib = (ImageView) findViewById(R.id.XL);
    	    s3 = (GradientDrawable) nib.getDrawable();
    	    s3.mutate();
    	    s3.setStroke(20, Color.BLACK);
    	    s3.invalidateSelf();
    	}else if(nibSize == nibXL){
            GradientDrawable s0;
    	    nib = (ImageView) findViewById(R.id.small);
    	    s0 = (GradientDrawable) nib.getDrawable();
    	    s0.mutate();
    	    s0.setStroke(20, Color.BLACK);
    	    s0.invalidateSelf();

    	    GradientDrawable s1;
    	    nib = (ImageView) findViewById(R.id.medium);
    	    s1 = (GradientDrawable) nib.getDrawable();
    	    s1.mutate();
    	    s1.setStroke(20, Color.BLACK);
    	    s1.invalidateSelf();

    	    GradientDrawable s2;
    	    nib = (ImageView) findViewById(R.id.large);
    	    s2 = (GradientDrawable) nib.getDrawable();
    	    s2.mutate();
    	    s2.setStroke(20, Color.BLACK);
    	    s2.invalidateSelf();

    	    GradientDrawable s3;
    	    nib = (ImageView) findViewById(R.id.XL);
    	    s3 = (GradientDrawable) nib.getDrawable();
    	    s3.mutate();
    	    s3.setStroke(20, Color.RED);
    	    s3.invalidateSelf();
    	}else{
            GradientDrawable s0;
    	    nib = (ImageView) findViewById(R.id.small);
    	    s0 = (GradientDrawable) nib.getDrawable();
    	    s0.mutate();
    	    s0.setStroke(20, Color.BLACK);
    	    s0.invalidateSelf();

    	    GradientDrawable s1;
    	    nib = (ImageView) findViewById(R.id.medium);
    	    s1 = (GradientDrawable) nib.getDrawable();
    	    s1.mutate();
    	    s1.setStroke(20, Color.BLACK);
    	    s1.invalidateSelf();

    	    GradientDrawable s2;
    	    nib = (ImageView) findViewById(R.id.large);
    	    s2 = (GradientDrawable) nib.getDrawable();
    	    s2.mutate();
    	    s2.setStroke(20, Color.BLACK);
    	    s2.invalidateSelf();

    	    GradientDrawable s3;
    	    nib = (ImageView) findViewById(R.id.XL);
    	    s3 = (GradientDrawable) nib.getDrawable();
    	    s3.mutate();
    	    s3.setStroke(20, Color.BLACK);
    	    s3.invalidateSelf();
    	}
    	
        GradientDrawable s4;
	    Button drawbutt = (Button) findViewById(R.id.button4);
	    s4 = (GradientDrawable) drawbutt.getBackground();
	    s4.mutate();
	    s4.setStroke(nibSize, paintColor);
	    s4.invalidateSelf();
    	
    }
    
    public void updatePrefs(View v){
    	
    	noPrefs = false;

		SharedPreferences settings = getSharedPreferences(OPTIONS, 0);
		SharedPreferences.Editor editor = settings.edit();
	    editor.putInt(COLOR_CHOICE, paintColor);
	    editor.putString(COLOR_NAME, colorName);
	    editor.putInt(NIB_CHOICE, nibSize);
	    editor.putString(PHOTO_LOC, noPhoto);
	    editor.commit();
	    
	    updateView();
    }

    // Functions to change the color preference
    public void chooseBlack(View v) {
    	paintColor = Color.rgb(0,0,0);
    	colorName = "black";
    	updatePrefs(v);
    }
    public void chooseGreen(View v) {
    	paintColor = Color.rgb(51,204,51);
    	colorName = "green";
    	updatePrefs(v);
    }
    public void choosePurple(View v) {
    	paintColor = Color.rgb(102,51,153);
    	colorName = "purple";
    	updatePrefs(v);
    }
    public void chooseOrange(View v) {
    	paintColor = Color.rgb(255,153,51);
    	colorName = "orange";
    	updatePrefs(v);
    }
    
    // Functions to change the nib size
    // NOTE: For the nib sizes to increase linearly, the AREA of the circle must
    //       increase linearly. The numbers below are radius (or diameter?) To 
    //       calculate the spread, choose the smallest size, r, use the formula
    //       for the area of a circle (pi*r^2) to calculate the area, A. Then use 
    //       A, A*2, A*3, A*4 in the formula sqrt(A/pi) to get the numbers used below.
    public void chooseSmall(View v) {
    	nibSize = nibSmall;	
    	updatePrefs(v);
    }
    public void chooseMedium(View v) {
    	nibSize = nibMed;
    	updatePrefs(v);
    }
    public void chooseLarge(View v) {
    	nibSize = nibLarge;
    	updatePrefs(v);
    }
    public void chooseXL(View v) {
    	nibSize = nibXL;
    	updatePrefs(v);
    }

}

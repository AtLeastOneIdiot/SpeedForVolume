package com.example.speedup;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.location.*;



public class MainActivity extends ActionBarActivity {
	private boolean accuracy;
	private float speed;
	private double lat, lon,alt;
	private Location loc;
	private LocationManager locMan;
	/**
	 * still need to add buttons and menues. Next I need to make on click listen to change screen
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start=(Button) findViewById(R.id.button1);
        locMan=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        loc=locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lat=loc.getLatitude();//gets current lat
        lon=loc.getLongitude();//gets current longitude
        lat=loc.getAltitude();//gets current altitude
        accuracy=loc.hasAccuracy();
        speed=loc.getSpeed();
        start.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		setContentView(R.layout.screen2);
        	}
        });
    }
    public void Start(){
//    	if(locMan.)
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
}

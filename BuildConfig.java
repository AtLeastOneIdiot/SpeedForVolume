package com.example.speedup;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Daniel Burke & Andrew Snyder
 *
 */
public class MainActivity extends ActionBarActivity {
	private boolean accuracy;
	private float speed, distance, target;//speed is the speed you're currently running at target is the user input
	private long timeRan, startTime, buffer,current;
	private Location loc;
	private LocationManager locMan;
	private MediaPlayer media;
	private Handler handler = new Handler();
	private TextView tv;
	private AudioManager audiomanager=null;
	private SeekBar seekbar=null;
	private NumberPicker min, sec;
	/**
	 * Store time so I can tell 
	 * 
	 * 
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize all variables
        timeRan=0;
        startTime=0;
        buffer=0;
		tv=(TextView) findViewById(R.id.TextView01);
        Button startRun=(Button) findViewById(R.id.start);
        Button pauseMusic=(Button) findViewById(R.id.mainactivity_playpausebutton);
        Button previousMusic=(Button) findViewById(R.id.mainactivity_previousbutton);
        Button nextMusic=(Button) findViewById(R.id.mainactivity_nextbutton);
        Button pauseRun=(Button)findViewById(R.id.pause_run);
        Button stopRun=(Button) findViewById(R.id.stop_run);
        min=(NumberPicker)findViewById(R.id.min_sel);
        sec=(NumberPicker)findViewById(R.id.sec_sel);
        sec.setMaxValue(60);
        locMan=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        target =min.getValue()*6000+sec.getValue()*1000;
        LocationListener loclisten= new LocationListener(){
        	public void onLocationChanged(Location loc){
        		speed=loc.getSpeed();
        		determinePace(speed,target);
        		
        	}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
        };
        loc=locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        accuracy=loc.hasAccuracy();//internet connection
        media=new MediaPlayer();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        seekBar();
        
        
       
        startRun.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		if(loc.hasAccuracy()){
            		setContentView(R.layout.screen2);
            		timeRan=SystemClock.uptimeMillis();
            		handler.postDelayed(updates, 0);
            	}
            	else{
            		Toast toast =Toast.makeText(getApplicationContext(), "Connect to the internet please", Toast.LENGTH_LONG); 
            		toast.show();
            		}
        	}
        });
        pauseRun.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		buffer+=timeRan;
        		handler.removeCallbacks(updates);
        	}
        });
        stopRun.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		try {
					setData();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });
        pauseMusic.setOnTouchListener(new OnTouchListener(){//could have to set it to keycode media like in previous/next
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(media.isPlaying()){
        			media.pause();
        			startTime=SystemClock.currentThreadTimeMillis();
        		}
        		else
        			media.start();
				return true;
			}
        });
        nextMusic.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
        		synchronized (this) {
        		            i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT));
        		            sendOrderedBroadcast(i, null);
        		            i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT));
        		            sendOrderedBroadcast(i, null);
        		 }
        	}
        });
        previousMusic.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
        		synchronized (this) {
        		            i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
        		            sendOrderedBroadcast(i, null);
        		            i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
        		            sendOrderedBroadcast(i, null);
        		 }
        	}
        });
    }
    /**
     * Need to determine the entered pace, do this by changing all to milis per mile
     *need to use this
     */
    private void determinePace(double currentSpeed, double target){
    	
    	
    	if(currentSpeed>target)
    		volumeUp();
    	else
    		volumeDown();
    }
    private void volumeUp(){
    	
    }
    private void volumeDown(){
    	
    }
    /**
     * Writes data to a txt file, storing in the format of TIMERAN DISTANCE RAN
     * @throws IOException
     */
    private void setData() throws IOException{
    	File log=new File("log.txt");
    	try{
    		PrintWriter out= new PrintWriter(new BufferedWriter(new FileWriter("log.txt",true)));  		
    		out.println(current+" "+distance);
    		out.close();
    	}catch(IOException e){
    		throw new IOException();
    	}
    }
  /**
   * sorts data based off of different metrics either distance, time, or avg speed
   */
    private void sortData(){
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    private void seekBar(){
    	try{
    		seekbar=(SeekBar) findViewById(R.id.volumeBar);
    		audiomanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
    		seekbar.setMax(audiomanager.getStreamMaxVolume(audiomanager.STREAM_MUSIC));
    		seekbar.setProgress(audiomanager.getStreamVolume(audiomanager.STREAM_MUSIC));
    		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
    			@Override
    			public void onStopTrackingTouch(SeekBar arg0){
    				
    			}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					audiomanager.setStreamVolume(audiomanager.STREAM_MUSIC, progress, 0);
				
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					
				}
    		});
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    private Runnable updates=new Runnable(){
		@Override
		public void run() {
			timeRan=SystemClock.uptimeMillis()-startTime;
			current=buffer+timeRan;
			int secs=(int) (current/1000);
			int mins=secs/60;
			secs=secs%60;
			int milis=(int) (current%1000);
			int hours=(int)(mins/60);
			tv.setText(hours+": "+mins+": "+secs+". "+milis);
		}
    	
    };
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

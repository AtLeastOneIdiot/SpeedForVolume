package SeveralMidgets.project.speedup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

/**
 * 
 * @author Daniel Burke & Andrew Snyder
 * 
 */
public class MainActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	private boolean accuracy;
	private float speed, distance, target;// speed is the speed you're currently
											// running at target is the user
											// input
	private long timeRan, startTime, buffer, current;
	private Location loc;
	private LocationManager locMan;
	private MediaPlayer media;
	private Handler handler = new Handler();
	private TextView tv;
	private AudioManager audiomanager;
	private SeekBar seekbar;
	private NumberPicker min, sec;
	LocationClient mLocationClient;
	// Define an object that holds accuracy and frequency parameters
	LocationRequest mLocationRequest;
	boolean mUpdatesRequested;
	Editor mEditor;
	SharedPreferences mPrefs;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	// Milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;

	/**
	 * Store time so I can tell
	 * 
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		// initialize all variables
		timeRan = 0;
		startTime = 0;
		buffer = 0;
		seekbar = (SeekBar) findViewById(R.id.volumeBar);
		audiomanager = (AudioManager) getSystemService(AUDIO_SERVICE);
		tv = (TextView) findViewById(R.id.TimeElapsed);
		Button startRun = (Button) findViewById(R.id.start);
		Button pauseMusic = (Button) findViewById(R.id.mainactivity_playpausebutton);
		Button previousMusic = (Button) findViewById(R.id.mainactivity_previousbutton);
		Button nextMusic = (Button) findViewById(R.id.mainactivity_nextbutton);
		Button pauseRun = (Button) findViewById(R.id.pause_run);
		Button stopRun = (Button) findViewById(R.id.stop_run);
		min = (NumberPicker) findViewById(R.id.minute_picker);
		sec = (NumberPicker) findViewById(R.id.NumberPicker01);
		min.setMaxValue(60);
		sec.setMaxValue(60);
		target = min.getValue() * 6000 + sec.getValue() * 1000;
		media = new MediaPlayer();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		seekBar();
		 // Open the shared preferences
        mPrefs = getSharedPreferences("SharedPreferences",
                Context.MODE_PRIVATE);
        // Get a SharedPreferences editor
        mEditor = mPrefs.edit();
        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);
        // Start with updates turned off
        mUpdatesRequested = false;
        

		startRun.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				setContentView(R.layout.screen2);
				timeRan = SystemClock.uptimeMillis();
				handler.postDelayed(updates, 0);
				mLocationClient.connect();
			}
		});
	}

	// pauseRun.setOnClickListener(new OnClickListener(){
	// public void onClick(View view){
	// buffer+=timeRan;
	// handler.removeCallbacks(updates);
	// }
	// });
	// stopRun.setOnClickListener(new OnClickListener(){
	// public void onClick(View view){
	// try {
	// setData();
	// if (mLocationClient.isConnected()) {
	// /*
	// * Remove location updates for a listener.
	// * The current Activity is the listener, so
	// * the argument is "this".
	// */
	// removeLocationUpdates(this);
	// }
	// /*
	// * After disconnect() is called, the client is
	// * considered "dead".
	// */
	// mLocationClient.disconnect();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// });
	// pauseMusic.setOnTouchListener(new OnTouchListener() {// could have to
	// // set it to
	// // keycode media
	// // like in
	// // previous/next
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// if (!media.equals(null)) {
	// if (media.isPlaying()) {
	// media.pause();
	// startTime = SystemClock
	// .currentThreadTimeMillis();
	// } else
	// media.start();
	// }
	// return true;
	// }
	// });
	// nextMusic.setOnClickListener(new OnClickListener() {
	// public void onClick(View view) {
	// Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
	// synchronized (this) {
	// // i.putExtra(Intent.EXTRA_KEY_EVENT, new
	// // KeyEvent(KeyEvent.ACTION_DOWN,
	// // KeyEvent.KEYCODE_MEDIA_NEXT));
	// sendOrderedBroadcast(i, null);
	// // i.putExtra(Intent.EXTRA_KEY_EVENT, new
	// // KeyEvent(KeyEvent.ACTION_UP,
	// // KeyEvent.KEYCODE_MEDIA_NEXT));
	// sendOrderedBroadcast(i, null);
	// }
	// }
	// });
	// previousMusic.setOnClickListener(new OnClickListener() {
	// public void onClick(View view) {
	// Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
	// synchronized (this) {
	// // i.putExtra(Intent.EXTRA_KEY_EVENT, new
	// // KeyEvent(KeyEvent.ACTION_DOWN,
	// // KeyEvent.KEYCODE_MEDIA_PREVIOUS));
	// sendOrderedBroadcast(i, null);
	// // i.putExtra(Intent.EXTRA_KEY_EVENT, new
	// // KeyEvent(KeyEvent.ACTION_UP,
	// // KeyEvent.KEYCODE_MEDIA_PREVIOUS));
	// sendOrderedBroadcast(i, null);
	// }
	// }
	// });
	// }

	/**
	 * Need to determine the entered pace, do this by changing all to milis per
	 * mile need to use this
	 */
	private void determinePace(double currentSpeed, double target) {

		if (currentSpeed > target)
			volumeUp();
		else
			volumeDown();
	}

	private void volumeUp() {
		audiomanager.adjustStreamVolume(audiomanager.STREAM_MUSIC,
				audiomanager.ADJUST_RAISE, audiomanager.FLAG_SHOW_UI);
	}

	private void volumeDown() {
		audiomanager.adjustStreamVolume(audiomanager.STREAM_MUSIC,
				audiomanager.ADJUST_LOWER, audiomanager.FLAG_SHOW_UI);
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				/*
				 * Try the request again
				 */

				break;
			}

		}
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason.
			// resultCode holds the error code.
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getFragmentManager(), "Location Updates");
			}
			return false;
		}
	}

	/**
	 * Writes data to a txt file, storing in the format of TIMERAN DISTANCE RAN
	 * 
	 * @throws IOException
	 */
	private void setData() throws IOException {
		File log = new File("log.txt");
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("log.txt", true)));
			out.println(current + " " + distance);
			out.close();
		} catch (IOException e) {
			throw new IOException();
		}
	}

	/**
	 * sorts data based off of different metrics either distance, time, or avg
	 * speed
	 */
	private void sortData() {

	}

	private void seekBar() {
		try {
			audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			seekbar.setMax(audiomanager
					.getStreamMaxVolume(audiomanager.STREAM_MUSIC));
			seekbar.setProgress(audiomanager
					.getStreamVolume(AudioManager.STREAM_MUSIC));
			seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar arg0) {

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					audiomanager.setStreamVolume(audiomanager.STREAM_MUSIC,
							progress, 0);

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Runnable updates = new Runnable() {
		@Override
		public void run() {
			// timeRan = SystemClock.uptimeMillis() - startTime;
			// current = buffer + timeRan;
			// int secs = (int) (current / 1000);
			// int mins = secs / 60;
			// secs = secs % 60;
			// int milis = (int) (current % 1000);
			// int hours = (int) (mins / 60);
			// tv.setText(hours + ": " + mins + ": " + secs + ". " + milis);
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

	@SuppressWarnings("deprecation")
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			showDialog(connectionResult.getErrorCode());
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();

	}

	// Define the callback method that receives location updates
	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
		String msg = "Updated Location: "
				+ Double.toString(location.getLatitude()) + ","
				+ Double.toString(location.getLongitude());
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
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
}

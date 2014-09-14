package com.example.lab4;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.lab3.ExEntryDataSource;
import com.example.lab3.HistoryDetailsActivity;
import com.example.lab6.R;
import com.example.lab5.SensorsService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MapDisplayActivity extends Activity implements
		LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	public GoogleMap mMap;
	public Marker whereAmI;
	public Marker initMarker;
	
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;


    // Handles to UI widgets
    private TextView mType;
    private TextView mAvgSpeed;
    private TextView mCurSpeed;
    private TextView mClimb;
    private TextView mCalorie;
    private TextView mDistance;

    // for database and UI
	private ExEntryDataSource datasource;
	private int inputType;
	private int activityType;
	private double avgSpd;
	private double curSpd;
	private double climb; // sum or final- init?
	private int calorie;
	private double dist; 
	static Calendar mDateAndTime;
	private Location lastLoc;
	private ArrayList<LatLng> mLocationList;
	
    // for location line
	PolylineOptions rectOptions;
	Polyline polyline;
	
    String connTag = "ConnectionService";
    
    // lab5 activity recognition
    // Sensor service for inputType
	private Intent mServiceIntent;
	NotifyServiceReceiver notifyServiceReceiver;
	
	int[] myActTypeCount;
	
	// notification
	public static final String notificationTitle = "Lab5";	
	public static final String notificationContent = "Recording your path now";	
	
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapdisplay);

        // Get the message from the intent
        Intent intent = getIntent();
        activityType = intent.getIntExtra("activity_type", 0);
        inputType = intent.getIntExtra("input_type", 0);

        // for database
		datasource = new ExEntryDataSource(this);
		datasource.open();
        
        // Get handles to the UI view objects
        mType = (TextView) findViewById(R.id.type);
        mAvgSpeed = (TextView) findViewById(R.id.avgSpeed);
        mCurSpeed = (TextView) findViewById(R.id.curSpeed);
        mClimb = (TextView) findViewById(R.id.climb);
        mCalorie = (TextView) findViewById(R.id.calorie);
        mDistance = (TextView) findViewById(R.id.distance);
                
        if (inputType == 2) {
        	Log.d("MapDisplayActivity", ".." + inputType);
        	mType.setText("Type: Unknown");
    		mServiceIntent = new Intent(this, SensorsService.class);
			startService(mServiceIntent);   
			
			notifyServiceReceiver = new NotifyServiceReceiver();

			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(SensorsService.ACTION);
			registerReceiver(notifyServiceReceiver, intentFilter);
			
			// for activity type recording
			myActTypeCount = new int[4];
			for (int i = 0; i < 4; i++)
				myActTypeCount[i] = 0;
        }
        else 
        	mType.setText("Type: " + HistoryDetailsActivity.actTypes[activityType]);
        
		// Get a reference to the MapView
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

	    // Configure the map display options
	    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);   

	    // Create a new global location parameters object
	    mLocationRequest = LocationRequest.create();  

	    mLocationRequest.setInterval(5000);

	    // Use high accuracy
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	    // Set the interval ceiling to one minute
	    mLocationRequest.setFastestInterval(1000);

	    // set locationClient
	    mLocationClient = new LocationClient(this, this, this);

	    // notification
		Intent myIntent = new Intent(this, MapDisplayActivity.class);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);    		
		PendingIntent pendingIntent 
				= PendingIntent.getActivity(this, 
						0, myIntent, 
						Intent.FLAG_ACTIVITY_NEW_TASK);	
		
		Notification notification = new Notification.Builder(this)
        .setContentTitle(notificationTitle)
        .setContentText(notificationContent).setSmallIcon(R.drawable.icon)
        .setContentIntent(pendingIntent).build();
	    NotificationManager notificationManager = 
	    		  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification.flags = notification.flags
				| Notification.FLAG_ONGOING_EVENT;
		
		notificationManager.notify(0, notification); 
	}

	/*
	 * Called when the Activity is no longer visible at all.
	 * Stop updates and disconnect.
	 */
	@Override
	public void onStop() {
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
        Log.d(connTag, "onStop");

		// If the client is connected
//		if (mLocationClient.isConnected()) {
//			stopPeriodicUpdates();
//		}

		super.onStop();
	}

	/*
	 * Called when the Activity is going into the background.
	 * Parts of the UI may be visible, but the Activity is inactive.
	 */
/*	@Override
	public void onPause() {
        Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();

		// Save the current setting for updates
//		mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, mUpdatesRequested);
//		mEditor.commit();

		super.onPause();
	}*/	

	/*
	 * Called when the Activity is restarted, even before it becomes visible.
	 */
	@Override
	public void onStart() {
        Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();

		/*
		 * Connect the client. Don't re-start any requests here;
		 * instead, wait for onResume()
		 */
		
		if (mLocationClient.isConnected() == false)
			mLocationClient.connect();
		
		super.onStart();

	}
	
	/*
	 * Called when the system detects that this Activity is now visible.
	 */
	@Override
	public void onResume() {
        Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
		
		super.onResume();

      // If the app already has a setting for getting location updates, get it
/*      if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
          mUpdatesRequested = mPrefs.getBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);

      // Otherwise, turn off location updates until requested
      } else {
          mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
          mEditor.commit();
      }
*/
	}
  


	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
        Toast.makeText(this, "Connect failed", Toast.LENGTH_SHORT).show();
		
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 9000);
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
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.d(connTag, "connect failed, no resolution");
        }		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
        Log.d(connTag, "onConnected");

        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        if (initMarker == null) {
        	// get start time;
            mDateAndTime = Calendar.getInstance();
            
        	// Get the current location
        	Location currentLocation = mLocationClient.getLastLocation();
            
        	LatLng latlng = fromLocationToLatLng(currentLocation); 
        	lastLoc = currentLocation;
        	mLocationList = new ArrayList<LatLng>();
        	mLocationList.add(latlng);
        	
            
        	initMarker = mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
        			BitmapDescriptorFactory.HUE_GREEN)));

        	whereAmI = mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
        			BitmapDescriptorFactory.HUE_RED)));
        	
    	    // for location line
    		rectOptions = new PolylineOptions().add(whereAmI.getPosition());

        	// UI data
    		dist = 0;
    		avgSpd = 0;
    		curSpd = 0;
        	climb = 0;
        	calorie = 0;
        	updateTextViews();
        	
        	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));
	
        	updateWithNewLocation(currentLocation);
        	

        	
        }
        
        startPeriodicUpdates();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        Log.d(connTag, "disconnected");

        mType.setText("Type: Unknown");

	}

    /**
     * Report location updates to the UI.
     *
     * @param location The updated location.
     */
    @Override
    public void onLocationChanged(Location location) {
    	Log.d(connTag, "onLocationChanged");

        // In the UI, set the latitude and longitude to the value received
        updateWithNewLocation(location);
        
    }

    
    
	/**
	 * Verify that Google Play services is available before making a request.
	 *
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected() {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			Log.d(connTag, "connect!");
			return true;
			
        // Google Play services was not available for some reason
		} else {
			Log.d(connTag, "not connected");
			return false;
		}
	}  
	
	private void updateWithNewLocation(Location location) {
		Log.d(connTag, "updateWithNewLocation");
		
		TextView myLocationText;
//		myLocationText = (TextView)findViewById(R.id.myLocationText);
		      
		    
		if (location != null) {
			// Update the map location.
		      
			LatLng latlng=fromLocationToLatLng(location);
		      
		    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));

		    if(whereAmI!=null)
		    	whereAmI.remove();
		      
		    whereAmI=mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
		  		     BitmapDescriptorFactory.HUE_RED)).title("Here I Am."));

			rectOptions.add(whereAmI.getPosition());
			rectOptions.color(Color.BLACK);
			polyline = mMap.addPolyline(rectOptions);

        	// UI data
    		dist = dist + location.distanceTo(lastLoc)/1000;
    		double time = location.getTime()-mDateAndTime.getTimeInMillis();
//    		Toast.makeText(this, "" + time, Toast.LENGTH_SHORT).show();
    		Log.d("test", "" + time);
    		avgSpd = dist/(time/1000/60/60);
        	curSpd = location.getSpeed();
        	climb = climb + (location.getAltitude() - lastLoc.getAltitude()) / 1000 / 1.609344;
        	calorie = (int) (dist * 1609.344 /15);
        	updateTextViews();
        	
        	lastLoc = location;
        	mLocationList.add(latlng);
		}
		      
//		myLocationText.setText("Your Current Position is:\n" +
//		      latLongString + "\n\n" + addressString);
	}	

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }
    
    private void stopPeriodicUpdates() {
    	Log.d(connTag, "stopPeriodicUpdates");
        mLocationClient.removeLocationUpdates(this);
    }
    
	public static LatLng fromLocationToLatLng(Location location){
		return new LatLng(location.getLatitude(), location.getLongitude());
		
	}  
  


	public void onSaveClicked(View v) {
		Log.d("mapdisplayActivity", "Save click!");

		// for automatic
        if (inputType == 2) {
    		stopService(mServiceIntent);
 
    		// stop receive activity type
    		this.unregisterReceiver(notifyServiceReceiver);
    		
    		// find the max activity type
    		activityType = 0;
    		int max = 0;
    		for (int i = 0; i < 4; i++) {
    			Log.d("MapDisplayActivity", ""+i+","+myActTypeCount[i]);
    			if (max < myActTypeCount[i]) {
    				max = myActTypeCount[i];
    				activityType = i;
    			}
    		}
    	}
        if (inputType != 0)
    		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();

               
		// After disconnect() is called, the client is considered "dead".
		mLocationClient.disconnect();
		
		
		Date d = mDateAndTime.getTime();
		String[] time = d.toString().split(" ");
		String mDisplayDateTime = time[3]+" " + time[1] +" " + time[2] + " " + time[5];

		int mDuration = (int) ((lastLoc.getTime() - mDateAndTime.getTimeInMillis())/1000);
		byte[] locs = new byte[16*mLocationList.size()];
		for (int i = 0; i < mLocationList.size(); i++) {
			byte[] lati = new byte[8];
			byte[] lngi = new byte[8];
			lati = getBytes(mLocationList.get(i).latitude);
			lngi = getBytes(mLocationList.get(i).longitude);
			for (int j = 0; j < 8; j++)
				locs[i*16+j] = lati[j];
			for (int j = 0; j < 8; j++)
				locs[i*16+8+j] = lngi[j];
		}
		
		datasource.createComment(inputType, activityType, mDisplayDateTime, mDuration, dist,
				avgSpd, calorie, climb, locs);		

		finish();
	}

	public void onCancelClicked(View v) {
		Log.d("mapdisplayActivity", "Cancel click!");
		
		// for automatic
        if (inputType == 2) {
    		stopService(mServiceIntent);
    		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
    		
    		// stop receive activity type
 			this.unregisterReceiver(notifyServiceReceiver);    		
    	}
        if (inputType != 0)
    		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
        
		// After disconnect() is called, the client is considered "dead".
		mLocationClient.disconnect();
		        
        
		finish();
	}  
	
	private void updateTextViews() {
		DecimalFormat df = new DecimalFormat("0.00");

		mAvgSpeed.setText("Avg speed: " + df.format(avgSpd) + " m/h");
    	mCurSpeed.setText("Cur speed: " + df.format(curSpd * 2.24) + " m/h");
    	mClimb.setText("Climb: " + df.format(climb) + " Miles");
    	mCalorie.setText("Calorie: " + calorie);
    	mDistance.setText("Distance: " + df.format(dist) + " Miles");
	}
	
    public static byte[] getBytes(double data)  
    {  
        long intBits = Double.doubleToLongBits(data);  
        return getBytes(intBits);  
    }
    
    public static byte[] getBytes(long data)  
    {  
        byte[] bytes = new byte[8];  
        bytes[0] = (byte) (data & 0xff);  
        bytes[1] = (byte) ((data >> 8) & 0xff);  
        bytes[2] = (byte) ((data >> 16) & 0xff);  
        bytes[3] = (byte) ((data >> 24) & 0xff);  
        bytes[4] = (byte) ((data >> 32) & 0xff);  
        bytes[5] = (byte) ((data >> 40) & 0xff);  
        bytes[6] = (byte) ((data >> 48) & 0xff);  
        bytes[7] = (byte) ((data >> 56) & 0xff);  
        return bytes;  
    }
    
	public class NotifyServiceReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int rqs = arg1.getIntExtra(SensorsService.AUTO_ACTIVITY_TYPE, 1);
			Log.d("return label", ""+rqs);
			myActTypeCount[rqs]++;
			
			mType.setText("Type: " + SensorsService.LABELS_AUTO[rqs]);
		}
	}    

}

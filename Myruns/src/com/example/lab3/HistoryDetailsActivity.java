package com.example.lab3;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.lab5.SensorsService;
import com.example.lab6.R;
import com.example.lab6.ServerUtilities;
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
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryDetailsActivity extends Activity {
	
	public static final String actTypes[] = {"Running", "Walking", "Standing", "Cycling", "Hiking"
		, "Downhill Skiing", "Cross-Country Skiing", "Snowboarding", "Skating"
		, "Swimming", "Mountain Biking", "Wheelchair", "Elliptical", "Other"
	};
	private ExEntryDataSource datasource;
	private int curPos;
	private ExerciseEntry entry;
	
	private int mInputType;

	public GoogleMap mMap;
	public Marker whereAmI;
	public Marker initMarker;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        datasource = new ExEntryDataSource(this);
        datasource.open();
        
        // Get the message from the intent
        Intent intent = getIntent();
        curPos = intent.getIntExtra("index", 0); 
        
        // get the entry in database
        List<ExerciseEntry> values = datasource.getAllComments();
        entry = values.get(curPos);
        
        mInputType = entry.getInputType(); 
        
        if (mInputType == 0) {
        	setContentView(R.layout.activity_historydetails);
        
            EditText editText = (EditText) findViewById(R.id.editActType);
            editText.setText(actTypes[entry.getActivityType()]);
            
            editText = (EditText) findViewById(R.id.editDateTime);
            editText.setText(entry.getDateandTime());
            
            editText = (EditText) findViewById(R.id.editDuration);
            editText.setText("" +entry.getDuration()/60+"mins 0secs");
            
            editText = (EditText) findViewById(R.id.editDistance);
            editText.setText(""+Double.toString(entry.getDistance())+" Miles");
        }
        else {
        	setContentView(R.layout.activity_mapdisplay);
        	Button btnSave = (Button) findViewById(R.id.btnSave);
        	Button btnCancel = (Button) findViewById(R.id.btnCancel);
        	btnSave.setVisibility(View.GONE);
        	btnCancel.setVisibility(View.GONE);
        	
    		// Get a reference to the MapView
    		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

    	    // Configure the map display options
    	    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
  
            // Get handles to the UI view objects            
            TextView mType = (TextView) findViewById(R.id.type);
            TextView mAvgSpeed = (TextView) findViewById(R.id.avgSpeed);
            TextView mCurSpeed = (TextView) findViewById(R.id.curSpeed);
            TextView mClimb = (TextView) findViewById(R.id.climb);
            TextView mCalorie = (TextView) findViewById(R.id.calorie);
            TextView mDistance = (TextView) findViewById(R.id.distance);

    		DecimalFormat df = new DecimalFormat("0.00");

    		if (mInputType == 2)
        		mType.setText("Type: " + SensorsService.LABELS_AUTO[entry.getActivityType()]);
    		else
    			mType.setText("Type: " + actTypes[entry.getActivityType()]);
    		mAvgSpeed.setText("Avg speed: " + df.format(entry.getAvgSpd()) + " m/h");
        	mCurSpeed.setText("Cur speed: n/a");
        	mClimb.setText("Climb: " + df.format(entry.getClimb()) + " Miles");
        	mCalorie.setText("Calorie: " + entry.getCalorie());
        	mDistance.setText("Distance: " + df.format(entry.getDistance()) + " Miles");
        	
        	ArrayList<LatLng> mLocationList = entry.getLocs();
		    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocationList.get(mLocationList.size()-1), 17));
        	
        	initMarker = mMap.addMarker(new MarkerOptions().position(mLocationList.get(0)).icon(BitmapDescriptorFactory.defaultMarker(
        			BitmapDescriptorFactory.HUE_GREEN)));

        	whereAmI = mMap.addMarker(new MarkerOptions().position(mLocationList.get(mLocationList.size()-1)).icon(BitmapDescriptorFactory.defaultMarker(
        			BitmapDescriptorFactory.HUE_RED)));
        	
            // for location line
        	PolylineOptions rectOptions;
        	Polyline polyline;  
    	    // for location line
    		rectOptions = new PolylineOptions().add(mLocationList.get(0));
    		
    		for (int i = 1; i < mLocationList.size(); i++) {
    			rectOptions.add(mLocationList.get(i));
    			rectOptions.color(Color.BLACK);
    			polyline = mMap.addPolyline(rectOptions);
    		}
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.historydetails, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuitem_delete:
			Toast.makeText(this, getString(R.string.history_menu_delete),
					Toast.LENGTH_SHORT).show();

			String params = "" + entry.getId();
			Log.d("postMsg id", ""+entry.getId());
			postMsg(params);
			
			datasource.deleteComment(entry);
			finish();
			return true;
		}
		return false;
	}	
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);

    }
    
	private void postMsg(String msg) {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				String url = getString(R.string.server_addr) + "/delete.do";
				String res = "";
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", arg0[0]);
				params.put("from", "phone");
				try {
					res = ServerUtilities.post(url, params);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				return res;
			}

			@Override
			protected void onPostExecute(String res) {
				
			}

		}.execute(msg);
	}    
}

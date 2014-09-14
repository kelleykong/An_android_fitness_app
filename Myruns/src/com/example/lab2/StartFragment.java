package com.example.lab2;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.lab3.ExEntryDataSource;
import com.example.lab3.ExerciseActivity;
import com.example.lab3.ExerciseEntry;
import com.example.lab4.MapDisplayActivity;
import com.example.lab6.R;
import com.example.lab6.ServerUtilities;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


public class StartFragment extends Fragment {
	
    private static final String TAG = "CS65";
    Spinner spinnerInputType;
    Spinner spinnerActType;
    int inputType = 0;
    int activityType = 0;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.d(TAG, "0...."+inputType);

		View v = super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.startfragment, container, false); 
		
        spinnerInputType = (Spinner) v.findViewById(R.id.spinnerInputType);
        spinnerActType = (Spinner) v.findViewById(R.id.spinnerActivityType);        
//		Log.d(TAG, "...."+inputType);
        // Inflate the layout for this fragment
		
		if (savedInstanceState != null) {
//			Log.d(TAG, "recover data");
			// Restore last state for checked position.
			inputType = savedInstanceState.getInt("curChoice", 0);
		}
        
        Button b = (Button) v.findViewById(R.id.btnStart);
	    b.setOnClickListener(new OnClickListener() {
	    	@Override
	        public void onClick(final View v) {
	    		inputType = spinnerInputType.getSelectedItemPosition();
	    		activityType = spinnerActType.getSelectedItemPosition();
//	    		Log.d(TAG, "" + inputType);
	            onStartClicked(v); 	    			
	        }
	    });
        b = (Button) v.findViewById(R.id.btnSync);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
            	onSyncClicked(v); 
            }
        });        
       
        return v;
    }
    
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		Toast.makeText(getActivity(), "onSaveInstanceState", Toast.LENGTH_LONG).show();

		outState.putInt("curChoice", inputType);
//        Log.d(TAG, "store" + inputType);

	}
	
    
	public void onStartClicked(View v) {
//		Log.d(TAG, "start" + inputType);
        
        if (inputType == 0) {
        	Intent intent = new Intent(getActivity(), ExerciseActivity.class);
			// pass the activity type
			intent.putExtra("activity_type", activityType);        	
        	startActivity(intent);
        }
        else {
        	Intent intent = new Intent(getActivity(), MapDisplayActivity.class);
			// pass the activity type
			intent.putExtra("activity_type", activityType);  
			intent.putExtra("input_type", inputType);        	
        	startActivity(intent);
        }
	}
	
	public void onSyncClicked(View v) {
		ExEntryDataSource datasource = new ExEntryDataSource(getActivity());
		datasource.open();	
		List<ExerciseEntry> values = datasource.getAllComments();
		
		DecimalFormat df = new DecimalFormat("0.00");
		for (ExerciseEntry value: values) {
			String[] params = new String[10];
			Log.d("postMsg id", ""+value.getId());
			params[0] = "" + value.getId();
			params[1] = "" + value.getInputType();
			params[2] = "" + value.getActivityType();
			params[3] = "" + value.getDateandTime();
			params[4] = "" + value.getDuration();
			params[5] = "" + df.format(value.getDistance());
			params[6] = "" + df.format(value.getAvgSpd());
			params[7] = "" + value.getCalorie();
			params[8] = "" + df.format(value.getClimb());
			params[9] = "0";
			postMsg(params);
		}

	}
	
	private void postMsg(String[] msg) {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				String url = getString(R.string.server_addr) + "/post.do";
				String res = "";
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", arg0[0]);
				params.put("inputType", arg0[1]);
				params.put("activityType", arg0[2]);
				params.put("dateTime", arg0[3]);
				params.put("duration", arg0[4]);
				params.put("distance", arg0[5]);
				params.put("avgSpeed", arg0[6]);
				params.put("calorie", arg0[7]);
				params.put("climb", arg0[8]);
				params.put("heartrate", arg0[9]);
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

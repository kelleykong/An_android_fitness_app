package com.example.lab3;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.example.lab6.R;
import com.example.lab6.ServerUtilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryFragment extends ListFragment {
	private ExEntryDataSource datasource;

	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console.
	 */
	private String SENDER_ID = "886473056009";

	private static final String TAG = "PostActivity";
	private GoogleCloudMessaging gcm;
	private Context context;
	private String regid;
	
	private IntentFilter mMessageIntentFilter;
	private BroadcastReceiver mMessageUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("broadcastreceiver", "onreceive showHistory");
				
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (datasource != null)
				showHistory();
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		datasource = new ExEntryDataSource(getActivity());
		datasource.open();

		mMessageIntentFilter = new IntentFilter();
		mMessageIntentFilter.addAction("GCM_NOTIFY");
		getActivity().registerReceiver(mMessageUpdateReceiver, mMessageIntentFilter);

		context = getActivity().getApplicationContext();
		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(getActivity());
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				registerInBackground();
			}
		}
		
		showHistory();
	}

	
	@Override
	public void onResume() {
		Toast.makeText(getActivity(),"Resume", Toast.LENGTH_LONG).show();		
		datasource.open();
		getActivity().registerReceiver(mMessageUpdateReceiver, mMessageIntentFilter);		
		showHistory();	
		super.onResume();
	}

	@Override
	public void onPause() {
		datasource.close();
		getActivity().unregisterReceiver(mMessageUpdateReceiver);
		super.onPause();
	}
	
	// If the user clicks on an item in the list (e.g., Henry V then the
	// onListItemClick() method is called. It calls a helper function in
	// this case.

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Toast.makeText(getActivity(),
				"onListItemClick position is" + position, Toast.LENGTH_LONG)
				.show();

		showDetails(position);
	}

	// Helper function to show the details of a selected item, either by
	// displaying a fragment in-place in the current UI, or starting a whole
	// new activity in which it is displayed.

	void showDetails(int index) {

			// launch a new activity to display

			// Create an intent for starting the DetailsActivity
			Intent intent = new Intent();

			// explicitly set the activity context and class
			// associated with the intent (context, class)
			intent.setClass(getActivity(), HistoryDetailsActivity.class);

			// pass the current position
			intent.putExtra("index", index);

			startActivity(intent);
	}
	
	// show History List
	
	void showHistory() {
		List<ExerciseEntry> values = datasource.getAllComments();
		
//        TextView view1 = (TextView) findViewById(android.R.id.text1);

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<ExerciseEntry> adapter = new ArrayAdapter<ExerciseEntry>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);		
	}
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				getActivity().finish();
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getActivity().getSharedPreferences(HistoryFragment.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.
					// The request to your server should be authenticated if
					// your app
					// is using accounts.
					ServerUtilities.sendRegistrationIdToBackend(context, regid);

					// For this demo: we don't need to send it because the
					// device
					// will send upstream messages to a server that echo back
					// the
					// message using the 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.i(TAG, "gcm register msg: " + msg);
			}
		}.execute(null, null, null);
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}
	
}

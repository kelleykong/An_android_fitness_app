package com.example.lab6;

import java.io.IOException;

import com.example.lab2.SettingsFragment;
import com.example.lab2.StartFragment;
import com.example.lab3.ExEntryDataSource;
import com.example.lab3.ExerciseEntry;
import com.example.lab3.HistoryFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String TAB_KEY_INDEX = "tab_key";
	
	public Fragment mHistoryFragment;

	//lab 6
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
			Log.d("broadcastreceiver", "onreceive");
			String msg = intent.getStringExtra("message");
			if (msg != null && msg.equals("delete")) {
				long id = Long.parseLong(intent.getStringExtra("id"));
				Log.d("broadcastreceiver", ""+id);
				
				ExerciseEntry entry = new ExerciseEntry();
				entry.setId(id);
	        	ExEntryDataSource datasource = new ExEntryDataSource(context);
	    		datasource.open();;
	        	datasource.deleteComment(entry);
				
			}
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		// ActionBar
		ActionBar actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// create new tabs and and set up the titles of the tabs
		ActionBar.Tab mStartTab = actionbar.newTab().setText(
				getString(R.string.tabname_start));
		ActionBar.Tab mHistoryTab = actionbar.newTab().setText(
				getString(R.string.tabname_history));
		ActionBar.Tab mSettingsTab = actionbar.newTab().setText(
				getString(R.string.tabname_settings));

		// create the fragments
		Fragment mStartFragment = new StartFragment();
		mHistoryFragment = new HistoryFragment();
		Fragment mSettingsFragment = new SettingsFragment();

		// bind the fragments to the tabs - set up tabListeners for each tab
		String str = getString(R.string.tabname_start);
		mStartTab.setTabListener(new TabListener<StartFragment>(this, str,
				StartFragment.class));
		str = getString(R.string.tabname_history);
		mHistoryTab.setTabListener(new TabListener<HistoryFragment>(this, str,
				HistoryFragment.class));
		str = getString(R.string.tabname_settings);		
		mSettingsTab.setTabListener(new TabListener<SettingsFragment>(this, str,
				SettingsFragment.class));

		// add the tabs to the action bar
		actionbar.addTab(mStartTab);
		actionbar.addTab(mHistoryTab);
		actionbar.addTab(mSettingsTab);

		// Crash the program -- example of debugging
		
		// Toast.makeText(getApplicationContext(),
		// "tab is " + savedInstanceState.getInt(TAB_KEY_INDEX, 0),
		// Toast.LENGTH_SHORT).show();

		// restore to navigation
		if (savedInstanceState != null) {
			Toast.makeText(getApplicationContext(),
					"tab is " + savedInstanceState.getInt(TAB_KEY_INDEX, 0),
					Toast.LENGTH_SHORT).show();

			actionbar.setSelectedNavigationItem(savedInstanceState.getInt(
					TAB_KEY_INDEX, 0));
		}
		
		// lab6
		mMessageIntentFilter = new IntentFilter();
		mMessageIntentFilter.addAction("GCM_NOTIFY");
		registerReceiver(mMessageUpdateReceiver, mMessageIntentFilter);

		context = getApplicationContext();
		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				registerInBackground();
			}
		}
		
		
    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the tab index before the activity goes into background.
		// Referred by string key TAB_INDEX_KEY
		outState.putInt(TAB_KEY_INDEX, getActionBar()
				.getSelectedNavigationIndex());
	}

	// Pretty standard stuff for TabListener.
	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = mActivity.getFragmentManager()
						.beginTransaction();
				ft.detach(mFragment);
				ft.commit();
			}
		}

		/* The following are each of the ActionBar.TabListener callbacks */
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}
	
	
	// lab6
	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				finish();
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
		return getSharedPreferences(MainActivity.class.getSimpleName(),
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

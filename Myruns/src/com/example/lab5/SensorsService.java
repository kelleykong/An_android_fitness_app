/**
 * LocationService.java
 * 
 * Created by Xiaochao Yang on Sep 11, 2011 4:50:19 PM
 * 
 */

package com.example.lab5;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.lab4.MapDisplayActivity;


public class SensorsService extends Service implements SensorEventListener {
	
	// Debugging tag
	public static final String TAG = "Lab5.SensorsService";	
	
	public static final int ACCELEROMETER_BUFFER_CAPACITY = 2048;
	public static final int ACCELEROMETER_BLOCK_CAPACITY = 64;
	
	public static final int SERVICE_TASK_TYPE_COLLECT = 0;
	public static final int SERVICE_TASK_TYPE_CLASSIFY = 1;
	
	public static final String ACTION_MOTION_UPDATED = "MYRUNS_MOTION_UPDATED";
	
	public static final String CLASS_LABEL_KEY = "label";	
	public static final String CLASS_LABEL_STANDING = "standing";
	public static final String CLASS_LABEL_WALKING = "walking";
	public static final String CLASS_LABEL_RUNNING = "running";
	public static final String CLASS_LABEL_OTHER = "others";
	
	public static final String FEAT_FFT_COEF_LABEL = "fft_coef_";
	public static final String FEAT_MAX_LABEL = "max";
	public static final String FEAT_SET_NAME = "accelerometer_features";

	public static final int FEATURE_SET_CAPACITY = 10000;
	
	public static final String LABELS_AUTO[] = {"Standing", "Walking", "Running", "Others"};
	
	public static final String AUTO_ACTIVITY_TYPE = "activityType_auto";
	public static final String ACTION = "updateActivityType";
	
//	public static final int NOTIFICATION_ID = 1;

	private static final int mFeatLen = ACCELEROMETER_BLOCK_CAPACITY + 2;
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private int mServiceTaskType;
	private Attribute mClassAttribute;
	private OnSensorChangedTask mAsyncTask;

	private static ArrayBlockingQueue<Double> mAccBuffer;
	public static final DecimalFormat mdf = new DecimalFormat("#.##");

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");

		mAccBuffer = new ArrayBlockingQueue<Double>(ACCELEROMETER_BUFFER_CAPACITY);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_FASTEST);

		mServiceTaskType = SERVICE_TASK_TYPE_COLLECT;

		// Create the container for attributes
		ArrayList<Attribute> allAttr = new ArrayList<Attribute>();

		// Adding FFT coefficient attributes
		DecimalFormat df = new DecimalFormat("0000");

		for (int i = 0; i < ACCELEROMETER_BLOCK_CAPACITY; i++) {
			allAttr.add(new Attribute(FEAT_FFT_COEF_LABEL + df.format(i)));
		}
		// Adding the max feature
		allAttr.add(new Attribute(FEAT_MAX_LABEL));

		// Declare a nominal attribute along with its candidate values
		ArrayList<String> labelItems = new ArrayList<String>(3);
		labelItems.add(CLASS_LABEL_STANDING);
		labelItems.add(CLASS_LABEL_WALKING);
		labelItems.add(CLASS_LABEL_RUNNING);
		labelItems.add(CLASS_LABEL_OTHER);
		mClassAttribute = new Attribute(CLASS_LABEL_KEY, labelItems);
		allAttr.add(mClassAttribute);


		Intent i = new Intent(this, MapDisplayActivity.class);
		// Read:
		// http://developer.android.com/guide/topics/manifest/activity-element.html#lmode
		// IMPORTANT!. no re-create activity
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

/*		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

		Notification notification = new Notification.Builder(this)
				.setContentTitle("??Lab5:notification?")
				.setContentText("notification...")
				.setSmallIcon(R.drawable.greend).setContentIntent(pi).build();
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification.flags = notification.flags
				| Notification.FLAG_ONGOING_EVENT;
		notificationManager.notify(0, notification);
*/

		mAsyncTask = new OnSensorChangedTask();
		mAsyncTask.execute();

		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		mAsyncTask.cancel(true);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mSensorManager.unregisterListener(this);
		Log.i("","");
		super.onDestroy();

	}

	private class OnSensorChangedTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {

			ArrayList<Double> featVect = new ArrayList<Double>();
//			inst.setDataset(mDataset);
			int blockSize = 0;
			FFT fft = new FFT(ACCELEROMETER_BLOCK_CAPACITY);
			double[] accBlock = new double[ACCELEROMETER_BLOCK_CAPACITY];
			double[] re = accBlock;
			double[] im = new double[ACCELEROMETER_BLOCK_CAPACITY];

			double max = Double.MIN_VALUE;

			while (true) {
				try {
					// need to check if the AsyncTask is cancelled or not in the while loop
					if (isCancelled () == true)
				    {
				        return null;
				    }
					
					// Dumping buffer
					accBlock[blockSize++] = mAccBuffer.take().doubleValue();

					if (blockSize == ACCELEROMETER_BLOCK_CAPACITY) {
						blockSize = 0;
						if (featVect.isEmpty() == false)
							featVect.clear();
						
						// time = System.currentTimeMillis();
						max = .0;
						for (double val : accBlock) {
							if (max < val) {
								max = val;
							}
						}

						fft.fft(re, im);

						for (int i = 0; i < re.length; i++) {
							double mag = Math.sqrt(re[i] * re[i] + im[i] * im[i]);
							featVect.add(Double.valueOf(mag));
							im[i] = .0; // Clear the field
						}

						// Append max after frequency component
						featVect.add(Double.valueOf(max));
						// classify
						double resultLabel = WekaClassifier.classify(featVect.toArray());
						
						Log.i("new instance", ""+resultLabel);
						
						Intent intent = new Intent();
						intent.setAction(ACTION);
						intent.putExtra(AUTO_ACTIVITY_TYPE, (int)resultLabel);
						sendBroadcast(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onCancelled() {
			
//			Log.e("123", mDataset.size()+"");
			
			if (mServiceTaskType == SERVICE_TASK_TYPE_CLASSIFY) {
				super.onCancelled();
				return;
			}
			Log.i("in the loop","still in the loop cancelled");

			super.onCancelled();
		}

	}

	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

			double m = Math.sqrt(event.values[0] * event.values[0]
					+ event.values[1] * event.values[1] + event.values[2]
					* event.values[2]);

			// Inserts the specified element into this queue if it is possible
			// to do so immediately without violating capacity restrictions,
			// returning true upon success and throwing an IllegalStateException
			// if no space is currently available. When using a
			// capacity-restricted queue, it is generally preferable to use
			// offer.

			try {
				mAccBuffer.add(new Double(m));
			} catch (IllegalStateException e) {
				Log.i(TAG, "onSensorChanged");

				// Exception happens when reach the capacity.
				// Doubling the buffer. ListBlockingQueue has no such issue,
				// But generally has worse performance
				ArrayBlockingQueue<Double> newBuf = new ArrayBlockingQueue<Double>(
						mAccBuffer.size() * 2);

				mAccBuffer.drainTo(newBuf);
				mAccBuffer = newBuf;
				mAccBuffer.add(new Double(m));
			}
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}

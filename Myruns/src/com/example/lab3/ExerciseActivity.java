package com.example.lab3;

import java.util.Calendar;
import java.util.Date;

import com.example.lab6.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ExerciseActivity extends Activity {
	
    public static final String[] TITLES = 
    {
            "Date",   
            "Time",
            "Duration",       
            "Distance",
            "Calories",
            "Heartrate",  
            "Comment",
    };
    
	private ExEntryDataSource datasource;
	private int mActivityType;
	private String mDisplayDateTime;
	static Calendar mDateAndTime;
	private int mDuration;
	private double mDistance;
	private int mCalorie;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		Toast.makeText(this, "FragmentLayout: OnCreate()", Toast.LENGTH_SHORT).show();
		setContentView(R.layout.activity_exercise);
		
		datasource = new ExEntryDataSource(this);
		datasource.open();
		
		mDateAndTime = Calendar.getInstance();
		
        // Get the message from the intent
        Intent intent = getIntent();
        mActivityType = intent.getIntExtra("activity_type", 0); 
	}

	public void onSaveClicked(View v) {
		Log.i("FragmentAlertDialog", "Save click!");

//		if (mActivity)
		if (mDisplayDateTime == null)
			updateDateAndTimeDisplay(mDateAndTime);
		datasource.createComment(0, mActivityType, mDisplayDateTime, mDuration*60, mDistance,
				0, 0, 0, null);		
		
		finish();
	}

	public void onCancelClicked(View v) {
		Log.i("FragmentAlertDialog", "Cancel click!");
		finish();
	}
	
	public void onPositiveClicked(int dialog_id, String input) {
		
		switch (dialog_id) {
		case 2:
			mDuration = Integer.parseInt(input);
			break;
		case 3:
			mDistance = Double.parseDouble(input);
			break;
			
		}
		
		Log.i("FragmentAlertDialog", "Positive click!");
	}

	public void onNegativeClicked() {
		// Do stuff here.
		Log.i("FragmentAlertDialog", "Negative click!");
	}
	
	private void updateDateAndTimeDisplay(Calendar cal) {
		Date d = cal.getTime();
		String[] time = d.toString().split(" ");
		mDisplayDateTime = time[3]+" " + time[1] +" " + time[2] + " " + time[5];
	}
	
	// This is the "top-level" fragment, showing a list of items that the user
	// can pick. Upon picking an item, it takes care of displaying the data to
	// the user as appropriate based on the current UI layout.

	// Displays a list of items that are managed by an adapter similar to
	// ListActivity. It provides several methods for managing a list view, such
	// as the onListItemClick() callback to handle click events.

	public static class TitlesFragment extends ListFragment {
		int mCurCheckPosition = 0;
		
		// onActivityCreated() is called when the activity's onCreate() method
		// has returned.

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// You can use getActivity(), which returns the activity associated
			// with a fragment.
			// The activity is a context (since Activity extends Context) .

//			Toast.makeText(getActivity(), "TitlesFragment:onActivityCreated",
//					Toast.LENGTH_LONG).show();

			// Populate list with our static array of titles in list in the
			// details about exercise
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_activated_1,
					TITLES));

			// Check to see if we have a frame in which to embed the details
			// fragment directly in the containing UI.
			// R.id.details relates to the res/layout-land/fragment_layout.xml
			// This is first created when the phone is switched to landscape
			// mode

//			View detailsFrame = getActivity().findViewById(R.id.details);

//			Toast.makeText(getActivity(), "detailsFrame " + detailsFrame,
//					Toast.LENGTH_LONG).show();

			if (savedInstanceState != null) {
				// Restore last state for checked position.
				mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
			}

		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			Toast.makeText(getActivity(), "onSaveInstanceState",
					Toast.LENGTH_LONG).show();

			outState.putInt("curChoice", mCurCheckPosition);
		}

		// If the user clicks on an item in the list (e.g., Henry V then the
		// onListItemClick() method is called. It calls a helper function in
		// this case.

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {

			Toast.makeText(getActivity(),
					"onListItemClick position is" + position, Toast.LENGTH_LONG)
					.show();

			switch (position) {
				case 0: onDateClicked(v); break;
				case 1: onTimeClicked(v); break;
				default: showDialog(position); break;
			}
			
		}
		
		public void showDialog(int index) {
			DialogFragment newFragment = MyDialogFragment.newInstance(index);
			newFragment.show(getFragmentManager(), "dialog");
		}
		
		public void onTimeClicked(View v) {

			TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
					mDateAndTime.set(Calendar.MINUTE, minute);
					((ExerciseActivity) getActivity()).updateDateAndTimeDisplay(mDateAndTime);					
				}
			};

			new TimePickerDialog(getActivity(), mTimeListener,
					mDateAndTime.get(Calendar.HOUR_OF_DAY),
					mDateAndTime.get(Calendar.MINUTE), true).show();

		}

		public void onDateClicked(View v) {

			DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					mDateAndTime.set(Calendar.YEAR, year);
					mDateAndTime.set(Calendar.MONTH, monthOfYear);
					mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					((ExerciseActivity) getActivity()).updateDateAndTimeDisplay(mDateAndTime);					
				}
			};

			new DatePickerDialog(getActivity(), mDateListener,
					mDateAndTime.get(Calendar.YEAR),
					mDateAndTime.get(Calendar.MONTH),
					mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();

		}
		

	}
	
	public static class MyDialogFragment extends DialogFragment {

		// For photo picker selection:
		public static final int ID_PHOTO_PICKER_FROM_CAMERA = 0;

		public static MyDialogFragment newInstance(int dialog_id) {
			MyDialogFragment frag = new MyDialogFragment();
			Bundle args = new Bundle();
			args.putInt("index", dialog_id);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final int dialog_id = getArguments().getInt("index");
			String title = new String();
			int inputType = InputType.TYPE_CLASS_NUMBER;
			
			final Activity parent = getActivity();

			switch (dialog_id) {
			case 2:
				title = "Duration(minutes):";
				break;
			case 3:
				title = "Distance(Miles):";
				break;
			case 4:
				title = "Calories:";
				break;
			case 5:
				title = "Average Heart Rate (BPM):";
				break;
			case 6:
				title = "Comment:";
				inputType = InputType.TYPE_CLASS_TEXT;
				break;
				
			}			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(title);
			
			// Set up the input
			final EditText input = new EditText(getActivity());
			// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
			input.setInputType(inputType);
			if (dialog_id == 6)
				input.setHint("\nHow did it go? Enter Notes here.\n");
			builder.setView(input);
			
			builder.setPositiveButton(R.string.alert_dialog_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							((ExerciseActivity) getActivity())
									.onPositiveClicked(dialog_id, input.getText().toString());
						}
					});
			builder.setNegativeButton(R.string.alert_dialog_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							((ExerciseActivity) getActivity())
									.onNegativeClicked();
						}
					});

			return builder.create();
			
		}
	}

	
}

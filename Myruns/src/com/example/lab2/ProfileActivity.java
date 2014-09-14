package com.example.lab2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.lab6.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
	public static final int REQUEST_CODE_FROM_GALLERY = 1;
	public static final int REQUEST_CODE_CROP_PHOTO = 2;

	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final String URI_INSTANCE_STATE_KEY = "saved_uri";

	private Uri mImageCaptureUri;
	private ImageView mImageView;
	private boolean isTakenFromCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        // Load user data to screen using the private helper function
        // loadProfile

        loadUserData();
      
		mImageView = (ImageView) findViewById(R.id.imageProfile);

		if (savedInstanceState != null) {
			mImageCaptureUri = savedInstanceState
					.getParcelable(URI_INSTANCE_STATE_KEY);
		}

		loadSnap();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                // Save the image capture uri before the activity goes into background
                outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
    }

	public void onChangePhotoClicked(View v) {
		
		displayDialog(MyRunsDialogFragment.DIALOG_ID_PHOTO_PICKER);
/*
		// Take photo from camera，
		// Construct an intent with action
		// MediaStore.ACTION_IMAGE_CAPTURE
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Construct temporary image path and name to save the taken
		// photo
		mImageCaptureUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageCaptureUri);
		intent.putExtra("return-data", true);
		try {
			// Start a camera capturing activity
			// REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
			// defined to identify the activity in onActivityResult()
			// when it returns
			startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
		isTakenFromCamera = true;
*/	}
	
	public void displayDialog(int id) {
		DialogFragment fragment = MyRunsDialogFragment.newInstance(id);
		fragment.show(getFragmentManager(),
				getString(R.string.dialog_fragment_tag_photo_picker));
	}
	
	public void onPhotoPickerItemSelected(int item) {
		Intent intent;

		switch (item) {

		case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:
			// Take photo from camera，
			// Construct an intent with action
			// MediaStore.ACTION_IMAGE_CAPTURE
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// Construct temporary image path and name to save the taken
			// photo
			mImageCaptureUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), "tmp_"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			intent.putExtra("return-data", true);
			try {
				// Start a camera capturing activity
				// REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
				// defined to identify the activity in onActivityResult()
				// when it returns
				startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
			isTakenFromCamera = true;
			break;
			
		case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_GALLERY:                                                             
			Intent nintent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);	
			startActivityForResult(nintent, REQUEST_CODE_FROM_GALLERY);	
			
			default:
				return ;
		}
	}
	
	// Handle data after activity returns.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case REQUEST_CODE_TAKE_FROM_CAMERA:
			// Send image taken from camera for cropping
			cropImage();
			break;

		case REQUEST_CODE_FROM_GALLERY:
			mImageCaptureUri = data.getData();
			cropImage();
			break;
            
		case REQUEST_CODE_CROP_PHOTO:
			// Update image view after image crop
			Bundle extras = data.getExtras();
			// Set the picture image in UI
			if (extras != null) {
				mImageView
						.setImageBitmap((Bitmap) extras.getParcelable("data"));
			}

			// Delete temporary image taken by camera after crop.
			if (isTakenFromCamera) {
				File f = new File(mImageCaptureUri.getPath());
				if (f.exists())
					f.delete();
			}

			break;
		}
	}
    
    public void onSaveClicked(View v) {
    	 
    	// Save all information from the screen into a "shared preferences"
        // using private helper function
    	
    	saveUserData();
    	
		// Save picture
		saveSnap();
    	
        Toast.makeText(getApplicationContext(),
                                getString(R.string.ui_profile_toast_save_text), Toast.LENGTH_SHORT).show();

		// Close the activity
		finish();
    }

    public void onCancelClicked(View v) {

        Toast.makeText(getApplicationContext(),
                                getString(R.string.ui_profile_toast_cancel_text), Toast.LENGTH_SHORT).show();

		// Close the activity
		finish();
    }
    
    // load the user data from shared preferences if there is no data make sure
    // that we set it to something reasonable
	private void loadUserData() {

                // We can also use log.d to print to the LogCat

//                Log.d(TAG, "loadUserData()");

                // Load and update all profile views

                // Get the shared preferences - create or retrieve the activity
                // preference object

                String mKey = getString(R.string.preference_name);
                SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

                // Load the user profile

                mKey = getString(R.string.preference_key_profile_name);
                String mValue = mPrefs.getString(mKey, "");
                ((EditText) findViewById(R.id.editName)).setText(mValue);
                
                mKey = getString(R.string.preference_key_profile_email);
                mValue = mPrefs.getString(mKey, "");
                ((EditText) findViewById(R.id.editEmail)).setText(mValue);
                
                mKey = getString(R.string.preference_key_profile_phone);
                mValue = mPrefs.getString(mKey, "");
                ((EditText) findViewById(R.id.editPhone)).setText(mValue);

                // Please Load gender info and set radio box

                mKey = getString(R.string.preference_key_profile_gender);

                int mIntValue = mPrefs.getInt(mKey, -1);
                // In case there isn't one saved before:
                if (mIntValue >= 0) {
                            // Find the radio button that should be checked.
                            RadioButton radioBtn = (RadioButton) ((RadioGroup) findViewById(R.id.radioGender))
                                                    .getChildAt(mIntValue);
                            // Check the button.
                            radioBtn.setChecked(true);
//                            Toast.makeText(getApplicationContext(),
//                                                    "number of the radioButton is : " + mIntValue,
//                                                    Toast.LENGTH_SHORT).show();
                }
                
                mKey = getString(R.string.preference_key_profile_class);
                mValue = mPrefs.getString(mKey, "");
                ((EditText) findViewById(R.id.editClass)).setText(mValue);
                
                mKey = getString(R.string.preference_key_profile_major);
                mValue = mPrefs.getString(mKey, "");
                ((EditText) findViewById(R.id.editMajor)).setText(mValue);

    }
    
    // load the user data from shared preferences if there is no data make sure
    // that we set it to something reasonable
    private void saveUserData() {

//                Log.d(TAG, "saveUserData()");

                // Getting the shared preferences editor

                String mKey = getString(R.string.preference_name);
                SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.clear();

                mKey = getString(R.string.preference_key_profile_name);
                String mValue = (String) ((EditText) findViewById(R.id.editName))
                                        .getText().toString();
                mEditor.putString(mKey, mValue);
                
                // Save email information

                mKey = getString(R.string.preference_key_profile_email);
                mValue = (String) ((EditText) findViewById(R.id.editEmail))
                                        .getText().toString();
                mEditor.putString(mKey, mValue);
                
                mKey = getString(R.string.preference_key_profile_phone);
                mValue = (String) ((EditText) findViewById(R.id.editPhone))
                                        .getText().toString();
                mEditor.putString(mKey, mValue);

                // Read which index the radio is checked.

                // edit this out and use as a debug example
                // interesting bug because you try and write an int to a string

                mKey = getString(R.string.preference_key_profile_gender);

                RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.radioGender);
                int mIntValue = mRadioGroup.indexOfChild(findViewById(mRadioGroup
                                        .getCheckedRadioButtonId()));
                mEditor.putInt(mKey, mIntValue);

                mKey = getString(R.string.preference_key_profile_class);
                mValue = (String) ((EditText) findViewById(R.id.editClass))
                                        .getText().toString();
                mEditor.putString(mKey, mValue);
                
                mKey = getString(R.string.preference_key_profile_major);
                mValue = (String) ((EditText) findViewById(R.id.editMajor))
                                        .getText().toString();
                mEditor.putString(mKey, mValue);
                
                // Commit all the changes into the shared preference
                mEditor.commit();

//                Toast.makeText(getApplicationContext(), "saved name: " + mValue,
//                                        Toast.LENGTH_SHORT).show();

    }
    
	private void loadSnap() {

		// Load profile photo from internal storage
		try {
			FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
			Bitmap bmap = BitmapFactory.decodeStream(fis);
			mImageView.setImageBitmap(bmap);
			fis.close();
		} catch (IOException e) {
			// Default profile photo if no photo saved before.
			mImageView.setImageResource(R.drawable.default_profile);
		}
	}
	
	private void saveSnap() {

		// Commit all the changes into preference file
		// Save profile image into internal storage.
		mImageView.buildDrawingCache();
		Bitmap bmap = mImageView.getDrawingCache();
		try {
			FileOutputStream fos = openFileOutput(
					getString(R.string.profile_photo_file_name), MODE_PRIVATE);
			bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	// Crop and resize the image for profile
	private void cropImage() {
		// Use existing crop activity.
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(mImageCaptureUri, IMAGE_UNSPECIFIED);

		// Specify image size
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);

		// Specify aspect ratio, 1:1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		// REQUEST_CODE_CROP_PHOTO is an integer tag you defined to
		// identify the activity in onActivityResult() when it returns
		startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
	}
	
	public static class MyRunsDialogFragment extends DialogFragment {

		// Different dialog IDs
		public static final int DIALOG_ID_ERROR = -1;
		public static final int DIALOG_ID_PHOTO_PICKER = 1;

		// For photo picker selection:
		public static final int ID_PHOTO_PICKER_FROM_CAMERA = 0;
		public static final int ID_PHOTO_PICKER_FROM_GALLERY = 1;

		private static final String DIALOG_ID_KEY = "dialog_id";
		
	    private static final String TAG = "photo";

		public static MyRunsDialogFragment newInstance(int dialog_id) {
			MyRunsDialogFragment frag = new MyRunsDialogFragment();
			Bundle args = new Bundle();
			Log.d(TAG, ""+dialog_id);
			args.putInt(DIALOG_ID_KEY, dialog_id);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int dialog_id = getArguments().getInt(DIALOG_ID_KEY);

			final Activity parent = getActivity();

			// Setup dialog appearance and onClick Listeners
			switch (dialog_id) {
			case DIALOG_ID_PHOTO_PICKER:
				// Build picture picker dialog for choosing from camera or gallery
				AlertDialog.Builder builder = new AlertDialog.Builder(parent);
				builder.setTitle(R.string.ui_profile_photo_picker_title);
				// Set up click listener, firing intents open camera
				DialogInterface.OnClickListener dlistener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// Item is ID_PHOTO_PICKER_FROM_CAMERA
						// Call the onPhotoPickerItemSelected in the parent
						// activity, i.e., ameraControlActivity in this case
						Log.d(TAG, ""+item);

						((ProfileActivity) parent)
								.onPhotoPickerItemSelected(item);
					}
				};
				// Set the item/s to display and create the dialog
				builder.setItems(R.array.ui_profile_photo_picker_items, dlistener);
				return builder.create();
			default:
				return null;
			}
		}
	}

}


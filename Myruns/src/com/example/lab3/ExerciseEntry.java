package com.example.lab3;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.example.lab5.SensorsService;
import com.google.android.gms.maps.model.LatLng;

public class ExerciseEntry {
	private long id;
    private int mInputType;        // Manual, GPS or automatic
    private int mActivityType;     // Running, cycling etc. 
    private String mDateTime;    // When does this entry happen
    private int mDuration;         // Exercise duration in seconds
    private double mDistance;      // Distance traveled. Either in meters or feet.   
    private double mAvgPace;       // Average pace
    private double mAvgSpeed;      // Average speed
    private int mCalorie;          // Calories burnt
    private double mClimb;         // Climb. Either in meters or feet.
    private int mHeartRate;        // Heart rate
    private String mComment;       // Comments
    private ArrayList<LatLng> mLocationList; // Location list


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getInputType() {
		return mInputType;
	}
	
	public int getActivityType() {
		return mActivityType;
	}
	
	public String getDateandTime() {
		return mDateTime;
	}

	public int getDuration() {
		return mDuration;
	}
	
	public double getDistance() {
		return mDistance;
	}
	
	public double getAvgSpd() {
		return mAvgSpeed;
	}
	
	public int getCalorie() {
		return mCalorie;
	}
	
	public double getClimb() {
		return mClimb;
	}
	
	public ArrayList<LatLng> getLocs() {
		return mLocationList;
	}

	public void setValues(int it, int at, String dat, int dur, float dis) {
		this.mInputType = it;
		this.mActivityType = at;
		this.mDateTime = dat;
		this.mDuration = dur;
		this.mDistance = dis;
	}

	public void setValues2(double avgSpd, int cal, double climb, byte[] locs) {
		this.mAvgSpeed = avgSpd;
		this.mCalorie = cal;
		this.mClimb = climb;
		if (locs != null) {
		this.mLocationList = new ArrayList<LatLng>();
		for (int i = 0; i < locs.length/16; i++) {
			byte[] lati = new byte[8];
			for (int j = 0; j < 8; j++)
				lati[j] = locs[i*16+j];
			byte[] lngi = new byte[8];
			for (int j = 0; j < 8; j++)
				lngi[j] = locs[i*16+8+j];
			double lat = getDouble(lati);
			double lng = getDouble(lngi);
			mLocationList.add(new LatLng(lat, lng));
		}
		}
	}

    public static long getLong(byte[] bytes)  
    {  
        return(0xffL & (long)bytes[0]) | (0xff00L & ((long)bytes[1] << 8)) | (0xff0000L & ((long)bytes[2] << 16)) | (0xff000000L & ((long)bytes[3] << 24))  
         | (0xff00000000L & ((long)bytes[4] << 32)) | (0xff0000000000L & ((long)bytes[5] << 40)) | (0xff000000000000L & ((long)bytes[6] << 48)) | (0xff00000000000000L & ((long)bytes[7] << 56));  
    }
    
    public static double getDouble(byte[] bytes)  
    {  
        long l = getLong(bytes);  
//        System.out.println(l);  
        return Double.longBitsToDouble(l);  
    } 
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		
		if (mInputType == 2)
			return SensorsService.LABELS_AUTO[mActivityType] + ", " + mDateTime + "\n" + 
					df.format(mDistance) + "Miles, " + mDuration/60 + "mins " + mDuration%60 + "secs";
		else
			return HistoryDetailsActivity.actTypes[mActivityType] + ", " + mDateTime + "\n" + 
		df.format(mDistance) + "Miles, " + mDuration/60 + "mins " + mDuration%60 + "secs";
		
	}
}

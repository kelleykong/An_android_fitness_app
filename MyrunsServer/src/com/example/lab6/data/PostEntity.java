package com.example.lab6.data;

public class PostEntity {
	public static String ENTITY_KIND_PARENT = "PostParent";
	public static String ENTITY_PARENT_KEY = ENTITY_KIND_PARENT;
	public static String ENTITY_KIND_POST = "Post";

	public static String FIELD_NAME_ID = "id";
	public static String FIELD_NAME_INPUTTYPE = "inputType";
	public static String FIELD_NAME_ACTIVITYTYPE = "activityType";
	public static String FIELD_NAME_DATE = "dateTime";
	public static String FIELD_NAME_DURATION = "duration";
	public static String FIELD_NAME_DISTANCE = "distance";
	public static String FIELD_NAME_AVGSPEED = "avgSpeed";
	public static String FIELD_NAME_CALORIE = "calorie";
	public static String FIELD_NAME_CLIMB = "climb";
	public static String FIELD_NAME_HEARTRATE = "heartrate";
	public static String FIELD_NAME_POST = "Post";

	
    public long id;					
	public int mInputType;			// Manual, GPS or automatic
	public int mActivityType;		// Running, cycling etc.
	public String mDateTime;		
	public int mDuration;			// Exercise duration in seconds
    public double mDistance;      	// Distance traveled. Either in meters or feet.   
    public double mAvgSpeed;      	// Average speed
    public int mCalorie;          	// Calories burnt
    public double mClimb;         	// Climb. Either in meters or feet.
    public int mHeartRate;        	// Heart rate	
	public String mPostString;		// comments

	public PostEntity() {
	}

	public PostEntity(long id, String post, String date) {
		this.id = id;
		mDateTime = date;
		mPostString = post;
	}
	
	public void setValues(int it, int at, String dat, int dur, float dis) {
		this.mInputType = it;
		this.mActivityType = at;
		this.mDateTime = dat;
		this.mDuration = dur;
		this.mDistance = dis;
	}

	public void setValues2(double avgSpd, int cal, double climb) {
		this.mAvgSpeed = avgSpd;
		this.mCalorie = cal;
		this.mClimb = climb;

	}
}

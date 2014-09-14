package com.example.lab3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_EXERCISE = "comments";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_INPUTTYPE = "input_type";
	public static final String COLUMN_ACTTYPE = "activity_type";
	public static final String COLUMN_DATEANDTIME = "date_time";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_AVGSPEED = "avg_speed";
	public static final String COLUMN_CALORIE = "calories";
	public static final String COLUMN_CLIMB = "climb";
	public static final String COLUMN_LOCS = "gps_data";

	private static final String DATABASE_NAME = "exercise.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_EXERCISE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_INPUTTYPE
			+ " integer not null, " + COLUMN_ACTTYPE
			+ " integer not null, " + COLUMN_DATEANDTIME
			+ " text not null, " + COLUMN_DURATION
			+ " integer not null, " + COLUMN_DISTANCE
			+ " float, " + COLUMN_AVGSPEED
			+ " float, " + COLUMN_CALORIE
			+ " integer, " + COLUMN_CLIMB
			+ " float, " + COLUMN_LOCS
			+ " BLOB );";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
		onCreate(db);
	}
}

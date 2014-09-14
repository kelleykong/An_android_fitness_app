package com.example.lab3;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class ExEntryDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_INPUTTYPE, MySQLiteHelper.COLUMN_ACTTYPE,
			MySQLiteHelper.COLUMN_DATEANDTIME, 
			MySQLiteHelper.COLUMN_DURATION, MySQLiteHelper.COLUMN_DISTANCE,
			MySQLiteHelper.COLUMN_AVGSPEED, MySQLiteHelper.COLUMN_CALORIE,
			MySQLiteHelper.COLUMN_CLIMB, MySQLiteHelper.COLUMN_LOCS};

	private static final String TAG = "DBDEMO";

	public ExEntryDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public ExerciseEntry createComment(int it, int at, String dt, int dur, double dis,
			double avgSpd, int cal, double climb, byte[] locs) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_INPUTTYPE, it);
		values.put(MySQLiteHelper.COLUMN_ACTTYPE, at);
		values.put(MySQLiteHelper.COLUMN_DATEANDTIME, dt);
		values.put(MySQLiteHelper.COLUMN_DURATION, dur);
		values.put(MySQLiteHelper.COLUMN_DISTANCE, dis);
		values.put(MySQLiteHelper.COLUMN_AVGSPEED, avgSpd);
		values.put(MySQLiteHelper.COLUMN_CALORIE, cal);
		values.put(MySQLiteHelper.COLUMN_CLIMB, climb);
		values.put(MySQLiteHelper.COLUMN_LOCS, locs);
		
		
		long insertId = database.insert(MySQLiteHelper.TABLE_EXERCISE, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISE,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		ExerciseEntry newComment = cursorToComment(cursor);

		// Log the comment stored
		Log.d(TAG, "comment = " + cursorToComment(cursor).toString()
				+ " insert ID = " + insertId);

		cursor.close();
		return newComment;
	}

	public void deleteComment(ExerciseEntry comment) {
		long id = comment.getId();
		Log.d(TAG, "delete comment = " + id);
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_EXERCISE, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}
	
	public void deleteAllComments() {
		System.out.println("Comment deleted all");
		Log.d(TAG, "delete all = ");
		database.delete(MySQLiteHelper.TABLE_EXERCISE, null, null);
	}
	
	public List<ExerciseEntry> getAllComments() {
		List<ExerciseEntry> comments = new ArrayList<ExerciseEntry>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISE,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ExerciseEntry comment = cursorToComment(cursor);
			Log.d(TAG, "get comment = " + cursorToComment(cursor).toString());
			comments.add(comment);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return comments;
	}

	private ExerciseEntry cursorToComment(Cursor cursor) {
		ExerciseEntry comment = new ExerciseEntry();
		comment.setId(cursor.getLong(0));
		comment.setValues(cursor.getInt(1), cursor.getInt(2),cursor.getString(3)
				, cursor.getInt(4), cursor.getFloat(5));
		comment.setValues2(cursor.getFloat(6), cursor.getInt(7)
				, cursor.getFloat(8), cursor.getBlob(9));
		return comment;
	}
}

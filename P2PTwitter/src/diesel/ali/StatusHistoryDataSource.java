package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StatusHistoryDataSource extends DataSource {

	public StatusHistoryDataSource(Context context) {
		super(context);
	}

	public void insertStatus(User sender, User recipient, String statusText,
			Integer time) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_SENDER, sender.getUsername());
		values.put(DatabaseHelper.COL_RECIPIENT, recipient.getUsername());
		values.put(DatabaseHelper.COL_STATUS, statusText);
		values.put(DatabaseHelper.COL_TIME, time);
		database.insert(DatabaseHelper.TABLE_STATUS_HISTORY, null, values);
	}

	/*
	 * public void deleteUser(User user) {
	 * database.delete(DatabaseHelper.TABLE_USERS, DatabaseHelper.COL_USERNAME +
	 * " =?", new String[] { user.getUsername() }); }
	 */

	public List<Status> getStatusHistory(User user) {
		List<Status> statuses = new ArrayList<Status>();
		/*Cursor cursor = database.query(DatabaseHelper.TABLE_STATUS_HISTORY,
				null, DatabaseHelper.COL_RECIPIENT + " =?" + " OR "
						+ DatabaseHelper.COL_SENDER + " =?", new String[] {
						user.getUsername(), user.getUsername() }, null, null,
				null);*/
		Cursor cursor = database.query(DatabaseHelper.TABLE_STATUS_HISTORY,
				null, DatabaseHelper.COL_RECIPIENT + " =?", new String[] {
						user.getUsername() }, null, null,
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			User sender = new User(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_SENDER)));
			User recipient = new User(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_RECIPIENT)));
			String statusText = cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_STATUS));
			Integer time = cursor.getInt(cursor
					.getColumnIndex(DatabaseHelper.COL_TIME));
			Status status = new Status(sender, recipient, statusText, time);
			statuses.add(status);
			cursor.moveToNext();
		}
		cursor.close();
		return statuses;

	}
}

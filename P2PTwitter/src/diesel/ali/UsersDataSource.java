package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UsersDataSource extends DataSource {

	public UsersDataSource(Context context) {
		super(context);
	}

	public void insertUser(User user) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_USERNAME, user.getUsername());
		database.insert(DatabaseHelper.TABLE_USERS, null, values);
	}

	public void deleteUser(User user) {
		database.delete(DatabaseHelper.TABLE_USERS, DatabaseHelper.COL_USERNAME
				+ " =?", new String[] { user.getUsername() });
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
				new String[] { DatabaseHelper.COL_USERNAME }, null, null, null,
				null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			User user = new User(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_USERNAME)));
			users.add(user);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}
}

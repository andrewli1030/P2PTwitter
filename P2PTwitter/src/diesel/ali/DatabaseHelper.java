package diesel.ali;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "P2PTwitterDB";
	public static final String TABLE_USERS = "Users";
	public static final String COL_USERNAME = "UserName";

	public static final String TABLE_STATUS_HISTORY = "Status_History";
	public static final String COL_RECIPIENT = "Recipient";
	public static final String COL_SENDER = "Sender";
	public static final String COL_STATUS = "Status";
	public static final String COL_TIME = "TIME";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 33);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_USERS + " (" + COL_USERNAME
				+ " TEXT)");

		db.execSQL("CREATE TABLE " + TABLE_STATUS_HISTORY + " (" + COL_SENDER
				+ " TEXT, " + COL_RECIPIENT + " TEXT, " + COL_STATUS + " TEXT, " + COL_TIME + " INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS_HISTORY);
		onCreate(db);
	}

}

package diesel.ali;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "P2PTwitterDB";
	public static final String TABLE_USERS = "Users";
	public static final String COL_USERNAME = "UserName";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 33);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_USERS + " (" + COL_USERNAME
				+ " TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		onCreate(db);
	}

}

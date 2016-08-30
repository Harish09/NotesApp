package harish.notesui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "notes";

    private static final String TABLE_FEED = "feed";

    private static final String ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DP = "dp";
    private static final String KEY_TIME = "time";
    private static final String KEY_STATUS = "status";
    private static final String KEY_IMAGE = "image";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT NOT NULL, %s TEXT NOT NULL, %s BLOB, %s DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL)",
                TABLE_FEED,
                ID,
                KEY_NAME,
                KEY_STATUS,
                KEY_IMAGE,
                KEY_TIME
        );

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED);

        onCreate(db);
    }

    public void getNotes() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_FEED, null);
        
       // ContentValues cv = new ContentValues();
       // cv.put(KEY_NAME,    name);
       // cv.put(KEY_IMAGE,   image);
//        database.insert( DB_TABLE, null, cv );
    }

}
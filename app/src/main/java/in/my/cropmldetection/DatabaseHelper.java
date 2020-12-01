package in.my.cropmldetection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CropDetectionSystem.db";

    public static final int version = 1;

    public static final String USERS_TABLE = "USERS";

    SQLiteDatabase sqLiteDatabase;

    public static final String user_sql = "CREATE TABLE " + USERS_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,EMAIL TEXT,PASSWORD TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(user_sql);
    }

    public void addUser(String name, String email, String password) {
        sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("EMAIL", email);
        contentValues.put("PASSWORD", password);
        sqLiteDatabase.insert("USERS", null, contentValues);
        Log.d("msg", "success");

    }

    public boolean CheckEmailExists(String email) {
        sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT EMAIL FROM USERS WHERE EMAIL=?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{email});
        if (cursor.getCount() == 1) {
            return true;
        }
        return false;
    }

    public boolean UserExists(String email, String password) {
        sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT EMAIL,PASSWORD FROM USERS WHERE EMAIL=? AND PASSWORD=?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{email, password});
        if (cursor.getCount() == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS" + USERS_TABLE;
        onCreate(db);
    }
}


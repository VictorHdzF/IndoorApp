package chaos.list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ListSQLHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "warehouse2.list.db";
    public static final String TABLE_NAME = "LIST";

    public static final String COL1_TASK = "item";
    public static final String UUID = "uuid";
    public static final String ZONE = "zone";
    public static final String MAJOR = "major";
    public static final String MINOR = "minor";
    public static final String X = "x";
    public static final String Y = "y";


    public static final String _ID = BaseColumns._ID;

    public ListSQLHelper(Context context) {
        //1 is todo list database version
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String createTodoListTable = "CREATE TABLE " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1_TASK + " TEXT, " + UUID + " TEXT, " + MAJOR + " INTEGER, " + MINOR + " INTEGER, " + X + " REAL, " + Y + " REAL, " + ZONE + " INTEGER)";
        sqlDB.execSQL(createTodoListTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqlDB);
    }
}

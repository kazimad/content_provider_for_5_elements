package data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Олег on 06.05.2014.
 */
public class MyDataBase extends SQLiteOpenHelper  {

    public static final String CREATE_DB = "create table mytable("
            + Columns._ID + " integer primary key,"
            + Columns.OPERATOR + " integer" + ");";


    public MyDataBase(Context context) {
        super(context, MyContentProvider.BASE_NAME, null, MyContentProvider.BASE_VERSION);

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(("DROP TABLE IF EXISTS mytable" ));              // if Base_version  changed, this line will drop dp to create a new one
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       onCreate(db);                                            // if Base_version  changed, this line will drop dp to create a new one
    }
public class Columns implements BaseColumns{


    public static final String OPERATOR = "operator";
}
}
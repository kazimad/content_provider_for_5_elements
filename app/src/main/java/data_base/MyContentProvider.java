package data_base;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.content_provider_for_5_elements.app.MainActivity;

import static android.content.UriMatcher.NO_MATCH;

/**
 * Created by Олег on 06.05.2014.
 */
public class MyContentProvider extends ContentProvider {
    public String myLog = "myLog";
    public static String mytable = "mytable";
    public static final String BASE_NAME = "base";
    public static final int BASE_VERSION = 10;
    public static final String AUTHORITY = "data_base.MyContentProvider.provider";
    public static final String PATH = "mytable";
    public static final String ID = "_id";
    public static final String OPERATOR = "OPERATOR";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);                          // общий URI
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PATH;               // тип набора строк
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PATH;         // тип одной строки
    private static final UriMatcher myUriMatcher;
    static final int URI_CONTACTS = 1;
    static final int URI_CONTACTS_ID = 2;
//////
    static {
        myUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        myUriMatcher.addURI(AUTHORITY, PATH, URI_CONTACTS);
        myUriMatcher.addURI(AUTHORITY, PATH + "/#",URI_CONTACTS_ID);
    }

    private SQLiteDatabase db;
    private MyDataBase myOpenHelper;


    @Override
    public boolean onCreate() {
        myOpenHelper = new MyDataBase(getContext());
//        Log.d(myLog, "OnCreate"+onCreate());
        return true;

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        db = myOpenHelper.getWritableDatabase();
        db.beginTransaction();
        int result = 0;
        try {
            for (result = 0; result < values.length; result++) {
                db.insert(mytable, null, values[result]);
            }

            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
        } finally {
            db.endTransaction();
        }

        return result;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (myUriMatcher.match(uri)) {
            case URI_CONTACTS:
                Log.d(myLog, "URi_ CONTaCTS");
                break;
            case URI_CONTACTS_ID:
                Log.d(myLog, "URI_CONTACTS_ID");
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + " = " + id;
                } else {
                    selection = selection + " AND " + ID + " = " + id;
                }
                break;
        }
        db = myOpenHelper.getWritableDatabase();
        Cursor myCursorContProv = db.query(mytable, null, selection, null, null, null, null);
        myCursorContProv.setNotificationUri(getContext().getContentResolver(),uri);   // тут посмотреть !!!!!!!
        return myCursorContProv;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(myLog, "getType, " + uri.toString());
        switch (myUriMatcher.match(uri)) {
            case URI_CONTACTS:
                return CONTENT_TYPE;
            case URI_CONTACTS_ID:
                return CONTENT_ITEM_TYPE;
        }
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
//        Log.d(myLog, "MyContentProvider.insert");
//        Log.d(myLog, uri.toString());
        db = myOpenHelper.getWritableDatabase();
//        Log.d(myLog, "db.getwritable");
//        Log.d(myLog, values.toString());
        long rowID = db.insert(mytable, null, values);
//        Log.d(myLog, "db.insert");
        Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
//        Log.d(myLog, "resultURI");
//        Log.d(myLog, resultUri.toString());
        getContext().getContentResolver().notifyChange(resultUri, null);
//        Log.d(myLog, "notify");
        return resultUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        db = myOpenHelper.getWritableDatabase();
        int delete = db.delete(mytable, selection, selectionArgs);
        return delete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (myUriMatcher.match(uri)) {
            case URI_CONTACTS:
                break;
            case URI_CONTACTS_ID:
                String selectedID = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + "=" + selectedID;
                } else {
                    selection = selection + "AND" + ID + "=" + selectedID;
                }
                break;
        }
        db = myOpenHelper.getWritableDatabase();
        int update = db.update(mytable, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return update;
    }

}


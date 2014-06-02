package com.example.content_provider_for_5_elements.app;

import android.annotation.TargetApi;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import data_base.MyContentProvider;
import data_base.MyDataBase;


public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {



    public final static Uri ID_FROM_DB = MyContentProvider.CONTENT_URI;
    public static final String myLog = "myLog";

    public ListView myView;
    MyAsyncQueryHendler myAsyncQueryHendler;



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(myLog, " content_values");

        getContentResolver().delete(ID_FROM_DB, null, null);
        Log.d(myLog, "getContentResolver.delete");
        myAsyncQueryHendler = new MyAsyncQueryHendler(getContentResolver());
        CounterTask myTask = new CounterTask(this);
        myTask.execute();

        getSupportLoaderManager().initLoader(1, Bundle.EMPTY, this);
//////////////////////////////////////////  тут!!!!!!!!!!




    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.d(myLog, "On createLoader");
        return new CursorLoader(this,ID_FROM_DB,null,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String from[] = {MyDataBase.Columns._ID, MyDataBase.Columns.OPERATOR};
        int to[] = {android.R.id.text1, android.R.id.text2};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, data, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        myView = (ListView) findViewById(R.id.listView);
        myView.setAdapter(adapter);
        Log.d(myLog, "onLoadFinish");

        myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Uri onClickUri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, id);
                Log.d(myLog, "enter to loop onItemClickListener");

                int random = (int) ((Math.random() * 5) + 1);
                Log.d(myLog, "random = " + random + " id= " + id);
                ContentValues onClickContentValues = new ContentValues();

                onClickContentValues.put(MyDataBase.Columns.OPERATOR, "№ of operator " + random);
                myAsyncQueryHendler.startUpdate(1, null, onClickUri, onClickContentValues, null, null);
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



}








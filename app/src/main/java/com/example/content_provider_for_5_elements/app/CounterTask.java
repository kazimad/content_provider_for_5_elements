package com.example.content_provider_for_5_elements.app;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import data_base.MyContentProvider;
import data_base.MyDataBase;

/**
 * Created by Олег on 21.05.2014.
 */
class CounterTask extends AsyncTask<Void, Void, ContentValues[]> {
    private Context myCounterContext;
    public final String myLog = "myLog";
    public final Uri ID_FROM_DB = MyContentProvider.CONTENT_URI;
    int valueOfQueue = 10;


    public ContentValues cv[] = new ContentValues[valueOfQueue + 1];

    CounterTask(Context context) {
        this.myCounterContext = context;
    }


    @Override
    protected ContentValues[] doInBackground(Void... params) {

        for (int i = 0; i <= valueOfQueue; i++) {
            ContentValues tempValue = new ContentValues();
            Log.d(myLog, "enter to loop");
            int random = (int) ((Math.random() * 5) + 1);

            tempValue.put(MyDataBase.Columns.OPERATOR, "№ of operator " + random);
            cv[i] = tempValue;

        }
        myCounterContext.getContentResolver().bulkInsert(ID_FROM_DB, cv);
        return cv;
    }

    @Override
    protected void onPostExecute(ContentValues[] contentValueses) {
        super.onPostExecute(contentValueses);

        Log.d(myLog, "onPostExecute");

    }

}

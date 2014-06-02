package com.example.content_provider_for_5_elements.app;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.util.Log;

/**
 * Created by Олег on 22.05.2014.
 */
public class MyAsyncQueryHendler extends AsyncQueryHandler{
//    ContentResolver MyAsyncQueryHendlerContentResorver;

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        super.onUpdateComplete(token, cookie, result);
        Log.d(MainActivity.myLog, "enter to  onUpdateComplete");


    }

    public MyAsyncQueryHendler(ContentResolver cr) {

        super(cr);
//        MyAsyncQueryHendlerContentResorver = cr;
        Log.d(MainActivity.myLog, "enter to  onUpdateComplete");


    }
}

package com.example.content_provider_for_5_elements.app;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import data_base.MyContentProvider;
import data_base.MyDataBase;

/**
 * Created by Олег on 21.05.2014.
 */
class CounterTask extends AsyncTask<Void, Void, ContentValues[]> {
    private Context myCounterContext;
    public final String myLog = "myLog";
    public final Uri ID_FROM_DB = MyContentProvider.CONTENT_URI;
    int valueOfQueue = 100;
    public static final String PATH = "https://www.dropbox.com/meta_dl/eyJzdWJfcGF0aCI6ICIiLCAidGVzdF9saW5rIjogZmFsc2UsICJzZXJ2ZXIiOiAiZGwuZHJvcGJveHVzZXJjb250ZW50LmNvbSIsICJpdGVtX2lkIjogbnVsbCwgImlzX2RpciI6IGZhbHNlLCAidGtleSI6ICI1ZnMxNHQ4c2VtM2hlNmcifQ/AAM8P8qa09O0djDojoUAqR5shVai_YQ5k9z5nx20AxxAlA?dl=1";
    public HttpURLConnection connection;
    public String in = null;
    public ContentValues cv[] = new ContentValues[valueOfQueue + 1];
    public BufferedReader bf;
    public StringBuilder sb;
    public URL url;
    public JSONArray jsonArray;
    public String NUMBER = "number";
    public String WINDOW = "window";
    public String OUTFILE;
    public static String CounterTaskJsonPath;

    CounterTask(Context context) {
        this.myCounterContext = context;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected ContentValues[] doInBackground(Void... params) {
        OUTFILE = (MainActivity.getJsonPath() + "/newfile_" + System.currentTimeMillis() + ".json");
        try {
            URL url = new URL(PATH);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(OUTFILE);

            int length;
            while ((length = is.read()) != -1) { // reads by bytes while it is possible
                os.write(length);


            }
            is.close();
            os.close();
            Log.d(myLog, OUTFILE);
            String readFile = null;
            try {
                BufferedReader br = new BufferedReader(new FileReader(OUTFILE));
                StringBuilder sbFile = new StringBuilder();
                String fileLine = br.readLine();
                while (fileLine != null) {
                    // append the line of the file
                    sbFile.append(fileLine);
                    // read the next line of the file
                    fileLine = br.readLine();
                }
                // this string contains the character sequence
                readFile = sbFile.toString();
                br.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                jsonArray = new JSONArray(readFile);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    Log.d(myLog, "enter to loop");

//                    String number = jsonArray.getString(Integer.parseInt(NUMBER));
//                    String window = jsonArray.getString(Integer.parseInt(WINDOW));
                    int number = jsonArray.getJSONObject(i).getInt(NUMBER);
                    int window = jsonArray.getJSONObject(i).getInt(WINDOW);

                    ContentValues tempValue = new ContentValues();

                    tempValue.put(MyDataBase.Columns.OPERATOR, "№ of Operator" + window);
                    tempValue.put(MyDataBase.Columns._ID, number);
                    cv[i] = tempValue;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            myCounterContext.getContentResolver().bulkInsert(ID_FROM_DB, cv);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cv;
    }
//    @Override
//    protected ContentValues[] doInBackground(Void... params) {
//
//        for (int i = 0; i <= valueOfQueue; i++) {
//
//
//            ContentValues tempValue = new ContentValues();
//            Log.d(myLog, "enter to loop");
//            int random = (int) ((Math.random() * 5) + 1);
//
//            tempValue.put(MyDataBase.Columns.OPERATOR, "№ of operator " + random);
//            cv[i] = tempValue;
//
//        }
//        myCounterContext.getContentResolver().bulkInsert(ID_FROM_DB, cv);
//        return cv;
//    }


    @Override
    protected void onPostExecute(ContentValues[] contentValueses) {
        super.onPostExecute(contentValueses);

        Log.d(myLog, "onPostExecute");

    }


}
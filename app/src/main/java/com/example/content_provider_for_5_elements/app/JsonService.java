package com.example.content_provider_for_5_elements.app;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import data_base.MyContentProvider;
import data_base.MyDataBase;

/**
 * Created by Олег on 03.06.2014.
 */
public class JsonService extends IntentService {

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

    public JsonService() {
        super("myService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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

            this.getContentResolver().bulkInsert(ID_FROM_DB, cv);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

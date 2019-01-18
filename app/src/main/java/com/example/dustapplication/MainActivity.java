package com.example.dustapplication;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String a, cityName, dust;
    boolean ifcityName, ifdust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.enableDefaults();

        TextView dustdata = (TextView) findViewById(R.id.dustData);
        String resultText = "값이 없음";

        try {
            resultText = new Task().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        resultText = finddust_data(resultText);
        dustdata.setText(resultText);
    }
    public String finddust_data (String jsonData) {
//        String [] data = new String[2];
        String gu=null, dusts = null, data = "";
        try {
            JSONArray jArray = new JSONObject(jsonData).getJSONArray("list");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);

                gu = jObject.getString("cityName");
                dusts = jObject.getString("pm10Value");

                data = data + gu + ": " + dusts + "\n";
                //data[0] = gu + dust;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}

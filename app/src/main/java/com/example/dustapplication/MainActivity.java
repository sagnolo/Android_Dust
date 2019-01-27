package com.example.dustapplication;

import android.nfc.Tag;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private TextView textView;

    private final String BASE_URL = "http://openapi.airkorea.or.kr/";
    private final String Key = "8DIGt1JffYRo9AxFNnQBjfud5kuDiROVhl0CBRCaWS8OJSZqwqH0A4dl2j3lWC+hMLJhn2maHKpIIoivntVtow==";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        GitHub gitHub = retrofit.create(GitHub.class);
        Map<String, String> params = new HashMap<>();

        params.put("sidoName","서울");
        params.put("searchCondition","DAILY");
        params.put("pageNo","1");
        params.put("numOfRows","25");
        params.put("ServiceKey",Key);
        params.put("_returnType","json");

        //Call<List_Data> call = gitHub.contributors("서울", "DAILY", "1","25", "json", Key);
        Call<List_Data> call = gitHub.contributors(params);

        call.enqueue(new Callback<List_Data>(){
            @Override
            public void onResponse(Call<List_Data> call, Response<List_Data> response) {
                List_Data list_data = response.body();
                String data= "";

                for(int i = 0; i < list_data.getList_detail().size();i++){ //값 넣어주기
                    data += "위치 : " + list_data.getList_detail().get(i).getCityName(); // 구
                    data += " 미세먼지 농도 : " + list_data.getList_detail().get(i).getPm10Value() + "\n"; // 미세먼지 농도
                }
                textView.setText(data);
            }

            @Override
            public void onFailure(Call<List_Data> call, Throwable t) {
                Toast.makeText(MainActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public void init() {
        textView = (TextView)findViewById(R.id.dustData);


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // GSON 컨버터를 사용하는 REST 어댑터 생성
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
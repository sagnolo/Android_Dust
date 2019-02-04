package com.example.dustapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import java.util.Locale;
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
    private TextView textView, gpsData;
    //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    private final int MY_PERMISSIONS_REQUEST_LOCATION=1001;
    ToggleButton gps;
    private final String BASE_URL = "http://openapi.airkorea.or.kr/";
    private final String Key = "8DIGt1JffYRo9AxFNnQBjfud5kuDiROVhl0CBRCaWS8OJSZqwqH0A4dl2j3lWC+hMLJhn2maHKpIIoivntVtow==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gps = (ToggleButton) findViewById(R.id.gps);
        gpsData = (TextView)findViewById(R.id.gpsData);

        // LocationManager 객체 얻어오기
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this,"위치 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                Toast.makeText(this,"위치 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            }
        }
        init();

        GitHub gitHub = retrofit.create(GitHub.class);
        Map<String, String> params = new HashMap<>();

        params.put("sidoName","서울");
        params.put("searchCondition","DAILY");
        params.put("pageNo","1");
        params.put("numOfRows","25");
        params.put("ServiceKey",Key);
        params.put("_returnType","json");

        //Call<List_Data> call = gitHub.contributors("서울", "DAILY", "1","25", "json", Key); // 쿼리 하나하나 보낼 때
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

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(gps.isChecked()){
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                100, // 최소 시간간격 (miliSecond)
                                1, // 최소 변경거리 (m)
                                mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                100,
                                1,
                                mLocationListener);
                    } else {
                        lm.removeUpdates(mLocationListener); // 자원 해제
                    }
                } catch (SecurityException ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged (Location location) {
            // 위치값 갱신 -> 이벤트 발생


            Log.d("test", "onLocationChanged, location:" + location);

            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude(); //위도
            double altitude = location.getAltitude(); //고도
            float accuracy = location.getAccuracy(); //정확도
            String provider = location.getProvider(); //위치제공자
            gpsData.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
                    + "\n고도 : " + altitude + "\n정확도 : "  + accuracy);
            findAddress(longitude,latitude);

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
            gpsData.setText("onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
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
    public void findAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault()); // 위도 및 경도 -> 주소 변환 geocoder 사용
        try {
            List<Address> address = geocoder.getFromLocation(lng, lat, 1); // 주소로 변환
            gpsData.setText(address.get(0).toString());
            Log.d("Data : ", address.get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
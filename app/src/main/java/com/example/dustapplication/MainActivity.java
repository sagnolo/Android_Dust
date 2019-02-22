package com.example.dustapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private TextView textView, gpsData;
    String localName;
    LocationManager lm;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ImageView khaiGrade;
    int [] imgid;

    //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1001;
    private final String BASE_URL = "http://openapi.airkorea.or.kr/";
    private final String Key = "8DIGt1JffYRo9AxFNnQBjfud5kuDiROVhl0CBRCaWS8OJSZqwqH0A4dl2j3lWC+hMLJhn2maHKpIIoivntVtow==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsData = (TextView) findViewById(R.id.gpsData);
        khaiGrade = (ImageView)findViewById(R.id.khaiGrade);
        // LocationManager 객체 얻어오기
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        imgid = new int [4];
        for( int i=0; i<4; i++) {
            imgid[i] = getResources().getIdentifier("@drawable/state"+ (i+1),"drawable",this.getPackageName());
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            }
        }

        init();

        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100, // 최소 시간간격 (miliSecond)
                    1, // 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    100,
                    1,
                    mLocationListener);
            //lm.removeUpdates(mLocationListener); // 자원 해제

        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치값 갱신 -> 이벤트 발생
            Log.d("test", "onLocationChanged, location:" + location);

            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude(); //위도
            double altitude = location.getAltitude(); //고도
            float accuracy = location.getAccuracy(); //정확도
            String provider = location.getProvider(); //위치제공자
            gpsData.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
                    + "\n고도 : " + altitude + "\n정확도 : " + accuracy);
            findAddress(longitude, latitude);
            lm.removeUpdates(mLocationListener);
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

    public void init() {
        textView = (TextView) findViewById(R.id.dustData);

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
            gpsData.setText(address.get(0).getSubLocality().toString() + " " + address.get(0).getThoroughfare()); // 구에 대한 정보 가져오기
            localName = address.get(0).getSubLocality().toString();
            find_dust();
            Log.d("Data : ", address.get(0).getSubLocality().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void find_dust() {
        GitHub gitHub = retrofit.create(GitHub.class);
        Map<String, String> params = new HashMap<>();

        params.put("stationName", localName);
        params.put("dataTerm", "DAILY");
        params.put("pageNo", "1");
        params.put("numOfRows", "25");
        params.put("ServiceKey", Key);
        params.put("_returnType", "json");
        params.put("ver","1.3");

        //Call<List_Data> call = gitHub.contributors("서울", "DAILY", "1","25", "json", Key); // 쿼리 하나하나 보낼 때
        Call<List_Data> call = gitHub.contributors(params);

        call.enqueue(new Callback<List_Data>() {
                @Override
                public void onResponse(Call<List_Data> call, Response<List_Data> response) {
                List_Data list_data = response.body();
                String data = "";

//                for (int i = 0; i < list_data.getList_detail().size(); i++) { //값 넣어주기
//                    if (list_data.getList_detail().get(i).getCityName().equals(gpsData.getText().toString())) {
//                        data += "위치 : " + list_data.getList_detail().get(i).getCityName(); // 구
//                        data += " 미세먼지 농도 : " + list_data.getList_detail().get(i).getPm10Value() + "\n"; // 미세먼지 농도
//                    }
//                }
                //data = " 미세먼지 농도 : " + list_data.getList_detail().get(0).getPm10Value() + "\n"; // 미세먼지 농도
                //textView.setText(data);
                ArrayList<ListInfo> list_dataArrayList = new ArrayList<>();
                khaiGrade.setImageResource(imgid[Integer.parseInt(list_data.getList_detail().get(0).getKhaiGrade())]);
                textView.setText("현재 위치");
                list_dataArrayList.add(new ListInfo(imgid[Integer.parseInt(list_data.getList_detail().get(0).getPm10Grade1h())],
                        list_data.getList_detail().get(0).getPm10Value() + " ㎍/m³","미세먼지"));
                list_dataArrayList.add(new ListInfo(imgid[Integer.parseInt(list_data.getList_detail().get(0).getPm25Grade1h())],
                        list_data.getList_detail().get(0).getPm25Value() + " ㎍/m³" ,"초미세먼지"));
                list_dataArrayList.add(new ListInfo(imgid[Integer.parseInt(list_data.getList_detail().get(0).getNo2Grade())],
                        list_data.getList_detail().get(0).getNo2Value() + " ppm","이산화질소"));
                list_dataArrayList.add(new ListInfo(imgid[Integer.parseInt(list_data.getList_detail().get(0).getO3Grade())],
                        list_data.getList_detail().get(0).getO3Value() + " ppm","오존"));
                list_dataArrayList.add(new ListInfo(imgid[Integer.parseInt(list_data.getList_detail().get(0).getCoGrade())],
                        list_data.getList_detail().get(0).getCoValue() + " ppm","일산화탄소"));
                list_dataArrayList.add(new ListInfo(imgid[Integer.parseInt(list_data.getList_detail().get(0).getSo2Grade())],
                        list_data.getList_detail().get(0).getSo2Value() + " ppm","아황산가스"));

                MyAdapter myAdapter = new MyAdapter(list_dataArrayList);
                mRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<List_Data> call, Throwable t) {
                Toast.makeText(MainActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

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
}
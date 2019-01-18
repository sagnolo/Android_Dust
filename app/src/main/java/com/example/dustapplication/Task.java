package com.example.dustapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Task extends AsyncTask<String, Void, String>{

    String Key = "8DIGt1JffYRo9AxFNnQBjfud5kuDiROVhl0CBRCaWS8OJSZqwqH0A4dl2j3lWC%2BhMLJhn2maHKpIIoivntVtow%3D%3D";
    private String str, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        try {
            //url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?sidoName=서울&searchCondition=DAILY&pageNo=1&numOfRows=25&ServiceKey=8DIGt1JffYRo9AxFNnQBjfud5kuDiROVhl0CBRCaWS8OJSZqwqH0A4dl2j3lWC%2BhMLJhn2maHKpIIoivntVtow%3D%3D&_returnType=json");
            url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/"
                    + "ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?sidoName=서울"
                    + "&searchCondition=DAILY&pageNo=1&numOfRows=25"
                    + "&ServiceKey=8DIGt1JffYRo9AxFNnQBjfud5kuDiROVhl0CBRCaWS8OJSZqwqH0A4dl2j3lWC%2BhMLJhn2maHKpIIoivntVtow%3D%3D"
                    + "&_returnType=json");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // HttpURLConnection을 통해 url과 통신
            conn.setRequestMethod("GET"); // GET방식

//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//            conn.setRequestProperty("x-waple-authorization", Key);
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);


                reader.close();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch ( MalformedURLException e) {
            e.printStackTrace();
        } catch ( IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}
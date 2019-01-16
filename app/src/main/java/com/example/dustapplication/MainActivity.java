package com.example.dustapplication;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String a, cityName, dust;
    boolean ifcityName, ifdust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.enableDefaults();

        TextView dustdata = (TextView)findViewById(R.id.dustData);

        try{ //Open API 공공데이터포탈 - 대기오염정보 조회 서비스
            URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?"
            + "sidoName=%EC%84%9C%EC%9A%B8&searchCondition=DAILY&pageNo=1&numOfRows=25&"
            + "ServiceKey=8DIGt1JffYRo9AxFNnQBjfud5kuDiROVhl0CBRCaWS8OJSZqwqH0A4dl2j3lWC%2BhMLJhn2maHKpIIoivntVtow%3D%3D");

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance(); //Xml 파싱을 위한 인스턴스 생성
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null); //입력 스트림 설정

            int parserEvent = parser.getEventType();
            System.out.println("파싱 시작");

            while(parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("cityName")){
                            ifcityName = true;
                        }
                        if(parser.getName().equals("pm10Value")){
                            ifdust = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(ifcityName){
                            cityName = parser.getText(); //지역명
                            ifcityName = false;
                        }
                        if(ifdust){
                            dust = parser.getText(); //미세먼지 농도
                            ifdust = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            dustdata.setText(dustdata.getText() + "지역 : " + cityName + "\t 미세먼지 : "+ dust +"\n");
                        }
                        break;
                }
                parserEvent = parser.next();
            }

        } catch(Exception e){
            dustdata.setText("[ " + e +" ]" + "오류");
        }
    }
}

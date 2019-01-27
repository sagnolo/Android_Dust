package com.example.dustapplication;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class List_Data {
    public List<List_Detail> list = new ArrayList<>();

    public List<List_Detail> getList_detail() {
        return list;
    }
    public void setList_detail(List<List_Detail> list_detail) {
        this.list = list_detail;
    }
    public class List_Detail {

        @SerializedName(value = "cityName")
        private String cityName;

        @SerializedName(value = "pm10Value")
        private String pm10Value;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getPm10Value() {
            return pm10Value;
        }

        public void setPm10Value(String pm10Value) {
            this.pm10Value = pm10Value;
        }
    }

}

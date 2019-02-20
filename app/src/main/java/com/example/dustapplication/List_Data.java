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

        @SerializedName(value = "so2Value")
        private String so2Value;

        @SerializedName(value = "so2Grade")
        private String so2Grade;

        @SerializedName(value = "pm10Value")
        private String pm10Value;

        @SerializedName(value = "pm10Grade1h")
        private String pm10Grade1h;

        @SerializedName(value = "coValue")
        private String coValue;

        @SerializedName(value = "coGrade")
        private String coGrade;

        @SerializedName(value = "o3Value")
        private String o3Value;

        @SerializedName(value = "o3Grade")
        private String o3Grade;

        @SerializedName(value = "no2Value")
        private String no2Value;

        @SerializedName(value = "no2Grade")
        private String no2Grade;

        @SerializedName(value = "pm25Value")
        private String pm25Value;

        @SerializedName(value = "pm25Grade1h")
        private String pm25Grade1h;

        @SerializedName(value = "khaiGrade")
        private String khaiGrade;

        public String getSo2Value() {
            return so2Value;
        }

        public void setSo2Value(String so2Value) {
            this.so2Value = so2Value;
        }

        public String getSo2Grade() {
            return so2Grade;
        }

        public void setSo2Grade(String so2Grade) {
            this.so2Grade = so2Grade;
        }

        public String getPm10Value() {
            return pm10Value;
        }

        public void setPm10Value(String pm10Value) {
            this.pm10Value = pm10Value;
        }

        public String getPm10Grade1h() {
            return pm10Grade1h;
        }

        public void setPm10Grade1h(String pm10Grade1h) {
            this.pm10Grade1h = pm10Grade1h;
        }

        public String getCoValue() {
            return coValue;
        }

        public void setCoValue(String coValue) {
            this.coValue = coValue;
        }

        public String getCoGrade() {
            return coGrade;
        }

        public void setCoGrade(String coGrade) {
            this.coGrade = coGrade;
        }

        public String getO3Value() {
            return o3Value;
        }

        public void setO3Value(String o3Value) {
            this.o3Value = o3Value;
        }

        public String getO3Grade() {
            return o3Grade;
        }

        public void setO3Grade(String o3Grade) {
            this.o3Grade = o3Grade;
        }

        public String getNo2Value() {
            return no2Value;
        }

        public void setNo2Value(String no2Value) {
            this.no2Value = no2Value;
        }

        public String getNo2Grade() {
            return no2Grade;
        }

        public void setNo2Grade(String no2Grade) {
            this.no2Grade = no2Grade;
        }

        public String getPm25Value() {
            return pm25Value;
        }

        public void setPm25Value(String pm25Value) {
            this.pm25Value = pm25Value;
        }

        public String getPm25Grade1h() {
            return pm25Grade1h;
        }

        public void setPm25Grade1h(String pm25Grade1h) {
            this.pm25Grade1h = pm25Grade1h;
        }

        public String getKhaiGrade() {
            return khaiGrade;
        }

        public void setKhaiGrade(String khaiGrade) {
            this.khaiGrade = khaiGrade;
        }

    }
}

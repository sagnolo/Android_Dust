package com.example.dustapplication;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GitHub {
    //@Headers({"Accept: application/json"})
    //@GET("openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst")
    @GET("openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty")
    Call<List_Data> contributors(
            //@Header("ServiceKey") String ServiceKey,
            // param 값
            @QueryMap Map<String, String> params);
//            @Query("sidoName") String sidoName, @Query("searchCondition") String searchCondition, @Query("pageNo") String pageNo,
//            @Query("numOfRows") String numOfRows, @Query("_returnType") String _returnType, @Query(value = "ServiceKey") String ServiceKey);
}

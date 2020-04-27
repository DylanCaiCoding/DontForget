package com.dylanc.dontforget.data.api

import com.dylanc.dontforget.data.bean.AppVersion
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface VersionApi {

  @GET("http://api.bq04.com/apps/latest/5e5f9a3e23389f2ac3d8a12a")
  fun check(
    @Query("api_token") api_token: String = "52bb77e7a2de204f5177c372541b0f99",
    @Query("type") type: String = "android"
  ): Single<AppVersion>
}
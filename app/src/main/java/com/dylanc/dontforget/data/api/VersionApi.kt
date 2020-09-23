package com.dylanc.dontforget.data.api

import com.dylanc.dontforget.data.bean.AppVersion
import com.dylanc.retrofit.helper.annotations.ApiUrl
import retrofit2.http.GET
import retrofit2.http.Query

@ApiUrl("http://api.bq04.com/")
interface VersionApi {

  @GET("/apps/latest/5f5ee3b123389f4c1b0fb730")
  suspend fun checkVersion(
    @Query("api_token") api_token: String = "52bb77e7a2de204f5177c372541b0f99",
    @Query("type") type: String = "android"
  ): AppVersion
}
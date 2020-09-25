package com.dylanc.dontforget.data.api

import com.dylanc.dontforget.data.bean.ApiResponse
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.ListPage
import retrofit2.http.*

/**
 * @author Dylan Cai
 * @since 2020/4/15
 */
interface InfoApi {
  companion object {
    private const val TODO = "/lg/todo"
  }

  @GET("$TODO/v2/list/{page}/json")
  suspend fun getInfoList(@Path("page") page: Int): ApiResponse<ListPage<DontForgetInfo>>

  @POST("$TODO/delete/{id}/json")
  suspend fun deleteInfo(@Path("id") id: Int): ApiResponse<Any>

  @FormUrlEncoded
  @POST("$TODO/update/{id}/json")
  suspend fun updateInfo(
    @Path("id") id: Int,
    @Field("title") title: String,
    @Field("date") date: String
  ): ApiResponse<DontForgetInfo>

  @FormUrlEncoded
  @POST("$TODO/add/json")
  suspend fun addInfo(@Field("title") title: String): ApiResponse<DontForgetInfo>
}
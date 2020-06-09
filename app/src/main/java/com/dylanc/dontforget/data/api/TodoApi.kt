package com.dylanc.dontforget.data.api

import com.dylanc.dontforget.data.bean.ApiResponse
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.ListPage
import io.reactivex.Single
import retrofit2.http.*

/**
 * @author Dylan Cai
 * @since 2020/4/15
 */
interface TodoApi {
  companion object {
    const val TODO = "/lg/todo"
  }

  @GET("$TODO/v2/list/{page}/json")
  fun getTodoList(
    @Path(value = "page", encoded = true) page: Int
  ): Single<ApiResponse<ListPage<DontForgetInfo>>>

  @GET("$TODO/v2/list/{page}/json")
  suspend fun getInfoList(@Path(value = "page") page: Int): ApiResponse<ListPage<DontForgetInfo>>

  @POST("$TODO/delete/{id}/json")
  fun deleteTodo(
    @Path(value = "id", encoded = true) id: Int
  ): Single<ApiResponse<Any?>>

  @FormUrlEncoded
  @POST("$TODO/update/{id}/json")
  fun updateTodo(
    @Path(value = "id", encoded = true) id: Int,
    @Field("title") title: String,
    @Field("date") date: String
  ): Single<ApiResponse<DontForgetInfo>>

  @FormUrlEncoded
  @POST("$TODO/add/json")
  fun addTodo(
    @Field("title") title: String
  ): Single<ApiResponse<DontForgetInfo>>
}
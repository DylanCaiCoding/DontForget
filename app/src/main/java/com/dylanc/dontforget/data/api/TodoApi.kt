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

  @GET
  fun getTodoList(
    @Url url: String
  ): Single<ApiResponse<ListPage<DontForgetInfo>>>

  @FormUrlEncoded
  @POST("$TODO/add/json")
  fun addTodo(
    @Field("title") title: String,
    @Field("content") content: String
  ): Single<ApiResponse<DontForgetInfo>>
}
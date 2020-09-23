package com.dylanc.dontforget.data.api

import com.dylanc.dontforget.data.bean.ApiResponse
import com.dylanc.dontforget.data.bean.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author Dylan Cai
 * @since 2020/4/15
 */
interface UserApi {
  companion object {
    const val USER = "/user"
  }

  @FormUrlEncoded
  @POST("$USER/login")
  suspend fun login(
    @Field("username") username: String,
    @Field("password") password: String
  ): ApiResponse<User>

  @FormUrlEncoded
  @POST("$USER/register")
  suspend fun register(
    @Field("username") username: String,
    @Field("password") password: String,
    @Field("repassword") confirmPassword: String
  ): ApiResponse<User>

  @GET("$USER/logout/json")
  suspend fun logout(): ApiResponse<Any>
}
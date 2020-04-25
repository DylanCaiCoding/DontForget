package com.dylanc.dontforget.data.api

import com.dylanc.dontforget.data.bean.ApiResponse
import com.dylanc.dontforget.data.bean.User
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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
  fun login(
    @Field("username") username: String,
    @Field("password") password: String
  ): Single<ApiResponse<User>>
}
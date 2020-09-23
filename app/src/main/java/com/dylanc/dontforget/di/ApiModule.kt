package com.dylanc.dontforget.di

import com.dylanc.dontforget.data.api.InfoApi
import com.dylanc.dontforget.data.api.UserApi
import com.dylanc.dontforget.data.api.VersionApi
import com.dylanc.retrofit.helper.apiOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApiModule {

  @Provides
  @Singleton
  fun provideUserApi(): UserApi = apiOf()

  @Provides
  @Singleton
  fun provideInfoApi(): InfoApi = apiOf()

  @Provides
  @Singleton
  fun provideVersionApi(): VersionApi = apiOf()
}
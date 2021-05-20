package com.dylanc.dontforget.di

import com.dylanc.dontforget.data.db.AppDatabase
import com.dylanc.dontforget.data.db.InfoDao
import com.dylanc.dontforget.data.db.UserDao
import com.dylanc.dontforget.utils.roomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataBaseModule {

  @Provides
  @Singleton
  fun provideAppDataBase(): AppDatabase = roomDatabase("app_db")

  @Provides
  @Singleton
  fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

  @Provides
  @Singleton
  fun provideInfoDao(appDatabase: AppDatabase): InfoDao = appDatabase.infoDao()
}
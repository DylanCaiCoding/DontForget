package com.dylanc.dontforget.di

import android.app.Application
import androidx.room.Room
import com.dylanc.dontforget.data.db.AppDatabase
import com.dylanc.dontforget.data.db.InfoDao
import com.dylanc.dontforget.data.db.UserDao
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
  fun provideAppDataBase(application: Application): AppDatabase =
    Room.databaseBuilder(application, AppDatabase::class.java, "app_db").build()

  @Provides
  @Singleton
  fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

  @Provides
  @Singleton
  fun provideInfoDao(appDatabase: AppDatabase): InfoDao = appDatabase.infoDao()
}
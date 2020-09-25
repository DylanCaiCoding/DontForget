package com.dylanc.dontforget.di

import com.dylanc.dontforget.data.repository.*
import com.dylanc.dontforget.data.api.InfoApi
import com.dylanc.dontforget.data.api.UserApi
import com.dylanc.dontforget.data.api.VersionApi
import com.dylanc.dontforget.data.db.InfoDao
import com.dylanc.dontforget.data.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

  @Provides
  @Singleton
  fun provideUserRepository(userDao: UserDao, infoDao: InfoDao, userApi: UserApi): UserRepository =
    UserRepository(UserLocalDataSource(userDao, infoDao), UserRemoteDataSource(userApi))

  @Provides
  @Singleton
  fun provideInfoRepository(infoDao: InfoDao, infoApi: InfoApi): InfoRepository =
    InfoRepository(InfoLocalDataSource(infoDao), InfoRemoteDataSource(infoApi))

  @Provides
  @Singleton
  fun provideVersionRepository(versionApi: VersionApi): VersionRepository =
    VersionRepository(VersionRemoteDataSource(versionApi))

  @Provides
  @Singleton
  fun provideSettingRepository(): SettingRepository =
    SettingRepository(SettingLocalDataSource())
}
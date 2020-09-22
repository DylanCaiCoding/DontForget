package com.dylanc.dontforget.module

import com.dylanc.dontforget.data.repository.*
import com.dylanc.dontforget.data.repository.api.InfoApi
import com.dylanc.dontforget.data.repository.api.UserApi
import com.dylanc.dontforget.data.repository.api.VersionApi
import com.dylanc.dontforget.data.repository.db.InfoDao
import com.dylanc.dontforget.data.repository.db.UserDao
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
  fun provideUserRepository(userDao: UserDao, userApi: UserApi, infoRepository: InfoRepository): UserRepository =
    UserRepository(UserLocalDataSource(userDao), UserRemoteDataSource(userApi), infoRepository)

  @Provides
  @Singleton
  fun provideInfoRepository(infoDao: InfoDao, infoApi: InfoApi): InfoRepository =
    InfoRepository(InfoLocalDataSource(infoDao), InfoRemoteDataSource(infoApi))

  @Provides
  @Singleton
  fun provideVersionRepository(versionApi: VersionApi): VersionRepository =
    VersionRepository(VersionRemoteDataSource(versionApi))
}
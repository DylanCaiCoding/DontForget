package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.repository.api.VersionApi
import com.dylanc.retrofit.helper.apiServiceOf

class VersionRepository(
  private val remoteDataSource: VersionRemoteDataSource = VersionRemoteDataSource()
) {
  suspend fun checkVersion()  =
    remoteDataSource.checkVersion()
}

class VersionRemoteDataSource {
  suspend fun checkVersion() =
    apiServiceOf<VersionApi>().checkVersion()
}
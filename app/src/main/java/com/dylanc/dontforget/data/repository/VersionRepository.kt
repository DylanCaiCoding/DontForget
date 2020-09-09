package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.repository.api.VersionApi
import com.dylanc.retrofit.helper.apiOf
import kotlinx.coroutines.flow.flow

class VersionRepository(
  private val remoteDataSource: VersionRemoteDataSource = VersionRemoteDataSource()
) {
  fun checkVersion() = flow {
    emit(remoteDataSource.checkVersion())
  }
}

class VersionRemoteDataSource {
  suspend fun checkVersion() =
    apiOf<VersionApi>().checkVersion()
}
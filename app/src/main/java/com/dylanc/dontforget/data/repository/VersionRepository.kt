package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.repository.api.VersionApi
import com.dylanc.retrofit.helper.apiOf
import kotlinx.coroutines.flow.flow

class VersionRepository(
  private val remoteDataSource: VersionRemoteDataSource
) {
  fun checkVersion() = flow {
    emit(remoteDataSource.checkVersion())
  }
}

class VersionRemoteDataSource(private val api: VersionApi) {
  suspend fun checkVersion() = api.checkVersion()
}
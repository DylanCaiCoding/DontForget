package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.api.VersionApi
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
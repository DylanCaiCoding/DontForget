package com.dylanc.dontforget.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dylanc.dontforget.data.bean.AppVersion
import com.dylanc.dontforget.data.repository.api.VersionApi
import com.dylanc.retrofit.helper.apiServiceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VersionRepository(
  private val remoteDataSource: VersionRemoteDataSource = VersionRemoteDataSource()
) {
  private val _appVersion = MutableLiveData<AppVersion>()
  val appVersion: LiveData<AppVersion> = _appVersion

  suspend fun checkVersion() {
    _appVersion.value = remoteDataSource.checkVersion()
  }
}

class VersionRemoteDataSource {
  suspend fun checkVersion() = withContext(Dispatchers.IO) {
    apiServiceOf<VersionApi>().checkVersion()
  }
}
package com.dylanc.dontforget.viewmodel.request

import androidx.lifecycle.asLiveData
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.repository.VersionRepository
import com.dylanc.retrofit.helper.coroutines.catch
import com.dylanc.retrofit.helper.coroutines.showLoading

class VersionRequestViewModel : RequestViewModel() {

  private val versionRepository = VersionRepository()

  fun checkVersion() =
    versionRepository.checkVersion()
      .showLoading(loading)
      .catch(exception)
      .asLiveData()
}
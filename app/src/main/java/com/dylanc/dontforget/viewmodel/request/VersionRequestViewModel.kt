package com.dylanc.dontforget.viewmodel.request

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.repository.VersionRepository
import com.dylanc.retrofit.helper.coroutines.livedata.catch
import com.dylanc.retrofit.helper.coroutines.livedata.showLoading

class VersionRequestViewModel @ViewModelInject constructor(
  private val versionRepository: VersionRepository
) : RequestViewModel() {

  fun checkVersion() =
    versionRepository.checkVersion()
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()
}
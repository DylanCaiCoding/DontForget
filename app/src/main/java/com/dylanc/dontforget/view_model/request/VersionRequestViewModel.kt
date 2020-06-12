package com.dylanc.dontforget.view_model.request

import androidx.lifecycle.liveData
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.repository.VersionRepository

class VersionRequestViewModel : RequestViewModel() {

  private val versionRepository = VersionRepository()

  fun checkVersion() = liveData(requestExceptionHandler) {
    emit(versionRepository.checkVersion())
  }
}
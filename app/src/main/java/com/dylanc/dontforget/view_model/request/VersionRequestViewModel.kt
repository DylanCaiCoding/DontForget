package com.dylanc.dontforget.view_model.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.data.repository.VersionRepository
import kotlinx.coroutines.launch

class VersionRequestViewModel: ViewModel() {

  private val versionRepository = VersionRepository()
  val appVersion = versionRepository.appVersion

  fun checkVersion() = viewModelScope.launch {
    versionRepository.checkVersion()
  }
}
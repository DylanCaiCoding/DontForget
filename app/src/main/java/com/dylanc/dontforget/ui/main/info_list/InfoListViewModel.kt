package com.dylanc.dontforget.ui.main.info_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.InfoRepository
import kotlinx.coroutines.launch

class InfoListViewModel : ViewModel() {
  private val infoRepository = InfoRepository()
  val list = infoRepository.allInfo
  val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(true)

  fun getInfoList() = viewModelScope.launch {
    infoRepository.getInfoList()
    isRefreshing.value = false
  }

  fun requestInfoList() = viewModelScope.launch {
    infoRepository.requestInfoList()
    isRefreshing.value = false
  }

  fun insertInfo(info: DontForgetInfo) = viewModelScope.launch {
    infoRepository.insertInfo(info)
  }
}
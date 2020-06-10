package com.dylanc.dontforget.view_model.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.infoRepository
import kotlinx.coroutines.launch

class InfoRequestViewModel : ViewModel() {
  val list = infoRepository.allInfo
  val insertedInfo = infoRepository.insertedInfo

  fun getInfoList() = viewModelScope.launch {
    infoRepository.getInfoList()
  }

  fun requestInfoList() = viewModelScope.launch {
    infoRepository.requestInfoList()
  }

  fun addInfo(title: String) = viewModelScope.launch {
    infoRepository.addInfo(title)
  }

  fun updateInfo(id: Int, title: String, date: String) = viewModelScope.launch {
    infoRepository.updateInfo(id, title, date)
  }

  fun deleteInfo(info: DontForgetInfo) = viewModelScope.launch {
    infoRepository.deleteInfo(info)
  }
}
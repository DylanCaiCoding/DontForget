package com.dylanc.dontforget.ui.main.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.infoRepository
import kotlinx.coroutines.launch

class InsertInfoViewModel : ViewModel() {
  val title: MutableLiveData<String> = MutableLiveData()
  val info: MutableLiveData<DontForgetInfo> = MutableLiveData()

  fun addInfo() = viewModelScope.launch {
    infoRepository.addInfo(title.value!!)
  }

  fun updateInfo() = viewModelScope.launch {
    infoRepository.updateInfo(info.value!!.id, title.value!!, info.value!!.dateStr)
  }
}
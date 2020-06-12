package com.dylanc.dontforget.view_model.request

import androidx.lifecycle.liveData
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.infoRepository

class InfoRequestViewModel : RequestViewModel() {
  val list = infoRepository.allInfo

  fun getInfoList() = liveData(requestExceptionHandler) {
    emit(infoRepository.getInfoList())
  }

  fun requestInfoList() = liveData(requestExceptionHandler) {
    emit(infoRepository.requestInfoList())
  }

  fun addInfo(title: String) = liveData(requestExceptionHandler) {
    emit(infoRepository.addInfo(title))
  }

  fun updateInfo(id: Int, title: String, date: String) = liveData(requestExceptionHandler) {
    emit(infoRepository.updateInfo(id, title, date))
  }

  fun deleteInfo(info: DontForgetInfo) = liveData(requestExceptionHandler) {
    emit(infoRepository.deleteInfo(info))
  }
}
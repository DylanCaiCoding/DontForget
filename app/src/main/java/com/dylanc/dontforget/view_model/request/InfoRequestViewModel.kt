package com.dylanc.dontforget.view_model.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.net.Resource
import com.dylanc.dontforget.data.repository.infoRepository

class InfoRequestViewModel : ViewModel() {
  val list = infoRepository.allInfo

  fun getInfoList() = liveData {
    emit(Resource.loading())
    emit(infoRepository.getInfoList())
  }

  fun requestInfoList() = liveData {
    emit(Resource.loading())
    emit(infoRepository.requestInfoList())
  }

  fun addInfo(title: String) = liveData {
    emit(Resource.loading())
    emit(infoRepository.addInfo(title))
  }

  fun updateInfo(id: Int, title: String, date: String) = liveData {
    emit(Resource.loading())
    emit(infoRepository.updateInfo(id, title, date))
  }

  fun deleteInfo(info: DontForgetInfo) = liveData {
    emit(Resource.loading())
    emit(infoRepository.deleteInfo(info))
  }
}
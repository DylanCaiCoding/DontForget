package com.dylanc.dontforget.viewmodel.request

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.InfoRepository
import com.dylanc.retrofit.helper.coroutines.livedata.LoadingLiveData
import com.dylanc.retrofit.helper.coroutines.livedata.catch
import com.dylanc.retrofit.helper.coroutines.livedata.showLoading
import kotlinx.coroutines.flow.flow

class InfoRequestViewModel @ViewModelInject constructor(
  private val infoRepository: InfoRepository
) : RequestViewModel() {

  val list = infoRepository.allInfo

  val isRefreshing = LoadingLiveData()

  fun initInfoList() =
    flow {
      emit(infoRepository.initInfoList())
    }
      .showLoading(isRefreshing)
      .catch(exception)
      .asLiveData()

  fun refreshInfoList() =
    flow {
      emit(infoRepository.refreshInfoList())
    }
      .showLoading(isRefreshing)
      .catch(exception)
      .asLiveData()

  fun addInfo(title: String?) =
    flow {
      checkNotNull(title) { "请输入标题" }
      emit(infoRepository.addInfo(title))
    }
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

  fun updateInfo(id: Int, title: String?, date: String) =
    flow {
      checkNotNull(title) { "请输入标题" }
      emit(infoRepository.updateInfo(id, title, date))
    }
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

  fun deleteInfo(info: DontForgetInfo) =
    flow {
      emit(infoRepository.deleteInfo(info))
    }
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()
}
package com.dylanc.dontforget.viewmodel.request

import androidx.lifecycle.asLiveData
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.infoRepository
import com.dylanc.retrofit.helper.coroutines.LoadingLiveData
import com.dylanc.retrofit.helper.coroutines.catch
import com.dylanc.retrofit.helper.coroutines.showLoading

class InfoRequestViewModel : RequestViewModel() {
  val list = infoRepository.allInfo
  val isRefreshing  = LoadingLiveData()

  fun getInfoList() =
    infoRepository.getInfoList()
    .showLoading(isRefreshing)
    .catch(exception)
    .asLiveData()

  fun requestInfoList() =
    infoRepository.refreshInfoList()
    .showLoading(isRefreshing)
    .catch(exception)
    .asLiveData()

  fun addInfo(title: String?) =
    infoRepository.addInfo(title)
    .showLoading(isLoading)
    .catch(exception)
    .asLiveData()

  fun updateInfo(id: Int, title: String?, date: String) =
    infoRepository.updateInfo(id, title, date)
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

  fun deleteInfo(info: DontForgetInfo) =
    infoRepository.deleteInfo(info)
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()
}
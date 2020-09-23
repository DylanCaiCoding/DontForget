package com.dylanc.dontforget.viewmodel.request

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.InfoRepository
import com.dylanc.retrofit.helper.coroutines.LoadingLiveData
import com.dylanc.retrofit.helper.coroutines.catch
import com.dylanc.retrofit.helper.coroutines.showLoading

class InfoRequestViewModel @ViewModelInject constructor(
  private val infoRepository: InfoRepository
) : RequestViewModel() {
  val list = infoRepository.allInfo
  val isRefreshing = LoadingLiveData()

  fun initInfoList() =
    infoRepository.initInfoList()
      .showLoading(isRefreshing)
      .catch(exception)
      .asLiveData()

  fun refreshInfoList() =
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
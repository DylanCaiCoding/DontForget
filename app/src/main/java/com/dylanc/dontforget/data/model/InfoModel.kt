package com.dylanc.dontforget.data.model

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.bean.ApiResponse
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.ListPage
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.rxjava.io2mainThread
import io.reactivex.Single

class InfoModel {
  private var page: Int = 1
  private val list = mutableListOf<DontForgetInfo>()

  @SuppressLint("CheckResult")
  fun requestList(infoList: MutableLiveData<List<DontForgetInfo>>) {
    list.clear()
    page = 1
    loadList()
      .io2mainThread()
      .subscribe({
        DontForgetInfoRepository.updateInfos(list)
        infoList.value = list
      }, {})
  }

  private fun loadList(): Single<ApiResponse<ListPage<DontForgetInfo>>> {
    return apiServiceOf<TodoApi>()
      .getTodoList(page)
      .flatMap { response ->
        if (response.data.over) {
          list.addAll(response.data.list)
          Single.just(response)
        } else {
          page++
          list.addAll(response.data.list)
          loadList()
        }
      }
  }
}
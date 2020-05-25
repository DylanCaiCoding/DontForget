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
  private val items = arrayListOf<Any>()

  @SuppressLint("CheckResult")
  fun requestList(list: MutableLiveData<MutableList<Any>>) {
    items.clear()
    page = 1
    loadList()
      .io2mainThread()
      .subscribe({
        val infoList = arrayListOf<DontForgetInfo>()
        for (item in items) {
          if (item is DontForgetInfo) {
            infoList.add(item)
          }
        }
        DontForgetInfoRepository.updateInfos(infoList)
        list.value = items
      }, {})
  }

  private fun loadList(): Single<ApiResponse<ListPage<DontForgetInfo>>> {
    return apiServiceOf<TodoApi>()
      .getTodoList(page)
      .flatMap { response ->
        if (response.data.over) {
          items.addAll(response.data.list)
          Single.just(response)
        } else {
          page++
          items.addAll(response.data.list)
          loadList()
        }
      }
  }
}
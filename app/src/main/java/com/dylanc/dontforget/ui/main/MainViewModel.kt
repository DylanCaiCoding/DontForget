package com.dylanc.dontforget.ui.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.bean.ApiResponse
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.ListPage
import com.dylanc.dontforget.data.net.RxLoadingDialog
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.transformer.io2mainThread
import com.dylanc.retrofit.helper.transformer.showLoading
import io.reactivex.Single

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
class MainViewModel : ViewModel() {

  val list: MutableLiveData<MutableList<Any>> = MutableLiveData(arrayListOf())
  private var page: Int = 1
  private val items = arrayListOf<Any>()

  @SuppressLint("CheckResult")
  fun requestList(context: Context) {
    items.clear()
    loadList()
      .io2mainThread()
      .showLoading(RxLoadingDialog(context))
      .subscribe({
        val infos = arrayListOf<DontForgetInfo>()
        for (item in items) {
          if (item is DontForgetInfo) {
            infos.add(item)
          }
        }
        DontForgetInfoRepository.updateInfos(infos)
        list.value = items
      }, {})
  }

  private fun loadList(): Single<ApiResponse<ListPage<DontForgetInfo>>> {
    return apiServiceOf<TodoApi>()
      .getTodoList("${TodoApi.TODO}/v2/list/$page/json").flatMap { response ->
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
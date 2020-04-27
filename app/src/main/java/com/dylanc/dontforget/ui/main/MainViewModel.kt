package com.dylanc.dontforget.ui.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.net.RxLoadingDialog
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.transformer.io2mainThread
import com.dylanc.retrofit.helper.transformer.showLoading

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
class MainViewModel : ViewModel() {

  val list: MutableLiveData<MutableList<Any>> = MutableLiveData(arrayListOf())
  private val page: MutableLiveData<Int> = MutableLiveData(1)

  @SuppressLint("CheckResult")
  fun requestList(context: Context) {
    apiServiceOf<TodoApi>()
      .getTodoList("${TodoApi.TODO}/v2/list/${page.value}/json")
      .io2mainThread()
      .showLoading(RxLoadingDialog(context))
      .subscribe({ response ->
        val items = arrayListOf<Any>()
        items.addAll(response.data.datas)
        DontForgetInfoRepository.updateInfos(response.data.datas)
        list.value = items
      }, {})
  }
}
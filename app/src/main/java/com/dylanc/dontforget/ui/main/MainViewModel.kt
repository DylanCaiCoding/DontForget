package com.dylanc.dontforget.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.transformer.io2mainThread

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
class MainViewModel : ViewModel() {

  val list: MutableLiveData<List<Any>> = MutableLiveData(arrayListOf())
  private val page: MutableLiveData<Int> = MutableLiveData(1)

  @SuppressLint("CheckResult")
  fun requestList() {
    apiServiceOf<TodoApi>()
      .getTodoList("${TodoApi.TODO}/v2/list/${page.value}/json")
      .io2mainThread()
      .subscribe({ response ->
        val items = arrayListOf<Any>()
        items.addAll(response.data.datas)
        DontForgetInfoRepository.updateInfos(response.data.datas)
        list.value = response.data.datas
      }, {})
  }
}
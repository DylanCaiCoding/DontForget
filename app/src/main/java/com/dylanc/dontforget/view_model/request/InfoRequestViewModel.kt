package com.dylanc.dontforget.view_model.request

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.model.InfoModel

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
class InfoRequestViewModel : ViewModel() {

  val list: MutableLiveData<MutableList<Any>> = MutableLiveData(arrayListOf())
  private val infoModel  = InfoModel()

  fun requestList(context: Context) {
    infoModel.requestList(context,list)
  }
}
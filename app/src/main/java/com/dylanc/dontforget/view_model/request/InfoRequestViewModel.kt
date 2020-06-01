package com.dylanc.dontforget.view_model.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.model.InfoModel

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
class InfoRequestViewModel : ViewModel() {

  val list: MutableLiveData<List<DontForgetInfo>> = MutableLiveData(arrayListOf())
  private val infoModel  = InfoModel()

  fun requestList() {
    infoModel.requestList(list)
  }
}
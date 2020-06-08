package com.dylanc.dontforget.ui.main.info_list

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.InfoRepository
import com.dylanc.retrofit.helper.rxjava.autoDispose
import com.dylanc.utilktx.logJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InfoListViewModel : ViewModel() {
  val list: MutableLiveData<List<DontForgetInfo>> = MutableLiveData()
  private val infoRepository = InfoRepository()

  fun requestList(lifecycleOwner: LifecycleOwner) {
    infoRepository.requestList(list)
      .autoDispose(lifecycleOwner)
      .subscribe({ response ->
        saveInfoList(response.data.list)
      }, {})
  }

  private fun saveInfoList(infoList: List<DontForgetInfo>) = viewModelScope.launch(Dispatchers.IO) {
    infoRepository.insertAll(infoList)
  }
}
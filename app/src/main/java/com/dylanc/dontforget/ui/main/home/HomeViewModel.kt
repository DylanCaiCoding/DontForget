package com.dylanc.dontforget.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
  val list: MutableLiveData<MutableList<Any>> = MutableLiveData(arrayListOf())

}

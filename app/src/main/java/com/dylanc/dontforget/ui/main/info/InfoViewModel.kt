package com.dylanc.dontforget.ui.main.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InfoViewModel : ViewModel() {
  val title: MutableLiveData<String> = MutableLiveData()
  val content: MutableLiveData<String> = MutableLiveData()
}
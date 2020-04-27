package com.dylanc.dontforget.ui.main.add_info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddInfoViewModel : ViewModel() {
  val title: MutableLiveData<String> = MutableLiveData()
  val content: MutableLiveData<String> = MutableLiveData()
}
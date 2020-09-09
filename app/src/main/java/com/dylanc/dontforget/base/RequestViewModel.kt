package com.dylanc.dontforget.base

import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.helper.coroutines.ExceptionLiveData
import com.dylanc.retrofit.helper.coroutines.LoadingLiveData

abstract class RequestViewModel :ViewModel(){
  val loading = LoadingLiveData()
  val exception = ExceptionLiveData()
}
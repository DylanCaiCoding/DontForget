package com.dylanc.dontforget.base

import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.helper.coroutines.ExceptionLiveData
import com.dylanc.retrofit.helper.coroutines.LoadingLiveData

abstract class RequestViewModel :ViewModel(){
  val isLoading = LoadingLiveData()
  val exception = ExceptionLiveData()
}
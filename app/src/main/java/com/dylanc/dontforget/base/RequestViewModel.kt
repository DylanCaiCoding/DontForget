package com.dylanc.dontforget.base

import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.helper.coroutines.RequestExceptionHandler

abstract class RequestViewModel :ViewModel(){
  protected val requestExceptionHandler = RequestExceptionHandler()
  val requestException = requestExceptionHandler.requestException
}
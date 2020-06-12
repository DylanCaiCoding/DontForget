package com.dylanc.dontforget.base

import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.net.RequestExceptionHandler

abstract class RequestViewModel :ViewModel(){
  protected val requestExceptionHandler = RequestExceptionHandler()
  val requestException = requestExceptionHandler.requestException
}
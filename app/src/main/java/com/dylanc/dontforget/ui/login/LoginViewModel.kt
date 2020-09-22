package com.dylanc.dontforget.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
class LoginViewModel : ViewModel() {
  val username: MutableLiveData<String> = MutableLiveData()
  val password: MutableLiveData<String> = MutableLiveData()
}
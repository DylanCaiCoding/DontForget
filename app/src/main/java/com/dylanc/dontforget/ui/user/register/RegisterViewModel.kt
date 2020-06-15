package com.dylanc.dontforget.ui.user.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
class RegisterViewModel : ViewModel() {
  val username: MutableLiveData<String> = MutableLiveData()
  val password: MutableLiveData<String> = MutableLiveData()
  val confirmPassword: MutableLiveData<String> = MutableLiveData()
}
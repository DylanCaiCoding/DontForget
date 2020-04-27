package com.dylanc.dontforget.ui.user.login

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.api.UserApi
import com.dylanc.dontforget.data.net.RxLoadingDialog
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.transformer.io2mainThread
import com.dylanc.retrofit.helper.transformer.showLoading
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
class LoginViewModel : ViewModel() {
  val username: MutableLiveData<String> = MutableLiveData("")
  val password: MutableLiveData<String> = MutableLiveData("")

  @SuppressLint("CheckResult")
  fun requestLogin(activity: Activity) {
    apiServiceOf<UserApi>()
      .login(username.value!!, password.value!!)
      .io2mainThread()
      .showLoading(RxLoadingDialog(activity))
      .subscribe({ response ->
        toast("登录成功")
        UserRepository.saveUser(response.data)
        startActivity<MainActivity>()
        activity.finish()
      }, {})
  }

}
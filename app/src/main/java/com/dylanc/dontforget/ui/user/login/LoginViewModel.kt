package com.dylanc.dontforget.ui.user.login

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.net.showLoadingDialog
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.retrofit.helper.rxjava.autoDispose
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
class LoginViewModel : ViewModel() {
  val username: MutableLiveData<String> = MutableLiveData()
  val password: MutableLiveData<String> = MutableLiveData()
  private val userRepository = UserRepository()

  @SuppressLint("CheckResult")
  fun requestLogin(activity: FragmentActivity) {
    val username = username.value
    val password = password.value
    if (TextUtils.isEmpty(username)) {
      toast("请输入账号")
      return
    }
    if (TextUtils.isEmpty(password)) {
      toast("请输入密码")
      return
    }
    userRepository.requestLogin(username!!, password!!)
      .showLoadingDialog(activity)
      .autoDispose(activity)
      .subscribe({ response ->
        toast("登录成功")
        saveUser(response.data)
        startActivity<MainActivity>()
        activity.finish()
      }, {})
  }

  private fun saveUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
    userRepository.updateUser(user)
  }

}
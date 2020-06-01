package com.dylanc.dontforget.ui.user.login

import android.annotation.SuppressLint
import android.provider.SyncStateContract.Helpers.insert
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.data.api.UserApi
import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.net.showLoadingDialog
import com.dylanc.dontforget.data.repository.insert
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.rxjava.io2mainThread
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
class LoginViewModel : ViewModel() {
  val username: MutableLiveData<String> = MutableLiveData("")
  val password: MutableLiveData<String> = MutableLiveData("")

  @SuppressLint("CheckResult")
  fun requestLogin(activity: FragmentActivity) {
    apiServiceOf<UserApi>()
      .login(username.value!!, password.value!!)
      .io2mainThread()
      .showLoadingDialog(activity)
      .subscribe({ response ->
        toast("登录成功")
        saveUser(response.data)
        startActivity<MainActivity>()
        activity.finish()
      }, {})
  }

  fun saveUser(user: User) = viewModelScope.launch (Dispatchers.IO){
    insert(user)
  }

}
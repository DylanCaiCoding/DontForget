package com.dylanc.dontforget.data.net

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.utilktx.finishAllActivities
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast


fun LiveData<RequestException>.observeProcessed(
  lifecycleOwner: LifecycleOwner,
  onChanged: (RequestException) -> Unit
) {
  observe(lifecycleOwner, Observer { e ->
    when (e.cause) {
      is AuthenticationException -> {
        toast(e.message)
        finishAllActivities()
        startActivity<LoginActivity>()
      }
      else -> onChanged(e)
    }
  })
}
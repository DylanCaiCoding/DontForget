package com.dylanc.dontforget.data.net

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.dylanc.dontforget.R
import com.dylanc.utilktx.finishAllActivities
import com.dylanc.utilktx.toast
import com.dylanc.utilktx.topActivity


fun LiveData<Throwable>.observe(
  lifecycleOwner: LifecycleOwner,
  onChanged: (Throwable) -> Unit = { toast(it.message) }
) {
  observe(lifecycleOwner, Observer { e ->
    when (e.cause) {
      is AuthenticationException -> {
        toast(e.message)
        finishAllActivities()
        topActivity.findNavController(R.id.nav_host_fragment)
          .navigate(R.id.action_mainFragment_to_loginFragment)
      }
      else -> onChanged(e)
    }
  })
}

fun LiveData<Boolean>.observe(
  lifecycleOwner: LifecycleOwner,
  loadingDialog: LoadingDialog
) {
  observe(lifecycleOwner, Observer {
    loadingDialog.show(it)
  })
}
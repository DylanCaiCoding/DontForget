package com.dylanc.dontforget.data.net

import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.dylanc.dontforget.R
import com.dylanc.longan.finishAllActivities
import com.dylanc.longan.toast
import com.dylanc.longan.topActivity

class GlobalExceptionObserver(
  private val onError: (Throwable) -> Unit = { toast(it.message) }
) : Observer<Throwable> {
  override fun onChanged(e: Throwable) {
    when (e.cause) {
      is AuthenticationException -> {
        toast(e.message)
        finishAllActivities()
        topActivity.findNavController(R.id.nav_host_fragment)
          .navigate(R.id.action_mainFragment_to_loginFragment)
      }
      else -> onError(e)
    }
  }
}
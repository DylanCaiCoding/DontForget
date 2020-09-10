package com.dylanc.dontforget.ui.user.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.loading.NavIconType
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.data.net.loadingDialog
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.utils.lifecycleOwner
import com.dylanc.dontforget.data.net.observe
import com.dylanc.dontforget.utils.setToolbar
import com.dylanc.dontforget.viewmodel.request.UserRequestViewModel
import com.dylanc.utilktx.toast

class RegisterFragment : Fragment() {

  private val viewModel: RegisterViewModel by viewModels()
  private val requestViewModel: UserRequestViewModel by viewModels()
  private val loadingDialog: LoadingDialog by loadingDialog()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_register, container, false)
    bindView(view, viewModel, BR.clickProxy to ClickProxy())
    return view.setToolbar("注册", NavIconType.BACK).decorView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    requestViewModel.loading.observe(this, loadingDialog)
    requestViewModel.exception.observe(this)
  }

  inner class ClickProxy {
    fun onRegisterBtnClick() {
      val username = viewModel.username.value
      val password = viewModel.password.value
      val confirmPassword = viewModel.confirmPassword.value
      requestViewModel.register(username, password, confirmPassword)
        .observe(lifecycleOwner, Observer {
          toast("注册成功")
          findNavController().popBackStack()
        })
    }
  }
}
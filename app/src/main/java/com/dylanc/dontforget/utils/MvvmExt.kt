@file:Suppress("unused")

package com.dylanc.dontforget.utils

import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.dylanc.dontforget.base.RequestLoading
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.net.GlobalExceptionObserver
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.utilktx.app
import kotlin.reflect.KClass

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
fun ComponentActivity.bindContentView(
  layoutId: Int,
  viewModel: ViewModel,
  vararg bindingParams: Pair<Int, Any>
): ViewDataBinding =
  DataBindingUtil.setContentView<ViewDataBinding>(this, layoutId)
    .apply {
      setVariable(BR.viewModel, viewModel)
      lifecycleOwner = this@bindContentView
      for (bindingParam in bindingParams) {
        setVariable(bindingParam.first, bindingParam.second)
      }
    }

fun Fragment.bindView(
  root: View,
  viewModel: ViewModel,
  vararg bindingParams: Pair<Int, Any>
): ViewDataBinding =
  DataBindingUtil.bind<ViewDataBinding>(root)!!
    .apply {
      setVariable(BR.viewModel, viewModel)
      lifecycleOwner = this@bindView
      for (bindingParam in bindingParams) {
        setVariable(bindingParam.first, bindingParam.second)
      }
    }

inline fun <reified VM : RequestViewModel> Fragment.requestViewModels(
  noinline requestLoadingProducer: () -> RequestLoading = { LoadingDialog(requireActivity()) },
  noinline exceptionObserverProducer: () -> Observer<Throwable> = { GlobalExceptionObserver() },
  noinline ownerProducer: () -> ViewModelStoreOwner = { this },
  noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): RequestViewModelLazy<VM> {
  val factoryPromise = factoryProducer ?: {
    defaultViewModelProviderFactory
  }
  return RequestViewModelLazy(
    this,
    VM::class,
    requestLoadingProducer,
    exceptionObserverProducer,
    { ownerProducer().viewModelStore },
    factoryPromise
  )
}

class RequestViewModelLazy<VM : RequestViewModel>(
  private val owner: LifecycleOwner,
  private val viewModelClass: KClass<VM>,
  private val requestLoadingProducer: () -> RequestLoading,
  private val exceptionObserverProducer: () -> Observer<Throwable>,
  private val storeProducer: () -> ViewModelStore,
  private val factoryProducer: () -> ViewModelProvider.Factory
) : Lazy<VM> {
  private var cached: VM? = null

  override val value: VM
    get() {
      val viewModel = cached
      return if (viewModel == null) {
        val factory = factoryProducer()
        val store = storeProducer()
        val requestLoading = requestLoadingProducer()
        ViewModelProvider(store, factory).get(viewModelClass.java).also {
          cached = it
          it.isLoading.observe(owner, Observer { isLoading ->
            requestLoading.show(isLoading)
          })
          it.exception.observe(owner, exceptionObserverProducer())
        }
      } else {
        viewModel
      }
    }

  override fun isInitialized() = cached != null
}

@MainThread
inline fun <reified VM : ViewModel> appViewModels(): Lazy<VM> =
  ViewModelLazy(VM::class, { ViewModelStore() }, { ViewModelProvider.AndroidViewModelFactory.getInstance(app) })
@file:Suppress("unused")

package com.dylanc.dontforget.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.dylanc.dontforget.base.RequestLoading
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.net.GlobalExceptionObserver
import com.dylanc.dontforget.data.net.LoadingDialog
import kotlin.reflect.KClass


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
        ViewModelProvider(store, factory).get(viewModelClass.java).also { vm ->
          cached = vm
          vm.isLoading.observe(owner, Observer { isLoading ->
            requestLoading.show(isLoading)
          })
          vm.exception.observe(owner, exceptionObserverProducer())
        }
      } else {
        viewModel
      }
    }

  override fun isInitialized() = cached != null
}
/*
 * Copyright (c) 2019. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.dylanc.dontforget.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.dylanc.dontforget.adapter.loading.NavIconType
import com.dylanc.dontforget.adapter.loading.ToolbarAdapter
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.loadinghelper.ViewType
import com.dylanc.viewbinding.base.inflateBindingWithGeneric

/**
 * ViewBindingKTX + LoadingHelper 的封装范例
 *
 * @author Dylan Cai
 */
abstract class BaseFragment<VB : ViewBinding>(
  @IdRes private val contentViewId: Int = 0,
  private val contentAdapter: LoadingHelper.ContentAdapter<*>? = null
) : androidx.fragment.app.Fragment() {

  lateinit var loadingHelper: LoadingHelper private set
  private var _binding: VB? = null
  val binding: VB get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = inflateBindingWithGeneric<VB>(inflater, container, false)
      .also { if (it is ViewDataBinding) it.lifecycleOwner = viewLifecycleOwner }
    return binding.root.let { rootView ->
      if (contentViewId > 0) {
        loadingHelper = LoadingHelper(rootView.findViewById<View>(contentViewId), contentAdapter)
        loadingHelper.setOnReloadListener(this::onReload)
        rootView
      } else {
        loadingHelper = LoadingHelper(rootView)
        loadingHelper.setOnReloadListener(this::onReload)
        loadingHelper.decorView
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  @JvmOverloads
  fun addToolbar(
    title: String, type: NavIconType = NavIconType.NONE,
    menuId: Int = 0, listener: ((MenuItem) -> Boolean)? = null
  ) {
    loadingHelper.register(ViewType.TITLE, ToolbarAdapter(title, type, menuId, listener))
    loadingHelper.addChildDecorHeader(ViewType.TITLE)
  }

  fun showLoadingView() = loadingHelper.showLoadingView()

  fun showContentView() = loadingHelper.showContentView()

  fun showErrorView() = loadingHelper.showErrorView()

  fun showEmptyView() = loadingHelper.showEmptyView()

  fun showCustomView(viewType: Any) = loadingHelper.showView(viewType)

  open fun onReload() {}
}
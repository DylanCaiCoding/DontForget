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

package com.dylanc.dontforget.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
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
@Suppress("unused")
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

  @IdRes
  protected var contentViewId: Int = 0
  lateinit var loadingHelper: LoadingHelper private set
  lateinit var binding: VB private set

  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)
    binding = inflateBindingWithGeneric(layoutInflater)
    val rootView = binding.root
    setContentView(rootView)
    loadingHelper = if (contentViewId > 0) {
      LoadingHelper(rootView.findViewById<View>(contentViewId))
    } else {
      LoadingHelper(rootView)
    }
    loadingHelper.setOnReloadListener(::onReload)
  }

  /**
   * 这是添加通用标题栏的示例方法。
   */
  @JvmOverloads
  fun setToolbar(
    title: String, type: NavIconType = NavIconType.NONE,
    menuId: Int = 0, listener: ((MenuItem) -> Boolean)? = null
  ) {
    loadingHelper.setDecorHeader(ToolbarAdapter(title, type, menuId, listener))
  }

  fun showLoadingView() = loadingHelper.showLoadingView()

  fun showContentView() = loadingHelper.showContentView()

  fun showErrorView() = loadingHelper.showErrorView()

  fun showEmptyView() = loadingHelper.showEmptyView()

  fun showCustomView(viewType: Any) = loadingHelper.showView(viewType)

  open fun onReload() {}
}
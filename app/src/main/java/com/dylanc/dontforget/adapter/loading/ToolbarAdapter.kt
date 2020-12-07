package com.dylanc.dontforget.adapter.loading

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.databinding.LayoutToolbarBinding
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.utilktx.topActivity

/**
 * @author Dylan Cai
 * @since 2020/3/4
 */
class ToolbarAdapter(
  private val title: String?,
  private val type: NavIconType = NavIconType.NONE,
  private val menuId: Int = 0,
  private val onMenuItemClickListener: ((MenuItem) -> Boolean)? = null
) : LoadingHelper.Adapter<LoadingHelper.ViewHolder>() {
  private lateinit var binding: LayoutToolbarBinding

  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): LoadingHelper.ViewHolder {
    binding = LayoutToolbarBinding.inflate(inflater, parent, false)
    return LoadingHelper.ViewHolder(binding.root)
  }

  override fun onBindViewHolder(holder: LoadingHelper.ViewHolder) {
    binding.apply {
      if (!title.isNullOrBlank()) {
        toolbar.title = title
      }
      if (type === NavIconType.BACK) {
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
          topActivity.findNavController(R.id.nav_host_fragment).popBackStack()
        }
      } else {
        toolbar.navigationIcon = null
      }
      if (menuId > 0 && onMenuItemClickListener != null) {
        toolbar.inflateMenu(menuId)
        toolbar.setOnMenuItemClickListener { item: MenuItem ->
          onMenuItemClickListener.invoke(item)
        }
      }
    }
  }
}

enum class NavIconType {
  BACK, NONE
}

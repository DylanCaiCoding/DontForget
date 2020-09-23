@file:Suppress("unused", "EXTENSION_SHADOWED_BY_MEMBER")

package com.dylanc.dontforget.adapter.binding

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter(value = ["isRefreshing"])
fun SwipeRefreshLayout.setRefreshing(refreshing: Boolean) {
  post { isRefreshing = refreshing }
}

@BindingAdapter(value = ["onRefresh"])
fun SwipeRefreshLayout.setOnRefreshListener(onRefresh: () -> Unit) {
  setOnRefreshListener(onRefresh)
}

@BindingAdapter(value = ["onRefresh"])
fun SwipeRefreshLayout.setOnRefreshListener(onRefreshListener: SwipeRefreshLayout.OnRefreshListener) {
  setOnRefreshListener(onRefreshListener)
}

@BindingAdapter(value = ["colorScheme"])
fun SwipeRefreshLayout.setColorSchemeColor(colorId: Int) {
  setColorSchemeColors(colorId)
}

@BindingAdapter(value = ["colorScheme"])
fun SwipeRefreshLayout.setColorSchemeColors(colorId: IntArray) {
  setColorSchemeColors(*colorId)
}

package com.dylanc.dontforget.adapter.binding

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter(value = ["isRefreshing"])
fun SwipeRefreshLayout.setRefreshing(refreshing:Boolean) {
  post {
    isRefreshing = refreshing
  }
}

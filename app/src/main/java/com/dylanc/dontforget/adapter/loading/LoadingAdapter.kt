package com.dylanc.dontforget.adapter.loading

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dylanc.dontforget.R
import com.dylanc.loadinghelper.LoadingHelper

class LoadingAdapter : LoadingHelper.Adapter<LoadingHelper.ViewHolder>() {
  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
    LoadingHelper.ViewHolder(inflater.inflate(R.layout.layout_loading, parent, false))

  override fun onBindViewHolder(holder: LoadingHelper.ViewHolder) {}
}
package com.dylanc.dontforget.base.title

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.BaseTitleAdapter
import com.dylanc.dontforget.base.TitleConfig

/**
 * @author Dylan Cai
 * @since 2019/7/10
 */
class TitleAdapter : BaseTitleAdapter<TitleConfig, TitleAdapter.TitleViewHolder>() {
  lateinit var holder: TitleViewHolder
  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): TitleViewHolder {
    return TitleViewHolder(
      inflater.inflate(
        R.layout.common_layout_title_bar,
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: TitleViewHolder) {
    this.holder = holder
    holder.tvTitle.text = config.titleText
    if (config.type == TitleConfig.Type.BACK) {
      holder.btnBack.visibility = View.VISIBLE
      holder.btnBack.setOnClickListener { holder.finish() }
    } else {
      holder.btnBack.visibility = View.GONE
    }
    holder.btnRight.text = config.rightText
    holder.btnRight.setOnClickListener{config.onRightBtnClickListener?.invoke()}
  }

  fun setTitle(text:String){
    holder.tvTitle.text = text
  }

  fun setRightTextListener(text:String?,listener:(()->Unit)?){
    holder.btnRight.text = text
    holder.btnRight.setOnClickListener { listener?.invoke() }
  }

  class TitleViewHolder(rootView: View) : LoadingHelper.ViewHolder(rootView) {
    val tvTitle: TextView = rootView.findViewById(R.id.tv_title)
    val btnBack: ImageView = rootView.findViewById(R.id.btn_back)
    val btnRight: TextView = rootView.findViewById(R.id.btn_right)

    fun finish() {
      val activity: Activity = rootView.context as Activity
      activity.finish()
    }
  }
}
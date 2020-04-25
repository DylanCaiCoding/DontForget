package com.dylanc.dontforget.base

/**
 * @author Dylan Cai
 * @since 2019/4/9
 */
open class TitleConfig(
  var titleText: String,
  var type: Type
) {
  var rightIcon: Int = 0
    private set
  var rightText: String? = null
    private set
  var onRightBtnClickListener: (()->Unit)? = null
    private set

  fun setRightBtn(rightIcon: Int, onRightBtnClickListener: ()->Unit) {
    this.rightIcon = rightIcon
    this.onRightBtnClickListener = onRightBtnClickListener
  }

  fun setRightBtn(rightText: String, onRightBtnClickListener: ()->Unit) {
    this.rightText = rightText
    this.onRightBtnClickListener = onRightBtnClickListener
  }

  enum class Type {
    BACK, NO_BACK
  }
}

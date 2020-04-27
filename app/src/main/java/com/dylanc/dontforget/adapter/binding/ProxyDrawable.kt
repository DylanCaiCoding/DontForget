/*
 * Copyright 2018-2019 KunMinX
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
package com.dylanc.dontforget.adapter.binding

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable

/**
 * Created by linjiang on 2018/9/6.
 */
class ProxyDrawable : StateListDrawable() {
  var originDrawable: Drawable? = null
    private set

  override fun addState(stateSet: IntArray, drawable: Drawable) {
    if (stateSet.size == 1 && stateSet[0] == 0) {
      originDrawable = drawable
    }
    super.addState(stateSet, drawable)
  }
}
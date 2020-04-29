package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.DontForgetInfo
import java.util.*

object DontForgetInfoRepository {
  val infos: MutableList<DontForgetInfo> = mutableListOf()

  val randomDontForgetInfo: DontForgetInfo?
    get() {
      val notForgetInfos = infos
      if (notForgetInfos.size == 0) {
        return null
      }
      val randomNum = Random().nextInt(notForgetInfos.size)
      return notForgetInfos[randomNum]
    }

  fun addInfo(info: DontForgetInfo) {
    infos.add(info)
  }

  fun addInfo(position: Int, info: DontForgetInfo) {
    infos.add(position, info)
  }

  fun removeInfo(index: Int) {
    infos.removeAt(index)
  }

  fun updateInfo(index: Int,info: DontForgetInfo) {
    infos[index] = info
  }

  fun updateInfos(list: List<DontForgetInfo>) {
    infos.clear()
    infos.addAll(list)
  }
}

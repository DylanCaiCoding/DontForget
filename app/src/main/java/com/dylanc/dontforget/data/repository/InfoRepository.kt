package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.db.InfoDao
import com.dylanc.dontforget.data.repository.db.infoDatabase

class InfoRepository(private val infoDao: InfoDao = infoDatabase.infoDao()) {

  val allInfo = infoDao.getAllInfo()

  suspend fun insert(dontForgetInfo: DontForgetInfo) {
    infoDao.insertInfo(dontForgetInfo)
  }
}
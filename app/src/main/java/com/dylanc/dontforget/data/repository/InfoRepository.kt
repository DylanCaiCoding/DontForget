package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.db.InfoDao
import com.dylanc.dontforget.data.repository.db.infoDatabase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

val infoRepository
  get() = InfoRepository()

class InfoRepository(private val infoDao: InfoDao = infoDatabase.infoDao()) {

  val allInfo = infoDao.getAllInfo()

  fun insert(dontForgetInfo: DontForgetInfo) {
    infoDao.insertInfo(dontForgetInfo)
  }

  fun updateAllInfo(infoList: List<DontForgetInfo>) {
    Single.just(infoDao)
      .subscribeOn(Schedulers.io())
      .subscribe ({
        infoDao.deleteAll()
        for (info in infoList) {
          infoDao.insertInfo(info)
        }
      },{})
  }
}
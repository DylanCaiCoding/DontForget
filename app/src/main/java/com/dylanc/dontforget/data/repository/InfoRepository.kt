package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.api.InfoApi
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.parseData
import com.dylanc.dontforget.data.db.InfoDao
import com.dylanc.dontforget.utils.exceptFirstEmpty
import java.util.*

/**
 * @author Dylan Cai
 */
class InfoRepository(
  private val localDataSource: InfoLocalDataSource,
  private val remoteDataSource: InfoRemoteDataSource
) {
  val allInfo = localDataSource.allInfo

  val randomInfo: DontForgetInfo?
    get() {
      val list = allInfo.value ?: return null
      return if (list.isNotEmpty()) {
        list[Random().nextInt(list.size)]
      } else {
        null
      }
    }

  suspend fun initInfoList(): List<DontForgetInfo> {
    return if (localDataSource.getAll().isEmpty()) {
      val list = remoteDataSource.refreshInfoList()
      localDataSource.insertAll(list)
      list
    } else {
      localDataSource.getAll()
    }
  }

  suspend fun refreshInfoList(): List<DontForgetInfo> {
    val list = remoteDataSource.refreshInfoList()
    localDataSource.deleteAll()
    localDataSource.insertAll(list)
    return list
  }

  suspend fun addInfo(title: String): DontForgetInfo {
    val info = remoteDataSource.requestAddInfo(title)
    localDataSource.insertInfo(info)
    return info
  }

  suspend fun updateInfo(id: Int, title: String, date: String): DontForgetInfo {
    val info = remoteDataSource.requestUpdateInfo(id, title, date)
    localDataSource.insertInfo(info)
    return info
  }

  suspend fun deleteInfo(info: DontForgetInfo): Any {
    val deleteInfo = remoteDataSource.requestDeleteInfo(info.id)
    localDataSource.deleteInfo(info)
    return deleteInfo
  }
}

class InfoLocalDataSource(private val infoDao: InfoDao) {

  val allInfo = infoDao.getAllInfoLiveData().exceptFirstEmpty()

  suspend fun getAll() =
    infoDao.getAll()

  suspend fun insertAll(infoList: List<DontForgetInfo>) =
    infoDao.insertAll(infoList)

  suspend fun insertInfo(info: DontForgetInfo) =
    infoDao.insertInfo(info)

  suspend fun deleteInfo(info: DontForgetInfo) =
    infoDao.deleteInfo(info)

  suspend fun deleteInfoList(info: List<DontForgetInfo>) =
    infoDao.deleteInfo(info)

  suspend fun deleteAll() =
    infoDao.deleteAll()
}

class InfoRemoteDataSource(private val api: InfoApi) {
  private var page: Int = 1
  private val list = mutableListOf<DontForgetInfo>()

  suspend fun refreshInfoList(): List<DontForgetInfo> {
    list.clear()
    page = 1
    return loadAllInfo()
  }

  private suspend fun loadAllInfo(): List<DontForgetInfo> {
    val pageList = requestInfoList()
    list.addAll(pageList.list)
    return if (pageList.over) {
      list
    } else {
      page++
      loadAllInfo()
    }
  }

  private suspend fun requestInfoList() =
    api.getInfoList(page).parseData()

  suspend fun requestAddInfo(title: String) =
    api.addInfo(title).parseData()

  suspend fun requestUpdateInfo(id: Int, title: String, date: String) =
    api.updateInfo(id, title, date).parseData()

  suspend fun requestDeleteInfo(id: Int) =
    api.deleteInfo(id).parseData()
}


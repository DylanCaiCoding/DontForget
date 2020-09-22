package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.parseData
import com.dylanc.dontforget.data.repository.api.InfoApi
import com.dylanc.dontforget.data.repository.db.InfoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
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

  fun getInfoList() = flow {
    val infoList = if (localDataSource.allInfo.value == null || localDataSource.allInfo.value!!.isEmpty()) {
      val infoList = remoteDataSource.refreshInfoList()
      localDataSource.insertAll(infoList)
      infoList
    } else {
      localDataSource.allInfo.value!!
    }
    emit(infoList)
  }

  fun refreshInfoList() = flow {
    val infoList = remoteDataSource.refreshInfoList()
    localDataSource.deleteAll()
    localDataSource.insertAll(infoList)
    emit(infoList)
  }

  fun addInfo(title: String?) = flow {
    checkNotNull(title) { "请输入标题" }
    val info = remoteDataSource.requestAddInfo(title)
    localDataSource.insertInfo(info)
    emit(info)
  }

  fun updateInfo(id: Int, title: String?, date: String) = flow {
    checkNotNull(title) { "请输入标题" }
    val info = remoteDataSource.requestUpdateInfo(id, title, date)
    localDataSource.insertInfo(info)
    emit(info)
  }

  fun deleteInfo(info: DontForgetInfo) = flow {
    val deleteInfo = remoteDataSource.requestDeleteInfo(info.id)
    localDataSource.deleteInfo(info)
    emit(deleteInfo)
  }

  suspend fun deleteAllInfo() = localDataSource.deleteAll()
}

class InfoLocalDataSource(private val infoDao: InfoDao) {

  val allInfo = infoDao.getAllInfo()

  suspend fun insertAll(infoList: List<DontForgetInfo>) = withContext(Dispatchers.IO) {
    infoDao.insertAll(infoList)
  }

  suspend fun insertInfo(info: DontForgetInfo) = withContext(Dispatchers.IO) {
    infoDao.insertInfo(info)
  }

  suspend fun deleteInfo(info: DontForgetInfo) = withContext(Dispatchers.IO) {
    infoDao.deleteInfo(info)
  }

  suspend fun deleteAll() = withContext(Dispatchers.IO) {
    infoDao.deleteAll()
  }
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
    return if (pageList.over) {
      list.addAll(pageList.list)
      list
    } else {
      page++
      list.addAll(pageList.list)
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


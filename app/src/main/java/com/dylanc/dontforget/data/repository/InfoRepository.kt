package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.parseData
import com.dylanc.dontforget.data.repository.api.InfoApi
import com.dylanc.dontforget.data.repository.db.InfoDao
import com.dylanc.dontforget.data.repository.db.appDatabase
import com.dylanc.retrofit.helper.apiOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @author Dylan Cai
 */
val infoRepository: InfoRepository by lazy { InfoRepository() }

class InfoRepository(
  private val model: InfoModel = InfoModel(),
  private val remoteDataSource: InfoRemoteDataSource = InfoRemoteDataSource()
) {
  val allInfo = model.allInfo

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
    val infoList = if (model.allInfo.value == null || model.allInfo.value!!.isEmpty()) {
      val infoList = remoteDataSource.refreshInfoList()
      model.insertAll(infoList)
      infoList
    } else {
      model.allInfo.value!!
    }
    emit(infoList)
  }

  fun refreshInfoList() = flow {
    val infoList = remoteDataSource.refreshInfoList()
    model.deleteAll()
    model.insertAll(infoList)
    emit(infoList)
  }

  fun addInfo(title: String?) = flow {
    checkNotNull(title) { "请输入标题" }
    val info = remoteDataSource.requestAddInfo(title)
    model.insertInfo(info)
    emit(info)
  }

  fun updateInfo(id: Int, title: String?, date: String) = flow {
    checkNotNull(title) { "请输入标题" }
    val info = remoteDataSource.requestUpdateInfo(id, title, date)
    model.insertInfo(info)
    emit(info)
  }

  fun deleteInfo(info: DontForgetInfo) = flow {
    val deleteInfo = remoteDataSource.requestDeleteInfo(info.id)
    model.deleteInfo(info)
    emit(deleteInfo)
  }

  suspend fun deleteAllInfo() = model.deleteAll()
}

class InfoModel(private val infoDao: InfoDao = appDatabase.infoDao()) {

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

class InfoRemoteDataSource {
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
    apiOf<InfoApi>().getInfoList(page).parseData()

  suspend fun requestAddInfo(title: String) =
    apiOf<InfoApi>().addInfo(title).parseData()

  suspend fun requestUpdateInfo(id: Int, title: String, date: String) =
    apiOf<InfoApi>().updateInfo(id, title, date).parseData()

  suspend fun requestDeleteInfo(id: Int) =
    apiOf<InfoApi>().deleteInfo(id).parseData()
}


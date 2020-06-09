package com.dylanc.dontforget.data.repository

import androidx.lifecycle.LiveData
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.repository.db.InfoDao
import com.dylanc.dontforget.data.repository.db.infoDatabase
import com.dylanc.retrofit.helper.apiServiceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @author Dylan Cai
 */
class InfoRepository(
  private val model: InfoModel = InfoModel(),
  private val remoteDataSource: InfoRemoteDataSource = InfoRemoteDataSource()
) {

  val allInfo = model.allInfo

  suspend fun getInfoList() {
    if (model.allInfo.value != null && model.allInfo.value!!.isEmpty()) {
      val list = remoteDataSource.requestInfoList()
      withContext(Dispatchers.IO) {
        model.insertAll(list)
      }
    }
  }

  suspend fun getRandomInfo(): DontForgetInfo? {
    val list = remoteDataSource.requestInfoList()
    return if (list.isNotEmpty()) {
      list[Random().nextInt(list.size)]
    } else {
      null
    }
  }

  suspend fun requestInfoList() {
    val list = remoteDataSource.requestInfoList()
    withContext(Dispatchers.IO) {
      model.deleteAll()
      model.insertAll(list)
    }
  }

  suspend fun insertInfo(info: DontForgetInfo) =
    model.insertInfo(info)
}

class InfoModel(private val infoDao: InfoDao = infoDatabase.infoDao()) {

  val allInfo =
    infoDao.getAllInfo()

  suspend fun insertAll(infoList: List<DontForgetInfo>) =
    infoDao.insertAll(infoList)

  suspend fun insertInfo(info: DontForgetInfo) =
    infoDao.insertInfo(info)

  suspend fun deleteInfo(info: DontForgetInfo) =
    infoDao.deleteInfo(info)

  suspend fun deleteAll() =
    infoDao.deleteAll()
}

class InfoRemoteDataSource {
  private var page: Int = 1
  private val list = mutableListOf<DontForgetInfo>()

  suspend fun requestInfoList(): List<DontForgetInfo> {
    list.clear()
    page = 1
    return loadInfoList()
  }

  private suspend fun loadInfoList(): List<DontForgetInfo> {
    val pageList = withContext(Dispatchers.IO) {
      apiServiceOf<TodoApi>().getInfoList(page).data
    }
    return if (pageList.over) {
      list.addAll(pageList.list)
      list
    } else {
      page++
      list.addAll(pageList.list)
      loadInfoList()
    }
  }
}


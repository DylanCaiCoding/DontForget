package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.net.request
import com.dylanc.dontforget.data.repository.api.InfoApi
import com.dylanc.dontforget.data.repository.db.InfoDao
import com.dylanc.dontforget.data.repository.db.infoDatabase
import com.dylanc.retrofit.helper.apiServiceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @author Dylan Cai
 */
val infoRepository: InfoRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
  InfoRepository()
}

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

  suspend fun getInfoList() =
    if (model.allInfo.value == null || model.allInfo.value!!.isEmpty()) {
      remoteDataSource.getInfoList().apply {
        model.insertAll(this)
      }
    } else {
      model.allInfo.value!!
    }


  suspend fun requestInfoList() =
    remoteDataSource.getInfoList()
      .apply {
        model.deleteAll()
        model.insertAll(this)
      }


  suspend fun addInfo(title: String) =
    remoteDataSource.requestAddInfo(title).apply {
      model.insertInfo(data!!)
    }


  suspend fun updateInfo(id: Int, title: String, date: String) =
    remoteDataSource.requestUpdateInfo(id, title, date).apply {
      model.insertInfo(data!!)
    }


  suspend fun deleteInfo(info: DontForgetInfo) =
    remoteDataSource.requestDeleteInfo(info.id).apply {
      model.deleteInfo(info)
    }
}

class InfoModel(private val infoDao: InfoDao = infoDatabase.infoDao()) {

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

  suspend fun getInfoList(): List<DontForgetInfo> {
    list.clear()
    page = 1
    return loadAllInfo()
  }

  private suspend fun loadAllInfo(): List<DontForgetInfo> {
    val pageList = requestInfoList().data!!
    return if (pageList.over) {
      list.addAll(pageList.list)
      list
    } else {
      page++
      list.addAll(pageList.list)
      loadAllInfo()
    }
  }

  private suspend fun requestInfoList() = request {
    apiServiceOf<InfoApi>().getInfoList(page)
  }

  suspend fun requestAddInfo(title: String) = request {
    apiServiceOf<InfoApi>().addInfo(title)
  }

  suspend fun requestUpdateInfo(id: Int, title: String, date: String) = request {
    apiServiceOf<InfoApi>().updateInfo(id, title, date)
  }

  suspend fun requestDeleteInfo(id: Int) = request {
    apiServiceOf<InfoApi>().deleteInfo(id)
  }

}


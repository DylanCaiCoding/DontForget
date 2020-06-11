package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.net.Resource
import com.dylanc.dontforget.data.net.resource
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
    resource {
      if (model.allInfo.value == null || model.allInfo.value!!.isEmpty()) {
        remoteDataSource.requestInfoList().apply {
          model.insertAll(this)
        }
      } else {
        model.allInfo.value!!
      }
    }

  suspend fun requestInfoList() =
    resource {
      remoteDataSource.requestInfoList()
        .apply {
          model.deleteAll()
          model.insertAll(this)
        }
    }

  suspend fun addInfo(title: String) =
    resource {
      remoteDataSource.requestAddInfo(title).apply {
        model.insertInfo(data)
      }
    }

  suspend fun updateInfo(id: Int, title: String, date: String) =
    resource {
      remoteDataSource.requestUpdateInfo(id, title, date).apply {
        model.insertInfo(data)
      }
    }

  suspend fun deleteInfo(info: DontForgetInfo) =
    resource {
      remoteDataSource.requestDeleteInfo(info.id).apply {
        model.deleteInfo(info)
      }
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

  suspend fun requestInfoList(): List<DontForgetInfo> {
    list.clear()
    page = 1
    return loadInfoList()
  }

  private suspend fun loadInfoList(): List<DontForgetInfo> {
    val pageList = withContext(Dispatchers.IO) {
      apiServiceOf<InfoApi>().getInfoList(page).data
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

  suspend fun requestAddInfo(title: String) =
    apiServiceOf<InfoApi>().addInfo(title)

  suspend fun requestUpdateInfo(id: Int, title: String, date: String) =
    apiServiceOf<InfoApi>().updateInfo(id, title, date)

  suspend fun requestDeleteInfo(id: Int) =
    apiServiceOf<InfoApi>().deleteInfo(id)

}


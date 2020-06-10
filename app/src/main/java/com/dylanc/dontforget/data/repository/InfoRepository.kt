package com.dylanc.dontforget.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dylanc.dontforget.data.repository.api.InfoApi
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
val infoRepository: InfoRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
  InfoRepository()
}

class InfoRepository(
  private val model: InfoModel = InfoModel(),
  private val remoteDataSource: InfoRemoteDataSource = InfoRemoteDataSource()
) {
  val allInfo = model.allInfo
  val insertedInfo = model.insertedInfo

  val randomInfo: DontForgetInfo?
    get() {
      val list = allInfo.value ?: return null
      return if (list.isNotEmpty()) {
        list[Random().nextInt(list.size)]
      } else {
        null
      }
    }

  suspend fun getInfoList() {
    if (model.allInfo.value == null || model.allInfo.value!!.isEmpty()) {
      val list = remoteDataSource.requestInfoList()
      model.insertAll(list)
    }
  }

  suspend fun requestInfoList() {
    val list = remoteDataSource.requestInfoList()
    model.deleteAll()
    model.insertAll(list)
  }

  suspend fun addInfo(title: String) {
    val info = remoteDataSource.requestAddInfo(title)
    model.insertInfo(info)
  }

  suspend fun updateInfo(id: Int, title: String, date: String) {
    try {
      val info = remoteDataSource.requestUpdateInfo(id, title, date)
      model.insertInfo(info)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  suspend fun deleteInfo(info: DontForgetInfo) {
    remoteDataSource.requestDeleteInfo(info.id)
    model.deleteInfo(info)
  }

}

class InfoModel(private val infoDao: InfoDao = infoDatabase.infoDao()) {

  private val _insertedInfo = MutableLiveData<DontForgetInfo>()
  val allInfo = infoDao.getAllInfo()
  val insertedInfo :LiveData<DontForgetInfo> = _insertedInfo

  suspend fun insertAll(infoList: List<DontForgetInfo>) = withContext(Dispatchers.IO) {
    infoDao.insertAll(infoList)
  }

  suspend fun insertInfo(info: DontForgetInfo) {
    withContext(Dispatchers.IO) {
      infoDao.insertInfo(info)
    }
    withContext(Dispatchers.Main) {
      _insertedInfo.value = info
    }
  }

  suspend fun deleteInfo(info: DontForgetInfo) {
    withContext(Dispatchers.IO) {
      infoDao.deleteInfo(info)
    }
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

  suspend fun requestAddInfo(title: String) = withContext(Dispatchers.IO) {
    apiServiceOf<InfoApi>().addInfo(title).data
  }

  suspend fun requestUpdateInfo(id: Int, title: String, date: String) =
    withContext(Dispatchers.IO) {
      apiServiceOf<InfoApi>().updateInfo(id, title, date).data
    }

  suspend fun requestDeleteInfo(id: Int) = withContext(Dispatchers.IO) {
    apiServiceOf<InfoApi>().deleteInfo(id).data
  }
}


package com.dylanc.dontforget.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.bean.ApiResponse
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.ListPage
import com.dylanc.dontforget.data.repository.db.InfoDao
import com.dylanc.dontforget.data.repository.db.infoDatabase
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.rxjava.io2mainThread
import io.reactivex.Single

/**
 * @author Dylan Cai
 */
class InfoRepository(
  private val model: InfoModel = InfoModel(),
  private val remoteDataSource: InfoRemoteDataSource = InfoRemoteDataSource()
) {
  fun requestList(infoList: MutableLiveData<List<DontForgetInfo>>) =
    remoteDataSource.requestList(infoList)

  suspend fun insertAll(infoList: List<DontForgetInfo>) =
    model.insertAll(infoList)

  suspend fun getAllInfo() = model.getAllInfo()
}

class InfoModel(private val infoDao: InfoDao = infoDatabase.infoDao()) {

  suspend fun getAllInfo() = infoDao.getAllInfo()

  suspend fun insertAll(infoList: List<DontForgetInfo>) =
    infoDao.insertAll(infoList)

  suspend fun insertInfo(dontForgetInfo: DontForgetInfo) {
    infoDao.insertInfo(dontForgetInfo)
  }

  suspend fun updateInfo(info: DontForgetInfo) {
    infoDao.updateInfo(info)
  }

  suspend fun deleteInfo(info: DontForgetInfo) {
    infoDao.deleteInfo(info)
  }
}

class InfoRemoteDataSource {
  private var page: Int = 1
  private val list = mutableListOf<DontForgetInfo>()

  fun requestList(infoList: MutableLiveData<List<DontForgetInfo>>): Single<ApiResponse<ListPage<DontForgetInfo>>> {
    list.clear()
    page = 1
    return loadList()
      .io2mainThread()
      .doOnSuccess {
        DontForgetInfoRepository.updateInfos(list)
        infoList.value = list
      }
  }

  private fun loadList(): Single<ApiResponse<ListPage<DontForgetInfo>>> {
    return apiServiceOf<TodoApi>()
      .getTodoList(page)
      .flatMap { response ->
        if (response.data.over) {
          list.addAll(response.data.list)
          Single.just(response)
        } else {
          page++
          list.addAll(response.data.list)
          loadList()
        }
      }
  }
}
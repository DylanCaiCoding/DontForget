package com.dylanc.dontforget.ui.info_list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.repository.InfoRepository

class InfoListViewModel @ViewModelInject constructor(
  infoRepository: InfoRepository
) : ViewModel() {

  val list = infoRepository.allInfo
}
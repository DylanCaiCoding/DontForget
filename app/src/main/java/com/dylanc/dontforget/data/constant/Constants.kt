package com.dylanc.dontforget.data.constant

import com.dylanc.retrofit.helper.annotations.BaseUrl
import com.dylanc.retrofit.helper.annotations.Domain

/**
 * @author Dylan Cai
 * @since 2020/1/23
 */
@BaseUrl
const val BASE_URL = "https://www.wanandroid.com/"

@Domain("fir.im")
const val URL_VERSION = "http://api.bq04.com/"

const val KEY_INFO = "info"
const val KEY_EDIT_MODE = "edit_mode"
const val KEY_UPDATE_INTERVALS = "update_intervals"
const val KEY_SHOW_NOTIFICATION = "show_notification"

const val EVENT_NOTIFICATION = "notify"

const val REQUEST_CODE_ALARM_NOTIFY = 0
const val REQUEST_CODE_ADD_INFO = 1
const val REQUEST_CODE_UPDATE_INFO = 2

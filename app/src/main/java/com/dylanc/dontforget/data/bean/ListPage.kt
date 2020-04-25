package com.dylanc.dontforget.data.bean

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
data class ListPage<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)
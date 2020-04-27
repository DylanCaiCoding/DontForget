package com.dylanc.dontforget.data.bean

data class AppVersion(
    val binary: Binary,
    val build: String,
    val changelog: Any,
    val direct_install_url: String,
    val installUrl: String,
    val install_url: String,
    val name: String,
    val update_url: String,
    val updated_at: Int,
    val version: String,
    val versionShort: String
)

data class Binary(
    val fsize: Int
)
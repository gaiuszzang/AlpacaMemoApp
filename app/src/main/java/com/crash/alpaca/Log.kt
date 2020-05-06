package com.crash.alpaca

private const val TAG = "Alpaca"

fun logi(tag: String, msg: String) = android.util.Log.i(TAG, "$tag::$msg")

fun logd(tag: String, msg: String) = android.util.Log.d(TAG, "$tag::$msg")

fun loge(tag: String, msg: String) = android.util.Log.e(TAG, "$tag::$msg")
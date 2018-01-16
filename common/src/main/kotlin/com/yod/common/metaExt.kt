package com.yod.common

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

/**
 * meta 相关
 * Created by tangJ on 2017/11/13
 */

/**
 * 获取 application meta_data, 返回 字符串
 */
fun application(context: Context, key: String): String {
  var result: String = ""
  try {
    val info = context.packageManager.getApplicationInfo(context.packageName,
        PackageManager.GET_META_DATA)
    result = info.metaData.getString(key)
  } catch (e: PackageManager.NameNotFoundException) {
    e.printStackTrace()
  }

  return result
}

/**
 * 获取 activity meta_data 返回 字符串
 */
fun activity(context: Activity, key: String): String {
  var result: String = ""
  try {
    val info = context.packageManager
        .getActivityInfo(context.componentName, PackageManager.GET_META_DATA)
    result = info.metaData.getString(key)
  } catch (e: PackageManager.NameNotFoundException) {
    e.printStackTrace()
  }

  return result
}

/**
 * 获取 SERVICE meta_data
 */
fun <T : Service> service(context: Context, key: String, tClass: Class<T>): String {
  var result: String = ""
  val cn = ComponentName(context, tClass)
  try {
    val info = context.packageManager.getServiceInfo(cn, PackageManager.GET_META_DATA)
    result = info.metaData.getString(key)
  } catch (e: PackageManager.NameNotFoundException) {
    e.printStackTrace()
  }

  return result
}

/**
 * 获取 RECEIVER meta_data
 */
fun <T : BroadcastReceiver> receiver(context: Context, key: String, tClass: Class<T>): String {
  var result: String = ""
  val cn = ComponentName(context, tClass)
  try {
    val info = context.packageManager.getReceiverInfo(cn, PackageManager.GET_META_DATA)
    result = info.metaData.getString(key)
  } catch (e: PackageManager.NameNotFoundException) {
    e.printStackTrace()
  }

  return result
}
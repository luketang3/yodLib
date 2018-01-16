package com.yod.common.helper

import android.content.Context
import timber.log.Timber
import java.util.ArrayList

/**
 * 分析 helper
 * Created by tangJ on 2017/11/13
 */
class AnalyticsHelper {

  companion object {

    fun onResume(context: Context) {
      MAIN_ASSAYER.onResume(context)
    }

    fun onPause(context: Context) {
      MAIN_ASSAYER.onPause(context)
    }

    fun pageStart(from: String) {
      MAIN_ASSAYER.pageStart(from)
    }

    fun pageEnd(from: String) {
      MAIN_ASSAYER.pageEnd(from)
    }

    fun login(account: String) {
      MAIN_ASSAYER.login(account)
    }

    fun logout() {
      MAIN_ASSAYER.logout()
    }


    /**
     * 添加分析类
     */
    fun add(assayer: Assayer?) {
      if (assayer == null) throw NullPointerException(" assayer can't be null")
      synchronized(assayerList) {
        assayerList.add(assayer)
        ASSAYERARR = assayerList.toTypedArray()
      }
    }

    private val assayerList = ArrayList<Assayer>()
    internal var ASSAYERARR = arrayOf<Assayer>()

    private var MAIN_ASSAYER: Assayer = object : Assayer() {
      override fun onResume(context: Context) {
        val arr = ASSAYERARR
        for (i in arr.indices) {
          arr[i].onResume(context)
        }
      }

      override fun onPause(context: Context) {
        val arr = ASSAYERARR
        for (i in arr.indices) {
          arr[i].onPause(context)
        }
      }

      override fun pageStart(from: String) {
        val arr = ASSAYERARR
        for (i in arr.indices) {
          arr[i].pageStart(from)
        }
      }

      override fun pageEnd(from: String) {
        val arr = ASSAYERARR
        for (i in arr.indices) {
          arr[i].pageEnd(from)
        }
      }

      override fun login(account: String) {
        val arr = ASSAYERARR
        for (i in arr.indices) {
          arr[i].login(account)
        }
      }

      override fun logout() {
        val arr = ASSAYERARR
        for (i in arr.indices) {
          arr[i].logout()
        }
      }
    }

  }


  abstract class Assayer {

    abstract fun onResume(context: Context)

    abstract fun onPause(context: Context)

    abstract fun pageStart(from: String)

    abstract fun pageEnd(from: String)

    abstract fun login(account: String)

    abstract fun logout()
  }
}
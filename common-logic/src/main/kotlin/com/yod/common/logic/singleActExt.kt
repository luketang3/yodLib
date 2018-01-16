package com.yod.common.logic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.yod.common.ui.BaseSubPresenter

/**
 * 启动 单个 Activity
 * Created by tangJ on 2017/11/14
 */

val F_TAG = "f_tag"
val P_TAG = "p_tag"
val DATA_TAG = "data_tag"
val TRANS_TAG = "trans_tag" //trans tag

/**
 * 单个 activity
 */
inline fun <reified F : Fragment, reified A : AppCompatActivity> startAct(
    from: Any,
    bundle: Bundle? = null,
    requestCode: Int = -1,
    transArr: IntArray? = null
) {
  var targetActivity: Activity? = null
  when (from) {
    is Activity -> { //activity
      Intent()
      targetActivity = from
      from.startActivityForResult(
          Intent(from, A::class.java).apply {
            intentProcessor(this, F::class.java, bundle, transArr)
          },
          requestCode
      )
    }
    is Fragment -> {
      targetActivity = from.activity
      from.startActivityForResult(
          Intent(from.activity, A::class.java).apply {
            intentProcessor(this, F::class.java, bundle, transArr)
          }, requestCode
      )
    }
  }

  targetActivity?.apply {
    //anim
    if (transArr != null && transArr.size == 4) {
      overridePendingTransition(transArr[0], transArr[1])
    }
  }
}

/**
 * activity + presenter
 */
inline fun <reified F : Fragment, reified A : AppCompatActivity, reified P : BaseSubPresenter<*>> startActP(
    from: Any,
    bundle: Bundle? = null,
    requestCode: Int = -1,
    transArr: IntArray? = null
) {

  var targetActivity: Activity? = null
  when (from) {
    is Activity -> { //activity
      Intent()
      targetActivity = from
      from.startActivityForResult(
          Intent(from, A::class.java).apply {
            intentProcessor(this, F::class.java, bundle, transArr, P::class.java)
          },
          requestCode
      )
    }
    is Fragment -> {
      targetActivity = from.activity
      from.startActivityForResult(
          Intent(from.activity, A::class.java).apply {
            intentProcessor(this, F::class.java, bundle, transArr, P::class.java)
          }, requestCode
      )
    }
  }

  targetActivity?.apply {
    //anim
    if (transArr != null && transArr.size == 4) {
      overridePendingTransition(transArr[0], transArr[1])
    }
  }
}

fun intentProcessor(
    intent: Intent,
    fClass: Class<out Fragment>?,
    bundle: Bundle?,
    transArr: IntArray?,
    pClass: Class<*>? = null
) {
  //fragment
  intent.putExtra(F_TAG, fClass)
  //bundle
  bundle?.apply { intent.putExtra(DATA_TAG, this) }
  //动画
  transArr?.apply { if (size == 4) intent.putExtra(TRANS_TAG, transArr) }
  //presenter
  pClass?.apply { intent.putExtra(P_TAG, this) }
}
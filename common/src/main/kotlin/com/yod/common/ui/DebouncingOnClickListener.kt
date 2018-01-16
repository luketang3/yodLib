package com.yod.common.ui

import android.view.View
import timber.log.Timber

/**
 * butterknife DebouncingOnClickListener
 * Created by tangJ on 2017/11/10
 */

abstract class DebouncingOnClickListener : View.OnClickListener {
  companion object {
    private var enable = true
  }

  private val ENABLE_RUNNABLE = Runnable { enable = true }
  override fun onClick(v: View) {
    Timber.d("==>enable:$enable")
    if (enable) {
      enable = false
      v.postDelayed(ENABLE_RUNNABLE, 50)
      doClick(v)
    }
  }

  abstract fun doClick(view: View)
}
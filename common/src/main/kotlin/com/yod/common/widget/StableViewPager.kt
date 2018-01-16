package com.yod.common.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import timber.log.Timber


/**
 * stable
 * Created by tangJ on 2017/9/5
 */
class StableViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : ViewPager(context, attrs) {

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    try {
      return super.onTouchEvent(ev)
    } catch (ex: IllegalArgumentException) {
      ex.printStackTrace()
      Timber.d("==>crash: onTouchEvent")
    }

    return false
  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    try {
      return super.onInterceptTouchEvent(ev)
    } catch (ex: IllegalArgumentException) {
      ex.printStackTrace()
      Timber.d("==>crash: onInterceptTouchEvent")
    }
    return false
  }
}
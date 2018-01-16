package com.yod.common

import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher

/**
 * 通用 方法
 * Created by tangJ on 2017/11/29
 */


open class EmptyPagerListener : ViewPager.OnPageChangeListener {
  override fun onPageScrollStateChanged(state: Int) {

  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

  }

  override fun onPageSelected(position: Int) {

  }

}


open class EmptyTextWatcher : TextWatcher {
  override fun afterTextChanged(s: Editable?) {
  }

  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
  }

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
  }

}

class CharLenFilter(maxLen: Int) : InputFilter {
  private val realLength = maxLen * 2

  override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int,
      dend: Int): CharSequence? {

    val total = dest.fold(0, { len, c -> len + c.realLen() })
    if (total >= realLength) return ""

    val targetSource = source.subSequence(start, end)
    var sourceCount = 0
    for ((index, c) in targetSource.withIndex()) {
      sourceCount += c.realLen()
      if (sourceCount + total > realLength) {
        return targetSource.subSequence(0, index)
      }
    }
    return null //origin
  }
}

fun Char.realLen() = if (toInt() < 128) 1 else 2
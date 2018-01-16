package com.yod.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.os.Build
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import timber.log.Timber

/**
 * Created by tangJ on 2017/11/13
 */

fun View.isLaid(): Boolean {
  if (Build.VERSION.SDK_INT >= 19) {
    return isLaidOut
  }
  return width > 0 && height > 0
}

/**
 * 视图加载完成执行
 */
fun View.onLaidOut(action: View.() -> Unit) {
  if (isLaid()) {
    action()
    return
  }
  val observer = viewTreeObserver
  observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
    override fun onGlobalLayout() {
      val trueObserver: ViewTreeObserver

      if (observer.isAlive) {
        trueObserver = observer
      } else {
        trueObserver = viewTreeObserver
      }

      if (Build.VERSION.SDK_INT >= 16) {
        trueObserver.removeOnGlobalLayoutListener(this)
      } else {
        @Suppress("DEPRECATION")
        trueObserver.removeGlobalOnLayoutListener(this)
      }
      action()
    }
  })
}

/**
 * 是否显示 View
 */
fun View.show(show: Boolean) {
  visibility = if (show) View.VISIBLE else View.GONE
}

/**
 * 改变 select 返回状态
 */
fun View.inverseSelected(): Boolean {
  isSelected = !isSelected
  return isSelected
}

//改变 ALPHA enable 值
fun View.alphaEnable(enable: Boolean) {
  isEnabled = enable
  alpha = if (enable) 1f else 0.5f
}

/**
 * 方形背景 相同倒角
 */
fun View.roundRect(color: Int, radius: Float) {
  val drawable = PaintDrawable(color).apply { setCornerRadius(radius) }
  backDrawable(drawable)
}


fun View.backDrawable(drawable: Drawable) {
  ViewCompat.setBackground(this, drawable)
}

/**
 * 移除当前 web
 */
fun WebView.remove() {
  (parent as? ViewGroup)?.apply {
    removeView(this)
    removeAllViews()
    destroy()
  }
}

/**
 * 清除 textView
 */
fun TextView.editClear(clearView: View) {
  addTextChangedListener(object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      val b = isFocused && s.isNotEmpty()
      clearView.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun afterTextChanged(s: Editable) {

    }
  })
  clearView.setOnClickListener { text = null }
  clearView.visibility = View.GONE
  onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
    clearView.visibility = if (hasFocus && this.text.isNotEmpty()) View.VISIBLE else View.GONE
  }
}

/**
 * view Enable
 */
fun EditText.viewEnable(enableView: View, minLen: Int, maxLen: Int) {
  addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      val len = if (s.isNullOrEmpty()) 0 else s!!.length
      enableView.isEnabled = len in minLen..maxLen
    }

  })
}

/**
 * menu enable
 */
fun EditText.menuEnable(enableView: MenuItem, minLen: Int, maxLen: Int) {
  addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      val len = if (s.isNullOrEmpty()) 0 else s!!.length
      enableView.isEnabled = len in minLen..maxLen
    }
  })
}

/**
 * 最后一个选中
 */
fun EditText.lastSelect() {
  if (text.length > 0) setSelection(length())
}

fun EditText.lastSelect(ch: CharSequence?) {
  setText(ch)
  if (ch?.isNotEmpty() == true) setSelection(ch.length)
}


/**
 * 添加 单个 menu
 */
fun Menu.addSingle(ch: CharSequence) =
    add(ch).apply {
      setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT or MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

/**
 * recycler layout manager
 */
fun RecyclerView.addVerticalLM(count: Int) {
  layoutManager = StaggeredGridLayoutManager(count, StaggeredGridLayoutManager.VERTICAL)
}

fun RecyclerView.pointTo(position: Int) {
  (layoutManager as? StaggeredGridLayoutManager)?.scrollToPositionWithOffset(position, 0)
}

fun RecyclerView.getLastVisibleP(): Int {
  val layoutManager = layoutManager
  var tPosition = -1
  if (layoutManager is LinearLayoutManager) {
    tPosition = layoutManager.findLastVisibleItemPosition()

  } else if (layoutManager is StaggeredGridLayoutManager) {
    val lm = layoutManager
    val it = lm.findLastVisibleItemPositions(kotlin.IntArray(lm.spanCount))
    tPosition = it[it.size - 1]
  }
  return tPosition
}

/**
 * 显示输入键盘
 */
fun View.showInput(show: Boolean) {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  val isOpen = imm.isActive
  Timber.d("==>is open:$isOpen")
  if (show) {
    imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
  } else if (!show && isOpen) {
    imm.hideSoftInputFromWindow(windowToken, 0)
  }
}

fun ViewGroup.lastChild(): View {
  if (childCount == 0) throw NoSuchElementException("parent is empty")
  return this[childCount - 1]
}

fun TextView.setPressColor(color: Int, alpha: Float = 0.7f) {
  val colorState = createPressColor(color, alpha)
  setTextColor(colorState)
}

//显示指定 txt count
fun TextView.countTxt(count: Int) {
  val show = count > 0
  show(show)
  if (show) text = count.toString()
}

operator fun ViewGroup.get(i: Int): View = getChildAt(i)

operator fun ViewGroup.plusAssign(child: View) = addView(child)

operator fun ViewGroup.minusAssign(child: View) = removeView(child)

fun ViewGroup.childSequence(): Sequence<View> =
    (0 until childCount).map { getChildAt(it) }.asSequence()


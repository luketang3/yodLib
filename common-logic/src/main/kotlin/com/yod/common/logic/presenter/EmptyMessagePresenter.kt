package com.yod.common.logic.presenter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yod.common.EmptyCallback
import com.yod.common.logic.R
import com.yod.common.onLaidOut
import com.yod.common.show
import com.yod.common.ui.MessagePresenter

/**
 * 空视图 presenter
 *
 * Created by tangJ on 2017/11/14
 */
class EmptyMessagePresenter(context: Context, specRootView: View? = null) : MessagePresenter {
  //rootView
  private val rootView = specRootView ?: View.inflate(context, R.layout.yd_view_content_msg, null)
  private var mImage: ImageView = rootView.findViewById(R.id.content_image)
  private var mText: TextView = rootView.findViewById(R.id.content_tip)
  private var refreshLayout: View = rootView.findViewById(R.id.refresh_content)
  private var currentAction: EmptyCallback? = null

  init {
    //默认展示
    rootView.show(true)
    mImage.show(false)
    refreshLayout.setOnClickListener { currentAction?.apply { this() } }
  }

  override fun empty(ch: CharSequence, res: Int) {
    showRoot(true)
    showImage(true)
    showClickView(false)
    mText.text = ch
  }

  override fun error(ch: CharSequence) {
//    mImage.show(true)
    showRoot(true)
    showImage(true)
    showClickView(true)
    mText.text = ch
    refreshLayout.post {
      refreshLayout.onLaidOut {
        val pView = parent as ViewGroup
        val param = layoutParams
        param.width = pView.width
        param.height = pView.height
        layoutParams = param
      }
    }

  }

  override fun loading(ch: CharSequence) {
//    mImage.show(false)
    showImage(false)
    //展示视图
    showRoot(true)
    showClickView(false)
    val target = if (ch.isBlank()) "加载中.." else ch
    mText.text = target
  }

  private fun showImage(show: Boolean) {
    mImage.show(show)
  }

  private fun showClickView(show: Boolean) {
    refreshLayout.show(show)
  }


  override fun dismiss() {
    showRoot(false)
  }

  override fun getView(): View = rootView

  private fun showRoot(show: Boolean) {
    rootView.show(show)
  }

  override fun setAction(action: EmptyCallback) {
    currentAction = action
  }

}
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
import timber.log.Timber

/**
 * 空列表 presenter
 * Created by tangJ on 2017/11/14
 */
class EmptyListPresenter(context: Context, action: EmptyCallback) : MessagePresenter {

  var rootView: View = View.inflate(context, R.layout.yd_view_list_msg, null)
  var mText: TextView
  var mImage: ImageView
  var msgContainer: View
  var refreshView: View

  init {
    mText = rootView.findViewById(R.id.tip)
    mImage = rootView.findViewById(R.id.image)
    msgContainer = rootView.findViewById(R.id.msg_container)
    refreshView = rootView.findViewById(R.id.refresh_content)
    mImage.show(false)
    refreshView.setOnClickListener { action() }
  }

  override fun getView(): View {
    return rootView
  }

  private fun showImage(show: Boolean, res: Int = -1) {
//    val showImg = show
    mImage.show(show)
    if (res != -1) {
      mImage.setImageResource(res)
    }

    refreshView.show(show)
    if (show) {
      refreshView.onLaidOut {
        val pView = parent as ViewGroup
        val param = layoutParams
        param.width = pView.width
        param.height = pView.height
        layoutParams = param
      }
    }
  }

  override fun empty(ch: CharSequence, res: Int) {
    Timber.d("==>empty :$ch")
    showImage(true, res)
    mText.text = ch
  }

  override fun error(ch: CharSequence) {
    Timber.d("==>error :$ch")
    showImage(true)
    mText.text = ch
  }

  override fun loading(ch: CharSequence) {
    Timber.d("==>loading")
    showImage(false)
    mText.text = if (ch.isNotEmpty()) ch else "加载中…"
  }

  override fun dismiss() {

  }
}
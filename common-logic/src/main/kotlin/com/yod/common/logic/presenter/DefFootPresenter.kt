package com.yod.common.logic.presenter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yod.common.EmptyCallback
import com.yod.common.logic.R
import com.yod.common.show
import com.yod.common.ui.FootPresenter

/**
 * list foot presenter
 * Created by tangJ on 2017/11/14
 */
class DefFootPresenter(context: Context, action: EmptyCallback) : FootPresenter {

  var rootView: View = View.inflate(context, R.layout.yd_include_foot, null)
  var loadingView: View
  var errorView: View
  var finishView: View
  var errorTxt: TextView

  init {
    rootView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)
    loadingView = rootView.findViewById(R.id.view1)
    errorView = rootView.findViewById(R.id.view2)
    finishView = rootView.findViewById(R.id.view3)
    errorTxt = errorView.findViewById(R.id.txt)
    errorView.setOnClickListener { action() }
    //defualt
    showPosition(0)
  }


  override fun loading() {
    showPosition(0)
  }

  override fun error(ch: CharSequence) {
    showPosition(1)
    errorTxt.text = if (ch.isEmpty()) "加载失败" else ch
  }

  override fun complete() {
    showPosition(2)
  }

  override fun getView() = rootView

  private fun showPosition(position: Int) {
    loadingView.show(position == 0)
    errorView.show(position == 1)
//    finishView.show(position == 2)
    finishView.show(false)
  }
}
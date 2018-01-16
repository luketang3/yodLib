package com.yod.common.logic.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.yod.common.logic.R
import com.yod.common.ui.DialogShowText

/**
 * default loading dialog
 * Created by tangJ on 2017/11/14
 */
class LoadingDialog : Dialog, DialogShowText {

  private var mProgress: ProgressBar
  private var mText: TextView
  private var mView: View

  constructor(context: Context) : super(context, R.style.yd_dialog_progress) {
    mView = View.inflate(context, R.layout.yd_common_progress, null)
    setCanceledOnTouchOutside(false)
    mText = mView.findViewById(R.id.txt)
    mProgress = mView.findViewById(R.id.spin_kit)
    mProgress.indeterminateDrawable = ThreeBounce()
    setContentView(mView)
  }

  constructor(context: Context, drawable: Drawable) : this(context) {
    mProgress.indeterminateDrawable = drawable
  }

  override fun showText(txt: CharSequence?) {
    if (txt.isNullOrBlank()) {
      mText.visibility = View.GONE
    } else {
      mText.visibility = View.VISIBLE
      mText.text = txt
    }
  }

  override fun changeText(content: String?) {
    showText(content)
  }

}
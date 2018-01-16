package com.yod.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.yod.common.EmptyCallback
import com.yod.common.childSequence
import com.yod.common.ui.MessagePresenter
import timber.log.Timber

/**
 * 公共加载视图
 * 支持 消息视图 + 空白视图
 * Created by tangJ on 2017/11/14
 */
class CommonLoadingLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
  companion object {
    private val HELP_TAG = "help_tag" //help 指定 tag
  }

  private var contentView: View? = null
  private lateinit var msgPresenter: MessagePresenter //一定要初始化，不然没有意义

  override fun onFinishInflate() {
    super.onFinishInflate()

    if (childCount > 1) throw RuntimeException(
        "except less than 2 child, but real count: $childCount")
    if (childCount == 1) {
      //内容视图
      contentView = getChildAt(0)
    }
  }

  fun resetPresenter(presenter: MessagePresenter) {
    childSequence().filter { it.tag == HELP_TAG }
        .toList()
        .forEach {
          Timber.d("==>already remove")
          removeView(it)
        }
    msgPresenter = presenter

    val param = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    addView(msgPresenter.getView(), param)
    msgPresenter.getView().visibility = View.GONE
  }


  fun loading(ch: CharSequence) {
    msgPresenter.loading(ch)
    contentView?.visibility = View.GONE
  }

  fun error(ch: CharSequence) {
    msgPresenter.error(ch)
    contentView?.visibility = View.GONE
  }

  fun dismiss() {
    msgPresenter.dismiss()
    contentView?.visibility = View.VISIBLE
  }

  fun setAction(action: EmptyCallback) {
    msgPresenter.setAction(action)
  }

}
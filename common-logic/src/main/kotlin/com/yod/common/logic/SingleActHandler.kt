package com.yod.common.logic

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.yod.common.ui.BackListener
import com.yod.common.ui.BasePresenter
import com.yod.common.ui.BaseSubPresenter
import com.yod.common.ui.BaseView
import com.yod.common.ui.FinishListener
import com.yod.common.widget.SlideEdge
import com.yod.common.widget.Slider
import timber.log.Timber

/**
 * 单 activity handler
 * Created by tangJ on 2017/11/14
 */
class SingleActHandler(private val activity: AppCompatActivity) {

  private var backListener: BackListener? = null
  private var finishListener: FinishListener? = null
  private var transitionArr: IntArray? = null

  fun handleIntent(bundle: Bundle?): Fragment? {

    var fragment: Fragment? = null

    activity.apply {
      (intent.getSerializableExtra(F_TAG) as? Class<*>)?.apply {
        val targetObj: Any = this.newInstance()
        fragment = targetObj as Fragment

        intent.getBundleExtra(DATA_TAG)?.apply { fragment!!.arguments = this }

        backListener = fragment as? BackListener
        finishListener = fragment as? FinishListener

        (intent.getSerializableExtra(P_TAG) as? Class<*>)?.let {
          if (targetObj is BaseView<*> && BasePresenter::class.java.isAssignableFrom(it)
              && BaseSubPresenter::class.java.isAssignableFrom(it)) { //presenter
            interfaces.firstOrNull { BaseView::class.java.isAssignableFrom(it) }
                ?.apply spec@ {

                  val constructor = it.getConstructor(this@spec)
                  val presenter = constructor.newInstance(fragment) as BaseSubPresenter<*>
                  (targetObj as BaseView<BasePresenter>).setPresenter(presenter)
                } ?: Timber.d("==>no relate interface")
          }
        }

        //can drag
        backListener?.apply {
          if (canDrag()) Slider.attachToScreen(activity, SlideEdge.LEFT)
        }

        //anim array
        transitionArr = intent.getIntArrayExtra(TRANS_TAG)
      }
    }
    return fragment
  }

  //返回
  fun onBack() = backListener?.onBack() ?: false

  //home
  fun onHome() = backListener?.onHome() ?: false

  //finish 事件
  fun onFinish() = finishListener?.onFinish()

  //完成 anim
  fun finishAnim() {
    transitionArr?.apply {
      if (size == 4) {
        Timber.d("==>overridePendingTransition")
        activity.overridePendingTransition(get(2), get(3))
      }
    }
  }
}
package com.yod.common.ui

import com.yod.common.helper.DisposeHelper

/**
 * mvp 基本信息定义
 * Created by tangJ on 2017/10/31
 */
interface BasePresenter {
  fun clear() {}
}

interface BaseView<in P> {

  fun setPresenter(p: P)

  fun supportDialog(action: DialogPresenter.() -> Unit)

  fun finishCurrent()
}

open class BaseSubPresenter<out V : BaseView<*>> constructor(val mView: V) : BasePresenter {
  //dispose relate
  protected val disposeHelper: DisposeHelper by lazy { DisposeHelper() }

  override fun clear() {
    disposeHelper.compositeDispose.clear()
  }
}
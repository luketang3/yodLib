package com.yod.common.ui

import android.view.View
import com.yod.common.EmptyCallback

/**
 * ui 公共逻辑
 * Created by tangJ on 2017/11/13
 */

/**
 * 消息 presenter
 */
interface MessagePresenter {
  /**
   * 空视图
   */
  fun empty(ch: CharSequence, res: Int = -1)

  /**
   * 错误视图
   */
  fun error(ch: CharSequence)

  /**
   * 开始加载
   */
  fun loading(ch: CharSequence)

  /**
   * 消失
   */
  fun dismiss()

  /**
   * 当前对应的VIEW
   */
  fun getView(): View

  /**
   * 注册响应 action
   */
  fun setAction(action: EmptyCallback) {}
}

/**
 * 底部 视图 presenter
 */
interface FootPresenter {

  fun loading()

  fun error(ch: CharSequence)

  fun complete()

  fun getView(): View
}

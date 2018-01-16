package com.yod.common.ui

import android.content.DialogInterface
import io.reactivex.disposables.Disposable

/**
 * 公共逻辑接口
 * Created by tangJ on 2017/11/13
 */

/**
 * back/ home/ canDrag
 */
interface BackListener {
  fun onBack(): Boolean = false
  fun onHome(): Boolean = false
  fun canDrag(): Boolean = true //是否可拖拽返回
}

interface FinishListener {
  fun onFinish()
}

/**
 * dialog 展示
 */
interface DialogPresenter {

  fun show(content: String? = null)

  fun dismiss()

  fun changeText(content: String? = null) {}

  fun onDismiss()

  fun addDispose(disposable: Disposable? = null, action: (DialogInterface) -> Boolean = { false })
}

interface DialogShowText {
  fun showText(txt: CharSequence?)
  fun changeText(content: String? = null)
}
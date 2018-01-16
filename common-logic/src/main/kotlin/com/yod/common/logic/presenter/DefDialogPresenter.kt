package com.yod.common.logic.presenter

import android.app.Dialog
import android.content.DialogInterface
import android.view.KeyEvent
import com.yod.common.ui.DialogPresenter
import com.yod.common.ui.DialogShowText
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * 默认 dialog presenter
 * Created by tangJ on 2017/11/14
 */
class DefDialogPresenter(private val mDialog: Dialog) : DialogPresenter {

  var txtShower: DialogShowText? = null
  var disposable: Disposable? = null

  init {
    if (mDialog is DialogShowText) {
      txtShower = mDialog
    }
    mDialog.setOnDismissListener { onDismiss() }
  }


  override fun show(content: String?) {
    //刷新文字
    txtShower?.showText(content)
    if (!mDialog.isShowing) {
      mDialog.show()
    }
  }

  override fun changeText(content: String?) {
    txtShower?.changeText(content)
  }

  override fun dismiss() {
    mDialog.takeIf { it.isShowing }?.apply { dismiss() }
  }

  override fun onDismiss() {
    disposable?.apply {
      if (!isDisposed) {
        Timber.d("==>try dismiss")
        dispose()
      }
    }
    disposable = null
  }

  override fun addDispose(disposable: Disposable?, action: (DialogInterface) -> Boolean) {
    this.disposable = disposable
    mDialog.setOnKeyListener { dialog, keyCode, event ->
      if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
        action(dialog)
      } else false
    }
  }
}
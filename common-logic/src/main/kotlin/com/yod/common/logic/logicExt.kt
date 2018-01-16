package com.yod.common.logic

import android.app.Dialog
import android.content.Context
import com.yod.common.EmptyCallback
import com.yod.common.logic.dialog.LoadingDialog
import com.yod.common.logic.presenter.DefDialogPresenter
import com.yod.common.logic.presenter.DefFootPresenter
import com.yod.common.logic.presenter.EmptyListPresenter
import com.yod.common.logic.presenter.EmptyMessagePresenter
import com.yod.common.ui.DialogPresenter
import com.yod.common.ui.FootPresenter
import com.yod.common.ui.MessagePresenter
import com.yod.common.widget.CommonLoadingLayout

/**
 * 公共视图逻辑
 * Created by tangJ on 2017/11/14
 */


/**
 * 指定 默认 presenter
 */
fun CommonLoadingLayout.defaultPresenter() {
  resetPresenter(EmptyMessagePresenter(context))
}

/**
 * 默认 dialog presenter
 */
fun getDefDialogPresenter(context: Context, dialog: Dialog? = null): DialogPresenter {
  val targetDialog = dialog ?: LoadingDialog(context)
  return DefDialogPresenter(targetDialog)
}

/**
 * list presenter
 */
fun getDefListPresenter(context: Context, action: EmptyCallback): MessagePresenter =
    EmptyListPresenter(context, action)

/**
 * foot presenter
 */
fun getDefFootPresenter(context: Context, action: EmptyCallback): FootPresenter =
    DefFootPresenter(context, action)
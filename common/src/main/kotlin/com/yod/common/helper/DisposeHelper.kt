package com.yod.common.helper

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * composite Disposable
 * Created by tangJ on 2017/11/13
 */
class DisposeHelper {

  val compositeDispose = CompositeDisposable()

  private val mapDispose = mutableMapOf<String, Disposable>()

  /**
   * 移除所有
   */
  fun removeAll() {
    compositeDispose.clear()
    mapDispose.clear()
  }

  fun add(disposable: Disposable) {
    compositeDispose.add(disposable)
  }

  fun add(tag: String, disposable: Disposable) {
    mapDispose[tag]?.let {
      if (!it.isDisposed) it.dispose()
    }
    mapDispose.put(tag, disposable)
  }

  fun remove(disposable: Disposable) {
    compositeDispose.remove(disposable)
  }

  fun remove(tag: String) {
    mapDispose[tag]?.let {
      if (!it.isDisposed) it.dispose()
      mapDispose.remove(tag)
    }
  }

  fun find(tag: String): Disposable? = mapDispose[tag]
}
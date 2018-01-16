package com.yod.common.helper

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * 全局总线
 * Created by tangJ on 2017/11/13
 */
class RxBus {

  val bus = PublishSubject.create<Any>().toSerialized()

  fun send(any: Any) {
    if (hasObserve()) bus.onNext(any)
  }

  fun hasObserve(): Boolean = bus.hasObservers()

  fun asObserver(): Observable<Any> = bus

  inline fun <reified T> asTypeObserver(): Observable<T> = bus.ofType(T::class.java)

  companion object {
    val instance: RxBus by lazy { RxBus() }
  }
}
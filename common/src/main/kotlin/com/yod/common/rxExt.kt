package com.yod.common

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Timed
import timber.log.Timber

/**
 * Created by tangJ on 2017/11/13
 */

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
  add(disposable)
}

fun Disposable?.isRun() = this != null && !isDisposed

fun Disposable?.cancel() {
  if (this != null && !isDisposed) dispose()
}


fun Disposable.addTo(compositeDisposable: CompositeDisposable) = apply {
  compositeDisposable.add(this)
}

fun <T> Flowable<T>.composeSub(): Flowable<T> =
    compose { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }

fun <T> Flowable<T>.composeCost(): Flowable<T> =
    compose {
      Flowable.just(java.lang.System.currentTimeMillis()).zipWith(it.timestamp(),
          BiFunction<Long, Timed<T>, T> { t1, t2 ->
            Timber.d("==>cost:${t2.time() - t1}ms")
            t2.value()
          }
      )
    }

fun Completable.composeSub(): Completable =
    compose { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }

fun <T> Flowable<T>.composeRetry(retryCount: Int = 2): Flowable<T> =
    retryWhen {
      val targetCount = if (retryCount < 0) 0 else retryCount
      /* it.zipWith(Flowable.range(0, targetCount + 1),
           BiFunction<Throwable, Int, Pair<Throwable, Int>>
           { t1, t2 -> Pair(t1, t2) })
           .delay(1000, MILLISECONDS)
           .flatMap {
             if (it.second >= retryCount) {
               Flowable.error(it.first)
             } else {
               Timber.d("==>retry time:${it.second + 1}")
               Flowable.just(it)
             }
           }*/
      it.zipWith(Flowable.range(0, targetCount + 1),
          BiFunction<Throwable, Int, Pair<Throwable, Int>>
          { t1, t2 -> kotlin.Pair(t1, t2) })
          .flatMap {
            if (it.second >= retryCount) Flowable.error(it.first)
            else {
              timber.log.Timber.d("==>retry time:${it.second + 1}")
              Flowable.timer(1, java.util.concurrent.TimeUnit.SECONDS)
            }
          }
    }

fun <T> Observable<T>.composeSub(): Observable<T> =
    compose { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
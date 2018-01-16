package com.yod.common.helper

import android.annotation.SuppressLint
import android.webkit.WebView
import com.yod.common.remove
import java.util.Collections
import java.util.HashSet

/**
 * web view 相关逻辑
 * Created by tangJ on 2017/11/13
 */
class WebViewHelper {

  private val webViewSet = HashSet<WebView>()

  fun manage(vararg webViewArr: WebView): WebViewHelper {
    Collections.addAll(webViewSet, *webViewArr)
    return this
  }

  @SuppressLint("SetJavaScriptEnabled")
  private fun js(enable: Boolean) {
    for (webView in webViewSet) {
      webView.settings.javaScriptEnabled = enable
    }
  }

  /**
   * 允许 JS 运行
   */
  fun enableJs(): WebViewHelper {
    js(true)
    return this
  }

  /**
   * 禁用JS
   */
  fun disableJs(): WebViewHelper {
    js(false)
    return this
  }


  private fun zoom(enable: Boolean) {
    for (webView in webViewSet) {
      webView.settings.setSupportZoom(enable)
    }
  }

  /**
   * 允许缩放
   */
  fun enableZoom(): WebViewHelper {
    zoom(true)
    return this
  }

  /**
   * 禁用缩放
   */
  fun disableZoom(): WebViewHelper {
    zoom(false)
    return this
  }

  /**
   * 暂停状态
   */
  fun pause() {
    for (webView in webViewSet) {
      webView.onPause()
    }
  }

  /**
   * 开始运行状态
   */
  fun resume() {
    for (webView in webViewSet) {
      webView.onResume()
    }
  }

  /**
   * 移除管理
   */
  fun remove() {
    for (webView in webViewSet) {
      webView.remove()
    }
    webViewSet.clear()
  }
}
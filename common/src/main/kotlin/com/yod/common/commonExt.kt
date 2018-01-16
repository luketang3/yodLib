package com.yod.common

import android.R.attr
import android.content.res.ColorStateList
import android.os.Build
import android.text.Spannable
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import java.util.Calendar

/**
 * Created by tangJ on 2017/11/13
 */

typealias FloatCallback = (Float) -> Unit
typealias IntCallback = (Int) -> Unit
typealias EmptyCallback = () -> Unit
typealias BoolCallback = (Boolean) -> Unit
typealias AnyCallback = (Any) -> Unit
typealias StringCallback = (String) -> Unit
typealias ErrorCallback = (Throwable) -> Unit
typealias CalendarCallback = (Calendar) -> Unit


typealias BoolResult = () -> Boolean

/**
 * 基于当前颜色改变透明
 */
fun changeAlpha(color: Int, alpha: Float): Int {
  var target = alpha
  if (target > 1.0f) {
    target = 1.0f
  } else if (target <= 0.0f) {
    target = 0.0f
  }
  return (0xFF * target).toInt() shl 24 or (color and 0x00FFFFFF)
}

/**
 * 当前颜色主色 alpha 改变
 */
fun getAlphaColor(argb: Int, alpha: Float): Int {
  var target = alpha
  if (target > 1.0f) {
    target = 1.0f
  } else if (target <= 0.0f) {
    target = 0.0f
  }
  return (argb.ushr(24) * target).toInt() shl 24 or (argb and 0x00FFFFFF)
}

/**
 *
 */
fun Double.floatNum(num: Int): Double {
//  Math.floor()
  if (num > 0) {
    val str = String.format("%.${num}f", this)
    return str.toDouble()
  }
  return this
}

fun createPressColor(color: Int, alpha: Float): ColorStateList {
  val pressColor = getAlphaColor(color, alpha)
  return ColorStateList(
      arrayOf(
          intArrayOf(attr.state_pressed),
          intArrayOf()
      ),
      intArrayOf(
          pressColor,
          color
      )
  )
}


//inline fun <reified T> Gson.fromGson(msg: String): T = fromJson(msg, T::class.java)

fun isNougat(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

//inline fun <E, R> SparseArray<E>.fold(initial : R, operation: (acc : R, E) -> )

/**
 * 16 进制转 bytes
 */
fun hexToBytes(s: String): ByteArray {
  val len = s.length
  val data = ByteArray(len / 2)
  var i = 0
  while (i < len) {
    data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
    i += 2
  }
  return data
}

private val hexArray = "0123456789ABCDEF".toCharArray()

/**
 * bytes 转 16进制字符串
 */
fun bytesToHex(bytes: ByteArray): String {
  val hexChars = CharArray(bytes.size * 2)
  for (j in bytes.indices) {
    val v = bytes[j].toInt() and 0xFF
    hexChars[j * 2] = hexArray[v.ushr(4)]
    hexChars[j * 2 + 1] = hexArray[v and 0x0F]
  }
  return String(hexChars)
}


/**
 * span
 */
fun <T : Spannable> T.colorSpan(color: Int): T {
  val span = ForegroundColorSpan(color)
  setSpan(span, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
  return this
}

fun <T : Spannable> T.backColorSpan(color: Int): T {
  val span = BackgroundColorSpan(color)
  setSpan(span, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
  return this
}

/**
 * size
 */
fun <T : Spannable> T.sizeSpan(size: Int): T {
  val span = AbsoluteSizeSpan(size)
  setSpan(span, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
  return this
}


/**
 * getUrl param
 */
fun String.getUrlParam(): Map<String, String> {
  val map = mutableMapOf<String, String>()
  val index = lastIndexOf("?")
  if (index > -1) {
    val paramString = substring(index + 1)

    val paramList = paramString.split(Regex("&"))

    paramList.forEach { string ->
      val kvList = string.split(Regex("="))
      if (kvList.size == 2) {
        map[kvList[0]] = kvList[1]
      }
    }
  }

  return map
}
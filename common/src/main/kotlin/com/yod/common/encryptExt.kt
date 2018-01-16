package com.yod.common

import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * 加密相关
 * Created by tangJ on 2017/11/13
 */

fun MD5(str: String): String {
  try {
    val mDigest = MessageDigest.getInstance("MD5")
    mDigest.update(str.toByteArray())
    val bytes = mDigest.digest()
    val sb = StringBuilder()
    for (i in bytes.indices) {
      val hex = Integer.toHexString(0xFF and bytes[i].toInt())
      if (hex.length == 1) {
        sb.append('0')
      }
      sb.append(hex)
    }
    return sb.toString()
  } catch (e: NoSuchAlgorithmException) {
    return str.hashCode().toString()
  }

}

fun BMD5(str: String): ByteArray {
  val target = MD5(str)
  return hex2byte(target.toByteArray())
}

fun hex2byte(strByte: ByteArray): ByteArray {
  if (strByte.size % 2 != 0) {
    throw IllegalArgumentException("异常")
  } else {
    val bs = ByteArray(strByte.size / 2)
    var i = 0
    while (i < strByte.size) {
      val var3 = String(strByte, i, 2)
      bs[i / 2] = Integer.parseInt(var3, 16).toByte()
      i += 2
    }
    return bs
  }
}

/**
 * base64 加密
 */
fun base64E(str: String?): String {
  return if (str == null) "" else Base64.encodeToString(str.toByteArray(), Base64.NO_WRAP)
}

/**
 * base64 解密
 */
fun base64D(str: String?): String {
  return if (str == null) "" else String(Base64.decode(str, Base64.NO_WRAP))
}

fun main(args: Array<String>) {
//  println("==>md5: ${MD5("123")}")
}
package com.yod.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.util.Base64
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * bitmap 相关
 * Created by tangJ on 2017/11/13
 */

fun drawableToBitmap(drawable: Drawable): Bitmap? {
  if (drawable is BitmapDrawable) {
    return drawable.bitmap
  } else if (drawable is NinePatchDrawable) {
    val bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
        drawable.getIntrinsicHeight(),
        if (drawable.getOpacity() != PixelFormat.OPAQUE)
          Bitmap.Config.ARGB_8888
        else
          Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
        drawable.getIntrinsicHeight())
    drawable.draw(canvas)
    return bitmap
  } else {
    return null
  }
}

fun bitmapToByte(bitmap: Bitmap): ByteArray {
  val baos = ByteArrayOutputStream()
  bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
  return baos.toByteArray()
}


fun rotate(bitmap: Bitmap?, f: Float): Bitmap? {
  if (bitmap == null) return null
  val matrix = Matrix()
  matrix.setRotate(f)
  return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

/**
 *
 */
fun bitmap2File(bitmap: Bitmap, fileName: String): Boolean {
  val file = File(fileName)
  if (!file.parentFile.exists()) file.parentFile.mkdirs()
  try {
    val bos = BufferedOutputStream(FileOutputStream(file))
    bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos)
    bos.flush()
    bos.close()
  } catch (e: Exception) {
    e.printStackTrace()
    return false
  }

  return true
}

fun getSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
  val height = options.outHeight
  val width = options.outWidth
  var inSampleSize = 1

  if (height > reqHeight || width > reqWidth) {

    // Calculate ratios of height and width to requested height and
    // width
    val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
    val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

    // Choose the smallest ratio as inSampleSize value, this will
    // guarantee
    // a final image with both dimensions larger than or equal to the
    // requested height and width.
    inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
  }

  return inSampleSize
}

fun getSmallBitmap(filePath: String): Bitmap {
  val options = BitmapFactory.Options()
  options.inJustDecodeBounds = true
  BitmapFactory.decodeFile(filePath, options)

  // Calculate inSampleSize
  options.inSampleSize = getSampleSize(options, 480, 800)

  // Decode bitmap with inSampleSize set
  options.inJustDecodeBounds = false


  //        bitmap.getByteCount()
  //        bitmap.getAllocationByteCount()
  return BitmapFactory.decodeFile(filePath, options)
}

/**
 * @param filePath 文件位置
 * @param type     JPG PNG ETC..
 */
fun getCompressBitmap(filePath: String, type: String): String {
  return bitmapTo64(getSmallBitmap(filePath), type)
}

/**
 * 直接返回  BASE64 数据
 */
fun getCompressBitmap(filePath: String): String {
  val bitmap = getSmallBitmap(filePath)
  val bs = bitmapToByte(bitmap)
  bitmap.recycle()
  //        | Base64.CRLF
  return Base64.encodeToString(bs, Base64.NO_WRAP)
}


fun bitmapTo64(bitmap: Bitmap, type: String): String {
  val bs = bitmapToByte(bitmap)
  bitmap.recycle()
  //        | Base64.CRLF
  val res = Base64.encodeToString(bs, Base64.NO_WRAP)
  return String.format("data:image/%s;base64,%s", type, res)
}

fun getByteCount(bitmap: Bitmap): Int {

  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    bitmap.allocationByteCount
  } else
    bitmap.byteCount
}

/**
 * 返回 默认控制在 100KB 以内
 */
fun getLimitByte(bitmap: Bitmap): ByteArray {

  val bos = ByteArrayOutputStream()
  val totalCount = 1024 * 100
  val count = getByteCount(bitmap)

  val times = count / totalCount
  var options = 100 - times / 2

  bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos)
  while (bos.toByteArray().size > totalCount) {
    options -= 5
    bos.reset()
    bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos)
  }
  bitmap.recycle()
  return bos.toByteArray()
}


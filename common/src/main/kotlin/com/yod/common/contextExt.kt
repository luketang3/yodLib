package com.yod.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_SERVICES
import android.content.pm.ServiceInfo
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v4.content.LocalBroadcastManager
import android.telephony.TelephonyManager
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import java.io.File

/**
 * context 相关
 * Created by tangJ on 2017/11/13
 */

fun Context.dp2px(dp: Float): Int {
  val displayMetrics = resources.displayMetrics
  return (displayMetrics.density * dp + 0.5).toInt()
}

/**
 * dp to float
 */
fun Context.dp2R(dp: Float): Float {
  val displayMetrics = resources.displayMetrics
  return displayMetrics.density * dp
}

fun Context.toSp(sp: Float): Float {
  val displayMetrics = resources.displayMetrics
  return displayMetrics.scaledDensity * sp
}

/**
 * 找不到资源 返回 0
 */
fun Context.targetRes(name: String, defType: String): Int =
  resources.getIdentifier(name, defType, packageName)

/**
 *
 */
fun Context.targetResString(name: String): Int {
  return targetRes(name, "string")
}

fun Context.targetResDrawable(name: String): Int {
  return targetRes(name, "drawable")
}

fun Context.targetResArray(name: String): Int {
  return targetRes(name, "array")
}

/**
 * 拷贝文字
 */
fun Context.copyText(text: CharSequence, success: CharSequence = "拷贝成功") {
  val clipData = ClipData.newPlainText("copy", text)
  val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  manager.primaryClip = clipData
  Toast.makeText(this, success, Toast.LENGTH_SHORT).show()
}

/**
 * 获取 系统 dimen
 */
fun Context.getNativeDimen(name: String): Int {
  val resId = resources.getIdentifier(name, "dimen", "android")
  return if (resId == 0) -1 else resources.getDimensionPixelSize(resId)
}

/**
 * 当前包信息
 */
fun Context.getPackage() = packageManager.getPackageInfo(packageName, 0)

/**
 * 获取当前 color
 */
fun Context.getResColor(color: Int): Int {
  if (VERSION.SDK_INT >= VERSION_CODES.M) {
    return resources.getColor(color, theme)
  }
  @Suppress("DEPRECATION")
  return resources.getColor(color)
}

/**
 * 获取 uri 的绝对路径
 */
fun Context.getUriPath(uri: Uri?): String? {
  var res: String? = null
  if (uri != null) {
    val schema = uri.scheme
    if ("file" == schema) { //file
      res = uri.path
    } else if ("content" == schema) { //content
      val proj = arrayOf(Media.DATA)
      @SuppressLint("Recycle")
      val cursor = contentResolver.query(uri, proj, null, null, null)
      cursor?.apply {
        if (moveToFirst()) {
          val column_index = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
          res = getString(column_index)
        }
        close()
      }
    }
  }
  return res
}

/**
 * 拨打电话
 */
fun Context.dial(num: String) {
  val intent = Intent(Intent.ACTION_DIAL)
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  val data = Uri.parse("tel:" + num)
  intent.data = data
  startActivity(intent)
}

/**
 * 安装 apk
 */
fun Context.installApk(path: String, authority: String) {
  val intent = Intent(Intent.ACTION_VIEW)
  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
  val uri: Uri
  uri = if (isNougat()) {
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    FileProvider.getUriForFile(this, authority, File(path))
  } else {
    Uri.fromFile(File(path))
  }
  intent.setDataAndType(uri, "application/vnd.android.package-archive")
  startActivity(intent)
}

/**
 * 常亮
 */
fun Activity.keepBright() {
  window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

/**
 * 打开浏览器
 */
fun Context.openUrl(url: String) {
  val intent = Intent()
  intent.action = "android.intent.action.VIEW"
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  val uri = Uri.parse(url)
  intent.data = uri
  startActivity(intent)
}

/**
 * 全屏
 */
fun Activity.showFullScreen(show: Boolean) {

  val attrs = window.attributes
  if (show) {
    attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
  } else {
    attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
  }
  window.attributes = attrs
}

fun View.clickFocus() {
  this.isClickable = true
  this.isFocusableInTouchMode = true

  this.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
    if (hasFocus) v.showInput(false)
  }
}

fun Fragment.clickFocus() {
  view?.clickFocus()
}

fun Fragment.showInput(show: Boolean) {
  view?.showInput(show)
}

/**
 * 更新图库识别
 */
fun Context.notifyPhotoScan(file: File) {
  val uri = Uri.fromFile(file)
  // 通知图库更新
  val scannerIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
  sendBroadcast(scannerIntent)
}

/**
 * single task model 启动 activity
 */
inline fun <reified T> Context.launchSingle(reload: Boolean = false, action: String? = null) {
  val intent = Intent(this, T::class.java)
  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
  if (!reload) {
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
  }
  //send action
  action?.apply { intent.action = this }
  startActivity(intent)
}

@SuppressLint("MissingPermission")
    /**
     * 网络是否 可连接
     *  <uses-permission android:name="android.permission.INTERNET"/>
     *  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     */
fun Context.isNetAvailable(): Boolean {
  val connectivity = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val networkInfo = connectivity.activeNetworkInfo
  return networkInfo?.isConnected == true && networkInfo.state == NetworkInfo.State.CONNECTED
}

/**
 * wifi 是否连接
 */
@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
fun Context.isWifiConnect(): Boolean {
  val connectivity = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val networkInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
  return networkInfo?.isConnected == true
}

/**
 * get imei
 */
@SuppressLint("MissingPermission")
fun Context.getImei(): String {
  val teleManager = getSystemService(Application.TELEPHONY_SERVICE) as TelephonyManager
  return teleManager.deviceId?.takeIf { it.isNotEmpty() } ?: ""
}

/**
 * before start activity
 */
inline fun Context.checkActivity(intent: Intent, callback: BoolCallback) {
  val ok = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null
  callback(ok)
}

/**
 * 发送本地广播
 * 防止敏感信息 泄漏
 */
fun Context.sendLocalBroadcast(intent: Intent) =
  LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

/**
 * register local receiver
 */
fun Context.registerLocalReceiver(receiver: BroadcastReceiver, filter: IntentFilter) =
  LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)

/**
 * unregister local receiver
 */
fun Context.unregisterLocalReceiver(receiver: BroadcastReceiver) =
  LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)

/**
 * 判断是否是指定的 service process
 * https://github.com/square/leakcanary.git
 * com.squareup.leakcanary.internal.LeakCanaryInternals.java
 */
fun Context.isInServiceProcess(serviceClass: Class<*>): Boolean {
  val packageManager = packageManager
  val packageInfo: PackageInfo
  try {
    packageInfo = packageManager.getPackageInfo(packageName, GET_SERVICES);
  } catch (e: Exception) {
//      CanaryLog.d(e, "Could not get package info for %s", context.getPackageName());
    return false
  }
  val mainProcess = packageInfo.applicationInfo.processName

  val component = ComponentName(this, serviceClass)
  val serviceInfo: ServiceInfo
  try {
    serviceInfo = packageManager.getServiceInfo(component, 0)
  } catch (ignored: PackageManager.NameNotFoundException) {
    // Service is disabled.
    return false
  }

  if (serviceInfo.processName == mainProcess) {
//      CanaryLog.d("Did not expect service %s to run in main process %s", serviceClass, mainProcess)
    // Technically we are in the service process, but we're not in the service dedicated process.
    return false
  }

  val myPid = android.os.Process.myPid()
  val activityManager =
    getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
  var myProcess: ActivityManager.RunningAppProcessInfo? = null
  val runningProcesses = activityManager.runningAppProcesses
  if (runningProcesses != null) {
    for (process in runningProcesses) {
      if (process.pid == myPid) {
        myProcess = process
        break
      }
    }
  }
  if (myProcess == null) {
//      CanaryLog.d("Could not find running process for %d", myPid)
    return false
  }
  return myProcess.processName.equals(serviceInfo.processName)
}


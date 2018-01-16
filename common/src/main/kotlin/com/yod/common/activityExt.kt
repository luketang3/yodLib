package com.yod.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * app compat 指定扩展
 * Created by tangJ on 2017/11/16
 */

fun AppCompatActivity.replaceSpecLayout(bundle: Bundle?, frameId: Int, fragment: Fragment,
    tag: String? = null) {
  if (bundle == null)
    supportFragmentManager.findFragmentById(frameId)
        ?: supportFragmentManager
        .beginTransaction()
        .replace(frameId, fragment, tag)
        .commit()
}
package com.hoicham.orc

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.hoicham.orc.core.utils.SharePrefUtils
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : MultiDexApplication() {
    companion object {
        private const val TAG = "MyApplication"
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        Timber.tag(TAG).d("onCreate()")

        SharePrefUtils.init(this)
    }
}
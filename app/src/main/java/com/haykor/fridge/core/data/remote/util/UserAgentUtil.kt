package com.haykor.fridge.core.data.remote.util

import android.os.Build
import com.haykor.fridge.BuildConfig

object UserAgentUtil {
    fun getUserAgent(): String {
        return "${BuildConfig.APPLICATION_ID}/${BuildConfig.VERSION_NAME} (Android:${Build.VERSION.SDK_INT})"
    }
}
package com.yasser.nearby.core.utils

import android.content.Context

fun Context.getConfigString(identifier: String): String {
    return this.getString(this.resources.getIdentifier(identifier, "string", this.packageName))
}

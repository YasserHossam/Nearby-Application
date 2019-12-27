package com.yasser.nearby.ui.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(
    parentView: View,
    message: String,
    actionButtonText: String = "OK",
    length: Int = Snackbar.LENGTH_LONG,
    action: (() -> Unit)? = null
) {

    Snackbar.make(parentView, message, length)
        .apply {
            action?.let { actionListener ->
                setAction(actionButtonText) {
                    actionListener.invoke()
                }
            }
        }.show()
}
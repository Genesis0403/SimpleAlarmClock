package com.epam.simplealarmclock

import android.content.Context
import android.widget.Toast

object Utils {

    const val PLAY_ALARM_REQUEST = 1
    const val FIVE_MINUTES_REQUEST = 2
    const val EXTRA_ALARM_TIME = "EXTRA_ALARM_TIME"
    const val EXTRA_HOURS = "EXTRA_HOURS"
    const val EXTRA_MINUTES = "EXTRA_MINUTES"
    const val PLAY_ALARM_ACTION = "PLAY_ALARM_ACTION"
    const val FIVE_MINUTES_ACTION = "FIVE_MINUTES_ACTION"

    fun makeToast(context: Context?, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

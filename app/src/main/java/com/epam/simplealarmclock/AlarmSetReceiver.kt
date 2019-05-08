package com.epam.simplealarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmSetReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { intent ->
            val time = AlarmTime(
                intent.getIntExtra(Utils.EXTRA_HOURS, -1),
                intent.getIntExtra(Utils.EXTRA_MINUTES, -1)
            )
            Log.d(TAG, intent.action)
            when (intent.action) {
                Utils.PLAY_ALARM_ACTION -> {
                    Utils.makeToast(context, "Wake up my darling! Current time: ${time.hour}:${time.minute}")
                }
                Utils.FIVE_MINUTES_ACTION -> {
                    Utils.makeToast(context, "Five minutes or less remain! Current time: ${time.hour}:${time.minute}")
                }
            }
        }
    }

    companion object {
        private const val TAG = "ALARM SET RECEIVER"
    }
}
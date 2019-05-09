package com.epam.simplealarmclock.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.epam.simplealarmclock.model.Alarm

class RebootService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        val prefs = getSharedPreferences(Alarm.REBOOT_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        if (prefs.getBoolean(Alarm.IS_ALARM_RUNNING, false)) {
            Alarm.setAlarmClock(this, Alarm.getTime(this))
        }
    }

    companion object {
        private const val JOB_ID = 1

        fun newInstance(context: Context, intent: Intent) =
            RebootService().apply {
                enqueueWork(context, RebootService::class.java,
                    JOB_ID, intent)
            }
    }
}

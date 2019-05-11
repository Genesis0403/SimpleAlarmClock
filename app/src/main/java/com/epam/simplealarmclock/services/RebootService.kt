package com.epam.simplealarmclock.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.epam.simplealarmclock.model.Alarm

/**
 * Service which starts work after device boot.
 *
 * @author Vlad Korotkevich
 */

class RebootService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        val prefs = getSharedPreferences(Alarm.REBOOT_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        if (prefs.getBoolean(Alarm.IS_ALARM_RUNNING, DEFAULT_IS_RUNNING)) {
            Alarm.setAlarmClock(this, Alarm.getTime(this))
        }
    }

    fun enqueueWork(context: Context, intent: Intent) {
        enqueueWork(context, RebootService::class.java, JOB_ID, intent)
    }

    companion object {
        private const val JOB_ID = 1
        private const val DEFAULT_IS_RUNNING = false

        fun newInstance(context: Context, intent: Intent) =
            RebootService().apply {
                enqueueWork(context, intent)
            }
    }
}

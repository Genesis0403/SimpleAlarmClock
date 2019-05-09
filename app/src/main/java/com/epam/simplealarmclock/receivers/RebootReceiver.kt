package com.epam.simplealarmclock.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.epam.simplealarmclock.services.RebootService

class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            RebootService.newInstance(context, intent)
        }
    }

}

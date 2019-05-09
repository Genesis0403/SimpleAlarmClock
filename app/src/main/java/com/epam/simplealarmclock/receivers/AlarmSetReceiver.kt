package com.epam.simplealarmclock.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.epam.simplealarmclock.AlarmActivity
import com.epam.simplealarmclock.model.AlarmTime
import com.epam.simplealarmclock.R
import com.epam.simplealarmclock.model.Alarm
import java.lang.IllegalArgumentException

class AlarmSetReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val time = AlarmTime(
                it.getIntExtra(Alarm.EXTRA_HOURS, -1),
                it.getIntExtra(Alarm.EXTRA_MINUTES, -1)
            )
            Log.d(TAG, it.action)
            when (it.action) {
                Alarm.PLAY_ALARM_ACTION -> {
                    context?.startActivity(Intent(context, AlarmActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                    //Alarm.makeToast(context, "Wake up my darling! Current time: ${time.hour}:${time.minute}")
                }
                Alarm.FIVE_MINUTES_ACTION -> {
                    createNotification(context,
                        context?.getString(R.string.notification_title),
                        context?.getString(R.string.five_minutes_left))
                } else -> IllegalArgumentException("${it.action} is not registered!")
            }
        }
    }

    private fun createNotification(context: Context?, title: String?, content: String?) {
        context?.let {
            createNotificationChannel(it,
                CHANNEL_NAME,
                CHANNEL_DESCRIPTION
            )
            val builder = createNotificationBuilder(it, title, content)
            with(NotificationManagerCompat.from(it)) {
                notify(FIVE_MINUTES_NOTIFICATION, builder.build())
            }
        }
    }

    private fun createNotificationBuilder(
        context: Context,
        title: String?,
        content: String?
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
            setContentTitle(title)
            setContentText(content)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

    private fun createNotificationChannel(
        context: Context,
        name: String,
        descriptionText: String
    ) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) return
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val TAG = "ALARM SET RECEIVER"
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val CHANNEL_NAME = "Alarm Clock"
        private const val CHANNEL_DESCRIPTION = "Alarm Clock"
        private const val FIVE_MINUTES_NOTIFICATION = 1
    }
}

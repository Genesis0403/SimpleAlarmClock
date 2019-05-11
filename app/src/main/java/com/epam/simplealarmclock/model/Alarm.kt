package com.epam.simplealarmclock.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.edit
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.epam.simplealarmclock.receivers.AlarmSetReceiver
import java.util.*
import kotlin.reflect.KClass

/**
 *  Singleton Alarm class.
 *  All methods are responsible to start/cancel alarm clock
 *  or get PendingIntents.
 *
 * @author Vlad Korotkevich
 */

object Alarm {

    const val PLAY_ALARM_REQUEST = 1
    const val FIVE_MINUTES_REQUEST = 2

    const val EXTRA_HOURS = "EXTRA_HOURS"
    const val EXTRA_MINUTES = "EXTRA_MINUTES"

    const val PLAY_ALARM_ACTION = "PLAY_ALARM_ACTION"
    const val FIVE_MINUTES_ACTION = "FIVE_MINUTES_ACTION"

    const val IS_ALARM_RUNNING = "IS_ALARM_RUNNING"
    const val REBOOT_SHARED_PREFERENCES = "REBOOT_SHARED_PREFERENCES"

    private const val FIVE_MINUTES = 5
    private const val ONE_HOUR_IN_MINUTES = 60

    fun setAlarmClock(context: Context, time: AlarmTime) {

        context.getSharedPreferences(REBOOT_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit {
            putInt(EXTRA_HOURS, time.hour)
            putInt(EXTRA_MINUTES, time.minute)
            putBoolean(IS_ALARM_RUNNING, true)
            commit()
        }

        setAlarm(
            context,
            AlarmSetReceiver::class,
            PLAY_ALARM_REQUEST,
            PLAY_ALARM_ACTION,
            time,
            getMillis(time.hour, time.minute, 0, 0)
        )

        val timeFiveMinutesBack = if (time.minute < FIVE_MINUTES) {
            Pair(time.hour - 1, ONE_HOUR_IN_MINUTES - FIVE_MINUTES + time.minute)
        } else {
            Pair(time.hour, time.minute - FIVE_MINUTES)
        }

        if (didFiveMinutesPassed(context, timeFiveMinutesBack)) return

        setAlarm(
            context,
            AlarmSetReceiver::class,
            FIVE_MINUTES_REQUEST,
            FIVE_MINUTES_ACTION,
            time,
            getMillis(timeFiveMinutesBack.first, timeFiveMinutesBack.second, 0, 0)
        )
    }

    private fun didFiveMinutesPassed(
        context: Context,
        timeFiveMinutesBack: Pair<Int, Int>
    ): Boolean {
        val fiveMinutesBackMillis =
            getMillis(timeFiveMinutesBack.first, timeFiveMinutesBack.second, 0, 0)
        if (fiveMinutesBackMillis - System.currentTimeMillis() < 0) {
            val intent = Intent(context, AlarmSetReceiver::class.java).apply {
                putExtra(EXTRA_HOURS, timeFiveMinutesBack.first)
                putExtra(EXTRA_MINUTES, timeFiveMinutesBack.second)
                action = FIVE_MINUTES_ACTION
            }
            //TODO read more about LocalBroadcastManager
            //LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            context.sendBroadcast(intent)
            return true
        }
        return false
    }

    fun setAlarm(
        context: Context,
        receiver: KClass<*>,
        requestCode: Int,
        action: String,
        time: AlarmTime,
        timeInMillis: Long
    ): PendingIntent {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pIntent = getPendingIntent(context, receiver, requestCode) {
            it.action = action
            it.putExtra(EXTRA_HOURS, time.hour)
            it.putExtra(EXTRA_MINUTES, time.minute)
        }

        setAlarmManager(
            alarmManager,
            timeInMillis,
            pIntent
        )

        return pIntent
    }

    fun getPendingIntent(
        context: Context,
        receiver: KClass<*>,
        requestCode: Int,
        init: (Intent) -> Intent
    ): PendingIntent {
        val intent = init.invoke(Intent(context, receiver.java))
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun setAlarmManager(
        alarmManager: AlarmManager?,
        playTime: Long,
        pendingIntent: PendingIntent?
    ) {
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            playTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun getMillis(hour: Int, minute: Int, second: Int, ms: Int): Long =
        Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, second)
            set(Calendar.MILLISECOND, ms)
        }.timeInMillis

    fun getTime(context: Context): AlarmTime =
        with(context.getSharedPreferences(REBOOT_SHARED_PREFERENCES, Context.MODE_PRIVATE)) {
            AlarmTime(
                getInt(EXTRA_HOURS, -1),
                getInt(EXTRA_MINUTES, -1)
            )
        }

    fun cancelAlarmClock(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val time = getTime(context)

        val startAlarmIntent = getPendingIntent(
            context,
            AlarmSetReceiver::class,
            PLAY_ALARM_REQUEST
        ) {
            it.action = PLAY_ALARM_ACTION
            it.putExtra(EXTRA_HOURS, time.hour)
            it.putExtra(EXTRA_MINUTES, time.minute)
        }

        val fiveMinutesAlarmIntent = getPendingIntent(
            context,
            AlarmSetReceiver::class,
            FIVE_MINUTES_REQUEST
        ) {
            it.action = FIVE_MINUTES_ACTION
            it.putExtra(EXTRA_HOURS, time.hour)
            it.putExtra(EXTRA_MINUTES, time.minute)
        }

        alarmManager.cancel(startAlarmIntent)
        alarmManager.cancel(fiveMinutesAlarmIntent)

        context.getSharedPreferences(REBOOT_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit {
            clear()
            putBoolean(IS_ALARM_RUNNING, false)
            commit()
        }
    }
}

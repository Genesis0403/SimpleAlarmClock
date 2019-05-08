package com.epam.simplealarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Duration
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var startAlarmIntent: PendingIntent? = null
    private var fiveMinutesAlarmIntent: PendingIntent? = null
    private var alarmManager: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmClock.setIs24HourView(true)

        setAlarmButton.setOnClickListener {
            setAlarmClock()
            Utils.makeToast(this, "Alarm clock is being set!")
        }

        cancelButton.setOnClickListener {
            cancelAlarmClock()
            Utils.makeToast(this, "Alarm clock is being canceled!")
        }
    }

    private fun cancelAlarmClock() {
        alarmManager?.cancel(startAlarmIntent)
        alarmManager?.cancel(fiveMinutesAlarmIntent)
    }

    private fun setAlarmClock() {
        val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlarmTime(alarmClock.hour, alarmClock.minute)
        } else {
            AlarmTime(alarmClock.currentHour, alarmClock.currentMinute)
        }
        //TODO add Intent init function
        val playAlarmIntent = Intent(this, AlarmSetReceiver::class.java).apply {
            action = Utils.PLAY_ALARM_ACTION
            putExtra(Utils.EXTRA_HOURS, time.hour)
            putExtra(Utils.EXTRA_MINUTES, time.minute)
        }

        startAlarmIntent = initPendingIntent(playAlarmIntent, Utils.PLAY_ALARM_REQUEST)

        val fiveMinutesIntent = Intent(this, AlarmSetReceiver::class.java).apply {
            action = Utils.FIVE_MINUTES_ACTION
            putExtra(Utils.EXTRA_HOURS, time.hour)
            putExtra(Utils.EXTRA_MINUTES, time.minute)
        }
        fiveMinutesAlarmIntent = initPendingIntent(fiveMinutesIntent, Utils.FIVE_MINUTES_REQUEST)


        //TODO move calendar init somewhere...
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, time.hour)
        calendar.set(Calendar.MINUTE, time.minute)
        calendar.set(Calendar.SECOND, 0)
        val timeInMillis = calendar.timeInMillis

        calendar.set(Calendar.HOUR, time.hour)
        calendar.set(Calendar.MINUTE, time.minute - 5)
        calendar.set(Calendar.SECOND, 0)
        val fiveMinutesLess = calendar.timeInMillis

        setAlarmManager(
            alarmManager,
            timeInMillis,
            startAlarmIntent
        )
        setAlarmManager(
            alarmManager,
            fiveMinutesLess,
            fiveMinutesAlarmIntent
        )
    }

    private fun initPendingIntent(intent: Intent, requestCode: Int) =
        PendingIntent.getBroadcast(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun setAlarmManager(
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

    companion object {
        private const val TAG = "MAIN ACTIVITY"
    }
}

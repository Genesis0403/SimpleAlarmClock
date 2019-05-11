package com.epam.simplealarmclock

import com.epam.simplealarmclock.receivers.AlarmSetReceiver

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.epam.simplealarmclock.model.Alarm
import kotlinx.android.synthetic.main.activity_alarm.*
import java.lang.StringBuilder

/**
 * Activity which starts when alarm invoked.
 *
 * @see [AlarmSetReceiver]
 *
 * @author Vlad Koroktevich
 */

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        alarmTime.text = constructTime()

        cancelButton.setOnClickListener {
            cancelButtonListener()
        }
    }

    private fun constructTime(): String {
        val alarm = Alarm.getTime(this)
        return StringBuilder("").apply {
            if (alarm.hour < 10) {
                append("0")
            }
            append(alarm.hour.toString())
            append(":")
            if (alarm.minute < 10) {
                append("0")
            }
            append(alarm.minute)
        }.toString()
    }

    private fun cancelButtonListener() {
        Alarm.cancelAlarmClock(this)
        Toast.makeText(this, getString(R.string.alarm_cancaled), Toast.LENGTH_SHORT).show()
        finish()
    }
}
package com.epam.simplealarmclock

import com.epam.simplealarmclock.receivers.AlarmSetReceiver

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.epam.simplealarmclock.model.Alarm
import kotlinx.android.synthetic.main.activity_alarm.*

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
        var time = ""
        if (alarm.hour < 10) {
            time += "0"
        }
        time += alarm.hour.toString() + ":"
        if (alarm.minute < 10) {
            time += 0
        }
        time += alarm.minute
        return time
    }

    private fun cancelButtonListener() {
        Alarm.cancelAlarmClock(this)
        Toast.makeText(this, getString(R.string.alarm_cancaled), Toast.LENGTH_SHORT).show()
        finish()
    }
}
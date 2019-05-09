package com.epam.simplealarmclock

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.epam.simplealarmclock.model.Alarm
import com.epam.simplealarmclock.model.AlarmTime
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmClock.setIs24HourView(true)

        setAlarmButton.setOnClickListener {
            val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AlarmTime(alarmClock.hour, alarmClock.minute)
            } else {
                AlarmTime(alarmClock.currentHour, alarmClock.currentMinute)
            }
            Alarm.setAlarmClock(this, time)
            Toast.makeText(this, getString(R.string.alarm_set), Toast.LENGTH_SHORT).show()
        }

        cancelButton.setOnClickListener {
            Alarm.cancelAlarmClock(this)
            Toast.makeText(this, getString(R.string.alarm_cancaled), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "MAIN ACTIVITY"
    }
}

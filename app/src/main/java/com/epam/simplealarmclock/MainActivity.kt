package com.epam.simplealarmclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmClock.setOnTimeChangedListener { _, hourOfDay, minute ->
            Toast.makeText(this, "Time has changed to $hourOfDay:$minute.", Toast.LENGTH_LONG).show()
        }

        setAlarmButton.setOnClickListener {
            Toast.makeText(this, "Clicked set alarm", Toast.LENGTH_SHORT).show()
        }

        cancelButton.setOnClickListener {
            Toast.makeText(this, "Clicked cancel alarm", Toast.LENGTH_SHORT).show()
        }
    }
}

package com.epam.simplealarmclock

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.epam.simplealarmclock.model.Alarm
import com.epam.simplealarmclock.model.AlarmClock
import kotlinx.android.synthetic.main.activity_main.*
import java.util.zip.Inflater

/**
 * Main Activity with alarm time picker.
 *
 * @see [Alarm]
 * @see [AlarmClock]
 *
 * @author Vlad Korotkevich
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainActivity, AlarmFragment.newInstance())
            commit()
        }
    }
}

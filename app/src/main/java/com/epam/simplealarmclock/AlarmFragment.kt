package com.epam.simplealarmclock

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.epam.simplealarmclock.model.Alarm
import com.epam.simplealarmclock.model.AlarmClock

/**
 * Fragment which responsible for interactions with [Alarm].
 *
 * @author Vlad Korotkevich
 */

class AlarmFragment : Fragment() {

    private var ringtoneUri: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        setHasOptionsMenu(HAS_OPTIONS_MENU)

        val fragment = inflater.inflate(R.layout.alarm_fragment, container, false)

        val timePicker = fragment.findViewById<TimePicker>(R.id.alarmPicker)
        timePicker.setIs24HourView(true)

        val setAlarmButton = fragment.findViewById<Button>(R.id.setAlarmButton)
        setAlarmButton.setOnClickListener {
            setAlarmButtonListenerInit(timePicker)
        }

        val cancelButton = fragment.findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            Alarm.cancelAlarmClock(context)
            Toast.makeText(context, getString(R.string.alarm_cancaled), Toast.LENGTH_SHORT).show()
        }

        return fragment
    }

    private fun setAlarmButtonListenerInit(timePicker: TimePicker) {
        val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlarmClock(timePicker.hour, timePicker.minute, ringtoneUri)
        } else {
            AlarmClock(timePicker.currentHour, timePicker.currentMinute, ringtoneUri)
        }
        Alarm.setAlarmClock(context, time)
        Toast.makeText(context, getString(R.string.alarm_set), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.alarm_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.chooseRingtone -> {
                chooseRingtone()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun chooseRingtone() {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.select_ringtone))
            putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
        }
        startActivityForResult(intent, PICK_RINGTONE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == PICK_RINGTONE_REQUEST) {
                val uri = data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) as? Uri
                ringtoneUri = uri.toString()
            }
        }
    }

    companion object {
        fun newInstance() = AlarmFragment()

        private const val PICK_RINGTONE_REQUEST = 1
        private const val HAS_OPTIONS_MENU = true
    }
}
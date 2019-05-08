package com.epam.simplealarmclock

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.TimeUnit

@Parcelize
data class AlarmTime(
    val hour: Int,
    val minute: Int
) : Parcelable

fun AlarmTime.milliSeconds(): Long =
    TimeUnit.HOURS.toMillis(hour.toLong()) +
            TimeUnit.MINUTES.toMillis(minute.toLong())
package com.epam.simplealarmclock.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlarmTime(
    val hour: Int,
    val minute: Int
) : Parcelable

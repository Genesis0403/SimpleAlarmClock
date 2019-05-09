package com.epam.simplealarmclock.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data class which represents alarm time.
 *
 * @author Vlad Korotkevich
 */

@Parcelize
data class AlarmTime(
    val hour: Int,
    val minute: Int
) : Parcelable

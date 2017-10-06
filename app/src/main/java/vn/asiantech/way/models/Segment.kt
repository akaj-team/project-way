package vn.asiantech.way.models

import android.location.Location
import java.util.*

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 29/09/2017.
 */
data class Segment(val id: String, val userId: String,
                   val startAt: Date, val startLocation: Location,
                   val endAt: Date, val endLocation: Location,
                   val distance: Float, val duration: Double)

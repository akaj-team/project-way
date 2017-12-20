package vn.asiantech.way.ui.custom

import android.view.animation.Animation
import android.view.animation.Transformation
import com.google.android.gms.maps.model.GroundOverlay

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 05/12/2017.
 */
class RadiusAnimation(private val groundOverlay: GroundOverlay?) : Animation() {
    companion object {
        private const val GROUND_OVERLAY_RADIUS = 400
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        groundOverlay?.setDimensions((GROUND_OVERLAY_RADIUS * interpolatedTime))
        groundOverlay?.transparency = interpolatedTime
    }
}

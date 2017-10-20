package vn.asiantech.way.ui.custom

import android.view.animation.Animation
import android.view.animation.Transformation
import com.google.android.gms.maps.model.GroundOverlay

/**
 * Custom Ground overlay
 * Created by haingoq on 18/10/2017.
 */
class RadiusAnimation(private val mGroundOverlay: GroundOverlay?) : Animation() {
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        mGroundOverlay?.setDimensions((400 * interpolatedTime))
        mGroundOverlay?.transparency = interpolatedTime
    }
}

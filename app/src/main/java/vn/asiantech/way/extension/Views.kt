package vn.asiantech.way.extension

import android.content.Context
import android.view.View
import android.view.ViewManager
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.maps.MapView
import org.jetbrains.anko.custom.ankoView
import vn.asiantech.way.ui.custom.BottomButtonCard

/**
 * Created by haingoq on 12/10/2017.
 */

/**
 * Extension method to hide keyboard.
 */
fun View.hideKeyboard(context: Context) {
    val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

internal fun ViewManager.mapView(init: MapView.() -> Unit): MapView {
    return ankoView({ MapView(it) }, 0, init)
}

internal fun ViewManager.rippleView(
        init: com.hypertrack.lib.internal.consumer.view.RippleView.() -> Unit):
        com.hypertrack.lib.internal.consumer.view.RippleView {
    return ankoView(
            { com.hypertrack.lib.internal.consumer.view.RippleView(it, null) }, 0, init)
}

internal fun ViewManager.wrappingViewPager(
        init: com.hypertrack.lib.internal.consumer.view.WrappingViewPager.() -> Unit):
        com.hypertrack.lib.internal.consumer.view.WrappingViewPager {
    return ankoView(
            { com.hypertrack.lib.internal.consumer.view.WrappingViewPager(it) }, 0, init)
}

internal fun ViewManager.bottomCard(init: BottomButtonCard.() -> Unit): BottomButtonCard {
    return ankoView({ BottomButtonCard(it) }, 0, init)
}

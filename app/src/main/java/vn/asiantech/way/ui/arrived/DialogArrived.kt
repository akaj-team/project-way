package vn.asiantech.way.ui.arrived

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.*
import kotlinx.android.synthetic.main.dialog_show_arrived_infor.view.*
import kotlinx.android.synthetic.main.show_detail_arrived.view.*
import vn.asiantech.way.R
import vn.asiantech.way.extension.makeAverageSpeed
import vn.asiantech.way.extension.makeDistance
import vn.asiantech.way.extension.makeDuration

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 26/09/2017.
 */
internal class DialogArrived : DialogFragment() {
    companion object {
        private const val TYPE_TIME = "time"
        private const val TYPE_DISTANCE = "distance"
        private const val TYPE_AVERAGE_SPEED = "average speed"
        private const val TYPE_UNIT_DIALOG_HEIGHT = 5
        private const val TYPE_DIALOG_MARGIN_WIDTH = 80

        /**
         *  Create new instance
         *  @param time: Long, time to show on dialog
         *  @param distance: Double , distance to show on dialog
         *  @averageSpeed: Double, averageSpeed to show on dialog
         *  @return Dialog to show detail user tracked
         */
        internal fun newInstance(time: String?, distance: String?)
                : DialogArrived {
            val dialogShowArrived = DialogArrived()
            val bundle = Bundle()
            bundle.putString(TYPE_TIME, time)
            bundle.putString(TYPE_DISTANCE, distance)
            dialogShowArrived.arguments = bundle
            return dialogShowArrived
        }
    }

    private var mTime: String? = null
    private var mDistance: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTime = arguments?.getString(TYPE_TIME, "")
        mDistance = arguments?.getString(TYPE_DISTANCE, "")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater?.inflate(R.layout.dialog_show_arrived_infor, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        v?.tvDistance?.text = mDistance
        v?.tvTimeTotal?.text = mTime
        v?.btnDoneDialog?.setOnClickListener {
            dismiss()
            // Todo : Back to Confirm screen
        }
        return v
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        val windowParams = WindowManager.LayoutParams()
        windowParams.copyFrom(dialog.window.attributes)
        windowParams.height = height - height /
                TYPE_UNIT_DIALOG_HEIGHT
        windowParams.width = width - TYPE_DIALOG_MARGIN_WIDTH
        dialog.window.attributes = windowParams
    }
}

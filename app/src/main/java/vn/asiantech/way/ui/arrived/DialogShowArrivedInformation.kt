package vn.asiantech.way.ui.arrived

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.dialog_show_arrived_infor.*
import kotlinx.android.synthetic.main.show_detail_arrived.view.*
import vn.asiantech.way.R
import vn.asiantech.way.extension.makeAverageSpeed
import vn.asiantech.way.extension.makeDistance
import vn.asiantech.way.extension.makeDuration

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 26/09/2017.
 */
internal class DialogShowArrivedInformation : DialogFragment() {
    private var mTime: Long? = null
    private var mDistance: Double? = null
    private var mAverageSpeed: Double? = null

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
        internal fun newInstance(time: Long, distance: Double, averageSpeed: Double)
                : DialogShowArrivedInformation {
            val dialogShowArrived = DialogShowArrivedInformation()
            val bundle = Bundle()
            bundle.putLong(TYPE_TIME, time)
            bundle.putDouble(TYPE_DISTANCE, distance)
            bundle.putDouble(TYPE_AVERAGE_SPEED, averageSpeed)
            dialogShowArrived.arguments = bundle
            return dialogShowArrived
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTime = arguments?.getLong(TYPE_TIME, 0)
        mDistance = arguments?.getDouble(TYPE_DISTANCE, 0.0)
        mAverageSpeed = arguments?.getDouble(TYPE_AVERAGE_SPEED, 0.0)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater?.inflate(R.layout.dialog_show_arrived_infor, container, false)
        v?.llShowDetailData?.tvDistance?.text = mDistance?.makeDistance(context)
        v?.llShowDetailData?.tvTimeTotal?.text = mTime?.makeDuration(context)
        v?.llShowDetailData?.tvAverageSpeed?.text = mAverageSpeed?.makeAverageSpeed(context)
        btnDoneDialog?.setOnClickListener {
            dismiss()
        }
        return v
    }

    override fun onResume() {
        super.onResume()
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
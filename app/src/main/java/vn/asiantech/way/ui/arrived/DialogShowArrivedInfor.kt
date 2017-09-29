package vn.asiantech.way.ui.arrived

import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import kotlinx.android.synthetic.main.dialog_show_arrived_infor.*
import vn.asiantech.way.R

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 26/09/2017.
 */
class DialogShowArrivedInfor : DialogFragment() {
    private lateinit var mTime: String
    private lateinit var mDistance: String
    private lateinit var mAverageSpeed: String

    companion object {

        val TYPE_TIME = "time"
        val TYPE_DISTANCE = "distance"
        val TYPE_AVERAGE_SPEED = "average speed"

        private val mDialogShowArrived by lazy { DialogShowArrivedInfor() }

        fun newInstance(time: String, distance: String, speedAverage: String) :DialogShowArrivedInfor{
            val bundel = Bundle()
            bundel.putString(TYPE_TIME, time)
            bundel.putString(TYPE_DISTANCE, distance)
            bundel.putString(TYPE_AVERAGE_SPEED, speedAverage)
            mDialogShowArrived.arguments=bundel
            return mDialogShowArrived
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTime = savedInstanceState?.getString(TYPE_TIME) ?: "0"
        mDistance = savedInstanceState?.getString(TYPE_DISTANCE) ?: "0"
        mAverageSpeed = savedInstanceState?.getString(TYPE_AVERAGE_SPEED) ?: "0"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater?.inflate(R.layout.dialog_show_arrived_infor, container, false)
        tvTimeTotalDialog.text = mTime
        tvDistanceDialog.text = mDistance
        tvAverageSpeedDialog.text = mAverageSpeed
        return v
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        val windowParams = WindowManager.LayoutParams()
        windowParams.copyFrom(dialog.getWindow().getAttributes());
        windowParams.height = height - (height / 5)
        windowParams.width = width - 80
        dialog.window.attributes = windowParams
    }
}
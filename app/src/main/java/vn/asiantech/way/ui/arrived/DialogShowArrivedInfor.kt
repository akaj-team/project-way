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
import vn.asiantech.way.extensions.makeAverageSpeed
import vn.asiantech.way.extensions.makeDistance
import vn.asiantech.way.extensions.makeDuration

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 26/09/2017.
 */
class DialogShowArrivedInfor : DialogFragment() {
    private  var mTime: Long?=null
    private  var mDistance: Double?=null
    private  var mAverageSpeed: Double?=null

    companion object {
        val TYPE_TIME = "time"
        val TYPE_DISTANCE = "distance"
        val TYPE_AVERAGE_SPEED = "average speed"

        fun newInstance(time: Long, distance: Double, speedAverage: Double): DialogShowArrivedInfor {
            val dialogShowArrived:DialogShowArrivedInfor = DialogShowArrivedInfor()
            val bundle :Bundle= Bundle()
            bundle.putLong(TYPE_TIME, time)
            bundle.putDouble(TYPE_DISTANCE, distance)
            bundle.putDouble(TYPE_AVERAGE_SPEED, speedAverage)
            dialogShowArrived.arguments=bundle
            return dialogShowArrived
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTime = arguments?.getLong(TYPE_TIME,0)
        mDistance = arguments?.getDouble(TYPE_DISTANCE,0.0)
        mAverageSpeed = arguments?.getDouble(TYPE_AVERAGE_SPEED,0.0)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater?.inflate(R.layout.dialog_show_arrived_infor, container, false)
        v?.llShowDetailData?.tvDistance?.text = mDistance?.makeDistance()
        v?.llShowDetailData?.tvTimeTotal?.text = mTime?.makeDuration()
        v?.llShowDetailData?.tvAverageSpeed?.text = mAverageSpeed?.makeAverageSpeed()
        btnDoneDialog?.setOnClickListener {
            //TODO("for button Done")
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
        windowParams.height = height - (height / 5)
        windowParams.width = width - 80
        dialog.window.attributes = windowParams
    }
}

package vn.asiantech.way.ui.custom

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.*
import vn.asiantech.way.R
import vn.asiantech.way.utils.ScreenUtil

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 01/12/2017.
 */
internal class ArrivedDialog : DialogFragment() {
    companion object {
        private const val TYPE_TIME = "time"
        private const val TYPE_DISTANCE = "distance"
        private const val LAYOUT_WEIGHT_VERY_SMALL = 1f
        private const val LAYOUT_WEIGHT_SMALL = 2f
        private const val LAYOUT_WEIGHT_MEDIUM = 3f
        private const val LAYOUT_WEIGHT_HIGH = 6f

        /**
         *  Create new instance
         *  @param time: Long, time to show on dialog
         *  @param distance: Double , distance to show on dialog
         *  @return Dialog to show detail user tracked
         */
        internal fun newInstance(time: String?, distance: String?)
                : ArrivedDialog {
            val dialogShowArrived = ArrivedDialog()
            val bundle = Bundle()
            bundle.putString(TYPE_TIME, time)
            bundle.putString(TYPE_DISTANCE, distance)
            dialogShowArrived.arguments = bundle
            return dialogShowArrived
        }
    }

    private lateinit var ui: DialogUI
    private var mTime: String? = null
    private var mDistance: String? = null
    internal lateinit var btnDoneDialog: Button
    internal lateinit var tvTimeTotal: TextView
    internal lateinit var tvDistance: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTime = arguments?.getString(TYPE_TIME, "")
        mDistance = arguments?.getString(TYPE_DISTANCE, "")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = DialogUI()
        return ui.createView(AnkoContext.create(context, this))
    }

    /**
     * View show arrived dialog when tracking finish
     */
    inner class DialogUI : AnkoComponent<ArrivedDialog> {
        override fun createView(ui: AnkoContext<ArrivedDialog>) = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)

                textView(R.string.arrived_trip_summary) {
                    gravity = Gravity.CENTER
                    textColor = ContextCompat.getColor(context, R.color.colorBlack)
                    textSize = px2dip(dimen(R.dimen.text_very_large))
                }.lparams(matchParent, dip(0), LAYOUT_WEIGHT_VERY_SMALL)

                textView {
                    textColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                }.lparams(matchParent, dip(1))

                verticalLayout {
                    lparams(matchParent, dip(0), LAYOUT_WEIGHT_HIGH)

                    imageView(R.drawable.ic_user_arrived) {
                    }.lparams(dip(dimen(R.dimen.avatar_width)), dip(dimen(R.dimen.avatar_height))) {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }

                    textViewComplete(R.string.arrived_you).lparams {
                        gravity = Gravity.CENTER_HORIZONTAL
                        topMargin = dimen(R.dimen.margin_very_low)
                    }

                    textViewComplete(R.string.arrived_completed).lparams {
                        gravity = Gravity.CENTER_HORIZONTAL
                        bottomMargin = dimen(R.dimen.margin_medium)
                    }

                    linearLayout {
                        lparams(matchParent, wrapContent)
                        isBaselineAligned = false

                        verticalLayout {
                            lparams(dip(0), wrapContent, LAYOUT_WEIGHT_MEDIUM)

                            tvTimeTotal = tvShowArrivedInfo(R.string.arrived_dialog_total_time).lparams {
                                gravity = Gravity.CENTER_HORIZONTAL
                                topMargin = dimen(R.dimen.margin_very_low)
                            }

                            tvDescriptionArrivedInfo(R.string.arrived_elapsed).lparams {
                                gravity = Gravity.CENTER_HORIZONTAL
                            }
                        }

                        verticalLayout {
                            lparams(dip(0), wrapContent, LAYOUT_WEIGHT_MEDIUM)

                            tvDistance = tvShowArrivedInfo(R.string.arrived_dialog_total_distance).lparams {
                                gravity = Gravity.CENTER_HORIZONTAL
                                topMargin = dimen(R.dimen.margin_very_low)
                            }

                            tvDescriptionArrivedInfo(R.string.arrived_traveled).lparams {
                                gravity = Gravity.CENTER_HORIZONTAL
                            }
                        }
                    }
                }

                textView {
                    backgroundResource = R.color.colorGrayLight
                }.lparams(matchParent, dip(1))

                textView().lparams(matchParent, dip(0), LAYOUT_WEIGHT_SMALL)

                textView {
                    backgroundResource = R.color.colorGrayLight
                }.lparams(matchParent, dip(1))

                btnDoneDialog = button(R.string.arrived_done) {
                    textColor = ContextCompat.getColor(context, R.color.colorWhite)
                    textSize = px2dip(dimen(R.dimen.text_size_normal))
                    backgroundResource = R.color.colorAccentLight
                }.lparams(matchParent, dip(0), LAYOUT_WEIGHT_VERY_SMALL)
            }
        }
    }

    private fun ViewManager.textViewComplete(string: Int) = textView(string) {
        textColor = ContextCompat.getColor(context, R.color.colorBlack)
        textSize = px2dip(dimen(R.dimen.text_size_normal))
    }

    private fun ViewManager.tvShowArrivedInfo(string: Int) = textView(string) {
        textColor = ContextCompat.getColor(context, R.color.colorBlack)
        textSize = px2dip(dimen(R.dimen.text_size_normal))
    }

    private fun ViewManager.tvDescriptionArrivedInfo(string: Int) = textView(string) {
        textSize = px2dip(dimen(R.dimen.text_size_little))
    }

    override fun onStart() {
        super.onStart()
        ScreenUtil().getSreenSize(context, dialog)
    }
}

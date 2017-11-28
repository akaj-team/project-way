package vn.asiantech.way.ui.group

import android.support.v4.app.ActivityCompat
import android.view.Gravity
import android.view.ViewManager
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Created by haingoq on 28/11/2017.
 */
class GroupInfoFragmentUI : AnkoComponent<GroupInfoFragment> {
    companion object {
        private const val MAX_LINES = 1
        private const val TV_GROUP_NAME_PADDING = 20
        private const val TV_COUNT_PADDING_LEFT = 10
        private const val TV_COUNT_PADDING_RIGHT = 5
    }

    override fun createView(ui: AnkoContext<GroupInfoFragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)

            textView {
                backgroundColor = ActivityCompat.getColor(context, R.color.colorBlueLight)
                gravity = Gravity.CENTER_VERTICAL
                maxLines = MAX_LINES
                textColor = ActivityCompat.getColor(context, R.color.white)
                textSize = px2dip(dimen(R.dimen.text_size_normal))
            }.lparams(matchParent, dimen(R.dimen.toolbar_height)) {
                leftPadding = dip(TV_GROUP_NAME_PADDING)
                rightPadding = dip(TV_GROUP_NAME_PADDING)
            }
            textViewInfo()
            textViewInfo()
            textViewInfo(resources.getString(R.string.members_list))

            relativeLayout {
                lparams(matchParent, wrapContent)
                padding = dip(10)
            }
        }
    }

    private fun ViewManager.textViewInfo(str: String = "") = textView {
        text = str
        textSize = px2dip(dimen(R.dimen.group_text_size_normal))
    }.apply {
        bottomPadding = GroupInfoFragmentUI.TV_COUNT_PADDING_RIGHT
        rightPadding = GroupInfoFragmentUI.TV_COUNT_PADDING_LEFT
        leftPadding = GroupInfoFragmentUI.TV_COUNT_PADDING_LEFT
        topPadding = GroupInfoFragmentUI.TV_COUNT_PADDING_RIGHT
    }
}

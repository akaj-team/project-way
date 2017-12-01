package vn.asiantech.way.ui.group

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.app.ActivityCompat
import android.text.InputFilter
import android.view.Gravity
import android.view.ViewManager
import android.widget.Button
import android.widget.EditText
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 *
 * Created by haingoq on 28/11/2017.
 */
class CreateGroupFragmentUI : AnkoComponent<CreateGroupFragment> {
    companion object {
        private const val MAX_LENGTH = 50
        private const val MAX_LINE = 1
    }

    internal lateinit var edtGroupName: EditText
    internal lateinit var btnCreateGroup: Button
    internal lateinit var btnBack: Button

    override fun createView(ui: AnkoContext<CreateGroupFragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            backgroundResource = R.color.colorBlueLight
            gravity = Gravity.CENTER
            padding = dimen(R.dimen.group_screen_group_name_padding)

            edtGroupName = editText {
                backgroundColor = Color.WHITE
                hint = resources.getString(R.string.group_name)
                filters = arrayOf(InputFilter.LengthFilter(MAX_LENGTH))
                maxLines = MAX_LINE
                singleLine = true
                padding = dimen(R.dimen.group_screen_tv_count_padding_left)
                textSize = px2dip(dimen(R.dimen.group_text_size_normal))
                setTypeface(null, Typeface.ITALIC)
            }.lparams(matchParent, wrapContent)

            btnCreateGroup = buttonCreateGroup(R.string.create_group, R.color.colorAccent)
                    .lparams(matchParent, wrapContent) {
                        topMargin = dimen(R.dimen.group_screen_group_name_padding)
                    }

            btnBack = buttonCreateGroup(R.string.back, android.R.color.darker_gray)
                    .lparams(matchParent, wrapContent) {
                        topMargin = dimen(R.dimen.group_screen_group_name_padding)
                    }
        }
    }

    private fun ViewManager.buttonCreateGroup(strResource: Int, color: Int) = button {
        backgroundColor = ActivityCompat.getColor(context, color)
        text = resources.getString(strResource)
        setAllCaps(false)
        textColor = Color.WHITE
        textSize = px2dip(dimen(R.dimen.group_text_size_normal))
    }
}

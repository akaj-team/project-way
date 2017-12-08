package vn.asiantech.way.ui.group.create

import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.app.ActivityCompat
import android.text.InputFilter
import android.view.Gravity
import android.view.ViewManager
import android.widget.EditText
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
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

            buttonCreateGroup(R.id.create_group_fragment_ui_btn_create, R.string.create_group, R.color.colorAccent)
                    .lparams(matchParent, wrapContent) {
                        topMargin = dimen(R.dimen.group_screen_group_name_padding)
                    }
                    .onClick {
                        owner.eventViewOnclick(it!!)
                    }
            buttonCreateGroup(R.id.create_group_fragment_ui_btn_back, R.string.back, android.R.color.darker_gray)
                    .lparams(matchParent, wrapContent) {
                        topMargin = dimen(R.dimen.group_screen_group_name_padding)
                    }
                    .onClick {
                        owner.eventViewOnclick(it!!)
                    }
        }
    }

    private fun ViewManager.buttonCreateGroup(viewId: Int, strResource: Int, color: Int) = button {
        id = viewId
        backgroundColor = ActivityCompat.getColor(context, color)
        text = resources.getString(strResource)
        setAllCaps(false)
        textColor = Color.WHITE
        textSize = px2dip(dimen(R.dimen.group_text_size_normal))
        onClick {

        }
    }
}

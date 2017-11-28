package vn.asiantech.way.ui.register

import android.content.DialogInterface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R

/**
 * Anko layout for RegisterActivity
 * Created by haingoq on 27/11/2017.
 */
class RegisterActivityUI(private val countryAdapter: CountryAdapter) : AnkoComponent<RegisterActivity> {
    companion object {
        // ID of view
        private const val FR_AVATAR_ID = 101
        private const val TV_DESCRIPTION_ID = 102
        private const val RL_INFORMATION_ID = 103
        private const val BTN_SAVE_ID = 104
        private const val TV_SKIP = 105
        private const val EDT_NAME_ID = 201
        private const val VIEW_LINE_ID = 202

        // Magic number
        private const val BORDER_WIDTH = 1
        private const val LL_PHONE_PADDING = 40
        private const val TV_TEL_WIDTH = 45
        private const val BTN_REGISTER_MARGIN = 20
        private const val TV_SKIP_MARGIN = 15
    }

    internal lateinit var dialog: DialogInterface
    internal lateinit var frAvatar: FrameLayout
    internal lateinit var progressBarAvatar: ProgressBar
    internal lateinit var imgAvatar: ImageView
    internal lateinit var edtName: EditText
    internal lateinit var imgFlag: ImageView
    internal lateinit var tvTel: TextView
    internal lateinit var edtPhone: EditText
    internal lateinit var btnRegister: Button
    internal lateinit var tvSkip: TextView
    internal lateinit var tvCancel: TextView
    internal lateinit var progressBar: ProgressBar

    override fun createView(ui: AnkoContext<RegisterActivity>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            frAvatar = frameLayout {
                id = FR_AVATAR_ID

                circleImageView {
                    lparams(dimen(R.dimen.register_screen_avatar_size),
                            dimen(R.dimen.register_screen_avatar_size))
                    background = ContextCompat.getDrawable(context, R.drawable.ic_default_avatar)
                    borderColor = ContextCompat.getColor(context, R.color.white)
                    borderWidth = dip(BORDER_WIDTH)
                }

                progressBarAvatar = progressBar {
                    visibility = View.GONE
                }.lparams {
                    gravity = Gravity.CENTER
                }

                imgAvatar = circleImageView {
                    background = ContextCompat.getDrawable(context, R.drawable.ic_profile_camera)
                    borderColor = ContextCompat.getColor(context, R.color.white)
                    borderWidth = dip(BORDER_WIDTH)
                }.lparams {
                    rightMargin = dimen(R.dimen.register_screen_avatar_margin)
                    gravity = Gravity.END
                }
            }.lparams(wrapContent, wrapContent) {
                topMargin = dimen(R.dimen.margin_huge)
                centerHorizontally()
            }

            textView(resources.getString(R.string.register_description)) {
                id = TV_DESCRIPTION_ID
                gravity = Gravity.CENTER
                textSize = px2dip(dimen(R.dimen.register_screen_name_text_size))
            }.lparams(matchParent, wrapContent) {
                below(FR_AVATAR_ID)
                val margin = resources.getDimension(R.dimen.margin_xxhigh).toInt()
                topMargin = margin
                leftMargin = margin
                rightMargin = margin
            }

            relativeLayout {
                id = RL_INFORMATION_ID
                backgroundResource = R.drawable.custom_layout_phone

                edtName = editText {
                    id = EDT_NAME_ID
                    backgroundColor = ContextCompat.getColor(context, android.R.color.transparent)
                    hint = resources.getString(R.string.register_hint_name)
                    inputType = InputType.TYPE_CLASS_TEXT
                    textSize = px2dip(dimen(R.dimen.register_screen_name_text_size))
                    gravity = Gravity.CENTER
                    imeOptions = EditorInfo.IME_ACTION_NEXT
                }.lparams(matchParent, dimen(R.dimen.register_screen_edit_text_height))

                view {
                    id = VIEW_LINE_ID
                    backgroundColor = ContextCompat.getColor(context, R.color.grayLight)
                }.lparams(matchParent, dip(BORDER_WIDTH)) {
                    below(EDT_NAME_ID)
                }

                linearLayout {
                    leftPadding = dip(LL_PHONE_PADDING)
                    rightPadding = dip(LL_PHONE_PADDING)
                    imgFlag = imageView {

                    }.lparams {
                        gravity = Gravity.CENTER_VERTICAL
                    }

                    imageView {
                        backgroundResource = R.drawable.ic_arrow_drop_down
                        onClick {
                            dialog = alert {
                                customView {
                                    recyclerView {
                                        layoutManager = LinearLayoutManager(context)
                                        adapter = countryAdapter
                                        countryAdapter.onItemClick = { country ->
                                            // TODO Set image to imgFlag and tel to tvTel
                                            dialog.dismiss()
                                        }

                                    }
                                }
                            }.show()
                        }
                    }.lparams {
                        gravity = Gravity.CENTER_VERTICAL
                    }

                    tvTel = textView(resources.getString(R.string.register_tel)) {
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                        textSize = px2dip(dimen(R.dimen.register_screen_phone_text_size))
                    }.lparams(dip(TV_TEL_WIDTH), matchParent)

                    edtPhone = editText {
                        backgroundColor = ContextCompat.getColor(context, android.R.color.transparent)
                        hint = resources.getString(R.string.register_hint_phone)
                        inputType = InputType.TYPE_CLASS_PHONE
                        textSize = px2dip(dimen(R.dimen.register_screen_phone_text_size))
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                        imeOptions = EditorInfo.IME_ACTION_DONE
                    }.lparams(matchParent, matchParent)
                }.lparams(matchParent, dimen(R.dimen.register_screen_edit_text_height)) {
                    below(VIEW_LINE_ID)
                }
            }.lparams(matchParent, wrapContent) {
                below(TV_DESCRIPTION_ID)
                val margin = dimen(R.dimen.margin_high)
                bottomMargin = dimen(R.dimen.margin_huge)
                leftMargin = margin
                rightMargin = margin
                topMargin = margin
            }

            btnRegister = button(resources.getString(R.string.register_button_save_text)) {
                id = BTN_SAVE_ID
                backgroundResource = R.drawable.custom_button_save
                setAllCaps(false)
                textColor = ContextCompat.getColor(context, R.color.white)
                textSize = px2dip(dimen(R.dimen.register_screen_save_button_text_size))
                isEnabled = false
            }.lparams(matchParent, dimen(R.dimen.register_screen_save_button_height)) {
                below(RL_INFORMATION_ID)
                leftMargin = dip(BTN_REGISTER_MARGIN)
                topMargin = dip(BTN_REGISTER_MARGIN)
                rightMargin = dip(BTN_REGISTER_MARGIN)
            }

            tvSkip = textView(resources.getString(R.string.register_skip)) {
                id = TV_SKIP
                textSize = px2dip(dimen(R.dimen.register_screen_phone_text_size))
                gravity = Gravity.CENTER
            }.lparams(matchParent, wrapContent) {
                below(BTN_SAVE_ID)
                topMargin = dip(TV_SKIP_MARGIN)
            }

            tvCancel = textView(resources.getString(R.string.register_cancel)) {
                textSize = px2dip(dimen(R.dimen.register_screen_phone_text_size))
                gravity = Gravity.CENTER
                visibility = View.GONE
            }.lparams(matchParent, wrapContent) {
                below(TV_SKIP)
                topMargin = dip(TV_SKIP_MARGIN)
            }

            progressBar = progressBar {
                visibility = View.GONE
            }.lparams {
                centerInParent()
            }
        }
    }

    /*
     * Add circleImageView library
     */
    private inline fun ViewManager.circleImageView(theme: Int = 0, init: CircleImageView.() -> Unit):
            CircleImageView {
        return ankoView({ CircleImageView(it) }, theme, init)
    }
}

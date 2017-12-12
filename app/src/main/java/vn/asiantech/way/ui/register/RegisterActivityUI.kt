package vn.asiantech.way.ui.register

import android.content.DialogInterface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onEditorAction
import vn.asiantech.way.R
import vn.asiantech.way.extension.circleImageView
import vn.asiantech.way.extension.hideKeyboard
import vn.asiantech.way.extension.onTextChangeListener

/**
 * Anko layout for RegisterActivity
 * Created by haingoq on 27/11/2017.
 */
class RegisterActivityUI(val countryAdapter: CountryAdapter) : AnkoComponent<RegisterActivity> {

    internal lateinit var frAvatar: FrameLayout
    internal lateinit var progressBarAvatar: ProgressBar
    internal lateinit var imgAvatar: ImageView
    internal lateinit var edtName: EditText
    internal lateinit var imgFlag: ImageView
    internal lateinit var tvTel: TextView
    internal lateinit var edtPhone: EditText
    internal lateinit var btnRegister: Button
    internal lateinit var tvSkip: TextView
    internal lateinit var progressBar: ProgressBar

    private lateinit var dialogInterface: DialogInterface

    override fun createView(ui: AnkoContext<RegisterActivity>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            frAvatar = frameLayout {
                id = R.id.register_activity_fr_avatar

                imgAvatar = circleImageView {
                    backgroundResource = R.drawable.ic_default_avatar
                    lparams(dimen(R.dimen.register_screen_avatar_size),
                            dimen(R.dimen.register_screen_avatar_size))
                }

                progressBarAvatar = progressBar {
                    visibility = View.GONE
                }.lparams {
                    gravity = Gravity.CENTER
                }

                circleImageView {
                    backgroundResource = R.drawable.ic_profile_camera
                }.lparams {
                    rightMargin = dimen(R.dimen.register_screen_avatar_margin)
                    gravity = Gravity.END
                }

                onClick {
                    owner.eventOnViewClicked(it!!)
                }
            }.lparams {
                topMargin = dimen(R.dimen.margin_huge)
                centerHorizontally()
            }.applyRecursively {
                when (it) {
                    is CircleImageView -> {
                        it.borderColor = ContextCompat.getColor(ctx, R.color.white)
                        it.borderWidth = dimen(R.dimen.border)
                    }
                }
            }

            textView(R.string.register_description) {
                id = R.id.register_activity_tv_description
                gravity = Gravity.CENTER
                textSize = px2dip(dimen(R.dimen.register_screen_name_text_size))
            }.lparams(matchParent, wrapContent) {
                below(R.id.register_activity_fr_avatar)
                val margin = dimen(R.dimen.margin_xxhigh)
                topMargin = margin
                horizontalMargin = margin
            }

            relativeLayout {
                id = R.id.register_activity_rl_information
                backgroundResource = R.drawable.custom_layout_phone

                edtName = editText {
                    id = R.id.register_activity_edt_name
                    backgroundResource = android.R.color.transparent
                    hint = resources.getString(R.string.register_hint_name)
                    textSize = px2dip(dimen(R.dimen.register_screen_name_text_size))
                    gravity = Gravity.CENTER
                    inputType = InputType.TYPE_CLASS_TEXT
                    singleLine = true
                    horizontalPadding = dimen(R.dimen.register_screen_edt_name_padding)

                    onEditorAction { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            edtPhone.requestFocus()
                        }
                    }
                }.lparams(matchParent, dimen(R.dimen.register_screen_edit_text_height))

                view {
                    id = R.id.register_activity_view_line
                    backgroundResource = R.color.grayLight
                }.lparams(matchParent, dimen(R.dimen.border)) {
                    below(R.id.register_activity_edt_name)
                }

                linearLayout {
                    horizontalPadding = dimen(R.dimen.register_screen_ll_phone_padding)
                    imgFlag = imageView(R.drawable.ic_vn).lparams {
                        gravity = Gravity.CENTER_VERTICAL
                    }

                    imageView(R.drawable.ic_arrow_drop_down) {
                        onClick {
                            dialogInterface = alert {
                                customView {
                                    recyclerView {
                                        layoutManager = LinearLayoutManager(ctx)
                                        adapter = countryAdapter
                                        countryAdapter.onItemClick = { country ->
                                            Picasso.with(ctx).load(country.flagFilePath).into(imgFlag)
                                            owner.isoCode = country.iso
                                            tvTel.text = resources.getString(R.string.register_plus).plus(country.tel)
                                            dialogInterface.dismiss()
                                        }
                                    }
                                }
                            }.show()
                        }
                    }.lparams {
                        gravity = Gravity.CENTER_VERTICAL
                    }

                    tvTel = textView(R.string.register_tel) {
                        id = R.id.register_activity_tv_tel
                        gravity = Gravity.CENTER_VERTICAL
                        textSize = px2dip(dimen(R.dimen.register_screen_phone_text_size))
                    }.lparams(dimen(R.dimen.register_screen_tv_tel_width), matchParent)

                    edtPhone = editText {
                        id = R.id.register_activity_edt_phone
                        backgroundResource = android.R.color.transparent
                        hint = resources.getString(R.string.register_hint_phone)
                        inputType = InputType.TYPE_CLASS_PHONE
                        textSize = px2dip(dimen(R.dimen.register_screen_phone_text_size))
                        gravity = Gravity.CENTER_VERTICAL
                        singleLine = true

                        onEditorAction { _, actionId, _ ->
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                hideKeyboard(ctx)
                            }
                        }
                    }.lparams(matchParent, matchParent)
                }.lparams(matchParent, dimen(R.dimen.register_screen_edit_text_height)) {
                    below(R.id.register_activity_view_line)
                }
            }.lparams(matchParent, wrapContent) {
                below(R.id.register_activity_tv_description)
                val margin = dimen(R.dimen.margin_high)
                bottomMargin = dimen(R.dimen.margin_huge)
                topMargin = margin
                horizontalMargin = margin
            }

            btnRegister = button(R.string.register_button_save_text) {
                id = R.id.register_activity_btn_save
                backgroundResource = R.drawable.custom_button_save
                setAllCaps(false)
                textColor = ContextCompat.getColor(ctx, R.color.white)
                textSize = px2dip(dimen(R.dimen.register_screen_save_button_text_size))
                isEnabled = false

                onClick {
                    owner.eventOnViewClicked(it!!)
                }
            }.lparams(matchParent, dimen(R.dimen.register_screen_save_button_height)) {
                val margin = dimen(R.dimen.register_screen_btn_register_margin)
                below(R.id.register_activity_rl_information)
                topMargin = margin
                horizontalMargin = margin
            }

            tvSkip = textView(R.string.register_skip) {
                id = R.id.register_activity_tv_skip
                textSize = px2dip(dimen(R.dimen.register_screen_phone_text_size))
                gravity = Gravity.CENTER

                onClick {
                    owner.eventOnViewClicked(it!!)
                }
            }.lparams(matchParent, wrapContent) {
                below(R.id.register_activity_btn_save)
                topMargin = dimen(R.dimen.register_screen_tv_skip_margin)
            }

            progressBar = progressBar {
                visibility = View.GONE
            }.lparams {
                centerInParent()
            }
        }.applyRecursively { view: View ->
            if (view is EditText) {
                when (view) {
                    edtName, edtPhone -> view.onTextChangeListener {
                        owner.onHandleTextChange(edtName.text.toString().trim(),
                                edtPhone.text.toString().trim())
                    }
                }
            }
        }
    }
}

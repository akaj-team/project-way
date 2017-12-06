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
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onEditorAction
import vn.asiantech.way.R
import vn.asiantech.way.extension.circleImageView
import vn.asiantech.way.extension.hideKeyboard

/**
 * Anko layout for RegisterActivity
 * Created by haingoq on 27/11/2017.
 */
class RegisterActivityUI(val countryAdapter: CountryAdapter) : AnkoComponent<RegisterActivity> {

    internal lateinit var dialogInterface: DialogInterface
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

    override fun createView(ui: AnkoContext<RegisterActivity>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            frAvatar = frameLayout {
                id = R.id.register_activity_fr_avatar

                imgAvatar = circleImageView {
                    backgroundResource = R.drawable.ic_default_avatar
                    lparams(dimen(R.dimen.register_screen_avatar_size),
                            dimen(R.dimen.register_screen_avatar_size))
                    borderColor = ContextCompat.getColor(context, R.color.white)
                    borderWidth = dimen(R.dimen.border)
                }

                progressBarAvatar = progressBar {
                    visibility = View.GONE
                }.lparams {
                    gravity = Gravity.CENTER
                }

                circleImageView {
                    backgroundResource = R.drawable.ic_profile_camera
                    borderColor = ContextCompat.getColor(context, R.color.white)
                    borderWidth = dimen(R.dimen.border)
                }.lparams {
                    rightMargin = dimen(R.dimen.register_screen_avatar_margin)
                    gravity = Gravity.END
                }

                onClick {
                    owner.checkPermissionGallery()
                }
            }.lparams {
                topMargin = dimen(R.dimen.margin_huge)
                centerHorizontally()
            }

            textView(R.string.register_description) {
                id = R.id.register_activity_tv_description
                gravity = Gravity.CENTER
                textSize = px2dip(dimen(R.dimen.register_screen_name_text_size))
            }.lparams(matchParent, wrapContent) {
                below(R.id.register_activity_fr_avatar)
                val margin = resources.getDimension(R.dimen.margin_xxhigh).toInt()
                topMargin = margin
                leftMargin = margin
                rightMargin = margin
            }

            relativeLayout {
                id = R.id.register_activity_rl_information
                backgroundResource = R.drawable.custom_layout_phone

                edtName = editText {
                    id = R.id.register_activity_edt_name
                    backgroundColor = ContextCompat.getColor(context, android.R.color.transparent)
                    hint = resources.getString(R.string.register_hint_name)
                    textSize = px2dip(dimen(R.dimen.register_screen_name_text_size))
                    gravity = Gravity.CENTER

                    onEditorAction { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            requestFocus()
                        }
                    }
                }.lparams(matchParent, dimen(R.dimen.register_screen_edit_text_height))

                view {
                    id = R.id.register_activity_view_line
                    backgroundColor = ContextCompat.getColor(context, R.color.grayLight)
                }.lparams(matchParent, dimen(R.dimen.border)) {
                    below(R.id.register_activity_edt_name)
                }

                linearLayout {
                    val padding = dimen(R.dimen.register_screen_ll_phone_padding)
                    leftPadding = padding
                    rightPadding = padding
                    imgFlag = imageView(R.drawable.ic_vn).lparams {
                        gravity = Gravity.CENTER_VERTICAL
                    }

                    imageView(R.drawable.ic_arrow_drop_down) {
                        onClick {
                            dialogInterface = alert {
                                customView {
                                    recyclerView {
                                        layoutManager = LinearLayoutManager(context)
                                        adapter = countryAdapter
                                        countryAdapter.onItemClick = { country ->
                                            Picasso.with(context).load(country.flagFilePath).into(imgFlag)
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
                        backgroundColor = ContextCompat.getColor(context, android.R.color.transparent)
                        hint = resources.getString(R.string.register_hint_phone)
                        inputType = InputType.TYPE_CLASS_PHONE
                        textSize = px2dip(dimen(R.dimen.register_screen_phone_text_size))
                        gravity = Gravity.CENTER_VERTICAL

                        onEditorAction { _, actionId, _ ->
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                hideKeyboard(getContext())
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
                leftMargin = margin
                rightMargin = margin
                topMargin = margin
            }

            btnRegister = button(R.string.register_button_save_text) {
                id = R.id.register_activity_btn_save
                backgroundResource = R.drawable.custom_button_save
                setAllCaps(false)
                textColor = ContextCompat.getColor(context, R.color.white)
                textSize = px2dip(dimen(R.dimen.register_screen_save_button_text_size))
                isEnabled = false

                onClick {
                    val user = owner.registerViewModel
                            .generateUserInformation(edtName.text.toString().trim(),
                                    edtPhone.text.toString().trim(),
                                    owner.isoCode,
                                    owner.avatarBitmap)
                    if (owner.registerViewModel.isRegister) {
                        owner.createUser(user)
                    } else {
                        owner.onUpdateUserInformation(user)
                    }
                }
            }.lparams(matchParent, dimen(R.dimen.register_screen_save_button_height)) {
                val margin = dimen(R.dimen.register_screen_btn_register_margin)
                below(R.id.register_activity_rl_information)
                leftMargin = margin
                topMargin = margin
                rightMargin = margin
            }

            tvSkip = textView(R.string.register_skip) {
                id = R.id.register_activity_tv_skip
                textSize = px2dip(dimen(R.dimen.register_screen_phone_text_size))
                gravity = Gravity.CENTER

                onClick {
                    owner.onSkipClick()
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
                when (view.id) {
                    R.id.register_activity_edt_name,
                    R.id.register_activity_edt_phone ->
                        view.addTextChangedListener(object : OnWayTextChangeListener {
                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                owner.onHandleTextChange()
                            }
                        })
                }
            }
        }
    }
}

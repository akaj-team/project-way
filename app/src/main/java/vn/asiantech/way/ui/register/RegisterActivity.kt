package vn.asiantech.way.ui.register

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.TextView
import com.hypertrack.lib.HyperTrackUtils
import com.hypertrack.lib.models.User
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.jetbrains.anko.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.remote.response.ResponseStatus
import vn.asiantech.way.extension.hideKeyboard
import vn.asiantech.way.ui.base.BaseActivity

/**
 * Activity register user
 * Created by haibt on 9/26/17.
 */
class RegisterActivity : BaseActivity(), View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {
    companion object {
        const val INTENT_CODE_SPLASH = 1001
        const val INTENT_CODE_HOME = 1002
        const val INTENT_REGISTER = "Register"
    }

    private var countries = MutableList(0, { Country("", "") })
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var ui: RegisterActivityUI
    private var userWay: User? = null
    private var avatarUri: Uri? = null
    private var avatarBitmap: Bitmap? = null
    private var isoCode: String? = null
    private var tel: String? = null
    private var isExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = RegisterActivityUI(CountryAdapter(this, countries))
        ui.setContentView(this)
        initListener()
        registerViewModel = RegisterViewModel(this)
        val user = User()
        user.photo = "http://3.bp.blogspot.com/-JWTHh-LmaZA/VaiyTEScmOI/AAAAAAAAP0U/pDHk_wZxMiE/s1600/2013_aston_martin_dbc_concept-wide.jpg"
        onGetUser(user)
    }

    override fun onBindViewModel() {
        addDisposables(registerViewModel.getCountries()
                .subscribe(this::showCountryList))
    }

    override fun onBackPressed() {
        if (intent.extras != null) {
            if (intent.extras.getInt(INTENT_REGISTER) == INTENT_CODE_SPLASH) {
                if (!isExit) {
                    isExit = true
                    toast(getString(R.string.register_double_click_to_exit))
                    Handler().postDelayed({
                        isExit = false
                    }, 1500)
                } else {
                    finishAffinity()
                }
            } else if (intent.extras.getInt(INTENT_REGISTER) == INTENT_CODE_HOME) {
                super.onBackPressed()
            }
        }
    }

    override fun onClick(view: View?) {
        var name: String = ui.edtName.text.toString().trim()
        val phoneNumber: String = ui.edtPhone.text.toString().trim()
        when (view) {
            ui.btnRegister -> {
                //TODO Register user
            }
            ui.tvSkip -> {
                if (checkPermission()) {
                    if (userWay == null) {
                        Log.d("xxx", "xxx")
                        if (name.isBlank()) {
                            name = getString(R.string.register_user_name_default)
                        }
                        if (phoneNumber.isBlank()) {
                            ui.edtPhone.text = null
                        }
                        alert {
                            title = resources.getString(R.string.register_title_dialog)
                            message = resources.getString(R.string.register_message_dialog)
                            yesButton {
                                //TODO Create User with name, phone and...
                            }
                            noButton { dialog -> dialog.dismiss() }
                        }.show()
                    }
                } else {
                    toast(getString(R.string.register_request_permission))
                }
            }
            ui.tvCancel -> {
                if (intent.extras[INTENT_REGISTER] == INTENT_CODE_HOME) {
                    // TODO Start HomeActivity
                } else {
                    ui.edtName.setText("")
                    ui.edtPhone.setText("")
                    avatarUri = null
                    avatarBitmap = null
                    Picasso.with(this).load(R.drawable.ic_default_avatar).into(ui.imgAvatar)
                }
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        // No-op
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // No-op
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val name: String = ui.edtName.text.toString().trim()
        val phone: String = ui.edtPhone.text.toString().trim()
        if (intent.extras != null) {
            if (intent.extras[INTENT_REGISTER] == INTENT_CODE_HOME) {
                ui.btnRegister.isEnabled = !(name == userWay?.name && phone == userWay?.phone?.removeRange(0, 3))
                ui.tvCancel.visibility = View.VISIBLE
                ui.tvSkip.visibility = View.GONE
            } else {
                if (name.isBlank() && phone.isBlank()) {
                    ui.tvSkip.visibility = View.VISIBLE
                    ui.tvCancel.visibility = View.GONE
                    ui.btnRegister.isEnabled = false
                } else {
                    ui.tvCancel.visibility = View.VISIBLE
                    ui.tvSkip.visibility = View.GONE
                    ui.btnRegister.isEnabled = true
                }
            }
        }
    }

    override fun onEditorAction(view: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        when (view) {
            ui.edtName -> {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    ui.edtPhone.requestFocus()
                    return true
                }
            }
            ui.edtPhone -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ui.edtPhone.hideKeyboard(this)
                    return true
                }
            }
        }
        return false
    }

    private fun showCountryList(data: List<Country>) {
        countries.clear()
        countries.addAll(data)
        ui.countryAdapter.notifyDataSetChanged()
    }

    private fun onUserCreated(responseStatus: ResponseStatus) {
        toast(responseStatus.message)
    }

    private fun onUserUpdated(responseStatus: ResponseStatus) {
        toast(responseStatus.message)
    }

    private fun onGetUser(user: User) {
        userWay = user
        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                visibleProgressBar(ui.progressBarAvatar)
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                invisibleProgressBar(ui.progressBarAvatar)
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                ui.imgAvatar.setImageBitmap(bitmap)
                invisibleProgressBar(ui.progressBarAvatar)
                avatarBitmap = bitmap
            }
        }
        Picasso.with(this)
                .load(user.photo)
                .into(target)
        ui.imgAvatar.tag = target
        ui.edtName.setText(user.name)
        val basePhone: List<String>? = user.phone?.split("/")
        if (basePhone != null) {
            if (basePhone.size > 1) {
                // Set isoCode
                isoCode = basePhone[0]
                for (i in 0 until countries.size) {
                    if (isoCode == countries[i].iso) {
                        val country = countries[i]
                        val telephone = country.tel
                        ui.tvTel.text = getString(R.string.register_plus).plus(telephone)
                        Picasso.with(this).load(country.flagFilePath).into(ui.imgFlag)
                        tel = telephone
                        break
                    }
                }
                ui.edtPhone.setText(basePhone[1])
            } else {
                ui.edtPhone.setText(basePhone[0])
            }
        }
    }

    private fun onAvatarSelected(data: Intent) {
        val uri = data.data
        if (uri != null) {
            Picasso.with(this)
                    .load(uri)
                    .resize(300, 300)
                    .into(object : Target {
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            // No-op
                        }

                        override fun onBitmapFailed(errorDrawable: Drawable?) {
                            // No-op
                        }

                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            updateView(bitmap)
                            avatarBitmap = bitmap
                        }
                    })
        } else {
            val bmp = data.extras.get("data") as Bitmap
            updateView(bmp)
        }
    }

    private fun updateView(bitmap: Bitmap?) {
        ui.imgAvatar.setImageBitmap(bitmap)
        ui.tvCancel.visibility = View.VISIBLE
        ui.tvSkip.visibility = View.GONE
        ui.btnRegister.isEnabled = true
    }

    private fun visibleProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun invisibleProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun checkPermission(): Boolean {
        return HyperTrackUtils.isLocationEnabled(this)
                && HyperTrackUtils.isInternetConnected(this)
    }

    private fun initListener() {
        ui.edtName.setOnEditorActionListener(this)
        ui.edtName.addTextChangedListener(this)
        ui.edtPhone.setOnEditorActionListener(this)
        ui.edtPhone.addTextChangedListener(this)
        ui.tvTel.addTextChangedListener(this)
        ui.btnRegister.setOnClickListener(this)
        ui.tvSkip.setOnClickListener(this)
        ui.tvCancel.setOnClickListener(this)
    }
}

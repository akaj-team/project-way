package vn.asiantech.way.ui.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.TextView
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.HyperTrackUtils
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.models.ErrorResponse
import com.hypertrack.lib.models.SuccessResponse
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.remote.response.ResponseStatus
import vn.asiantech.way.extension.hideKeyboard
import vn.asiantech.way.extension.toBase64
import vn.asiantech.way.ui.base.BaseActivity

/**
 * Activity register user
 * Created by haibt on 9/26/17.
 */
class RegisterActivity : BaseActivity(), View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {
    companion object {
        const val INTENT_CODE_SPLASH = 1001
        const val INTENT_CODE_HOME = 1002
        private const val REQUEST_CODE_PICK_IMAGE = 1003
        private const val REQUEST_CODE_GALLERY = 1004
        const val INTENT_REGISTER = "Register"
    }

    private var countries = MutableList(0, { Country("", "") })
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var ui: RegisterActivityUI
    private var userWay: User? = null
    private var avatarUri: Uri? = null
    private var avatarBitmap: Bitmap? = null
    internal var isoCode: String? = null
    private var tel: String? = null
    private var isExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = RegisterActivityUI(CountryAdapter(this, countries))
        ui.setContentView(this)
        initListener()
        isoCode = getString(R.string.register_iso_code_default)
        registerViewModel = RegisterViewModel(this)
        registerViewModel.getUser()
                .subscribe(this::onGetUser, {
                    toast(it.message.toString())
                })
    }

    override fun onBindViewModel() {
        addDisposables(registerViewModel.getCountries().subscribe(this::showCountryList),
                registerViewModel.progressBarStatus
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::updateProgressBarStatus),
                registerViewModel.selectAvatar()
                        .subscribe(this::onAvatarSelected))
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
        val userParam = UserParams()
                .setName(name)
                .setPhoto(avatarBitmap?.toBase64())
                .setPhone(isoCode?.plus("/")?.plus(phoneNumber))
                .setLookupId(phoneNumber)

        when (view) {
            ui.frAvatar -> {
                checkPermissionGallery()
            }

            ui.btnRegister -> {
                createUser(userParam)
            }

            ui.tvSkip -> {
                if (checkPermission()) {
                    if (userWay == null) {
                        Log.d("xxx", "create")
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
                                userParam.name = name
                                createUser(userParam)
                            }
                            noButton { dialog -> dialog.dismiss() }
                        }.show()
                    } else {
                        Log.d("xxx", "update")
                        if (ui.edtName.text.toString() != userWay?.name
                                || ui.edtPhone.text.toString() != userWay?.phone?.removeRange(0, 3)
                                || avatarUri != null) {
                            visibleProgressBar(ui.progressBar)
                            // Update user information
                            HyperTrack.updateUser(userParam, object : HyperTrackCallback() {
                                override fun onSuccess(p0: SuccessResponse) {
                                    HyperTrack.startTracking()
                                    invisibleProgressBar(ui.progressBar)
                                    updateUser(userParam)
                                    // TODO Intent to HomeActivity
                                }

                                override fun onError(error: ErrorResponse) {
                                    alert {
                                        title = getString(R.string.dialog_title_error)
                                        message = error.errorMessage
                                        yesButton { dialog ->
                                            title = getString(R.string.dialog_button_ok)
                                            dialog.dismiss()
                                        }
                                    }.show()
                                }
                            })
                        }
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
//        if (intent.extras != null) {
//            if (intent.extras[INTENT_REGISTER] == INTENT_CODE_HOME) {
//                ui.btnRegister.isEnabled = !(name == userWay?.name && phone == userWay?.phone?.removeRange(0, 3))
//                ui.tvCancel.visibility = View.VISIBLE
//                ui.tvSkip.visibility = View.GONE
//            } else {
        if (name.isBlank() && phone.isBlank()) {
            ui.tvSkip.visibility = View.VISIBLE
            ui.tvCancel.visibility = View.GONE
            ui.btnRegister.isEnabled = false
        } else {
            ui.tvCancel.visibility = View.VISIBLE
            ui.tvSkip.visibility = View.GONE
            ui.btnRegister.isEnabled = true
        }
//            }
//        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            registerViewModel.setIntent(data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_GALLERY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intentGallery()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun createUser(userParam: UserParams) {
        addDisposables(registerViewModel.createUser(userParam)
                .subscribe(this::onUserCreated))
    }

    private fun updateUser(userParam: UserParams) {
        addDisposables(registerViewModel.updateUser(userParam)
                .subscribe(this::onUserUpdated))
    }

    private fun showCountryList(data: List<Country>) {
        countries.clear()
        countries.addAll(data)
        ui.countryAdapter.notifyDataSetChanged()
    }

    private fun updateProgressBarStatus(isShow: Boolean) {
        if (isShow) {
            visibleProgressBar(ui.progressBar)
        } else {
            invisibleProgressBar(ui.progressBar)
        }
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

    private fun onAvatarSelected(data: Intent?) {
        Log.d("xxx", "data" + (data == null))
        val uri = data?.data
        Log.d("xxx", uri.toString())
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
            val bmp = data?.extras?.get("data") as Bitmap
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

    private fun checkPermissionGallery() {
        if (ContextCompat.checkSelfPermission(this
                , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this
                        , arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
            }
        } else {
            intentGallery()
        }
    }

    private fun intentGallery() {
        // Gallery intent
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_PICK

        // Camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val pickTitle = getString(R.string.register_select_image)

        // Chooser intent
        val chooserIntent = Intent.createChooser(galleryIntent, pickTitle)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        startActivityForResult(chooserIntent, REQUEST_CODE_PICK_IMAGE)
    }

    private fun initListener() {
        ui.frAvatar.setOnClickListener(this)
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

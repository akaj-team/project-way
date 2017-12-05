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
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.jetbrains.anko.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.remote.response.ResponseStatus
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.home.HomeActivity
import java.util.*

/**
 * Activity register user
 * Created by haibt on 9/26/17.
 */
class RegisterActivity : BaseActivity() {
    companion object {
        private const val REQUEST_CODE_SPLASH = 1001
        private const val REQUEST_CODE_PICK_IMAGE = 1003
        private const val REQUEST_CODE_GALLERY = 1004
        private const val KEY_FROM_REGISTER = "Register"
        private const val NUM_CHAR_REMOVE = 3
        private const val AVATAR_SIZE = 300
        private const val RESULT_SUCCESS = "Success"
    }

    private var countries: MutableList<Country> = ArrayList()
    internal lateinit var registerViewModel: RegisterViewModel
    private lateinit var ui: RegisterActivityUI
    private var userWay: User? = null
    private var avatarUri: Uri? = null
    internal var avatarBitmap: Bitmap? = null
    internal var isoCode: String? = null
    private var tel: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = RegisterActivityUI(CountryAdapter(this, countries))
        ui.setContentView(this)
        isoCode = getString(R.string.register_iso_code_default)
        if (intent.extras != null && intent.extras.getInt(KEY_FROM_REGISTER) == REQUEST_CODE_SPLASH) {
            registerViewModel = RegisterViewModel(this, true)
        } else {
            registerViewModel = RegisterViewModel(this, false)
            ui.tvSkip.text = resources.getString(R.string.cancel)
        }
    }

    override fun onBindViewModel() {
        addDisposables(registerViewModel.getCountries().subscribe(this::showCountryList),
                registerViewModel.progressBarStatus
                        .observeOnUiThread()
                        .subscribe(this::updateProgressBarStatus),
                registerViewModel.getUser()
                        .subscribe(this::onGetUser, {
                            toast(it.message.toString())
                        }),
                registerViewModel.onBack()
                        .subscribe(this::onBackButtonPress))
    }

    override fun onBackPressed() {
        registerViewModel.onBack()
    }

    override fun onResume() {
        super.onResume()
        registerViewModel.resume()
    }

    private fun onBackButtonPress(isBack: Boolean) {
        if (isBack) {
            super.onBackPressed()
        } else {
            toast(getString(R.string.register_double_click_to_exit))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            setUserAvatar(data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_GALLERY && grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            intentGallery()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    internal fun createUser(userParam: UserParams) {
        val disposable = registerViewModel.createUser(userParam)
                .subscribe(this::onUserCreated)
        addDisposables(disposable)
    }

    internal fun onHandleTextChange() {
        val name: String = ui.edtName.text.toString().trim()
        val phone: String = ui.edtPhone.text.toString().trim()
        if (registerViewModel.isRegister) {
            if (name.isBlank() && phone.isBlank()) {
                ui.tvSkip.visibility = View.VISIBLE
                ui.btnRegister.isEnabled = false
            } else {
                ui.tvSkip.visibility = View.GONE
                ui.btnRegister.isEnabled = true
            }
        } else {
            ui.btnRegister.isEnabled = !(name == userWay?.name && phone == userWay?.phone?.removeRange(0, NUM_CHAR_REMOVE))
            ui.tvSkip.visibility = View.GONE
        }
    }

    internal fun onSkipClick() {
        var name: String = ui.edtName.text.toString().trim()
        val phoneNumber: String = ui.edtPhone.text.toString().trim()
        val userParam = registerViewModel.generateUserInformation(name, phoneNumber, isoCode, avatarBitmap)
        if (registerViewModel.isRegister) {
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
                noButton { it.dismiss() }
            }.show()
        } else {
            if (ui.edtName.text.toString() != userWay?.name
                    || ui.edtPhone.text.toString() != userWay?.phone?.removeRange(0, NUM_CHAR_REMOVE)
                    || avatarUri != null) {
                updateUser(userParam)
            }
        }
        startActivity<HomeActivity>()
    }

    internal fun onUpdateUserInformation(userParam: UserParams) {
        if (ui.edtName.text.toString() != userWay?.name
                || ui.edtPhone.text.toString() != userWay?.phone?.removeRange(0, NUM_CHAR_REMOVE)
                || avatarUri != null) {
            updateUser(userParam)
        }
    }

    private fun updateUser(userParam: UserParams) {
        val disposable = registerViewModel.updateUser(userParam)
                .subscribe(this::onUserUpdated)
        addDisposables(disposable)
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
        val message = responseStatus.message
        if (message == RESULT_SUCCESS) {
            registerViewModel.saveLoginStatus(true)
        }
        toast(message)
    }

    private fun onUserUpdated(responseStatus: ResponseStatus) {
        val updateMessage = responseStatus.message
        if (updateMessage == RESULT_SUCCESS) {
            startActivity<HomeActivity>()
            toast(responseStatus.message)
        } else {
            alert {
                title = getString(R.string.dialog_title_error)
                message = updateMessage
                yesButton {
                    title = getString(R.string.dialog_button_ok)
                    it.dismiss()
                }
            }.show()
        }
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
                ui.imgAvatar.imageBitmap = bitmap
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

    private fun setUserAvatar(data: Intent?) {
        val uri = data?.data
        if (uri != null) {
            Picasso.with(this)
                    .load(uri)
                    .resize(AVATAR_SIZE, AVATAR_SIZE)
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
            val bitmap = data?.extras?.get("data") as? Bitmap
            updateView(bitmap)
        }
    }

    private fun updateView(bitmap: Bitmap?) {
        ui.imgAvatar.imageBitmap = bitmap
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

    internal fun checkPermissionGallery() {
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
}

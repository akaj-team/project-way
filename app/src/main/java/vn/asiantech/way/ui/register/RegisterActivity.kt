package vn.asiantech.way.ui.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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

/**
 *
 * Created by haingoq on 06/12/2017.
 */
class RegisterActivity : BaseActivity() {
    companion object {
        private const val REQUEST_REGISTER = 1001
        private const val REQUEST_CODE_PICK_IMAGE = 1002
        private const val REQUEST_CODE_GALLERY = 1003
        private const val NUM_CHAR_REMOVE = 3
        private const val KEY_FROM_REGISTER = "Register"
    }

    private lateinit var ui: RegisterActivityUI
    private lateinit var registerViewModel: RegisterViewModel
    private val countries: MutableList<Country> = ArrayList()
    private var userWay: User? = null
    internal var isoCode: String? = null
    private var avatarBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = RegisterActivityUI(CountryAdapter(this, countries))
        ui.setContentView(this)
        if (intent.extras != null && intent.extras.getInt(KEY_FROM_REGISTER) == REQUEST_REGISTER) {
            registerViewModel = RegisterViewModel(this, true)
        } else {
            registerViewModel = RegisterViewModel(this, false)
            addDisposables(registerViewModel.getUser()
                    .subscribe(this::handleGetUserCompleted, this::handleGetUserError))
            ui.tvSkip.text = getString(R.string.cancel)
        }
    }

    override fun onBindViewModel() {
        addDisposables(
                registerViewModel.getCountries()
                        .subscribe(this::handleGetCountriesCompleted),

                registerViewModel.progressBarStatus
                        .observeOnUiThread()
                        .subscribe(this::handleGetProgressBarStatusCompleted),

                registerViewModel.createDefaultUserStatus
                        .observeOnUiThread()
                        .subscribe(this::handleSkipClicked),

                registerViewModel.backStatus
                        .observeOnUiThread()
                        .subscribe(this::handleBackKeyEvent))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        registerViewModel.eventBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
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
                                avatarBitmap = bitmap
                                handleGetAvatarCompleted()
                            }
                        })
            } else {
                avatarBitmap = data.extras.get("data") as Bitmap
                handleGetAvatarCompleted()
            }
        }
    }

    internal fun onHandleTextChange(name: String, phone: String) {
        if (registerViewModel.isRegister) {
            if (name.isBlank() && phone.isBlank()) {
                ui.btnRegister.isEnabled = false
                ui.tvSkip.visibility = View.GONE
            } else {
                ui.btnRegister.isEnabled = true
                ui.tvSkip.visibility = View.VISIBLE
            }
        } else {
            ui.btnRegister.isEnabled = !(name == userWay?.name && phone == userWay?.phone?.removeRange(0, NUM_CHAR_REMOVE))
        }
    }

    internal fun eventClicked(view: View) {
        when (view) {
            ui.frAvatar -> checkPermissionGallery()

            ui.tvSkip ->
                if (registerViewModel.isRegister) {
                    // Create default user
                    registerViewModel.createUserDefault(getString(R.string.register_user_name_default))
                } else {
                    // Come back Home when cancel update user
                    startActivity<HomeActivity>()
                }

            ui.btnRegister -> {
                // Create User param
                val userParam = registerViewModel.generateUserInformation(
                        ui.edtName.text.toString().trim(),
                        ui.edtPhone.text.toString().trim(),
                        isoCode, avatarBitmap)

                if (registerViewModel.isRegister) {
                    // Register user
                    addDisposables(registerViewModel.createUser(userParam)
                            .subscribe(this::handleCreateUserCompleted))
                    // Save login status to SharePreference
                    registerViewModel.saveLoginStatus(true)
                } else {
                    // Update user
                    if (ui.edtName.text.toString() != userWay?.name
                            || ui.edtPhone.text.toString() != userWay?.phone?.removeRange(0, NUM_CHAR_REMOVE)
                            || avatarBitmap != null) {
                        addDisposables(registerViewModel.updateUser(userParam)
                                .subscribe(this::handleUpdateUserCompleted))
                    }
                }
            }
        }
    }

    private fun handleGetUserCompleted(user: User) {
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
            }
        }
        Picasso.with(this)
                .load(user.photo)
                .into(target)
        ui.imgAvatar.tag = target
        ui.edtName.setText(user.name)
        ui.edtPhone.setText(user.phone)
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
                        break
                    }
                }
                ui.edtPhone.setText(basePhone[1])
            } else {
                ui.edtPhone.setText(basePhone[0])
            }
        }
    }

    private fun handleGetUserError(t: Throwable) {
        toast(t.message.toString())
    }

    private fun handleCreateUserCompleted(responseStatus: ResponseStatus) {
        toast(responseStatus.message)
    }

    private fun handleUpdateUserCompleted(responseStatus: ResponseStatus) {
        toast(responseStatus.message)
    }

    private fun handleGetCountriesCompleted(list: List<Country>) {
        countries.clear()
        countries.addAll(list)
        ui.countryAdapter.notifyDataSetChanged()
    }

    private fun handleBackKeyEvent(isBack: Boolean) {
        if (isBack) {
            super.onBackPressed()
        } else {
            toast(getString(R.string.register_double_click_to_exit))
        }
    }

    private fun handleGetProgressBarStatusCompleted(isShow: Boolean) {
        if (isShow) {
            visibleProgressBar(ui.progressBar)
        } else {
            invisibleProgressBar(ui.progressBar)
        }
    }

    private fun handleGetAvatarCompleted() {
        ui.imgAvatar.imageBitmap = avatarBitmap
        ui.btnRegister.isEnabled = true
    }

    private fun handleSkipClicked(userParam: UserParams) {
        alert {
            title = getString(R.string.register_title_dialog)
            message = getString(R.string.register_message_dialog)
            yesButton {
                addDisposables(registerViewModel.createUser(userParam)
                        .subscribe(this@RegisterActivity::handleCreateUserCompleted))
            }
            noButton { it.dismiss() }
        }.show()
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

    private fun visibleProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun invisibleProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}

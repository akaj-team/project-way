package vn.asiantech.way.ui.register

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.models.ErrorResponse
import com.hypertrack.lib.models.SuccessResponse
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_register.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.extension.hideKeyboard
import vn.asiantech.way.extension.toBase64
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.home.HomeActivity
import java.io.ByteArrayOutputStream

/**
 * Activity register user
 * Created by haibt on 9/26/17.
 */
class RegisterActivity : BaseActivity(), TextView.OnEditorActionListener
        , View.OnClickListener, TextWatcher {
    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
        private const val REQUEST_CODE_GALLERY = 500
        const val INTENT_CODE_SPLASH = 100
        const val INTENT_CODE_HOME = 101
        const val INTENT_REGISTER = "Register"
        const val SHARED_NAME = "shared"
        const val KEY_LOGIN = "login"
    }

    var mBitmap: Bitmap? = null
    var mCountries: List<Country> = ArrayList()
    var mPreviousName: String? = null
    var mPreviousPhone: String? = null
    var mIsoCode: String? = null
    var mTel: String? = null
    var mIsExitPressed = false
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initListener()
        mCountries = getCountries(readJsonFromDirectory())
        mIsoCode = getString(R.string.register_iso_code_default)
        mSharedPreferences = getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
        initCountrySpinner()
        setUserInformation()
        frAvatar.setOnClickListener {
            checkPermissionGallery()
        }
    }

    override fun onBackPressed() {
        if (intent.extras.getInt(INTENT_REGISTER) == INTENT_CODE_SPLASH) {
            if (!mIsExitPressed) {
                mIsExitPressed = true
                toast(getString(R.string.register_double_click_to_exit))
                Handler().postDelayed({
                    mIsExitPressed = false
                }, 1500)
            } else {
                finishAffinity()
            }
        } else if (intent.extras.getInt(INTENT_REGISTER) == INTENT_CODE_HOME) {
            super.onBackPressed()
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        when (v?.id) {
            R.id.edtName -> {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edtPhoneNumber.requestFocus()
                    return true
                }
            }
            R.id.edtPhoneNumber -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edtPhoneNumber.hideKeyboard(this)
                    return true
                }
            }
        }
        return false
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSave -> {
                val name: String = edtName.text.toString().trim()
                val phoneNumber: String = edtPhoneNumber.text.toString().trim()
                if (name.isBlank()) {
                    edtName.setText(R.string.register_user_name_default)
                }
                if (phoneNumber.isBlank()) {
                    edtPhoneNumber.text = null
                }
                createUser(name, phoneNumber)
                startActivity(Intent(this, HomeActivity::class.java))
            }
            R.id.tvCancel -> {
                startActivity(Intent(this, HomeActivity::class.java))
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
        val name: String = edtName.text.toString().trim()
        val phone: String = edtPhoneNumber.text.toString().trim()
        var tel: String = ""
        if (tvTel.text.isNotEmpty()) {
            tel = tvTel.text.toString().removeRange(0, 1)
        }
        if ((name.isBlank() && phone.isBlank())
                || (mPreviousName?.trim() == name
                && mPreviousPhone?.removeRange(0, 3) == phone
                && mTel == tel)) {
            tvCancel.text = getString(R.string.register_skip)
            btnSave.isEnabled = false
        } else {
            tvCancel.text = getString(R.string.register_cancel)
            btnSave.isEnabled = true
        }

    }

    private fun createUser(name: String, phoneNumber: String) {
        val userParam: UserParams = UserParams()
                .setName(name)
                .setPhoto(mBitmap?.toBase64())
                .setPhone(mIsoCode?.plus("/")?.plus(phoneNumber))
                .setLookupId(phoneNumber)

        // Create new user
        if (mPreviousPhone?.removeRange(0, 3) != phoneNumber) {
            HyperTrack.getOrCreateUser(userParam, object : HyperTrackCallback() {
                override fun onSuccess(p0: SuccessResponse) {
                    HyperTrack.startTracking()
                    saveLoginStatus(true)
                    toast(getString(R.string.register_create_user))
                }

                override fun onError(p0: ErrorResponse) {
                    // No-op
                }
            })
        } else {
            // Update user information
            HyperTrack.updateUser(userParam, object : HyperTrackCallback() {
                override fun onSuccess(p0: SuccessResponse) {
                    HyperTrack.startTracking()
                    toast(getString(R.string.register_update_user))
                }

                override fun onError(p0: ErrorResponse) {
                    // No-op
                }
            })
        }
    }

    private fun setUserInformation() {
        visibleProgressBar(progressBar)
        HyperTrack.getUser(object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                val user: User? = response.responseObject as User
                updateView(user)
                invisibleProgressBar(progressBar)
            }

            override fun onError(p0: ErrorResponse) {
                invisibleProgressBar(progressBar)
            }
        })
    }

    private fun updateView(user: User?) {
        val target: Target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                visibleProgressBar(progressBarAvatar)
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                // No-op
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                imgAvatar.setImageBitmap(bitmap)
                invisibleProgressBar(progressBarAvatar)
                mBitmap = bitmap
            }
        }
        Picasso.with(this)
                .load(user?.photo)
                .into(target)
        imgAvatar.tag = target
        edtName.setText(user?.name)
        val basePhone: List<String>? = user?.phone?.split("/")
        if (basePhone!!.size > 1) {
            // Set isoCode
            mIsoCode = basePhone[0]
            for (i in 0 until mCountries.size) {
                if (mIsoCode == mCountries[i].iso) {
                    spinnerNation.setSelection(i)
                    val tel = mCountries[i].tel
                    tvTel.text = getString(R.string.register_plus).plus(tel)
                    mTel = tel
                    break
                }
            }
            edtPhoneNumber.setText(basePhone[1])
        } else {
            edtPhoneNumber.setText(basePhone[0])
        }
        btnSave.isEnabled = checkUser(user.name, user.phone)
        mPreviousName = user.name
        mPreviousPhone = user.phone
    }

    private fun checkUser(name: String, phone: String): Boolean {
        if (mPreviousName != name.trim() && mPreviousPhone != phone.trim()) {
            return true
        }
        return false
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

    private fun checkPermissionGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
            }
        } else {
            intentGallery()
        }
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

    private fun initListener() {
        edtName.setOnEditorActionListener(this)
        edtName.addTextChangedListener(this)
        edtPhoneNumber.setOnEditorActionListener(this)
        edtPhoneNumber.addTextChangedListener(this)
        tvTel.addTextChangedListener(this)
        btnSave.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
    }

    private fun initCountrySpinner() {
        spinnerNation.adapter = CountrySpinnerAdapter(this, R.layout.item_list_country, mCountries)
        // Set priority for VietNam nation (index 198)
        spinnerNation.setSelection(198)
        spinnerNation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                // No-op
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tvTel.text = getString(R.string.register_plus).plus(mCountries[position].tel)
                mIsoCode = mCountries[position].iso
            }
        }
    }

    /**
     * Read file json from raw directory
     */
    private fun readJsonFromDirectory(): String {
        val iStream = resources.openRawResource(R.raw.countries)
        val byteStream = ByteArrayOutputStream()
        val buffer = ByteArray(iStream.available())
        iStream.read(buffer)
        byteStream.write(buffer)
        byteStream.close()
        iStream.close()
        return byteStream.toString()
    }

    private fun getCountries(json: String): List<Country> {
        return Gson().fromJson(json, object : TypeToken<List<Country>>() {}.type)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE) {
            val uri: Uri? = data?.data
            Picasso.with(this)
                    .load(uri)
                    .resize(300, 300)
                    .into(object : Target {
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            visibleProgressBar(progressBarAvatar)
                        }

                        override fun onBitmapFailed(errorDrawable: Drawable?) {
                            // No-op
                        }

                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            invisibleProgressBar(progressBarAvatar)
                            imgAvatar.setImageBitmap(bitmap)
                            mBitmap = bitmap
                        }
                    })
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

    private fun saveLoginStatus(login: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(KEY_LOGIN, login)
        editor.apply()
    }
}

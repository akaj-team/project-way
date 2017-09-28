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
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
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
import vn.asiantech.way.models.Country
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.util.Utils
import java.io.ByteArrayOutputStream

/**
 * Activity register user
 * Created by haibt on 9/26/17.
 */
class RegisterActivity : BaseActivity(), TextView.OnEditorActionListener
        , View.OnClickListener, TextWatcher {
    companion object {
        const private val REQUEST_CODE_PICK_IMAGE = 1001
        const private val REQUEST_CODE_GALLERY = 500
    }

    var mBitmap: Bitmap? = null
    var mCountries: List<Country> = ArrayList()
    var mPreviousName: String? = null
    var mPreviousPhone: String? = null
    var mIsoCode: String? = null
    var mTel: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
        mCountries = getCountries(readJsonFromDirectory())
        mIsoCode = resources.getString(R.string.iso_code_default)
        initCountrySpinner()
        setUserInformation()
        frAvatar.setOnClickListener {
            checkPermissionGallery()
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
                    Utils.hideKeyboard(this, edtPhoneNumber)
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
                    edtName.setText(R.string.user_name_default)
                }
                if (phoneNumber.isBlank()) {
                    edtPhoneNumber.text = null
                }
                createUser(name, phoneNumber)
                /**
                 * TODO
                 * intent to home activity
                 */
            }
            R.id.tvCancel -> {
                /**
                 * TODO
                 * intent to home activity
                 */
            }

        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val name: String = edtName.text.toString().trim()
        val phone: String = edtPhoneNumber.text.toString().trim()
        val tel: String = tvTel.text.toString().removeRange(0, 1)
        if ((name.isBlank() && phone.isBlank())
                || (mPreviousName?.trim() == name
                && mPreviousPhone?.removeRange(0, 3) == phone
                && mTel == tel)) {
            tvCancel.text = resources.getString(R.string.skip)
            btnSave.isEnabled = false
        } else {
            tvCancel.text = resources.getString(R.string.cancel)
            btnSave.isEnabled = true
        }
    }

    private fun createUser(name: String, phoneNumber: String) {
        val userParam: UserParams = UserParams()
                .setName(name)
                .setPhoto(encodeImage(mBitmap))
                .setPhone(mIsoCode?.plus("/")?.plus(phoneNumber))
                .setLookupId(phoneNumber)

        //create new user
        if (mPreviousPhone != phoneNumber) {
            HyperTrack.getOrCreateUser(userParam, object : HyperTrackCallback() {
                override fun onSuccess(p0: SuccessResponse) {
                    HyperTrack.startTracking()
                    Log.d("xxx", "create ok")
                }

                override fun onError(p0: ErrorResponse) {
                    Log.d("xxx", "create fail" + p0.errorMessage)
                }
            })
        } else {
            // update user information
            HyperTrack.updateUser(userParam, object : HyperTrackCallback() {
                override fun onSuccess(p0: SuccessResponse) {
                    HyperTrack.startTracking()
                    Log.d("xxx", "update ok")
                }

                override fun onError(p0: ErrorResponse) {
                    Log.d("xxx", "update fail" + p0.errorMessage)
                }
            })
        }

    }

    private fun setUserInformation() {
        visibleProgressBar()
        HyperTrack.getUser(object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                val user: User? = response.responseObject as User
                updateView(user)
                invisibleProgressBar()
            }

            override fun onError(p0: ErrorResponse) {
                invisibleProgressBar()
            }
        })
    }

    private fun updateView(user: User?) {
        val target: Target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                imgAvatar.setImageBitmap(bitmap)
                mBitmap = bitmap
            }
        }
        Picasso.with(this)
                .load(user?.photo)
                .resize(300, 300)
                .into(target)
        imgAvatar.tag = target
        edtName.setText(user?.name)
        val basePhone: List<String>? = user?.phone?.split("/")
        if (basePhone!!.size > 1) {
            //set isoCode
            mIsoCode = basePhone[0]
            for (i in 0..mCountries.size - 1) {
                if (mIsoCode == mCountries[i].iso) {
                    spinnerNation.setSelection(i)
                    val tel = mCountries[i].tel
                    tvTel.text = tel
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
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
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

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun invisibleProgressBar() {
        progressBar.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun initView() {
        edtName.setOnEditorActionListener(this)
        edtName.addTextChangedListener(this)
        edtPhoneNumber.setOnEditorActionListener(this)
        edtPhoneNumber.addTextChangedListener(this)
        tvTel.addTextChangedListener(this)
        btnSave.setOnClickListener(this)
    }

    private fun initCountrySpinner() {
        spinnerNation.adapter = CountrySpinnerAdapter(this, R.layout.item_list_country, mCountries)
        spinnerNation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                tvTel.text = resources.getString(R.string.plus).plus(mCountries[position].tel)
                mIsoCode = mCountries[position].iso
            }
        }
    }

    /**
     * read file json from raw directory
     */
    private fun readJsonFromDirectory(): String {
        val iStream = resources.openRawResource(R.raw.countries)
        val byteStream: ByteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(iStream.available())
        iStream.read(buffer)
        byteStream.write(buffer)
        byteStream.close()
        iStream.close()
        return byteStream.toString()
    }

    private fun getCountries(json: String): List<Country> {
        val countries: List<Country> = Gson().fromJson(json, object : TypeToken<List<Country>>() {}.type)
        return countries
    }

    private fun encodeImage(bitmap: Bitmap?): String {
        val baoStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 60, baoStream)
        val b = baoStream.toByteArray()
        val encodeImage = Base64.encodeToString(b, Base64.DEFAULT)
        return encodeImage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE) {
            val uri: Uri = data!!.data
            imgAvatar.setImageURI(uri)
            mBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
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
}

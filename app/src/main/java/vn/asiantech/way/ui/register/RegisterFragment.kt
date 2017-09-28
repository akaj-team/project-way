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
import android.view.*
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
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import vn.asiantech.way.R
import vn.asiantech.way.models.Country
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.util.Utils
import java.io.ByteArrayOutputStream

/**
 * Fragment register user
 * Created by haibt on 9/26/17.
 */
class RegisterFragment : BaseFragment(), TextView.OnEditorActionListener
        , View.OnClickListener, TextWatcher {
    companion object {
        const private val REQUEST_CODE_PICK_IMAGE = 1001
        const private val REQUEST_CODE_GALLERY = 500
    }

    var mIsoCode: String? = null
    var mBitmap: Bitmap? = null
    var mCountries: List<Country> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_register, container, false)
        initView(view)
        mCountries = getCountries(readJsonFromDirectory())
        initCountrySpinner(view)
        setUserInformation(view)
        view.frAvatar.setOnClickListener {
            checkPermissionGallery()
        }
        mIsoCode = resources.getString(R.string.iso_code_default)
        return view
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
                    Utils.hideKeyboard(context, edtPhoneNumber)
                    return true
                }
            }
        }
        return false
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSave -> {
                val name: String = edtName.text.toString()
                val phoneNumber: String = edtPhoneNumber.text.toString()
                if (name.isBlank()) {
                    edtName.setText(R.string.user_name_default)
                }
                if (phoneNumber.isBlank()) {
                    edtPhoneNumber.text = null
                }
                createUser(name, phoneNumber)
            }
            R.id.tvCancel -> {
                //TODO
            }

        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (edtName.text.isBlank() && edtPhoneNumber.text.isBlank()) {
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
                .setPhone(phoneNumber)
                .setLookupId(mIsoCode)
        Log.d("xxx", "" + mBitmap.toString())
        HyperTrack.getOrCreateUser(userParam, object : HyperTrackCallback() {
            override fun onSuccess(p0: SuccessResponse) {
                HyperTrack.startTracking()
                Log.d("xxx", "success")
            }

            override fun onError(p0: ErrorResponse) {
                Log.e("xxx", " create error " + p0.errorMessage)
            }
        })
    }

    private fun setUserInformation(view: View) {
        visibleProgressBar(view)
        HyperTrack.getUser(object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                val user: User? = response.responseObject as User
                updateView(view, user)
                invisibleProgressBar(view)
            }

            override fun onError(p0: ErrorResponse) {
                invisibleProgressBar(view)
            }
        })
    }

    private fun updateView(view: View, user: User?) {
        val target: Target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                view.imgAvatar.setImageBitmap(bitmap)
                mBitmap = bitmap
            }
        }
        Picasso.with(context).load(user?.photo).into(target)
        view.imgAvatar.tag = target
        view.edtName.setText(user?.name)
        view.edtPhoneNumber.setText(user?.phone)
        val isoCode: String = user!!.lookupId
        for (i in 0..mCountries.size - 1) {
            if (isoCode == mCountries[i].iso) {
                view.spinnerNation.setSelection(i)
                view.tvBaseNumber.text = mCountries[i].tel
                break
            }
        }
    }

    private fun intentGallery() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    private fun checkPermissionGallery() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
            }
        } else {
            intentGallery()
        }
    }

    private fun visibleProgressBar(view: View) {
        view.progressBar.visibility = View.VISIBLE
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun invisibleProgressBar(view: View) {
        view.progressBar.visibility = View.GONE
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun initView(view: View) {
        view.edtName.setOnEditorActionListener(this)
        view.edtPhoneNumber.setOnEditorActionListener(this)
        view.btnSave.setOnClickListener(this)
        view.edtName.addTextChangedListener(this)
        view.edtPhoneNumber.addTextChangedListener(this)
    }

    private fun initCountrySpinner(view: View) {
        view.spinnerNation.adapter = CountrySpinnerAdapter(context, R.layout.item_list_country, mCountries)
        view.spinnerNation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                mIsoCode = mCountries[position].iso
                view.tvBaseNumber.text = resources.getString(R.string.plus).plus(mCountries[position].tel)
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
            mBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
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

package vn.asiantech.way.ui.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import vn.asiantech.way.R
import vn.asiantech.way.models.Country
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.util.Utils
import java.io.ByteArrayOutputStream

/**
 * Fragment register
 * Created by haibt on 9/26/17.
 */
class RegisterFragment : BaseFragment(), TextView.OnEditorActionListener {
    companion object {
        const private val REQUEST_CODE_PICK_IMAGE = 1001
        const private val REQUEST_CODE_GALLERY = 500
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_register, container, false)
        initView(view)
        initCountrySpinner(view)
        view.frAvatar.setOnClickListener {
            checkPermissionGallery()
        }
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

    private fun initView(view: View) {
        view.edtName.setOnEditorActionListener(this)
        view.edtPhoneNumber.setOnEditorActionListener(this)
    }

    private fun initCountrySpinner(view: View) {
        val countries: List<Country> = getCountries(readJsonFromDirectory())
        view.spinnerNation.adapter = CountrySpinnerAdapter(context, R.layout.item_list_country, countries)
        view.spinnerNation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                view.tvBaseNumber.text = resources.getString(R.string.plus).plus(countries[position].tel)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE) {
            val uri: Uri = data!!.data
            imgAvatar.setImageURI(uri)
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

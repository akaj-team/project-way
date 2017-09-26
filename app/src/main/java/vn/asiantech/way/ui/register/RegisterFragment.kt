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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import vn.asiantech.way.R
import vn.asiantech.way.models.Country
import vn.asiantech.way.ui.base.BaseFragment
import java.io.ByteArrayOutputStream

/**
 * Fragment register
 * Created by haibt on 9/26/17.
 */
class RegisterFragment : BaseFragment() {
    companion object {
        const private val REQUEST_CODE_PICK_IMAGE = 1001
        const private val REQUEST_CODE_GALLERY = 500
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_register, container, false)
        initCountrySpinner(view)
        view.frAvatar.setOnClickListener {
            checkPermissionGallery()
        }
        return view
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

    private fun initCountrySpinner(view: View) {
        view.spinnerNation.adapter = CountrySpinnerAdapter(context, R.layout.item_list_country,
                getCountries(readJsonFromDirectory()))

    }

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
        Log.d("xxx", "" + countries.size)
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

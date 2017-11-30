package vn.asiantech.way.ui.register

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.remote.response.ResponseStatus
import vn.asiantech.way.ui.base.BaseActivity

/**
 * Activity register user
 * Created by haibt on 9/26/17.
 */
class RegisterActivity : BaseActivity() {
    private var countries = MutableList(0, { Country("", "") })
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var ui: RegisterActivityUI
    private var bmp: Bitmap? = null
    private var isoCode: String? = null
    private var tel: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = RegisterActivityUI(CountryAdapter(countries, HashMap<String, Bitmap>(), "VietNam"))
        ui.setContentView(this)
        registerViewModel = RegisterViewModel(this)
    }

    override fun onBindViewModel() {
        addDisposables(registerViewModel.getCountries()
                .subscribe(this::getCountries))
    }

    private fun getCountries(data: List<Country>) {
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
                bmp = bitmap
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
                        spinnerNation.setSelection(i)
                        val telephone = countries[i].tel
                        ui.tvTel.text = getString(R.string.register_plus).plus(telephone)
//                        ui.imgFlag.
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

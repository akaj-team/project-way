package vn.asiantech.way.ui.register

import android.graphics.Bitmap
import android.os.Bundle
import com.hypertrack.lib.models.UserParams
import org.jetbrains.anko.setContentView
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

    private fun createUser(userParams: UserParams) {

    }
}

package vn.asiantech.way.ui.register

import android.graphics.Bitmap
import android.os.Bundle
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.ui.base.BaseActivity

/**
 * Activity register user
 * Created by haibt on 9/26/17.
 */
class RegisterActivity : BaseActivity() {
    companion object {
        const val INTENT_CODE_SPLASH = 100
        const val INTENT_CODE_HOME = 101
        const val INTENT_REGISTER = "Register"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RegisterActivityUI(CountryAdapter(createCountries(), HashMap<String, Bitmap>(), "VietNam"))
                .setContentView(this)
    }

    private fun createCountries(): List<Country> {
        val countries = ArrayList<Country>()
        countries.add(Country("+84", "VN"))
        countries.add(Country("+84", "VN"))
        countries.add(Country("+84", "VN"))
        countries.add(Country("+84", "VN"))
        countries.add(Country("+84", "VN"))
        countries.add(Country("+84", "VN"))
        return countries
    }
}

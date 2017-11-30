package vn.asiantech.way.data.model

import java.util.*

/**
 * Model store information of country
 * Created by haibt on 9/26/17.
 */
data class Country(private val iso: String, val tel: String) {
    val countryName: String
        get() {
            val locale = Locale(Locale.getDefault().displayLanguage, iso)
            return locale.displayCountry.trim()
        }
    val flagFilePath: String
        get() = "file:///android_asset/flags/${iso.toLowerCase()}.png"
}
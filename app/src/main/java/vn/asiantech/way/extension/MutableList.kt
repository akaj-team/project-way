package vn.asiantech.way.extension

import vn.asiantech.way.data.model.Country

/**
 *
 * Created by haingoq on 22/12/2017.
 */
fun MutableList<Country>.getCountryByIso(iso: String): Country? {
    val country = filter { iso == it.iso }[0]
    if (country.iso.isNotEmpty()) {
        return country
    }
    return null
}

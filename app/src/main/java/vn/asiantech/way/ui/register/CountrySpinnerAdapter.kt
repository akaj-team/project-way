package vn.asiantech.way.ui.register

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_list_country.view.*
import vn.asiantech.way.models.Country
import java.util.*

/**
 * Adapter spinner Country
 * Created by haibt on 9/26/17.
 */
class CountrySpinnerAdapter(val mContext: Context,
                            val mResource: Int,
                            val mCountries: List<Country>) : ArrayAdapter<Country>(mContext, mResource, mCountries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomSelectionView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomDialogView(position, parent)
    }

    private fun getCustomDialogView(position: Int, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(mContext).inflate(mResource, parent, false)
        val isoCode: String = mCountries[position].iso
        view.imgFlag.setImageResource(getCountryFlag(isoCode))
        view.tvCountryName.text = getCountryName(isoCode)
        return view
    }

    private fun getCustomSelectionView(position: Int, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(mContext).inflate(mResource, parent, false)
        val isoCode: String = mCountries[position].iso
        view.imgFlag.setImageResource(getCountryFlag(isoCode))
        view.tvCountryName.visibility = View.GONE
        return view
    }

    private fun getCountryFlag(isoCode: String): Int {
        val imageName = isoCode.trim().toLowerCase()
        val imageResId = mContext.resources.getIdentifier("drawable/" + imageName, null, mContext.packageName)
        return imageResId
    }

    private fun getCountryName(isoCode: String): String {
        val locale = Locale(Locale.getDefault().displayLanguage, isoCode)
        return locale.displayCountry.trim()
    }
}

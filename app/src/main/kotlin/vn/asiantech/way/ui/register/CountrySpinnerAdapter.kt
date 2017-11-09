package vn.asiantech.way.ui.register

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_list_country.view.*
import vn.asiantech.way.data.model.Country
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Adapter custom spinner Country
 * Created by haibt on 9/26/17.
 */
class CountrySpinnerAdapter(private val mContext: Context,
                            private val mResource: Int,
                            private val mCountries: List<Country>) : ArrayAdapter<Country>(mContext, mResource, mCountries) {
    var mFlags: HashMap<String, Bitmap>? = getFlagMap()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomSelectionView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomDialogView(position, parent)
    }

    private fun getCustomDialogView(position: Int, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(mContext).inflate(mResource, parent, false)
        val isoCode: String = mCountries[position].iso
        view.imgFlag.setImageBitmap(mFlags?.get(isoCode.plus(".png").toLowerCase()))
        view.tvCountryName.text = getCountryName(isoCode.toLowerCase())
        return view
    }

    private fun getCustomSelectionView(position: Int, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(mContext).inflate(mResource, parent, false)
        val isoCode: String = mCountries[position].iso
        view.imgFlag.setImageBitmap(mFlags?.get(isoCode.plus(".png").toLowerCase()))
        view.tvCountryName.visibility = View.GONE
        return view
    }

    private fun getFlagMap(): HashMap<String, Bitmap> {
        val flagMap: HashMap<String, Bitmap> = HashMap()
        val flagArray = mContext.assets.list("flags")
        val flags: MutableList<String> = ArrayList()
        flags.addAll(flagArray)
        flags.forEach {
            val inputStream: InputStream = mContext.assets.open("flags/".plus(it))
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
            flagMap.put(it, bitmap)
        }
        return flagMap
    }

    private fun getCountryName(isoCode: String): String {
        val locale = Locale(Locale.getDefault().displayLanguage, isoCode)
        return locale.displayCountry.trim()
    }
}

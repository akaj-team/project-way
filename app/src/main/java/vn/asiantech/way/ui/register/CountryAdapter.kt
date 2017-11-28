package vn.asiantech.way.ui.register

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.data.model.Country
import java.io.InputStream
import java.util.*

/**
 * Created by haingoq on 27/11/2017.
 */
class CountryAdapter(private val context: Context, private val countries: List<Country>) : RecyclerView.Adapter<CountryAdapter.CountryHolder>() {
    companion object {
        private const val IMG_FLAG_ID = 1001
    }

    val mFlags: HashMap<String, Bitmap> = getFlagMap()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CountryHolder {
        return ItemCountryUI().createView(AnkoContext.create(parent!!.context, parent, false))
                .tag as CountryHolder
    }

    override fun onBindViewHolder(holder: CountryHolder?, position: Int) {
        holder?.bindHolder(countries[position])
    }

    override fun getItemCount() = countries.size

    inner class ItemCountryUI : AnkoComponent<ViewGroup> {
        private lateinit var imgFlag: ImageView
        private lateinit var tvTel: TextView
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val itemView = with(ui) {
                linearLayout {
                    lparams(matchParent, wrapContent)
                    padding = dip(10)
                    gravity = Gravity.START
                    imgFlag = imageView { id = IMG_FLAG_ID }.
                            lparams(dip(36), dip(28)){
                                gravity = Gravity.CENTER_VERTICAL
                            }
                    tvTel = textView{
                        gravity = Gravity.CENTER_VERTICAL
                    }.lparams(wrapContent, dip(28)) {
                        leftMargin = dip(10)
                    }
                }
            }
            itemView.tag = CountryHolder(itemView, imgFlag, tvTel)
            return itemView
        }
    }

    var onItemClick: (Country) -> Unit = {}

    inner class CountryHolder(itemView: View, private val imgFlag: ImageView,
                              private val tvTel: TextView) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.onClick {
                onItemClick(countries[adapterPosition])
            }
        }
        fun bindHolder(country: Country) {
            val isoCode = country.iso
            imgFlag.setImageBitmap(mFlags[isoCode.plus(".png").toLowerCase()])
            tvTel.text = getCountryName(isoCode.toLowerCase())
        }
    }

    private fun getCountryName(isoCode: String): String {
        val locale = Locale(Locale.getDefault().displayLanguage, isoCode)
        return locale.displayCountry.trim()
    }

    private fun getFlagMap(): HashMap<String, Bitmap> {
        val flagMap: HashMap<String, Bitmap> = HashMap()
        val flagArray = context.assets.list("flags")
        val flags: MutableList<String> = ArrayList()
        flags.addAll(flagArray)
        flags.forEach {
            val inputStream: InputStream = context.assets.open("flags/".plus(it))
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
            flagMap.put(it, bitmap)
        }
        return flagMap
    }
}

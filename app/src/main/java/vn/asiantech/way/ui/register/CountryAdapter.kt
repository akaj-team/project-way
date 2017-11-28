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
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap

/**
 * Adapter of RecyclerView Country.
 * Created by haingoq on 27/11/2017.
 */
class CountryAdapter(private val context: Context, private val countries: List<Country>,
                     private val flagMap: HashMap<String, Bitmap>, private val countryName: String) :
        RecyclerView.Adapter<CountryAdapter.CountryHolder>() {
    companion object {
        private const val ID_IMG_FLAG = 1001
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemCountryUI()
            .createView(AnkoContext.create(parent.context, parent, false))
            .tag as? CountryHolder

    override fun onBindViewHolder(holder: CountryHolder?, position: Int) {
        holder?.bindHolder(countries[position])
    }

    override fun getItemCount() = countries.size

    /**
     *  Item country UI
     */
    inner class ItemCountryUI : AnkoComponent<ViewGroup> {
        private lateinit var imgFlag: ImageView
        private lateinit var tvTel: TextView
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val itemView = with(ui) {
                linearLayout {
                    lparams(matchParent, wrapContent)
                    padding = dip(dimen(R.dimen.register_screen_avatar_margin))
                    gravity = Gravity.START
                    imgFlag = imageView { id = ID_IMG_FLAG }.
                            lparams(dip(dimen(R.dimen.register_screen_img_flag_width)),
                                    dip(dimen(R.dimen.margin_xxhigh))) {
                                gravity = Gravity.CENTER_VERTICAL
                            }
                    tvTel = textView {
                        gravity = Gravity.CENTER_VERTICAL
                    }.lparams(wrapContent, dip(dimen(R.dimen.margin_xxhigh))) {
                        leftMargin = dip(dimen(R.dimen.register_screen_avatar_margin))
                    }
                }
            }
            itemView.tag = CountryHolder(itemView, imgFlag, tvTel)
            return itemView
        }
    }

    var onItemClick: (Country) -> Unit = {}

    /**
     * Holder Country
     */
    inner class CountryHolder(itemView: View, private val imgFlag: ImageView, private val tvTel: TextView) :
            RecyclerView.ViewHolder(itemView) {
        init {
            itemView.onClick {
                onItemClick(countries[adapterPosition])
            }
        }

        /**
         * Bind country
         */
        fun bindHolder(country: Country) {
            val isoCode = country.iso
            imgFlag.setImageBitmap(flagMap[isoCode.plus(".png").toLowerCase()])
            tvTel.text = countryName
        }
    }
}

package vn.asiantech.way.ui.register

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country

/**
 * Adapter of RecyclerView Country.
 * Created by haingoq on 27/11/2017.
 */
class CountryAdapter(private val countries: List<Country>) :
        RecyclerView.Adapter<CountryAdapter.CountryHolder>() {

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
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val itemView = with(ui) {
                linearLayout {
                    lparams(matchParent, wrapContent)
                    padding = dimen(R.dimen.register_screen_avatar_margin)
                    gravity = Gravity.START
                    imageView {
                        id = R.id.country_adapter_img_flag
                    }.lparams(dimen(R.dimen.register_screen_img_flag_width),
                            dimen(R.dimen.margin_xxhigh)) {
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    textView {
                        id = R.id.country_adapter_tv_tel
                        gravity = Gravity.CENTER_VERTICAL
                    }.lparams(wrapContent, dimen(R.dimen.margin_xxhigh)) {
                        leftMargin = dimen(R.dimen.register_screen_avatar_margin)
                    }
                }
            }
            itemView.tag = CountryHolder(itemView)
            return itemView
        }
    }

    var onItemClick: (Country) -> Unit = {}

    /**
     * Holder Country
     */
    inner class CountryHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
        private val imgFlag: ImageView = itemView.find(R.id.country_adapter_img_flag)
        private val tvTel: TextView = itemView.find(R.id.country_adapter_tv_tel)

        init {
            itemView.onClick {
                onItemClick(countries[adapterPosition])
            }
        }

        /**
         * Bind country
         */
        fun bindHolder(country: Country) {
            Picasso.with(itemView.context).load(country.flagFilePath).into(imgFlag)
            tvTel.text = country.countryName
        }
    }
}

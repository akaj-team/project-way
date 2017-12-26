package vn.asiantech.way.ui.register

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.data.model.Country

/**
 * Adapter of RecyclerView Country.
 * Created by haingoq on 27/11/2017.
 */
class CountryAdapter(val context: Context, private val countries: List<Country>) :
        RecyclerView.Adapter<CountryAdapter.CountryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder? {
        val countryItemUI = CountryItemUI()
        return CountryHolder(countryItemUI,
                countryItemUI.createView(AnkoContext.Companion.create(context, parent, false)))
    }

    override fun onBindViewHolder(holder: CountryHolder?, position: Int) {
        holder?.bindHolder(countries[position])
    }

    override fun getItemCount() = countries.size

    var onItemClick: (Country) -> Unit = {}

    /**
     * Holder Country
     */
    inner class CountryHolder(val ui: CountryItemUI, itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.onClick {
                onItemClick(countries[adapterPosition])
            }
        }

        /**
         * Bind country
         */
        fun bindHolder(country: Country) {
            Picasso.with(context).load(country.flagFilePath).into(ui.imgFlag)
            ui.tvTel.text = country.countryName
        }
    }
}

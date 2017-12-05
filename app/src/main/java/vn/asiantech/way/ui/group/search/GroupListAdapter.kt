package vn.asiantech.way.ui.group.search

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Group

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupAdapter(private val context: Context, private val groups: MutableList<Group>, private val listener: (Group) -> Unit)
    : RecyclerView.Adapter<SearchGroupAdapter.SearchGroupHolder>() {
    override fun onBindViewHolder(holder: SearchGroupHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount() = groups.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchGroupAdapterUI()
            .createView(AnkoContext.create(context, parent, false))
            .tag as? SearchGroupHolder

    /**
     * Custom view item  of groups list.
     */
    inner class SearchGroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvJoinGroup: TextView = itemView.find(R.id.group_search_adapter_tv_join_group)
        private val tvNameUser: TextView = itemView.find(R.id.group_search_adapter_tv_name_user)

        /**
         * Bind data to item view.
         */
        fun onBind() {
            tvNameUser.text = groups[adapterPosition].name
            tvJoinGroup.onClick {
                listener(groups[adapterPosition])
            }
        }
    }

    /**
     * Class to bind ViewHolder with Anko layout
     */
    inner class SearchGroupAdapterUI : AnkoComponent<ViewGroup> {

        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val itemView = ui.apply {
                relativeLayout {
                    lparams(matchParent, wrapContent)
                    backgroundColor = Color.WHITE

                    view {
                        id = R.id.group_search_adapter_view_break_line
                        backgroundResource = R.color.colorSearchScreenBackground
                    }.lparams(matchParent, dip(0.5f)) {
                        bottomMargin = dip(2)
                        leftMargin = dip(64)
                        topMargin = dip(2)
                    }

                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL

                        textView {
                            id = R.id.group_search_adapter_tv_name_user
                            padding = dip(10)
                            typeface = Typeface.DEFAULT_BOLD
                            textSize = px2dip(dimen(R.dimen.search_screen_text_size))
                            maxLines = 1
                            ellipsize = TextUtils.TruncateAt.END
                        }.lparams(dip(0), wrapContent) {
                            leftMargin = dip(10)
                            weight = 1f
                        }

                        textView {
                            id = R.id.group_search_adapter_tv_join_group
                            textColor = R.color.colorWhite
                            padding = dip(5)
                            backgroundResource = R.color.colorPrimaryDark
                            textSize = px2dip(dimen(R.dimen.group_text_size_normal))
                            maxLines = 1
                            ellipsize = TextUtils.TruncateAt.END
                            text = resources.getString(R.string.join)
                        }.lparams {
                            leftMargin = dip(10)
                            rightMargin = dip(10)
                        }

                    }.lparams(matchParent, wrapContent) {
                        below(R.id.group_search_adapter_view_break_line)
                    }

                }
            }.view
            itemView.tag = SearchGroupHolder(itemView)
            return itemView
        }
    }
}

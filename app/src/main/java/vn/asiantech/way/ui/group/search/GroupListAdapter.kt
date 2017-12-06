package vn.asiantech.way.ui.group.search

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
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
class GroupListAdapter(private val context: Context, private val groups: MutableList<Group>, private val listener: (Group) -> Unit)
    : RecyclerView.Adapter<GroupListAdapter.SearchGroupHolder>() {
    private var groupIdCurrentrequest = ""
    override fun onBindViewHolder(holder: SearchGroupHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount() = groups.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchGroupAdapterUI()
            .createView(AnkoContext.create(context, parent, false))
            .tag as? SearchGroupHolder

    internal fun setIdforGroupRequest(id: String) {
        Log.d("hhhh","ok kkkkkk $id")
        groupIdCurrentrequest = id
       notifyDataSetChanged()
    }

    /**
     * Custom view item  of groups list.
     */
    inner class SearchGroupHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val tvJoinGroup: TextView = item.find(R.id.group_search_adapter_tv_join_group)
        private val tvNameUser: TextView = item.find(R.id.group_search_adapter_tv_name_user)

        init {
            tvJoinGroup.onClick {
                tvJoinGroup.visibility = View.GONE
                listener(groups[adapterPosition])
            }
        }

        /**
         * Bind data to item view.
         */
        fun onBind() {
            tvNameUser.text = groups[adapterPosition].name
            if (groups[adapterPosition].id.equals(groupIdCurrentrequest)) {
                tvJoinGroup.visibility = View.INVISIBLE
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
                        leftMargin = dimen(R.dimen.search_group_left_margin)
                        topMargin = dip(2)
                    }

                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL

                        textView {
                            id = R.id.group_search_adapter_tv_name_user
                            padding = dimen(R.dimen.search_group_padding)
                            typeface = Typeface.DEFAULT_BOLD
                            textSize = px2dip(dimen(R.dimen.search_screen_text_size))
                            maxLines = 1
                            ellipsize = TextUtils.TruncateAt.END
                        }.lparams(dip(0), wrapContent) {
                            weight = 1f
                        }

                        textView {
                            id = R.id.group_search_adapter_tv_join_group
                            textColor = R.color.colorWhite
                            padding = dimen(R.dimen.search_group_padding)
                            backgroundResource = R.color.colorPinkLight
                            textSize = px2dip(dimen(R.dimen.group_text_size_normal))
                            maxLines = 1
                            ellipsize = TextUtils.TruncateAt.END
                            text = resources.getString(R.string.join)
                        }.lparams {
                            rightMargin = dimen(R.dimen.search_group_padding)
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

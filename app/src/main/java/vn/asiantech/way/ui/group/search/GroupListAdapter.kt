package vn.asiantech.way.ui.group.search

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
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
import vn.asiantech.way.data.model.Invite

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class GroupListAdapter(private val context: Context, private val groups: MutableList<Group>)
    : RecyclerView.Adapter<GroupListAdapter.SearchGroupHolder>() {

    internal var onJoinButtonClick: (Group) -> Unit = {}
    private var currentRequest = Invite("", "", "", false)

    override fun onBindViewHolder(holder: SearchGroupHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount() = groups.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchGroupAdapterUI()
            .createView(AnkoContext.create(context, parent, false))
            .tag as? SearchGroupHolder

    internal fun updateCurrentRequest(invite: Invite) {
        currentRequest = invite
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
                onJoinButtonClick(groups[adapterPosition])
            }
        }

        /**
         * Bind data to item view.
         */
        fun onBind() {
            tvNameUser.text = groups[adapterPosition].name
            if (groups[adapterPosition].id.equals(currentRequest.to)) {
                tvJoinGroup.visibility = View.INVISIBLE
            } else {
                tvJoinGroup.visibility = View.VISIBLE
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
                    backgroundResource = R.color.colorWhite

                    view {
                        id = R.id.group_search_adapter_view_break_line
                        backgroundResource = R.color.colorSearchScreenBackground
                    }.lparams(matchParent, dip(1)) {
                        leftMargin = dimen(R.dimen.search_group_left_margin)
                        verticalMargin = dimen(R.dimen.search_group_adapter_vertical_margin)
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

                        textView(R.string.join) {
                            id = R.id.group_search_adapter_tv_join_group
                            textColor = ContextCompat.getColor(context, R.color.colorWhite)
                            padding = dimen(R.dimen.search_group_padding)
                            backgroundResource = R.color.colorPinkLight
                            textSize = px2dip(dimen(R.dimen.group_text_size_normal))
                            maxLines = 1
                            ellipsize = TextUtils.TruncateAt.END
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

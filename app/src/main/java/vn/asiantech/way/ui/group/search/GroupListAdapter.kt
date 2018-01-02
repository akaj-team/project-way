package vn.asiantech.way.ui.group.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class GroupListAdapter(private val context: Context, private val groups: MutableList<Group>)
    : RecyclerView.Adapter<GroupListAdapter.SearchGroupItemHolder>() {

    internal var onJoinButtonClick: (Group) -> Unit = {}
    private var currentRequest = Invite("", "", "", false)

    override fun onBindViewHolder(holder: SearchGroupItemHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount() = groups.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchGroupItemHolder? {
        val searchGroupItemUI = SearchGroupItemUI()
        return SearchGroupItemHolder(searchGroupItemUI,
                searchGroupItemUI.createView(AnkoContext.Companion.create(context, parent)))
    }

    internal fun updateCurrentRequest(invite: Invite) {
        currentRequest = invite
    }

    /**
     * Custom view item  of groups list.
     */
    inner class SearchGroupItemHolder(val ui: SearchGroupItemUI, item: View) : RecyclerView.ViewHolder(item) {
        init {
            ui.tvJoinGroup.onClick {
                ui.tvJoinGroup.visibility = View.GONE
                onJoinButtonClick(groups[adapterPosition])
            }
        }

        /**
         * Bind data to item view.
         */
        fun onBind() {
            with(groups[adapterPosition]) {
                ui.run {
                    tvNameUser.text = name
                    if (id == currentRequest.to) {
                        tvJoinGroup.visibility = View.INVISIBLE
                    } else {
                        tvJoinGroup.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}

package vn.asiantech.way.ui.group.showinvite

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
import vn.asiantech.way.data.model.Invite

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 05/12/2017.
 */
class InviteListAdapter(private val context: Context, private val invites: MutableList<Invite>, val listener: OnItemClick)
    : RecyclerView.Adapter<InviteListAdapter.InviteViewHolder>() {

    override fun onBindViewHolder(holder: InviteViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InviteListAdapterUI()
            .createView(AnkoContext.create(context, parent, false))
            .tag as? InviteViewHolder

    override fun getItemCount() = invites.size

    /**
     * Custom item of invites list.
     */
    inner class InviteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvOk: TextView = itemView.find(R.id.group_show_invite_adapter_tv_ok)
        private val tvCancel: TextView = itemView.find(R.id.group_show_invite_adapter_tv_cancel)
        private val tvNameGroup: TextView = itemView.find(R.id.group_show_invite_adapter_tv_name_group)

        /**
         * Bind data to InviteViewHolder.
         */
        fun onBind() {
            tvNameGroup.text = invites[adapterPosition].groupName

            tvOk.onClick {
                listener.onOkClick(invites[adapterPosition])
                invites.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }

            tvCancel.onClick {
                listener.onCancelClick(invites[adapterPosition])
                invites.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    /**
     * Class to bind ViewHolder with Anko layout
     */
    inner class InviteListAdapterUI : AnkoComponent<ViewGroup> {

        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val itemView = ui.apply {
                relativeLayout {
                    lparams(matchParent, dimen(R.dimen.group_screen_adapter_item_width))
                    backgroundColor = Color.WHITE

                    textView {
                        id = R.id.group_show_invite_adapter_tv_name_group
                        maxLines = 1
                        ellipsize = TextUtils.TruncateAt.END
                        typeface = Typeface.DEFAULT_BOLD
                        textSize = px2dip(dimen(R.dimen.text_size_normal))
                        textColor = Color.BLACK
                    }.lparams {
                        margin = dip(10)
                        leftMargin = dip(20)
                    }

                    textView(R.string.ok) {
                        id = R.id.group_show_invite_adapter_tv_ok
                        backgroundResource = R.color.colorPinkLight
                        gravity = Gravity.CENTER
                        typeface = Typeface.DEFAULT_BOLD
                        textSize = px2dip(dimen(R.dimen.group_text_size_normal))
                    }.lparams(dimen(R.dimen.group_screen_tv_ok_width), wrapContent) {
                        below(R.id.group_show_invite_adapter_tv_name_group)
                        leftMargin = dip(10)
                        padding = dip(5)
                    }

                    textView(R.string.cancel) {
                        id = R.id.group_show_invite_adapter_tv_cancel
                        backgroundResource = R.color.colorGrayLight
                        gravity = Gravity.CENTER
                        typeface = Typeface.DEFAULT_BOLD
                        textSize = px2dip(dimen(R.dimen.group_text_size_normal))
                    }.lparams(dimen(R.dimen.group_screen_tv_ok_width), wrapContent) {
                        rightOf(R.id.group_show_invite_adapter_tv_ok)
                        below(R.id.group_show_invite_adapter_tv_name_group)
                        leftMargin = dip(10)
                        padding = dip(5)
                    }

                    view {
                        backgroundResource = R.color.grayLight
                    }.lparams(matchParent, dip(0.5f)) {
                        below(R.id.group_show_invite_adapter_tv_ok)
                        leftMargin = dip(10)
                        topMargin = dip(5)
                    }
                }
            }.view

            itemView.tag = InviteViewHolder(itemView)
            return itemView
        }
    }

    /**
     * This interface used to handle item of InviteViewHolder click.
     */
    interface OnItemClick {

        /**
         * This method used to handle button ok on click.
         */
        fun onOkClick(invite: Invite)

        /**
         * This method used to handle button cancel on click.
         */
        fun onCancelClick(invite: Invite)
    }
}

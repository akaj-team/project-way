package vn.asiantech.way.ui.group.info

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hypertrack.lib.models.User
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.utils.AppConstants

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 05/12/2017
 */
class MemberListAdapter(val userId: String, var groupOwnerId: String, val members: MutableList<User>)
    : RecyclerView.Adapter<MemberListAdapter.MemberItemViewHolder>() {

    var onImageUpToAdminClick: (userId: String) -> Unit = {}

    override fun getItemCount() = members.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemViewHolder? {
        return MemberItemUI().createView(AnkoContext.Companion.create(parent.context, parent,
                false)).tag as? MemberItemViewHolder
    }

    override fun onBindViewHolder(holder: MemberItemViewHolder?, position: Int) {
        holder?.onBind()
    }

    /**
     * Item view holder of MemberListAdapter.
     */
    inner class MemberItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgAvatar = itemView.find<CircleImageView>(R.id.item_member_img_avatar)
        private val tvName = itemView.find<TextView>(R.id.item_member_tv_name)
        private val imgUpToAdmin = itemView.find<ImageView>(R.id.item_member_img_up_to_admin)
        private val imgCall = itemView.find<ImageView>(R.id.item_member_img_call)

        init {
            imgCall.onClick {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(itemView.context.getString(R.string.call_intent,
                        members[adapterPosition].lookupId))
                itemView.context.startActivity(intent)
            }
            imgUpToAdmin.onClick {
                onImageUpToAdminClick(members[adapterPosition].id)
            }
        }

        /**
         * Bind data to view holder.
         */
        fun onBind() {
            with(members[adapterPosition]) {
                Glide.with(itemView.context).load(photo).into(imgAvatar)
                tvName.text = name
                if (userId != id && lookupId != null) {
                    imgCall.visibility = View.VISIBLE
                } else {
                    imgCall.visibility = View.GONE
                }
                if (userId == groupOwnerId && id != groupOwnerId) {
                    imgUpToAdmin.visibility = View.VISIBLE
                } else {
                    imgUpToAdmin.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Item layout of MemberListAdapter.
     */
    inner class MemberItemUI : AnkoComponent<ViewGroup> {

        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val view = with(ui) {
                verticalLayout {
                    lparams(matchParent, wrapContent)
                    backgroundColor = Color.WHITE

                    linearLayout {
                        val paddingLeft = dimen(R.dimen.group_screen_tv_count_padding_left)
                        val paddingTop = dimen(R.dimen.group_screen_recycler_view_padding)
                        verticalPadding = paddingTop
                        horizontalPadding = paddingLeft
                        backgroundColor = Color.WHITE

                        circleImageView {
                            id = R.id.item_member_img_avatar
                            backgroundResource = R.mipmap.ic_launcher_round
                        }.lparams(dimen(R.dimen.group_screen_avatar_width),
                                dimen(R.dimen.group_screen_avatar_width))

                        textView {
                            id = R.id.item_member_tv_name
                            textColor = Color.BLACK
                            textSize = px2dip(dimen(R.dimen.text_size_normal))
                            singleLine = true
                        }.lparams(dimen(R.dimen.group_screen_tv_name_width), wrapContent) {
                            leftMargin = dimen(R.dimen.group_text_size_normal)
                            weight = AppConstants.MEMBER_ITEM_TEXT_VIEW_NAME_WEIGHT
                        }

                        imageView(R.drawable.ic_up_to_admin) {
                            id = R.id.item_member_img_up_to_admin
                        }.lparams(dimen(R.dimen.group_screen_avatar_width),
                                dimen(R.dimen.group_screen_avatar_width))

                        imageView(R.drawable.ic_phone_forwarded_blue_a700_48dp) {
                            id = R.id.item_member_img_call
                        }.lparams(dimen(R.dimen.group_screen_avatar_width),
                                dimen(R.dimen.group_screen_avatar_width)) {
                            leftMargin = dimen(R.dimen.default_padding_margin)
                        }
                    }.lparams(matchParent, wrapContent)

                    view {
                        backgroundResource = R.color.colorSearchScreenBackground
                    }.lparams(matchParent, dimen(R.dimen.break_line_view_height)) {
                        verticalMargin = dimen(R.dimen.break_line_top_bot_margin)
                        leftMargin = dimen(R.dimen.break_line_left_margin)
                    }
                }
            }
            view.tag = MemberItemViewHolder(view)
            return view
        }

        /*
         * Add circleImageView library
         */
        private inline fun ViewManager.circleImageView(theme: Int = 0, init: CircleImageView.() -> Unit):
                CircleImageView {
            return ankoView({ CircleImageView(it) }, theme, init)
        }
    }
}

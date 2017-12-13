package vn.asiantech.way.ui.group.invite

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R

/**
 * User List Adapter
 * @author NgocTTN
 */
class InviteUserListAdapter(val users: MutableList<User>)
    : RecyclerView.Adapter<InviteUserListAdapter.UserViewHolder>() {

    var onItemInviteClick: (user: User) -> Unit = {}

    override fun onBindViewHolder(holder: UserViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder? =
            UserViewHolder(InviteItemUserUI().createView(AnkoContext.create(parent.context, parent)))


    override fun getItemCount() = users.size

    /**
     * This class used to custom item of user list.
     */
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgAvatar: ImageView = itemView.find(R.id.item_user_img_avatar)
        private val tvName: TextView = itemView.find(R.id.item_user_tv_name)
        private val tvInvite: TextView = itemView.find(R.id.item_user_tv_invite)

        init {
            tvInvite.onClick {
                onItemInviteClick(users[adapterPosition])
                tvInvite.visibility = View.GONE
            }
        }

        /**
         * Bind data to view holder.
         */
        fun onBind() {
            with(users[adapterPosition]) {
                tvName.text = name
                Glide.with(itemView.context).load(photo).into(imgAvatar)
                val isShow: Int = if (groupId != null) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
                tvInvite.visibility = isShow
            }
        }
    }
}

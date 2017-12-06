package vn.asiantech.way.ui.group.request

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.hypertrack.lib.models.User
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.extension.circleImageView

/**
 * Request Adapter
 * Created by haingoq on 05/12/2017.
 */
class RequestAdapter(private val users: List<User>) : RecyclerView.Adapter<RequestAdapter.RequestHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemRequestUI()
            .createView(AnkoContext.create(parent.context, parent, false))
            .tag as? RequestHolder

    override fun onBindViewHolder(holder: RequestHolder?, position: Int) {
        holder?.bindHolder(users[position])
    }

    override fun getItemCount() = users.size

    /**
     * Request holder
     */
    inner class RequestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgAvatar: ImageView = itemView.find(R.id.group_id_img_avatar)
        private val tvName: TextView = itemView.find(R.id.group_id_tv_name)
        private val imgBtnAccept: ImageButton = itemView.find(R.id.group_id_image_button_accept)
        private val imgBtnCancel: ImageButton = itemView.find(R.id.group_id_image_button_cancel)

        init {
            imgBtnAccept.onClick {
                onAcceptClick(users[adapterPosition].id)
            }

            imgBtnAccept.onClick {
                onCancelClick(users[adapterPosition].id)
            }
        }

        /**
         * Bind view holder
         */
        fun bindHolder(user: User) {
            Picasso.with(itemView.context).load(user.photo).into(imgAvatar)
            tvName.text = user.name
        }
    }

    var onAcceptClick: (String) -> Unit = {}
    var onCancelClick: (String) -> Unit = {}

    /**
     * Item request UI
     * Created by haingoq on 05/12/2017.
     */
    class ItemRequestUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
            linearLayout {
                lparams(matchParent, wrapContent)

                circleImageView {
                    id = R.id.group_id_img_avatar
                    backgroundResource = R.drawable.ic_default_avatar
                }.lparams(dimen(R.dimen.group_screen_avatar_width), dimen(R.dimen.group_screen_avatar_width)) {
                    leftMargin = dimen(R.dimen.group_screen_tv_count_padding_left)
                    gravity = Gravity.CENTER_VERTICAL
                }

                textView {
                    id = R.id.group_id_tv_name
                    textColor = Color.BLACK
                    textSize = px2dip(dimen(R.dimen.text_size_normal))
                    singleLine = true
                }.lparams {
                    leftMargin = dimen(R.dimen.group_screen_tv_count_padding_left)
                    gravity = Gravity.CENTER_VERTICAL
                }

                imageButton(R.drawable.ic_check) {
                    id = R.id.group_id_image_button_accept
                }.lparams {
                    leftMargin = dimen(R.dimen.group_screen_tv_count_padding_left)
                    gravity = Gravity.CENTER_VERTICAL
                }

                imageButton(R.drawable.ic_close) {
                    id = R.id.group_id_image_button_cancel
                }.lparams {
                    leftMargin = dimen(R.dimen.group_screen_tv_count_padding_left)
                    gravity = Gravity.CENTER_VERTICAL
                }
            }
        }
    }
}

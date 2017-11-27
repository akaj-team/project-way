package vn.asiantech.way.ui.share

import android.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.MapFragment
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 27/11/2017.
 */
class ShareActivityUI : AnkoComponent<ShareActivity> {

    override fun createView(ui: AnkoContext<ShareActivity>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            frameLayout {
                lparams(matchParent,matchParent)
                relativeLayout {
                    lparams(matchParent,matchParent)
                    backgroundColor = ContextCompat.getColor(context, R.color.colorWhite)

                    val fragment = MapFragment()

                }
            }
        }
    }
}

package vn.asiantech.way.ui.custom

import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * FloatingButtonHorizontalUI.
 *
 * @author at-ToanNguyen
 */
class FloatingButtonHorizontalUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            relativeLayout {
                imageView {
                    imageResource = R.drawable.ic_search
                }.lparams(dip(50), dip(50)) {
                    alignParentRight()
                    rightMargin = dip(5)
                    backgroundResource = R.drawable.custom_bg_item_search_button
                }
            }
        }
    }
}
//<ImageButton
//android:id="@+id/imgBtnSearch"
//android:layout_width="50dp"
//android:layout_height="50dp"
//android:layout_alignParentRight="true"
//android:layout_marginEnd="5dp"
//android:layout_marginRight="5dp"
//android:background="@drawable/custom_bg_item_search_button"
//android:src="@drawable/ic_search" />
//
//<TextView
//android:id="@+id/tvSearchTitle"
//android:layout_width="wrap_content"
//android:layout_height="30dp"
//android:layout_centerVertical="true"
//android:layout_marginEnd="10dp"
//android:layout_marginRight="10dp"
//android:layout_toLeftOf="@id/imgBtnSearch"
//android:background="@drawable/custom_bg_item_menu_title"
//android:gravity="center"
//android:paddingEnd="10dp"
//android:paddingLeft="10dp"
//android:paddingRight="10dp"
//android:paddingStart="10dp"
//android:text="@string/custom_floating_menu_search_title"
//android:textSize="@dimen/custom_menu_text_size" />
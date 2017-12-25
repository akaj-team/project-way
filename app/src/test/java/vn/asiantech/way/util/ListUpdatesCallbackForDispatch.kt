package vn.asiantech.way.util

import android.support.v7.util.ListUpdateCallback


/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by at-hoavo on 24/12/2017.
 */
class ListUpdatesCallbackForDispatch {
    companion object {
        fun callback(
                onChanged: (Int, Int, Any?) -> Unit? = { _, _, _ -> },
                onMoved: (Int, Int) -> Unit? = { _, _ -> },
                onInserted: (Int, Int) -> Unit? = { _, _ -> },
                onRemoved: (Int, Int) -> Unit? = { _, _ -> }
        ): ListUpdateCallback = object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                onChanged.invoke(position, count, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                onMoved.invoke(fromPosition, toPosition)
            }

            override fun onInserted(position: Int, count: Int) {
                onInserted.invoke(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                onRemoved.invoke(position, count)
            }
        }
    }
}

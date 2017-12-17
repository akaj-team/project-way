package vn.asiantech.way.ui.base

import android.support.v7.util.DiffUtil

/**
 * Use this class to make diff for list view items
 */
class Diff<T>(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {
    private var areItemsTheSameFunc: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem }
    private var areContentsTheSameFunc: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem }

    internal fun areItemsTheSame(areItemsTheSame: (oldItem: T, newItem: T) -> Boolean): Diff<T> {
        areItemsTheSameFunc = areItemsTheSame
        return this
    }

    internal fun areContentsTheSame(areItemsTheSame: (oldItem: T, newItem: T) -> Boolean): Diff<T> {
        areContentsTheSameFunc = areItemsTheSame
        return this
    }

    internal fun calculateDiff() = DiffUtil.calculateDiff(this)

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = areItemsTheSameFunc.invoke(oldList[oldItemPosition], newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = areContentsTheSameFunc.invoke(oldList[oldItemPosition], newList[newItemPosition])
}

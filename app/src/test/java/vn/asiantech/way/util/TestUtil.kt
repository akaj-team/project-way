package vn.asiantech.way.util

import org.mockito.Mockito

/**
 * Created by tien.hoang on 12/13/17.
 */
object TestUtil {
    fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}
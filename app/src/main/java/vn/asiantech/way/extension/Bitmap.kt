package vn.asiantech.way.extension

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Created by haingoq on 13/10/2017.
 */

/**
 * Extension method to get base64 string for Bitmap.
 */
fun Bitmap.toBase64(): String {
    var result = ""
    val baoStream = ByteArrayOutputStream()
    try {
        compress(Bitmap.CompressFormat.JPEG, 100, baoStream)
        baoStream.flush()
        baoStream.close()
        val bitmapBytes = baoStream.toByteArray()
        result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            baoStream.flush()
            baoStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return result
}

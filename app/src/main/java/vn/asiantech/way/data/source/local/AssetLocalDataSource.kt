package vn.asiantech.way.data.source.local

import android.content.Context
import android.support.annotation.RawRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.AssetDataSource
import java.io.ByteArrayOutputStream

/**
 * Created by tien.hoang on 11/29/17.
 */
class AssetLocalDataSource(val context: Context) : AssetDataSource {
    companion object {
        val COUNTRIES_RAW_ID = R.raw.countries
    }

    override fun getCountries(): Observable<List<Country>> {
        val rawData = readJsonFromDirectory(COUNTRIES_RAW_ID)
        return BehaviorSubject.create<List<Country>> {
            it.onNext(getCountries(rawData))
            it.onComplete()
        }
    }

    private fun readJsonFromDirectory(@RawRes resId: Int): String {
        val iStream = context.resources.openRawResource(resId)
        val byteStream = ByteArrayOutputStream()
        val buffer = ByteArray(iStream.available())
        iStream.read(buffer)
        byteStream.write(buffer)
        byteStream.close()
        iStream.close()
        return byteStream.toString()
    }

    private fun getCountries(json: String): List<Country> {
        return Gson().fromJson(json, object : TypeToken<List<Country>>() {}.type)
    }
}
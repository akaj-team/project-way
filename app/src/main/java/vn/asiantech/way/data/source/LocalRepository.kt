package vn.asiantech.way.data.source

import android.content.Context
import android.support.annotation.RawRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.datasource.LocalDataSource
import java.io.ByteArrayOutputStream

/**
 * Created by tien.hoang on 12/1/17.
 */
class LocalRepository(val context: Context) : LocalDataSource {
    companion object {
        val COUNTRIES_RAW_ID = R.raw.countries
        const val PREFS_FILE = "AppPrefsKey"
        const val KEY_LOGIN_TOKEN = "login"
    }

    private val pref = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    override fun getUserToken(): String {
        return pref.getString(KEY_LOGIN_TOKEN, "")
    }

    override fun setUserToken(token: String) {
        pref.edit().putString(KEY_LOGIN_TOKEN, token).apply()
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
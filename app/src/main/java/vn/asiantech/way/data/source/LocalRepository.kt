package vn.asiantech.way.data.source

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.RawRes
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.json.JSONArray
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.data.source.datasource.LocalDataSource
import vn.asiantech.way.utils.AppConstants
import java.io.ByteArrayOutputStream

/**
 *
 * Created by tien.hoang on 12/1/17.
 */
class LocalRepository(val context: Context) : LocalDataSource {
    companion object {
        val COUNTRIES_RAW_ID = R.raw.countries
        const val PREFS_FILE = "AppPrefsKey"
        const val KEY_LOGIN_TOKEN = "login"
    }

    private var pref: SharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    override fun getSearchHistory(): List<WayLocation>? {
        val result = mutableListOf<WayLocation>()
        return try {
            val history = pref.getString(AppConstants.KEY_SEARCH_SCREEN_WAY_LOCATION_HISTORY,
                    "[]")
            val jsonArray = JSONArray(history)
            (0 until jsonArray.length())
                    .mapTo(result) {
                        Gson().fromJson(jsonArray.getJSONObject(it).toString()
                                , WayLocation::class.java)
                    }
            result.toList()
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    override fun saveSearchHistory(location: WayLocation) {
        val editor = pref.edit()
        var history = getSearchHistory()
        if (history == null) {
            history = mutableListOf()
        }
        history = history.filter {
            it.id != location.id
        }.toMutableList()
        location.isHistory = true
        history.add(0, location)
        if (history.size > AppConstants.SEARCH_SCREEN_HISTORY_MAX_SIZE) {
            history.removeAt(AppConstants.SEARCH_SCREEN_HISTORY_MAX_SIZE - 1)
        }
        editor?.putString(AppConstants.KEY_SEARCH_SCREEN_WAY_LOCATION_HISTORY, Gson().toJson(history))
        editor?.apply()
    }

    override fun getLoginStatus(): Boolean = pref.getBoolean(KEY_LOGIN_TOKEN, false)

    override fun setLoginStatus(isLogin: Boolean) = pref.edit().putBoolean(KEY_LOGIN_TOKEN, isLogin).apply()

    override fun getCountries(): Observable<List<Country>> {
        val rawData = readJsonFromDirectory(COUNTRIES_RAW_ID)
        return BehaviorSubject.create<List<Country>> {
            it.onNext(getCountries(rawData))
            it.onComplete()
        }
    }

    override fun getTrackingHistory(): MutableList<TrackingInformation>? {
        val result = mutableListOf<TrackingInformation>()
        return try {
            val history = pref.getString(AppConstants.KEY_TRACKING_HISTORY, "[]")
            val jsonArray = JSONArray(history)
            (0 until jsonArray.length())
                    .mapTo(result) { Gson().fromJson(jsonArray.getJSONObject(it).toString(), TrackingInformation::class.java) }
            result.toMutableList()
        } catch (e: JsonSyntaxException) {
            null
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

    private fun getCountries(json: String): List<Country> = Gson().fromJson(json, object : TypeToken<List<Country>>() {}.type)
}

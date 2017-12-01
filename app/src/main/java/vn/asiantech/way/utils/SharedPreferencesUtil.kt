package vn.asiantech.way.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONArray
import vn.asiantech.way.data.model.WayLocation

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 01/12/2017
 */
class SharedPreferencesUtil(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(AppConstants.KEY_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    internal fun getSearchHistory(): List<WayLocation>? {
        val gson = Gson()
        val result = mutableListOf<WayLocation>()
        return try {
            val history = prefs.getString(AppConstants.KEY_SEARCH_SCREEN_WAY_LOCATION_HISTORY,
                    "[]")
            val jsonArray = JSONArray(history)
            (0 until jsonArray.length())
                    .mapTo(result) {
                        gson.fromJson(jsonArray.getJSONObject(it).toString()
                                , WayLocation::class.java)
                    }
            result.toList()
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    internal fun saveSearchHistory(location: WayLocation) {
        val gson = Gson()
        val editor = prefs.edit()
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
        editor?.putString(AppConstants.KEY_SEARCH_SCREEN_WAY_LOCATION_HISTORY, gson.toJson(history))
        editor?.apply()
    }
}

package vn.asiantech.way.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONArray

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 14/11/2017.
 */
class Preference {
    companion object {
        private var context: Context? = null

        fun init(context: Context) {
            this.context = context
        }
    }

    private var mSharedPreferences: SharedPreferences? = null

    init {
        mSharedPreferences = context?.getSharedPreferences(AppConstants.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    internal fun getTrackingHistory(): MutableList<vn.asiantech.way.data.model.Location>? {
        val gson = Gson()
        val result = mutableListOf<vn.asiantech.way.data.model.Location>()
        return try {
            val history = mSharedPreferences?.getString(AppConstants.KEY_TRACKING_HISTORY, "[]")
            Log.d("zxc", "history " + history)
            val jsonArray = JSONArray(history)
            Log.d("zxc", "jsonArray " + jsonArray)
            (0 until jsonArray.length())
                    .mapTo(result) { gson.fromJson(jsonArray.getJSONObject(it).toString(), vn.asiantech.way.data.model.Location::class.java) }
            Log.d("zxc", "result " + result)
            result
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    internal fun saveTrackingHistory(location: vn.asiantech.way.data.model.Location) {
        val gson = Gson()
        val editor = mSharedPreferences?.edit()
        var history = getTrackingHistory()
        if (history == null) {
            history = mutableListOf()
        }
        history.forEach {
            if (it.status == location.status) {
                return
            }
        }
        if (history.size > AppConstants.HISTORY_MAX_SIZE) {
            history.removeAt(0)
        }
        history.add(location)
        editor?.putString(AppConstants.KEY_TRACKING_HISTORY, gson.toJson(history))
        editor?.apply()
    }

    internal fun getActionType(): String? {
        return mSharedPreferences?.getString(AppConstants.KEY_ACTION_TYPE, "")
    }

    internal fun setActionType(actionType: String) {
        val editor = mSharedPreferences?.edit()
        editor?.putString(AppConstants.KEY_ACTION_TYPE, actionType)
        editor?.apply()
    }
}
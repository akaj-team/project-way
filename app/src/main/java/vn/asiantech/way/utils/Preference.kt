package vn.asiantech.way.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONArray
import vn.asiantech.way.data.model.TrackingInformation

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

    internal fun getTrackingHistory(): MutableList<TrackingInformation>? {
        val gson = Gson()
        val result = mutableListOf<TrackingInformation>()
        return try {
            val history = mSharedPreferences?.getString(AppConstants.KEY_TRACKING_HISTORY, "[]")
            val jsonArray = JSONArray(history)
            (0 until jsonArray.length())
                    .mapTo(result) { gson.fromJson(jsonArray.getJSONObject(it).toString(), TrackingInformation::class.java) }
            result
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    internal fun saveTrackingHistory(location: TrackingInformation) {
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

    internal fun getListLocationLatLng(): MutableList<LatLng>? {
        val string = mSharedPreferences?.getString(AppConstants.KEY_LOCATION_LAT_LNG, "")
        val result = mutableListOf<LatLng>()
        val list = string?.split(" ")
        if (list != null) {
            for (i in 0 until list.size) {
                if (list[i] != "") {
                    val latLng = list[i].split(",")
                    result.add(LatLng(latLng[0].toDouble(), latLng[1].toDouble()))
                }
            }
        }
        return result
    }

    internal fun saveListLocationLatLng(listLatLng: MutableList<LatLng>) {
        Log.d("zxc", "list " + listLatLng)
        val editor = mSharedPreferences?.edit()
        var s = ""
        for (i in 0 until listLatLng.size) {
            val result = "${listLatLng[i].latitude},${listLatLng[i].longitude}"
            s += result + " "
        }
        editor?.putString(AppConstants.KEY_LOCATION_LAT_LNG, s)
        editor?.apply()
    }

    internal fun getActionType(): String? {
        Log.d("zxc", "action type " + mSharedPreferences?.getString(AppConstants.KEY_ACTION_TYPE, ""))
        return mSharedPreferences?.getString(AppConstants.KEY_ACTION_TYPE, "")
    }

    internal fun setActionType(actionType: String) {
        val editor = mSharedPreferences?.edit()
        editor?.putString(AppConstants.KEY_ACTION_TYPE, actionType)
        editor?.apply()
    }

    internal fun setDestinationLatLng(latLng: LatLng) {
        val editor = mSharedPreferences?.edit()
        val result = "${latLng.latitude},${latLng.longitude}"
        editor?.putString(AppConstants.KEY_DESTINATION, result)
        editor?.apply()
    }

    internal fun getDestinationLatLng(): LatLng {
        val s = mSharedPreferences?.getString(AppConstants.KEY_DESTINATION, "")
        val latLng = s?.split(",")
        return latLng?.get(0)?.toDouble()?.let { LatLng(it, latLng[1].toDouble()) }!!
    }

    internal fun setCurrentLatLng(latLng: LatLng) {
        val editor = mSharedPreferences?.edit()
        val result = "${latLng.latitude},${latLng.longitude}"
        editor?.putString("ahihi", result)
        editor?.apply()
    }

    internal fun getCurrentLatLng(): LatLng {
        val s = mSharedPreferences?.getString("ahihi", "")
        val latLng = s?.split(",")
        return latLng?.get(0)?.toDouble()?.let { LatLng(it, latLng[1].toDouble()) }!!
    }
}

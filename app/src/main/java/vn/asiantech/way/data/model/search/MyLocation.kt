package vn.asiantech.way.data.model.search

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */
class MyLocation() : Serializable, Parcelable {

    var isHistory: Boolean? = null
    @SerializedName("formattedAddress")
    var formattedAddress: String? = null
    var id: String? = null
    var name: String? = null
    @SerializedName("placeId")
    var placeId: String? = null
    var geometry: Geometry? = null

    constructor(parcel: Parcel) : this() {
        isHistory = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        formattedAddress = parcel.readString()
        id = parcel.readString()
        name = parcel.readString()
        placeId = parcel.readString()
        geometry = parcel.readParcelable(Geometry::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(isHistory)
        parcel.writeString(formattedAddress)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(placeId)
        parcel.writeParcelable(geometry, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyLocation> {
        override fun createFromParcel(parcel: Parcel): MyLocation {
            return MyLocation(parcel)
        }

        override fun newArray(size: Int): Array<MyLocation?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 *  Geometry of location
 */
data class Geometry(var location: Coordinates) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable<Coordinates>(Coordinates::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(location, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Geometry> {
        override fun createFromParcel(parcel: Parcel): Geometry {
            return Geometry(parcel)
        }

        override fun newArray(size: Int): Array<Geometry?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 *  Coordinates of location
 */
data class Coordinates(var lat: Double, var lng: Double) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Coordinates> {
        override fun createFromParcel(parcel: Parcel): Coordinates {
            return Coordinates(parcel)
        }

        override fun newArray(size: Int): Array<Coordinates?> {
            return arrayOfNulls(size)
        }
    }
}

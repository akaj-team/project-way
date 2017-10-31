package vn.asiantech.way.ui.confirm

import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.view_confirm_location_layout.*
import java.util.*

/**
 * Class get location name
 * Created by haingoq on 30/10/2017.
 */
class LocationNameAsyncTask(private val mFragment: ConfirmLocationFragment)
    : AsyncTask<LatLng, Void, String>() {

    override fun doInBackground(vararg latLng: LatLng?): String {
        val geoCoder = Geocoder(mFragment.context, Locale.getDefault())
        var addressLine = ""
        val lat = latLng[0]?.latitude
        val lng = latLng[0]?.longitude
        if (lat != null && lng != null) {
            if (geoCoder.getFromLocation(lat, lng, 1) != null) {
                val addresses: List<Address> = geoCoder.getFromLocation(lat, lng, 1)
                if (addresses.isNotEmpty()) {
                    val address: Address = addresses[0]
                    addressLine = address.getAddressLine(0)
                    val name = if (!address.subThoroughfare.isNullOrEmpty()) {
                        address.subThoroughfare.plus(" ").plus(address.thoroughfare)
                    } else {
                        address.thoroughfare ?: ""
                    }
                    mFragment.setDestinationName(name)
                } else {
                    addressLine = ""
                }
            }
        } else {
            addressLine = ""
        }
        return addressLine
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        mFragment.tvLocation?.text = result
    }
}

package vn.asiantech.way.data.model

import com.google.android.gms.maps.model.LatLng

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 28/09/2017.
 */
class TrackingInformation(var time: String?, var status: String?, var description: String?, var point: LatLng) {
    var isChoose: Boolean = false
}

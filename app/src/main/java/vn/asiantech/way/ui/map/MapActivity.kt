package vn.asiantech.way.ui.map

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import vn.asiantech.way.R
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.os.Build
import vn.asiantech.way.ui.base.BaseActivity


class MapActivity : BaseActivity() {
    private companion object {
        private const val REQUESTCODE_PERMISSION = 200
        private const val VERSION_SDK = 23
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        replaceCreatePOStepFragment(MapFragment(), false)
        askPermissionsAccessLocation()
    }

    private fun replaceCreatePOStepFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(fragment.tag)
        }
        transaction.replace(R.id.frContainer, fragment)
        transaction.commitAllowingStateLoss()
        supportFragmentManager.executePendingTransactions()
    }

    /**
     * Method use check permission
     */
    private fun askPermissionsAccessLocation() {
        // Ask for permission with API >= 23
        if (Build.VERSION.SDK_INT >= VERSION_SDK) {
            val accessFineLocationPermission = ContextCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (accessFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                // Permissions
                val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                // Dialog
                ActivityCompat.requestPermissions(this, permissions, REQUESTCODE_PERMISSION)
            } else {
                // No-op
            }
        }
    }
}

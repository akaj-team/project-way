package vn.asiantech.way.ui.tracking

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import vn.asiantech.way.R

/**
 *
 */
class ProgressLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_location)
        supportFragmentManager.beginTransaction()
                .replace(R.id.frContainer, ProgressLocationFragment())
                .commit()
    }
}

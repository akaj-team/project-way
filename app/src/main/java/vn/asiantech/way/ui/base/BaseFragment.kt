package vn.asiantech.way.ui.base

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/21/17.
 */
abstract class BaseFragment : Fragment() {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setMessage(getString(R.string.processing))
    }

    /**
     * Toast the given text
     */
    fun showToast(stringId: Int) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show()
    }

    /**
     * Send broadcast
     */
    fun sendBroadcast(action: String) {
        activity.sendBroadcast(Intent(action))
    }
}
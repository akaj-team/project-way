package vn.asiantech.way.ui.register

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.remote.response.ResponseStatus
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.base.BaseActivity

/**
 *
 * Created by haingoq on 06/12/2017.
 */
class NewRegisterActivity : BaseActivity() {
    private lateinit var ui: RegisterActivityUI
    private lateinit var registerViewModel: RegisterViewModel
    private val countries: MutableList<Country> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = RegisterActivityUI(CountryAdapter(this, countries))
        ui.setContentView(this)
        // TODO check register or update
        registerViewModel = RegisterViewModel(this, true)
    }

    override fun onBindViewModel() {
        addDisposables(registerViewModel.getUser()
                .subscribe(this::handleGetUserCompleted, this::handleGetUserError),

                registerViewModel.getCountries()
                        .subscribe(this::handleGetCountriesCompleted),

                registerViewModel.progressBarStatus
                        .observeOnUiThread()
                        .subscribe(this::handleGetProgressBarStatusCompleted),

                registerViewModel.backStatus
                        .observeOnUiThread()
                        .subscribe(this::handleBackKeyEvent))
    }

    internal fun eventCreateUserClicked(userParams: UserParams) {
        addDisposables(registerViewModel.createUser(userParams)
                .subscribe(this::handleCreateUserCompleted))
    }

    internal fun eventUpdateUserClicked(userParams: UserParams) {
        addDisposables(registerViewModel.updateUser(userParams)
                .subscribe(this::handleUpdateUserCompleted))
    }

    internal fun eventClicked(view: View) {
        when(view){
            ui.frAvatar -> registerViewModel.getAvatar()
                    .subscribe(this::handleGetAvatarCompleted)
            
            ui.tvSkip -> registerViewModel.onSkipClicked()
                    .subscribe(this::handleOnSkipClicked)
        }
    }

    private fun handleGetUserCompleted(user: User) {
        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                visibleProgressBar(ui.progressBarAvatar)
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                invisibleProgressBar(ui.progressBarAvatar)
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                ui.imgAvatar.imageBitmap = bitmap
                invisibleProgressBar(ui.progressBarAvatar)
            }
        }
        Picasso.with(this)
                .load(user.photo)
                .into(target)
        ui.imgAvatar.tag = target
        ui.edtName.setText(user.name)
        ui.edtPhone.setText(user.phone)
        // TODO set tel to textview and flag to image
    }

    private fun handleGetUserError(t: Throwable) {
        toast(t.message.toString())
    }

    private fun handleCreateUserCompleted(responseStatus: ResponseStatus) {
        toast(responseStatus.message)
    }

    private fun handleUpdateUserCompleted(responseStatus: ResponseStatus) {
        toast(responseStatus.message)
    }

    private fun handleGetCountriesCompleted(list: List<Country>) {
        countries.clear()
        countries.addAll(list)
        //TODO adapter notifisetchange
    }

    private fun handleBackKeyEvent(isBack: Boolean) {
        if (isBack) {
            super.onBackPressed()
        } else {
            toast(getString(R.string.register_double_click_to_exit))
        }
    }

    private fun handleGetProgressBarStatusCompleted(isShow: Boolean) {
        if (isShow) {
            visibleProgressBar(ui.progressBar)
        } else {
            invisibleProgressBar(ui.progressBar)
        }
    }

    private fun handleGetAvatarCompleted(intent: Intent) {

    }

    private fun handleOnSkipClicked(userParams: UserParams) {

    }

    private fun visibleProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun invisibleProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}

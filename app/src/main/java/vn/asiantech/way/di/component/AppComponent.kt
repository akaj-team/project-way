package vn.asiantech.way.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import vn.asiantech.way.MyApplication
import javax.inject.Singleton


/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/26/17.
 */

@Singleton
@Component()
interface AppComponent {

    @Component.Builder interface Builder {

        @BindsInstance fun application(application: Application): Builder

        fun build(): AppComponent

    }

    fun inject(app: MyApplication)
}

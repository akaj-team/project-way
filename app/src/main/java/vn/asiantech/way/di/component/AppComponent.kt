package vn.asiantech.way.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import vn.asiantech.way.MyApplication
import vn.asiantech.way.di.builder.ActivityBuilder
import vn.asiantech.way.di.module.AppModule
import javax.inject.Singleton

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/26/17.
 */

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class))
interface AppComponent {

    @Component.Builder interface Builder {
        @BindsInstance fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: MyApplication)
}

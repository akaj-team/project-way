package vn.asiantech.way.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import vn.asiantech.way.MyApplication
import javax.inject.Singleton

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/28/17.
 */

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: MyApplication): Context {
        return application
    }
}

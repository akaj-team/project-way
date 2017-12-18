package vn.asiantech.way.util

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Callable

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

/**
 * This rule registers Handlers for RxJava and RxAndroid to ensure that subscriptions
 * always subscribeOn and observeOn Schedulers.trampoline().
 * Warning, this rule will reset RxAndroidPlugins and RxJavaPlugins before and after each test so
 * if the application code uses RxJava plugins this may affect the behaviour of the testing method.
 */
class RxSchedulersOverrideRule : TestRule {

    private val mRxAndroidSchedulersHook = Function<Callable<Scheduler>, Scheduler> { Schedulers.trampoline() }

    private val mRxJavaImmediateScheduler = Function<Scheduler, Scheduler> { Schedulers.trampoline() }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxAndroidPlugins.reset()
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(mRxAndroidSchedulersHook)
                RxJavaPlugins.reset()
                RxJavaPlugins.setIoSchedulerHandler(mRxJavaImmediateScheduler)
                RxJavaPlugins.setNewThreadSchedulerHandler(mRxJavaImmediateScheduler)
                RxJavaPlugins.setComputationSchedulerHandler (mRxJavaImmediateScheduler)
                RxJavaPlugins.setSingleSchedulerHandler (mRxJavaImmediateScheduler)

                base.evaluate()

                RxAndroidPlugins.reset()
                RxJavaPlugins.reset()
            }
        }
    }

}
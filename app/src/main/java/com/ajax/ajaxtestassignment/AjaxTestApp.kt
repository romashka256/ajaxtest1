package com.ajax.ajaxtestassignment

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment
import com.ajax.ajaxtestassignment.di.AppComponent
import com.ajax.ajaxtestassignment.di.DaggerAppComponent
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.MemoryCategory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class AjaxTestApp : Application(), HasAndroidInjector {

    @Inject lateinit var androidInjector : DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()

        appComponent.inject(this)

        Glide.init(applicationContext, GlideBuilder().setLogLevel(Log.DEBUG))
        Glide.get(applicationContext).setMemoryCategory(MemoryCategory.LOW)

    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}
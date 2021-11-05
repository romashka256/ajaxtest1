package com.ajax.ajaxtestassignment.di

import android.content.Context
import com.ajax.ajaxtestassignment.AjaxTestApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ContactsFragmentBuilderModule::class,
        ViewModelModule::class, DataModule::class
    ]
)

interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(applicationContext: Context): Builder

        fun build(): AppComponent
    }

    fun mainActivityComponentFactory(): MainActivitySubcomponent.Factory

    fun inject(app: AjaxTestApp)
}
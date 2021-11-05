package com.ajax.ajaxtestassignment.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ajax.ajaxtestassignment.common.ViewModelFactory
import com.ajax.ajaxtestassignment.ui.contactslist.viewmodel.ContactListViewModel
import com.ajax.ajaxtestassignment.ui.details.viewmodel.ContactDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ContactListViewModel::class)
    abstract fun bindContactsViewModel(contactsViewModel: ContactListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactDetailsViewModel::class)
    abstract fun bindContactDetailsViewModel(contactDetailsViewModel: ContactDetailsViewModel): ViewModel
}
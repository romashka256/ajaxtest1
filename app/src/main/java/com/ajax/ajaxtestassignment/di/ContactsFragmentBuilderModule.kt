package com.ajax.ajaxtestassignment.di

import com.ajax.ajaxtestassignment.ui.contactslist.ContactsListFragment
import com.ajax.ajaxtestassignment.ui.details.DetailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
abstract class ContactsFragmentBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun contributeContactsFragment(): ContactsListFragment

    @ContributesAndroidInjector
    internal abstract fun contributeContactDetailsFragment(): DetailsFragment

}
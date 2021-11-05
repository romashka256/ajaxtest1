package com.ajax.ajaxtestassignment.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ajax.ajaxtestassignment.AjaxTestApp
import com.ajax.ajaxtestassignment.R
import com.ajax.ajaxtestassignment.ui.contactslist.ContactsListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AjaxTestApp.appComponent.mainActivityComponentFactory().create(this, this).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startFragment = ContactsListFragment();
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            startFragment).commit();
    }
}
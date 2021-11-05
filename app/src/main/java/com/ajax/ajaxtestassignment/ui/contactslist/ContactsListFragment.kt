package com.ajax.ajaxtestassignment.ui.contactslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajax.ajaxtestassignment.R
import com.ajax.ajaxtestassignment.common.OperationResult
import com.ajax.ajaxtestassignment.common.ViewModelFactory
import com.ajax.ajaxtestassignment.databinding.FragmentContactsListBinding
import com.ajax.ajaxtestassignment.ui.contactslist.viewmodel.ContactListViewModel
import com.ajax.ajaxtestassignment.ui.details.DetailsFragment
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

open class ContactsListFragment : DaggerFragment() {
    private lateinit var contactAdapter: ContactAdapter

    private var binding: FragmentContactsListBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var contactsViewModel: ContactListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contactsViewModel = ViewModelProvider(this, viewModelFactory).get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contactAdapter = ContactAdapter(
            requireActivity(),
            onDelete = {
                contactsViewModel.delete(it)
            },
            onContact = {
                navigateToDetails(it)
            }
        )

        // Creates a vertical Layout Manager
        return FragmentContactsListBinding.inflate(layoutInflater, container, false)
            .apply {
                contactList.layoutManager = LinearLayoutManager(context)
                contactList.adapter = contactAdapter
                refresh.setOnClickListener {
                    contactsViewModel.refresh()
                }
            }
            .also {
                binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenStarted {
                contactsViewModel.contactsUpdates.collect {
                    //todo move check to viewmodel
                    if (it is OperationResult.Success) {
                        contactAdapter.items = it.value
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun navigateToDetails(id: Int) {
        val ft = requireActivity().supportFragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, DetailsFragment.newInstance(id), "DetailsFragment")
        ft.addToBackStack("DetailsFragment")
        ft.commit();
    }
}
package com.ajax.ajaxtestassignment.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.ajax.ajaxtestassignment.R
import com.ajax.ajaxtestassignment.common.OperationResult
import com.ajax.ajaxtestassignment.common.ViewModelFactory
import com.ajax.ajaxtestassignment.databinding.FragmentDetailsBinding
import com.ajax.ajaxtestassignment.ui.contactslist.viewmodel.ContactListViewModel
import com.ajax.ajaxtestassignment.ui.details.viewmodel.ContactDetailsViewModel
import com.bumptech.glide.Glide
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


open class DetailsFragment : DaggerFragment() {
    var binding: FragmentDetailsBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var contactViewModel: ContactDetailsViewModel

    var contactId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactViewModel = ViewModelProvider(this, viewModelFactory).get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contactId = arguments?.getInt(ID) ?: 0

        return FragmentDetailsBinding.inflate(layoutInflater, container, false)
            .apply {
                delete.setOnClickListener {
                    contactViewModel.delete(contactId)
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
            .also {
                binding = it
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenStarted {
                contactViewModel.contactUpdates(contactId).collect {
                    //todo move check to viewmodel
                    if (it is OperationResult.Success) {
                        binding?.apply {
                            textView3.text = it.value.firstName
                            textView4.text = it.value.lastName

                            Glide.with(requireContext())
                                .load(it.value.photo)
                                .into(imageView2)
                        }
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        val ID: String = "ID"
        fun newInstance(id: Int): DetailsFragment {
            val fragment = DetailsFragment();
            val arguments = bundleOf(ID to id)
            fragment.arguments = arguments

            return fragment
        }
    }
}
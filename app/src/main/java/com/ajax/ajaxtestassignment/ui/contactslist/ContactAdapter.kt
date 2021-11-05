package com.ajax.ajaxtestassignment.ui.contactslist

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajax.ajaxtestassignment.databinding.ItemContactListBinding
import com.ajax.ajaxtestassignment.ui.contactslist.viewmodel.Contact
import com.bumptech.glide.Glide

class ContactAdapter(
    private val context: Activity,
    val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    var items: List<Contact> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            ItemContactListBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = items[position]
        holder.name.text = contact.firstName
        holder.lastname.text = contact.lastName
        holder.delete.setOnClickListener {
            onDelete(contact.id)
        }

        Glide.with(context)
            .load(contact.photo)
            .into(holder.picture)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder(binding: ItemContactListBinding) : RecyclerView.ViewHolder(binding.root) {
    val name = binding.textView
    val lastname = binding.textView2
    val picture = binding.imageView
    val delete = binding.button
}
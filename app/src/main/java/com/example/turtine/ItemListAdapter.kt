package com.example.turtine


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turtine.data.Item
import com.example.turtine.databinding.ItemListItemBinding

/**
 * [ListAdapter] implementation for the recyclerview.
 */

class ItemListAdapter(
    private val onItemClicked: (Item) -> Unit,
    private val onTimeClicked: (Item) -> Unit,
) :
    ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            root.setOnClickListener {
                onItemClicked(item)
            }

            timerImageView.setOnClickListener {
                onTimeClicked(item)
            }

            itemListRoutine.text = item.itemRoutine
            itemListMin.text = item.itemMin.toString()
            itemListSec.text = item.itemSec.toString()
        }
    }

    class ItemViewHolder(val binding: ItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemRoutine == newItem.itemRoutine
            }
        }
    }
}
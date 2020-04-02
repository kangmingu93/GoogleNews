package com.myrealtrip.newsreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.myrealtrip.newsreader.R
import com.myrealtrip.newsreader.databinding.LayoutItemBinding
import com.myrealtrip.newsreader.model.Item

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val items : ArrayList<Item> = ArrayList()
    private lateinit var itemClick: ItemClick

    interface ItemClick { fun onClick(view: View, position: Int) }

    fun setItemClickListener(itemClickListener: ItemClick) {
        this.itemClick = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item)
            itemView.tag = item
            itemView.setOnClickListener {
                    v -> itemClick.onClick(v, position)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    // 단일 추가
    fun addItem(item: Item) {
        items.add(item)
        notifyDataSetChanged()
    }

    // 단일 조회
    fun getItem(position: Int) : Item {
        return items[position]
    }

    // 초기화
    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: LayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(i: Item) {
            binding.apply {
                item = i
            }
        }

    }

}
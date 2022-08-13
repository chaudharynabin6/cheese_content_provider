package com.chaudharynabin6.cheese_content_provider.presentation

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chaudharynabin6.cheese_content_provider.data.datasource.local.entities.Cheese
import com.chaudharynabin6.cheese_content_provider.databinding.RvCheeseItemBinding
import javax.inject.Inject

class CheeseAdapter @Inject constructor(): RecyclerView.Adapter<CheeseAdapter.CheeseViewHolder>() {

    private var cursor: Cursor? = null

    inner class CheeseViewHolder(val binding: RvCheeseItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseViewHolder {
        val binding =
            RvCheeseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheeseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) {
        cursor?.let { cursor ->
            if (cursor.moveToPosition(position)) {
                holder.binding.rvChCheese.text = cursor.getString(
                    cursor.getColumnIndexOrThrow(Cheese.COLUMN_NAME)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    fun setCheeses(cursor: Cursor?) {
        this.cursor = cursor
        notifyDataSetChanged()
    }
}
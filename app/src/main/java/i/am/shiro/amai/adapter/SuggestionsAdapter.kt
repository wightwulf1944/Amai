package i.am.shiro.amai.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import i.am.shiro.amai.adapter.SuggestionsAdapter.ViewHolder
import i.am.shiro.amai.databinding.ItemSearchSuggestionBinding
import i.am.shiro.amai.util.inflateChild

class SuggestionsAdapter : ListAdapter<String, ViewHolder>(DiffCallback()) {

    var onSuggestionCLickListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = parent.inflateChild(ItemSearchSuggestionBinding::inflate)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = getItem(position)
        holder.b.textView.text = suggestion
        holder.b.textView.setOnClickListener {
            onSuggestionCLickListener(suggestion)
        }
    }

    class ViewHolder(val b: ItemSearchSuggestionBinding) : RecyclerView.ViewHolder(b.root)
}

private class DiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}
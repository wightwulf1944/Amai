package i.am.shiro.amai.adapter

import android.view.ViewGroup
import android.widget.TextView
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
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = getItem(position)
        holder.textView.text = suggestion
        holder.textView.setOnClickListener {
            onSuggestionCLickListener(suggestion)
        }
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
}

private class DiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}
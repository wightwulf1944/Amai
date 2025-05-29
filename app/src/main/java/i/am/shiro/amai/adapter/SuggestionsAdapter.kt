package i.am.shiro.amai.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import i.am.shiro.amai.adapter.SuggestionsAdapter.ViewHolder
import i.am.shiro.amai.databinding.ItemSearchSuggestionBinding
import i.am.shiro.amai.util.inflateChild

class SuggestionsAdapter : RecyclerView.Adapter<ViewHolder>() {

    var suggestions = emptyList<String>()
        set(value) {
            field = value
            notifyDataSetChanged() // TODO optimize this
        }

    var onSuggestionCLickListener: (String) -> Unit = {}

    override fun getItemCount() = suggestions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = parent.inflateChild(ItemSearchSuggestionBinding::inflate)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.textView.text = suggestion
        holder.textView.setOnClickListener {
            onSuggestionCLickListener(suggestion)
        }
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
}

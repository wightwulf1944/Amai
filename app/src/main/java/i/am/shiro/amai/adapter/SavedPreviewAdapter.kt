package i.am.shiro.amai.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.SavedPreviewAdapter.ViewHolder
import i.am.shiro.amai.data.view.SavedPreviewView
import i.am.shiro.amai.util.inflateChild

class SavedPreviewAdapter(
    private val onItemClick: (SavedPreviewView) -> Unit,
    private val onItemLongClick: (SavedPreviewView) -> Unit
) : ListAdapter<SavedPreviewView, ViewHolder>(DiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = getItem(position).bookId.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflateChild(R.layout.item_staggered_book))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val thumbnailImage = view.findViewById<ImageView>(R.id.thumbnailImage)

        private val titleText = view.findViewById<TextView>(R.id.titleText)

        private val pageText = view.findViewById<TextView>(R.id.pageText)

        fun bind(position: Int) {
            val book = getItem(position)

            itemView.setOnClickListener { onItemClick(book) }

            itemView.setOnLongClickListener {
                onItemLongClick(book)
                true
            }

            titleText.text = book.title

            pageText.text = book.pageCount.toString()

            thumbnailImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                dimensionRatio = "${book.thumbnailWidth}:${book.thumbnailHeight}"
            }

            thumbnailImage.load(book.thumbnailUrl) {
                allowRgb565(true)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<SavedPreviewView>() {

        override fun areItemsTheSame(oldItem: SavedPreviewView, newItem: SavedPreviewView): Boolean {
            return oldItem.bookId == newItem.bookId
        }

        override fun areContentsTheSame(oldItem: SavedPreviewView, newItem: SavedPreviewView) = true
    }
}

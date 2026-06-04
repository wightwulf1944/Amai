package i.am.shiro.amai.adapter

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import i.am.shiro.amai.adapter.FavoritesPreviewAdapter.ViewHolder
import i.am.shiro.amai.data.view.FavoritesPreviewView
import i.am.shiro.amai.databinding.ItemStaggeredBookBinding
import i.am.shiro.amai.util.inflateChild

class FavoritesPreviewAdapter(
    private val onItemClick: (FavoritesPreviewView) -> Unit,
    private val onItemLongClick: (FavoritesPreviewView) -> Unit
) : ListAdapter<FavoritesPreviewView, ViewHolder>(DiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = getItem(position).bookId.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflateChild(ItemStaggeredBookBinding::inflate))

    override fun onBindViewHolder(holder: ViewHolder, position: Int): Unit = with(holder.binding) {
        val book = getItem(position)

        root.setOnClickListener { onItemClick(book) }

        root.setOnLongClickListener {
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

    class ViewHolder(val binding: ItemStaggeredBookBinding) : RecyclerView.ViewHolder(binding.root)

    private class DiffCallback : DiffUtil.ItemCallback<FavoritesPreviewView>() {

        override fun areItemsTheSame(oldItem: FavoritesPreviewView, newItem: FavoritesPreviewView) =
            oldItem.bookId == newItem.bookId

        override fun areContentsTheSame(oldItem: FavoritesPreviewView, newItem: FavoritesPreviewView) = true
    }
}

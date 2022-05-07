package i.am.shiro.amai.adapter

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import i.am.shiro.amai.adapter.CachedPreviewAdapter.ViewHolder
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.databinding.ItemStaggeredBookBinding
import i.am.shiro.amai.util.inflateChild

class CachedPreviewAdapter(
    private val onItemClick: (CachedPreviewView) -> Unit,
    private val onPositionBind: (Int) -> Unit
) : ListAdapter<CachedPreviewView, ViewHolder>(DiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = getItem(position).bookId.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflateChild(ItemStaggeredBookBinding::inflate))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val book = getItem(position)

            root.setOnClickListener { onItemClick(book) }

            savedBadge.isVisible = book.isSaved
            titleText.text = book.title
            pageText.text = book.pageCount.toString()

            thumbnailImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                dimensionRatio = "${book.thumbnailWidth}:${book.thumbnailHeight}"
            }

            thumbnailImage.load(book.thumbnailUrl) {
                bitmapConfig(Bitmap.Config.RGB_565)
            }
        }

        onPositionBind(position)
    }

    class ViewHolder(val binding: ItemStaggeredBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class DiffCallback : DiffUtil.ItemCallback<CachedPreviewView>() {

        override fun areItemsTheSame(oldItem: CachedPreviewView, newItem: CachedPreviewView) =
            oldItem.bookId == newItem.bookId

        override fun areContentsTheSame(oldItem: CachedPreviewView, newItem: CachedPreviewView) =
            oldItem.isSaved == newItem.isSaved
    }
}
package i.am.shiro.amai.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import i.am.shiro.amai.R
import i.am.shiro.amai.adapter.CachedPreviewAdapter.ViewHolder
import i.am.shiro.amai.data.view.CachedPreviewView
import i.am.shiro.amai.util.inflateChild
import kotlin.math.min

class CachedPreviewAdapter(
    private val parentFragment: Fragment,
    private val onItemClick: (CachedPreviewView) -> Unit,
    private val onPositionBind: (Int) -> Unit
) : ListAdapter<CachedPreviewView, ViewHolder>(DiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = getItem(position).bookId.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflateChild(R.layout.item_staggered_book))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        onPositionBind(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val thumbnailImage = view.findViewById<ImageView>(R.id.thumbnailImage)

        private val titleText = view.findViewById<TextView>(R.id.titleText)

        private val pageText = view.findViewById<TextView>(R.id.pageText)

        private val savedBadge = view.findViewById<ImageView>(R.id.badgeSaved)

        fun bind(position: Int) {
            val book = getItem(position)

            itemView.setOnClickListener { onItemClick(book) }

            savedBadge.visibility = if (book.isSaved) View.VISIBLE else View.INVISIBLE
            titleText.text = book.title
            pageText.text = book.pageCount.toString()

            thumbnailImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                val width = book.thumbnailWidth
                val maxHeight = (width / 200.0 * 364.0).toInt()
                val height = min(book.thumbnailHeight, maxHeight)
                dimensionRatio = "$width:$height"
            }

            Glide.with(parentFragment)
                .load(book.thumbnailUrl)
                .format(DecodeFormat.PREFER_RGB_565)
                .into(thumbnailImage)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CachedPreviewView>() {

        override fun areItemsTheSame(oldItem: CachedPreviewView, newItem: CachedPreviewView): Boolean {
            return oldItem.bookId == newItem.bookId
        }

        override fun areContentsTheSame(oldItem: CachedPreviewView, newItem: CachedPreviewView): Boolean {
            return oldItem.isSaved == newItem.isSaved
        }
    }
}
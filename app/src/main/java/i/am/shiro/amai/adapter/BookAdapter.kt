package i.am.shiro.amai.adapter

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
import i.am.shiro.amai.model.Book
import i.am.shiro.amai.util.inflateChild

class BookAdapter(
        private val parentFragment: Fragment,
        private val onItemClick: (Book) -> Unit,
        private val onItemLongClick: (Book) -> Unit = {},
        private val onPositionBind: (Int) -> Unit = {}
) : ListAdapter<Book, BookAdapter.ViewHolder>(DiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

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

        private lateinit var book: Book

        init {
            view.setOnClickListener { onItemClick(book) }
            view.setOnLongClickListener { onItemLongClick() }
        }

        private fun onItemLongClick(): Boolean {
            onItemLongClick(book)
            return true
        }

        fun bind(position: Int) {
            book = getItem(position)

            savedBadge.visibility = if (book.isDownloaded) VISIBLE else INVISIBLE
            titleText.text = book.title
            pageText.text = book.pageCount.toString()

            val coverThumbnailImage = book.coverThumbnailImage

            thumbnailImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                val width = coverThumbnailImage.width
                val height = coverThumbnailImage.height
                dimensionRatio = "h,$width:$height"
            }

            Glide.with(parentFragment)
                    .load(coverThumbnailImage.url)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .into(thumbnailImage)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Book>() {

        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.isDownloaded == newItem.isDownloaded
        }
    }
}

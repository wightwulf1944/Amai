package i.am.shiro.amai.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.annimon.stream.function.Consumer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import i.am.shiro.amai.R
import i.am.shiro.amai.model.Book
import i.am.shiro.amai.model.Image
import i.am.shiro.amai.util.addChild
import i.am.shiro.amai.util.inflateChild
import kotlin.math.min

const val HEADER = 0
const val THUMBNAIL = 1

class DetailAdapter(
    private val parentFragment: Fragment,
    private val book: Book,
    private val onThumbnailClickListener: (Int) -> Unit
) : Adapter<ViewHolder>() {

    private val pageThumbnailImages: List<Image> = book.pageThumbnailImages

    override fun getItemCount() = pageThumbnailImages.size + 1

    override fun getItemViewType(position: Int) = if (position == 0) HEADER else THUMBNAIL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            HEADER -> {
                val view = parent.inflateChild(R.layout.item_detail_header)
                HeaderViewHolder(view, book)
            }
            THUMBNAIL -> {
                val view = parent.inflateChild(R.layout.item_preview_image)
                ThumbnailViewHolder(view)
            }
            else -> throw RuntimeException()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ThumbnailViewHolder) {
            holder.bind(pageThumbnailImages[position - 1])
        }
    }

    private class HeaderViewHolder(itemView: View, book: Book) : ViewHolder(itemView) {

        init {
            itemView.findViewById<TextView>(R.id.titleText).text = book.title

            val pageCount = itemView.resources.getString(R.string.pages_format, book.pageCount)
            itemView.findViewById<TextView>(R.id.text_pages).text = pageCount

            val tagsLayout = itemView.findViewById<ViewGroup>(R.id.layout_tags)
            tagsLayout.addTagGroup("Artists", book.artistTags)
            tagsLayout.addTagGroup("Groups", book.groupTags)
            tagsLayout.addTagGroup("Parodies", book.parodyTags)
            tagsLayout.addTagGroup("Characters", book.characterTags)
            tagsLayout.addTagGroup("Language", book.languageTags)
            tagsLayout.addTagGroup("Categories", book.categoryTags)
            tagsLayout.addTagGroup("Tags", book.generalTags)
        }

        private fun ViewGroup.addTagGroup(label: String, tags: List<String>) {
            if (tags.isEmpty()) return

            addChild<ViewGroup>(R.layout.layout_taggroup) {
                addChild<TextView>(R.layout.item_label) {
                    text = label
                }
                for (tag in tags) {
                    addChild<TextView>(R.layout.item_tag) {
                        text = tag
                    }
                }
            }
        }
    }

    private inner class ThumbnailViewHolder(itemView: View) : ViewHolder(itemView) {

        private val previewImage = itemView.findViewById<ImageView>(R.id.thumbnailImage)

        init {
            itemView.setOnClickListener {
                onThumbnailClickListener(adapterPosition - 1)
            }
        }

        fun bind(image: Image) {
            val width = image.width
            val maxHeight = (width / 200.0 * 364.0).toInt()
            val height = min(image.height, maxHeight)

            previewImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                dimensionRatio = "$width:$height"
            }

            Glide.with(parentFragment)
                    .load(image.url)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .into(previewImage)
        }
    }
}

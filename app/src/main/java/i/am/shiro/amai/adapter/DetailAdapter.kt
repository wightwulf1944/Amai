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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import i.am.shiro.amai.R
import i.am.shiro.amai.data.view.ThumbnailView
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.util.addChild
import i.am.shiro.amai.util.inflateChild

private const val HEADER = 0
private const val THUMBNAIL = 1

class DetailAdapter(
    private val parentFragment: Fragment,
    private val model: DetailModel,
    private val onThumbnailClickListener: (Int) -> Unit
) : Adapter<ViewHolder>() {

    override fun getItemCount() = model.pageImages.size + 1

    override fun getItemViewType(position: Int) = if (position == 0) HEADER else THUMBNAIL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            HEADER -> {
                val view = parent.inflateChild(R.layout.item_detail_header)
                HeaderViewHolder(view)
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
            holder.bind(model.pageImages[position - 1])
        }
    }

    private inner class HeaderViewHolder(itemView: View) : ViewHolder(itemView) {

        init {
            itemView.findViewById<TextView>(R.id.titleText).text = model.book.title

            val pageCount = itemView.resources.getString(R.string.pages_format, model.book.pageCount)
            itemView.findViewById<TextView>(R.id.text_pages).text = pageCount

            val tagsLayout = itemView.findViewById<ViewGroup>(R.id.layout_tags)
            tagsLayout.addTagGroup("Artists", model.artistTags)
            tagsLayout.addTagGroup("Groups", model.groupTags)
            tagsLayout.addTagGroup("Parodies", model.parodyTags)
            tagsLayout.addTagGroup("Characters", model.characterTags)
            tagsLayout.addTagGroup("Language", model.languageTags)
            tagsLayout.addTagGroup("Categories", model.categoryTags)
            tagsLayout.addTagGroup("Tags", model.generalTags)
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

        fun bind(thumbnail: ThumbnailView) {
            previewImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                dimensionRatio = "${thumbnail.width}:${thumbnail.height}"
            }

            Glide.with(parentFragment)
                .load(thumbnail.url)
                .format(DecodeFormat.PREFER_RGB_565)
                .into(previewImage)
        }
    }
}
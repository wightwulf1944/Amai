package i.am.shiro.amai.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import i.am.shiro.amai.R
import i.am.shiro.amai.databinding.InflateTagBinding
import i.am.shiro.amai.databinding.InflateTagGroupBinding
import i.am.shiro.amai.databinding.ItemPreviewImageBinding
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.model.Thumbnail
import i.am.shiro.amai.util.addChild

class DetailAdapter(
    private val model: DetailModel,
    private val onThumbnailClick: (Int) -> Unit,
    private val onTagClick: (String) -> Unit
) : GroupieAdapter() {

    init {
        add(HeaderItem())
        addAll(model.thumbnails.map { ThumbnailItem(it) })
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = spanSizeLookup
        spanCount = layoutManager.spanCount
    }

    private inner class HeaderItem : Item<GroupieViewHolder>() {

        private var isBound = false

        override fun getLayout() = R.layout.item_detail_header

        override fun bind(vh: GroupieViewHolder, position: Int) = with(vh) {
            if (isBound) return

            itemView.findViewById<TextView>(R.id.titleText).text = model.title

            val pageCount = itemView.context.getString(R.string.pages_format, model.pageCount)
            itemView.findViewById<TextView>(R.id.text_pages).text = pageCount

            val tagsLayout = itemView.findViewById<ViewGroup>(R.id.layout_tags)
            tagsLayout.addTagGroup(R.string.artists, "artist", model.tags)
            tagsLayout.addTagGroup(R.string.groups, "group", model.tags)
            tagsLayout.addTagGroup(R.string.parodies, "parody", model.tags)
            tagsLayout.addTagGroup(R.string.characters, "character", model.tags)
            tagsLayout.addTagGroup(R.string.language, "language", model.tags)
            tagsLayout.addTagGroup(R.string.categories, "category", model.tags)
            tagsLayout.addTagGroup(R.string.tags, "tag", model.tags)

            isBound = true
        }

        private fun ViewGroup.addTagGroup(
            @StringRes res: Int,
            namespace: String,
            tags: Map<String, List<String>>
        ) {
            val tags = tags[namespace] ?: return

            addChild(InflateTagGroupBinding::inflate) {
                label.setText(res)
                for (tag in tags) {
                    root.addChild(InflateTagBinding::inflate) {
                        root.text = tag
                        root.setOnClickListener {
                            val searchTag = if (tag.contains(Regex("\\s+"))) "\"$tag\"" else tag
                            onTagClick("$namespace:$searchTag")
                        }
                    }
                }
            }
        }
    }

    private inner class ThumbnailItem(
        private val thumbnail: Thumbnail
    ) : BindableItem<ItemPreviewImageBinding>() {

        override fun getLayout() = R.layout.item_preview_image

        override fun getSpanSize(spanCount: Int, position: Int) = 1

        override fun initializeViewBinding(p0: View) = ItemPreviewImageBinding.bind(p0)

        override fun bind(binding: ItemPreviewImageBinding, position: Int) {
            binding.root.setOnClickListener {
                onThumbnailClick(position - 1)
            }

            binding.thumbnailImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                dimensionRatio = "${thumbnail.width}:${thumbnail.height}"
            }

            binding.thumbnailImage.load(thumbnail.url) {
                allowRgb565(true)
            }
        }
    }
}
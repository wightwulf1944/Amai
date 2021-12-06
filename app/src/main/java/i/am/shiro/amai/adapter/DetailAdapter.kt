package i.am.shiro.amai.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import i.am.shiro.amai.R
import i.am.shiro.amai.data.view.ThumbnailView
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.util.addChild

class DetailAdapter(
    private val parentFragment: Fragment,
    private val model: DetailModel,
    private val onThumbnailClick: (Int) -> Unit,
    private val onTagClick: (String) -> Unit
) : GroupieAdapter() {

    init {
        add(HeaderItem())
        addAll(model.pageImages.map { ThumbnailItem(it) })
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = spanSizeLookup
        spanCount = layoutManager.spanCount
    }

    private inner class HeaderItem : Item<GroupieViewHolder>() {

        override fun getLayout() = R.layout.item_detail_header

        override fun bind(vh: GroupieViewHolder, position: Int) = with(vh) {
            itemView.findViewById<TextView>(R.id.titleText).text = model.book.title

            val pageCount = itemView.context.getString(R.string.pages_format, model.book.pageCount)
            itemView.findViewById<TextView>(R.id.text_pages).text = pageCount

            val tagsLayout = itemView.findViewById<ViewGroup>(R.id.layout_tags)
            tagsLayout.addTagGroup(R.string.artists, "artist:", model.artistTags)
            tagsLayout.addTagGroup(R.string.groups, "group:", model.groupTags)
            tagsLayout.addTagGroup(R.string.parodies, "parody:", model.parodyTags)
            tagsLayout.addTagGroup(R.string.characters, "character:", model.characterTags)
            tagsLayout.addTagGroup(R.string.language, "language:", model.languageTags)
            tagsLayout.addTagGroup(R.string.categories, "category:", model.categoryTags)
            tagsLayout.addTagGroup(R.string.tags, "", model.generalTags)
        }

        private fun ViewGroup.addTagGroup(
            @StringRes res: Int,
            namespace: String?,
            tags: List<String>
        ) {
            if (tags.isEmpty()) return

            addChild<ViewGroup>(R.layout.layout_taggroup) {
                addChild<TextView>(R.layout.item_label) {
                    setText(res)
                }
                for (tag in tags) {
                    addChild<TextView>(R.layout.item_tag) {
                        text = tag
                        setOnClickListener {
                            val searchTag = if (tag.contains(Regex("\\s+"))) "\"$tag\"" else tag
                            onTagClick(namespace + searchTag)
                        }
                    }
                }
            }
        }
    }

    private inner class ThumbnailItem(
        private val thumbnail: ThumbnailView
    ) : Item<ThumbnailViewHolder>() {

        override fun getLayout() = R.layout.item_preview_image

        override fun getSpanSize(spanCount: Int, position: Int) = 1

        override fun createViewHolder(itemView: View) = ThumbnailViewHolder(itemView)

        override fun bind(vh: ThumbnailViewHolder, position: Int) {
            vh.itemView.setOnClickListener {
                onThumbnailClick(thumbnail.pageIndex)
            }

            vh.imageView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                dimensionRatio = "${thumbnail.width}:${thumbnail.height}"
            }

            Glide.with(parentFragment)
                .load(thumbnail.url)
                .format(DecodeFormat.PREFER_RGB_565)
                .into(vh.imageView)
        }
    }

    private class ThumbnailViewHolder(rootView: View) : GroupieViewHolder(rootView) {
        val imageView = itemView.findViewById<ImageView>(R.id.thumbnailImage)!!
    }
}
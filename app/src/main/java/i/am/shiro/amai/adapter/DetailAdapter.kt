package i.am.shiro.amai.adapter

import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
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
import i.am.shiro.amai.compose.AmaiTheme
import i.am.shiro.amai.databinding.ItemPreviewImageBinding
import i.am.shiro.amai.model.DetailModel
import i.am.shiro.amai.model.Thumbnail

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

        override fun getLayout() = R.layout.item_detail_header

        override fun bind(vh: GroupieViewHolder, position: Int) {
            val composeView = vh.itemView as ComposeView
            composeView.setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            composeView.setContent {
                AmaiTheme {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        DetailHeaderContent(
                            model = model,
                            onTagClick = onTagClick
                        )
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
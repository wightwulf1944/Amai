package i.am.shiro.amai.adapter

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import i.am.shiro.amai.R
import i.am.shiro.amai.compose.AmaiTheme
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
    ) : Item<GroupieViewHolder>() {

        override fun getLayout() = R.layout.item_detail_header

        override fun getSpanSize(spanCount: Int, position: Int) = 1

        override fun bind(vh: GroupieViewHolder, position: Int) {
            val composeView = vh.itemView as ComposeView
            composeView.setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            composeView.setContent {
                AmaiTheme {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        ThumbnailContent(
                            thumbnail = thumbnail,
                            onClick = { onThumbnailClick(position - 1) }
                        )
                    }
                }
            }
        }
    }
}
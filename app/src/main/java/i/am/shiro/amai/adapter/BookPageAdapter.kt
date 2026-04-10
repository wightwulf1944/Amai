package i.am.shiro.amai.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.imageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import i.am.shiro.amai.data.entity.RemoteImageEntity
import i.am.shiro.amai.databinding.ItemReadPageBinding
import i.am.shiro.amai.util.inflateChild

class BookPageAdapter(
    private val pages: List<RemoteImageEntity>
) : RecyclerView.Adapter<BookPageAdapter.ViewHolder>() {

    override fun getItemCount() = pages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflateChild(ItemReadPageBinding::inflate))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page = pages[position]

        holder.pageImage.dispose()

        val smallRequest = ImageRequest.Builder(holder.pageImage.context)
            .data(page.thumbnailUrl)
            .target { drawable ->
                holder.pageImage.load(page.url) {
                    memoryCachePolicy(CachePolicy.DISABLED)
                    placeholder(drawable)
                }
            }
            .build()

        holder.pageImage.context.imageLoader.enqueue(smallRequest)
    }

    class ViewHolder(binding: ItemReadPageBinding) : RecyclerView.ViewHolder(binding.root) {
        val pageImage = binding.root
    }
}

package i.am.shiro.amai.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import i.am.shiro.amai.data.view.PageView
import i.am.shiro.amai.databinding.ItemReadPageBinding
import i.am.shiro.amai.util.inflateChild
import java.io.File

class BookPageAdapter(
    private val pages: List<PageView>
) : RecyclerView.Adapter<BookPageAdapter.ViewHolder>() {

    override fun getItemCount() = pages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflateChild(ItemReadPageBinding::inflate))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page = pages[position]
        // TODO use page.thumbnailUrl as intermediate image
        if (page.url.startsWith("https://")) {
            holder.pageImage.load(page.url)
        } else {
            holder.pageImage.load(File(page.url))
        }
    }

    class ViewHolder(binding: ItemReadPageBinding) : RecyclerView.ViewHolder(binding.root) {
        val pageImage = binding.root
    }
}
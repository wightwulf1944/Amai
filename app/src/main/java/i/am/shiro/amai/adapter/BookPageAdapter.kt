package i.am.shiro.amai.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import i.am.shiro.amai.R
import i.am.shiro.amai.data.view.PageView
import i.am.shiro.amai.util.inflateChild
import java.io.File

class BookPageAdapter(
    private val pages: List<PageView>
) : RecyclerView.Adapter<BookPageAdapter.ViewHolder>() {

    override fun getItemCount() = pages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflateChild(R.layout.item_read_page))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page = pages[position]
        // TODO use page.thumbnailUrl as intermediate image
        if (page.url.startsWith("https://")) {
            holder.pageImage.load(page.url)
        } else {
            holder.pageImage.load(File(page.url))
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pageImage: ImageView = itemView as ImageView
    }
}
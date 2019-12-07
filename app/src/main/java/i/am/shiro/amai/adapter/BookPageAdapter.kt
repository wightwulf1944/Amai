package i.am.shiro.amai.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import i.am.shiro.amai.R
import i.am.shiro.amai.model.Image
import i.am.shiro.amai.util.inflateChild

class BookPageAdapter(
    parentFragment: Fragment,
    private val data: List<Image>
) : RecyclerView.Adapter<BookPageAdapter.ViewHolder>() {

    private val requestManager = Glide.with(parentFragment)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflateChild(R.layout.item_read_page))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        requestManager
            .load(data[position].url)
            .thumbnail(0.1f)
            .into(holder.pageImage)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        requestManager.clear(holder.pageImage)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pageImage: ImageView = itemView as ImageView
    }
}
package i.am.shiro.amai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import i.am.shiro.amai.R;
import i.am.shiro.amai.model.Book;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private final List<String> items = new ArrayList<>();

    private final Set<Integer> labelIndexes = new HashSet<>();

    public TagAdapter(Book book) {
        addItems("Parodies:", book.getParodyTags());
        addItems("Characters:", book.getCharacterTags());
        addItems("Tags:", book.getGeneralTags());
        addItems("Artists:", book.getArtistTags());
        addItems("Groups:", book.getGroupTags());
        addItems("Language:", book.getLanguageTags());
        addItems("Categories:", book.getCategoryTags());
    }

    private void addItems(String label, List<String> tags) {
        if (tags.isEmpty()) return;

        labelIndexes.add(items.size());
        items.add(label);
        items.addAll(tags);
    }

    @Override
    public int getItemViewType(int position) {
        if (labelIndexes.contains(position)) {
            return R.layout.item_label;
        } else {
            return R.layout.item_tag;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(viewType, parent, false);

        // current FlexBoxLayoutManager does not respect wrapBefore attribute
        if (viewType == R.layout.item_label) {
            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) view.getLayoutParams();
            layoutParams.setWrapBefore(true);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = items.get(position);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}

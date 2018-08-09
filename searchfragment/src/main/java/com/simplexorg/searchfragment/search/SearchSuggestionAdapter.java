package com.simplexorg.searchfragment.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.decorator.SearchSuggestionDecorator.OnSuggestionClickListener;
import com.simplexorg.searchfragment.search.SearchSuggestionAdapter.SearchSuggestionViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionViewHolder> {

    private List<String> mSuggestions;
    private OnSuggestionClickListener mOnSuggestionClickListener;

    public SearchSuggestionAdapter() {
        mSuggestions = new ArrayList<>();
    }

    public void setOnSuggestionClickListener(OnSuggestionClickListener onSuggestionClickListener) {
        mOnSuggestionClickListener = onSuggestionClickListener;
    }

    @NonNull
    @Override
    public SearchSuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_suggestion, parent, false);
        return new SearchSuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSuggestionViewHolder holder, int position) {
        holder.mSuggestionText.setText(mSuggestions.get(position));
        holder.itemView.setOnClickListener((view) -> {
            if (mOnSuggestionClickListener != null) {
                mOnSuggestionClickListener.onSuggestionClick(holder.mSuggestionText.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSuggestions.size();
    }

    public void setSuggestions(List<String> suggestions) {
        mSuggestions.clear();
        mSuggestions.addAll(suggestions);
        notifyDataSetChanged();
    }

    static class SearchSuggestionViewHolder extends RecyclerView.ViewHolder {
        private TextView mSuggestionText;

        SearchSuggestionViewHolder(View itemView) {
            super(itemView);
            mSuggestionText = itemView.findViewById(R.id.search_id_text);
        }
    }
}

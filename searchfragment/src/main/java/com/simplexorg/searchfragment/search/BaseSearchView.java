package com.simplexorg.searchfragment.search;

import android.content.Context;
import android.text.TextWatcher;
import android.view.View;

import com.simplexorg.searchfragment.search.SearchFragment.OnSearchClickListener;

public abstract class BaseSearchView implements SearchContract.View {
    public abstract void attach(View view);

    public abstract void attach(Context context);

    public abstract void setSearchHint(CharSequence searchHint);

    public abstract void start();

    public abstract void clearFocus();

    public abstract void addTextChangedListener(TextWatcher watcher);

    public abstract void setOnSearchClickListener(OnSearchClickListener onSearchClickListener);
}

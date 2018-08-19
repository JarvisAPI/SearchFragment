package com.simplexorg.searchfragment.decorator;

import android.content.Context;
import android.text.TextWatcher;
import android.view.View;

import com.simplexorg.searchfragment.search.BaseSearchView;
import com.simplexorg.searchfragment.search.SearchContract.Presenter;
import com.simplexorg.searchfragment.search.SearchFragment.OnSearchClickListener;

public abstract class SearchDecorator extends BaseSearchView {
    BaseSearchView mSearchBar;

    SearchDecorator(BaseSearchView searchBar) {
        mSearchBar = searchBar;
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        mSearchBar.addTextChangedListener(watcher);
    }

    @Override
    public void setSearchHint(CharSequence searchHint) {
        mSearchBar.setSearchHint(searchHint);
    }

    @Override
    public void setSearchText(String text) {
        mSearchBar.setSearchText(text);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mSearchBar.setPresenter(presenter);
    }

    @Override
    public void attach(View view) {
        mSearchBar.attach(view);
    }

    @Override
    public void attach(Context context) {
        mSearchBar.attach(context);
    }

    @Override
    public void start() {
        mSearchBar.start();
    }

    @Override
    public void clearFocus() {
        mSearchBar.clearFocus();
    }

    @Override
    public void showRemoveIcon() {
        mSearchBar.showRemoveIcon();
    }

    @Override
    public void hideRemoveIcon() {
        mSearchBar.hideRemoveIcon();
    }

    @Override
    public void gainFocus() {
        mSearchBar.gainFocus();
    }

    @Override
    public void removeFocus() {
        mSearchBar.removeFocus();
    }

    @Override
    public void hideKeyboard() {
        mSearchBar.hideKeyboard();
    }

    @Override
    public void showKeyboard() {
        mSearchBar.showKeyboard();
    }

    @Override
    public void removeText() {
        mSearchBar.removeText();
    }

    @Override
    public void initDisplay() {
        mSearchBar.initDisplay();
    }

    @Override
    public void setOnSearchClickListener(OnSearchClickListener onSearchClickListener) {
        mSearchBar.setOnSearchClickListener(onSearchClickListener);
    }
}

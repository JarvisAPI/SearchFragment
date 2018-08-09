package com.simplexorg.searchfragment.search;

import android.support.annotation.NonNull;

class SearchPresenter implements SearchContract.Presenter {
    private static final String TAG = SearchPresenter.class.getSimpleName();

    private SearchContract.View mSearchView;

    private SearchPresenter() {

    }

    /**
     * Attach a search presenter to a search view.
     * @param searchView the search view to attach.
     */
    static void attach(@NonNull SearchContract.View searchView) {
        SearchPresenter presenter = new SearchPresenter();
        presenter.mSearchView = searchView;
        presenter.mSearchView.setPresenter(presenter);
    }

    @Override
    public void onTextChanged(@NonNull String text) {
        if (text.isEmpty()) {
            mSearchView.hideRemoveIcon();
        } else {
            mSearchView.showRemoveIcon();
        }
    }

    @Override
    public void onOutsideClicked() {
        mSearchView.removeFocus();
        mSearchView.hideKeyboard();
    }

    @Override
    public void onSearchClicked() {
        mSearchView.gainFocus();
        mSearchView.showKeyboard();
    }

    @Override
    public void onRemoveTextClicked() {
        mSearchView.removeText();
        mSearchView.hideRemoveIcon();
    }

    @Override
    public void subscribe() {
        mSearchView.removeFocus();
        mSearchView.initDisplay();
    }
}

package com.simplexorg.searchfragment.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator;
import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.OnMenuClickListener;
import com.simplexorg.searchfragment.decorator.SearchIconDecorator;
import com.simplexorg.searchfragment.decorator.SearchSuggestionDecorator;
import com.simplexorg.searchfragment.decorator.SearchSuggestionDecorator.OnSuggestionClickListener;


public class SearchFragment extends Fragment {
    private static final String TAG = SearchFragment.class.getSimpleName();

    private BaseSearchView mSearchBaseView;
    private NavDrawerSearchDecorator mNavDecor;
    private SearchIconDecorator mIconDecor;
    private SearchSuggestionDecorator mSuggestDecor;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_search, container, false);
        mSearchBaseView.attach(view);
        mSearchBaseView.attach(getContext());
        return view;
    }

    public void clearFocus() {
        mSearchBaseView.clearFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Search Fragment resume!");
        mSearchBaseView.start();
    }

    @Override
    public void onInflate(Context context, AttributeSet attributeSet, Bundle savedInstanceState) {
        super.onInflate(context, attributeSet, savedInstanceState);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SearchFragment);
        extractDecorators(typedArray.getText(R.styleable.SearchFragment_search_bar_decorators));

        mSearchBaseView.setSearchHint(typedArray.getText(R.styleable.SearchFragment_search_hint));

        typedArray.recycle();
    }

    private void extractDecorators(CharSequence decorators) {
        final String DECOR_NAV = "nav";
        final String DECOR_ICONS = "icons";
        final String DECOR_SUGGEST = "suggest";
        mSearchBaseView = new SearchView();

        String[] decors = decorators.toString().split("\\|");
        for (String decor : decors) {
            switch (decor) {
                case DECOR_NAV:
                    mNavDecor = new NavDrawerSearchDecorator(mSearchBaseView);
                    mSearchBaseView = mNavDecor;
                    break;
                case DECOR_ICONS:
                    mIconDecor = new SearchIconDecorator(mSearchBaseView);
                    mSearchBaseView = mIconDecor;
                    break;
                case DECOR_SUGGEST:
                    mSuggestDecor = new SearchSuggestionDecorator(mSearchBaseView);
                    mSearchBaseView = mSuggestDecor;
                    break;
            }
        }

        SearchPresenter.attach(mSearchBaseView);
    }

    /**
     * Set on menu click listener if search bar is decorated with "nav".
     * @param onMenuClickListener listener.
     */
    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        if (mNavDecor != null) {
            mNavDecor.setOnMenuClickListener(onMenuClickListener);
        }
    }

    /**
     * Add icons to the search bar if it is decorated with "icons".
     * @param resourceIds drawable resource ids for the icons.
     */
    public void addIcons(int[] resourceIds) {
        if (mIconDecor != null) {
            mIconDecor.addIcons(resourceIds);
        }
    }

    /**
     * Attach click listeners to icons if search bar is decorated with "icons".
     * @param iconIndex the index that icon is in.
     * @param listener the listener to attach.
     */
    public void attachOnClickListener(int iconIndex, OnClickListener listener) {
        if (mIconDecor != null) {
            mIconDecor.attachOnClickListener(iconIndex, listener);
        }
    }

    /**
     * Add text changed listener to the search bar.
     * @param watcher the text watcher.
     */
    public void addTextChangedListener(TextWatcher watcher) {
        mSearchBaseView.addTextChangedListener(watcher);
    }

    /**
     * Update search suggestions if search bar is decorated with "suggest".
     * @param searchData data used to get the suggestions.
     */
    public void updateSuggestion(SearchData searchData) {
        if (mSuggestDecor != null) {
            mSuggestDecor.updateSuggestions(searchData);
        }
    }

    /**
     * Set on suggestion click listener if search bar is decorated with "suggest".
     * @param onSuggestionClickListener listener invoked when suggestion is clicked.
     */
    public void setOnSuggestionClickListener(OnSuggestionClickListener onSuggestionClickListener) {
        if (mSuggestDecor != null) {
            mSuggestDecor.setOnSuggestionClickListener(onSuggestionClickListener);
        }
    }

    /**
     * Set search suggestion supplier if search bar is decorated with "suggest".
     * @param searchSuggestionSupplier supplier used to obtain search suggestions.
     */
    public void setSearchSuggestionSupplier(SearchSuggestionSupplier searchSuggestionSupplier) {
        if (mSuggestDecor != null) {
            mSuggestDecor.setSearchSuggestionSupplier(searchSuggestionSupplier);
        }
    }
}

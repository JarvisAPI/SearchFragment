package com.simplexorg.searchfragment.util;

import android.support.annotation.VisibleForTesting;

import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator;
import com.simplexorg.searchfragment.decorator.SearchIconDecorator;
import com.simplexorg.searchfragment.decorator.SearchSuggestionDecorator;
import com.simplexorg.searchfragment.search.BaseSearchView;
import com.simplexorg.searchfragment.search.SearchView;

public class Factory {
    private static Factory mFactory;

    private Factory() {

    }

    public BaseSearchView createBaseSearchView() {
        return new SearchView();
    }

    public NavDrawerSearchDecorator wrapNavDecorator(BaseSearchView baseSearchView) {
        return new NavDrawerSearchDecorator(baseSearchView);
    }

    public SearchIconDecorator wrapIconDecorator(BaseSearchView baseSearchView) {
        return new SearchIconDecorator(baseSearchView);
    }

    public SearchSuggestionDecorator wrapSuggestionDecorator(BaseSearchView baseSearchView) {
        return new SearchSuggestionDecorator(baseSearchView);
    }

    public static Factory getInstance() {
        if (mFactory == null) {
            mFactory = new Factory();
        }
        return mFactory;
    }

    @VisibleForTesting
    public static void setFactory(Factory factory) {
        mFactory = factory;
    }
}

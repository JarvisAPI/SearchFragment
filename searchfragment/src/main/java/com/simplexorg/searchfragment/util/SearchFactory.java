package com.simplexorg.searchfragment.util;

import android.support.annotation.VisibleForTesting;

import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator;
import com.simplexorg.searchfragment.decorator.SearchIconDecorator;
import com.simplexorg.searchfragment.decorator.SearchSuggestionDecorator;
import com.simplexorg.searchfragment.search.BaseSearchView;
import com.simplexorg.searchfragment.search.SearchView;

public class SearchFactory {
    private static SearchFactory mFactory;

    private SearchFactory() {

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

    public ClassLoader getClassLoader(String className) {
        try {
            return Class.forName(className).getClassLoader();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static SearchFactory getInstance() {
        if (mFactory == null) {
            mFactory = new SearchFactory();
        }
        return mFactory;
    }

    @VisibleForTesting
    public static void setFactory(SearchFactory factory) {
        mFactory = factory;
    }
}

package com.simplexorg.searchfragment.search;

import com.simplexorg.searchfragment.model.Suggestion;

import java.util.List;

public interface SearchSuggestionSupplier {
    interface OnSuggestionObtainedListener {
        void onSuggestionObtained(List<Suggestion> suggestions);
    }

    void getSuggestion(SearchData searchData, OnSuggestionObtainedListener listener);
}

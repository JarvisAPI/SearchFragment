package com.simplexorg.searchfragment.search;

import java.util.List;

public interface SearchSuggestionSupplier {
    interface OnSuggestionObtainedListener {
        void onSuggestionObtained(List<String> suggestions);
    }

    void getSuggestion(SearchData searchData, OnSuggestionObtainedListener listener);
}

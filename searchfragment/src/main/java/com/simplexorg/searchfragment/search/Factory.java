package com.simplexorg.searchfragment.search;

import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator;
import com.simplexorg.searchfragment.decorator.SearchIconDecorator;
import com.simplexorg.searchfragment.decorator.SearchSuggestionDecorator;

class Factory {
    BaseSearchView createBaseSearchView() {
        return new SearchView();
    }

    NavDrawerSearchDecorator wrapNavDecorator(BaseSearchView baseSearchView) {
        return new NavDrawerSearchDecorator(baseSearchView);
    }

    SearchIconDecorator wrapIconDecorator(BaseSearchView baseSearchView) {
        return new SearchIconDecorator(baseSearchView);
    }

    SearchSuggestionDecorator wrapSuggestionDecorator(BaseSearchView baseSearchView) {
        return new SearchSuggestionDecorator(baseSearchView);
    }
}

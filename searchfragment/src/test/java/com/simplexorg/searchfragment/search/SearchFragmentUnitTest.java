package com.simplexorg.searchfragment.search;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.AttributeSet;
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
import com.simplexorg.searchfragment.search.SearchFragment.OnSearchClickListener;
import com.simplexorg.searchfragment.util.Factory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.simplexorg.searchfragment.search.SearchFragment.SAVED_SUGGEST_DECOR;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchFragmentUnitTest {
    private SearchFragment mSearchFragment;
    @Mock private Factory mFactory;
    @Mock private Context mContext;
    @Mock private TypedArray typedArray;
    @Mock private BaseSearchView mSearchBaseView;

    @Before
    public void setup() {
        initMocks(this);
        when(mFactory.createBaseSearchView()).thenReturn(mSearchBaseView);
        mSearchFragment = new SearchFragment();
        Factory.setFactory(mFactory);
    }

    @After
    public void cleanup() {
        Factory.setFactory(null);
    }

    private void fragmentOnInflate() {
        AttributeSet attributeSet = mock(AttributeSet.class);
        Bundle savedInstanceState = mock(Bundle.class);
        when(mContext.obtainStyledAttributes(any(AttributeSet.class), any(int[].class)))
                .thenReturn(typedArray);
        mSearchFragment.onInflate(mContext, attributeSet, savedInstanceState);
    }

    @Test
    public void test_onCreateView() {
        fragmentOnInflate();
        verify(mSearchBaseView).setSearchHint(any(CharSequence.class));

        LayoutInflater inflater = mock(LayoutInflater.class);
        View view = mock(View.class);
        when(inflater.inflate(anyInt(), any(ViewGroup.class), anyBoolean())).thenReturn(view);
        mSearchFragment.onCreateView(inflater, mock(ViewGroup.class), mock(Bundle.class));
        verify(mSearchBaseView).attach(view);
        verify(mSearchBaseView).attach(any(Context.class));
    }

    @Test
    public void test_clearFocus() {
        fragmentOnInflate();
        mSearchFragment.clearFocus();
        verify(mSearchBaseView).clearFocus();
    }

    @Test
    public void test_onResume() {
        fragmentOnInflate();
        mSearchFragment.onResume();
        verify(mSearchBaseView).start();
    }

    @Test
    public void test_setup_nav_decorator_before_fragmentInflated() {
        OnMenuClickListener onMenuClickListener = mock(OnMenuClickListener.class);
        mSearchFragment.setOnMenuClickListener(onMenuClickListener);

        NavDrawerSearchDecorator navDecor = mock(NavDrawerSearchDecorator.class);
        when(mFactory.wrapNavDecorator(mSearchBaseView)).thenReturn(navDecor);
        when(typedArray.getText(R.styleable.SearchFragment_search_bar_decorators)).thenReturn("nav");
        fragmentOnInflate();

        verify(navDecor).setOnMenuClickListener(onMenuClickListener);
    }

    @Test
    public void test_setup_nav_decorator_after_fragmentInflated() {
        NavDrawerSearchDecorator navDecor = mock(NavDrawerSearchDecorator.class);
        when(mFactory.wrapNavDecorator(mSearchBaseView)).thenReturn(navDecor);
        when(typedArray.getText(R.styleable.SearchFragment_search_bar_decorators)).thenReturn("nav");
        fragmentOnInflate();

        OnMenuClickListener onMenuClickListener = mock(OnMenuClickListener.class);
        mSearchFragment.setOnMenuClickListener(onMenuClickListener);

        verify(navDecor).setOnMenuClickListener(onMenuClickListener);
    }

    private SearchIconDecorator inflateIconDecor() {
        SearchIconDecorator iconDecor = mock(SearchIconDecorator.class);
        when(mFactory.wrapIconDecorator(mSearchBaseView)).thenReturn(iconDecor);
        when(typedArray.getText(R.styleable.SearchFragment_search_bar_decorators)).thenReturn("icons");
        fragmentOnInflate();

        return iconDecor;
    }

    @Test
    public void test_addIconsTo_icon_decorator_before_fragmentInflated() {
        int[] resourceIds = new int[0];
        mSearchFragment.addIcons(resourceIds);

        SearchIconDecorator iconDecor = inflateIconDecor();

        verify(iconDecor).addIcons(resourceIds);
    }

    @Test
    public void test_addIconsTo_icon_decorator_after_fragmentInflated() {
        SearchIconDecorator iconDecor = inflateIconDecor();

        int[] resourceIds = new int[0];
        mSearchFragment.addIcons(resourceIds);

        verify(iconDecor).addIcons(resourceIds);
    }

    @Test
    public void test_attachOnClickListenerTo_icon_decorator_before_fragmentInflated() {
        OnClickListener listener = mock(OnClickListener.class);
        int iconIndex = 0;
        mSearchFragment.attachOnClickListener(iconIndex, listener);

        SearchIconDecorator iconDecor = inflateIconDecor();

        verify(iconDecor).attachOnClickListener(iconIndex, listener);
    }

    @Test
    public void test_attachOnClickListenerTo_icon_decorator_after_fragmentInflated() {
        SearchIconDecorator iconDecor = inflateIconDecor();

        OnClickListener listener = mock(OnClickListener.class);
        int iconIndex = 0;
        mSearchFragment.attachOnClickListener(iconIndex, listener);

        verify(iconDecor).attachOnClickListener(iconIndex, listener);
    }

    @Test
    public void test_addTextChangedListener() {
        fragmentOnInflate();
        TextWatcher watcher = mock(TextWatcher.class);
        mSearchFragment.addTextChangedListener(watcher);
        verify(mSearchBaseView).addTextChangedListener(watcher);
    }

    private SearchSuggestionDecorator inflateSuggestDecor() {
        SearchSuggestionDecorator suggestDecor = mock(SearchSuggestionDecorator.class);
        when(mFactory.wrapSuggestionDecorator(mSearchBaseView)).thenReturn(suggestDecor);
        when(typedArray.getText(R.styleable.SearchFragment_search_bar_decorators)).thenReturn("suggest");
        fragmentOnInflate();

        return suggestDecor;
    }

    @Test
    public void test_updateSuggestionTo_suggestion_decorator() {
        SearchData searchData = mock(SearchData.class);
        mSearchFragment.updateSuggestion(searchData);

        SearchSuggestionDecorator suggestDecor = inflateSuggestDecor();
        mSearchFragment.updateSuggestion(searchData);

        verify(suggestDecor).updateSuggestions(searchData);
    }

    @Test
    public void test_setOnSuggestionClickListenerTo_suggestion_decorator_before_fragmentInflated() {
        OnSuggestionClickListener onSuggestionClickListener = mock(OnSuggestionClickListener.class);
        mSearchFragment.setOnSuggestionClickListener(onSuggestionClickListener);

        SearchSuggestionDecorator suggestDecor = inflateSuggestDecor();

        verify(suggestDecor).setOnSuggestionClickListener(onSuggestionClickListener);
    }

    @Test
    public void test_setOnSuggestionClickListenerTo_suggestion_decorator_after_fragmentInflated() {
        SearchSuggestionDecorator suggestDecor = inflateSuggestDecor();

        OnSuggestionClickListener onSuggestionClickListener = mock(OnSuggestionClickListener.class);
        mSearchFragment.setOnSuggestionClickListener(onSuggestionClickListener);

        verify(suggestDecor).setOnSuggestionClickListener(onSuggestionClickListener);
    }

    @Test
    public void test_setSearchSuggestionSupplierTo_suggestion_decorator_before_fragmentInflated() {
        SearchSuggestionSupplier searchSuggestionSupplier = mock(SearchSuggestionSupplier.class);
        mSearchFragment.setSearchSuggestionSupplier(searchSuggestionSupplier);

        SearchSuggestionDecorator suggestDecor = inflateSuggestDecor();

        verify(suggestDecor).setSearchSuggestionSupplier(searchSuggestionSupplier);
    }

    @Test
    public void test_setSearchSuggestionSupplierTo_suggestion_decorator_after_fragmentInflated() {
        SearchSuggestionDecorator suggestDecor = inflateSuggestDecor();

        SearchSuggestionSupplier searchSuggestionSupplier = mock(SearchSuggestionSupplier.class);
        mSearchFragment.setSearchSuggestionSupplier(searchSuggestionSupplier);

        verify(suggestDecor).setSearchSuggestionSupplier(searchSuggestionSupplier);
    }

    @Test
    public void test_setOnSearchClickListenerTo_before_fragmentInflated() {
        OnSearchClickListener onSearchClickListener = mock(OnSearchClickListener.class);
        mSearchFragment.setOnSearchClickListener(onSearchClickListener);

        fragmentOnInflate();

        verify(mSearchBaseView).setOnSearchClickListener(onSearchClickListener);
    }

    @Test
    public void test_setOnSearchClickListenerTo_suggestion_decorator_after_fragmentInflated() {
        fragmentOnInflate();

        OnSearchClickListener onSearchClickListener = mock(OnSearchClickListener.class);
        mSearchFragment.setOnSearchClickListener(onSearchClickListener);

        verify(mSearchBaseView).setOnSearchClickListener(onSearchClickListener);
    }

    @Test
    public void test_onSaveInstanceState_whenSuggestDecorNotNull() {
        SearchSuggestionDecorator suggestDecor = inflateSuggestDecor();

        Bundle outState = mock(Bundle.class);
        mSearchFragment.onSaveInstanceState(outState);

        verify(outState).putBoolean(SAVED_SUGGEST_DECOR, true);
        verify(suggestDecor).onSaveInstanceState(outState);
    }

    @Test
    public void test_onSaveInstanceState_whenSuggestDecorNull() {
        Bundle outState = mock(Bundle.class);
        mSearchFragment.onSaveInstanceState(outState);

        verify(outState).putBoolean(SAVED_SUGGEST_DECOR, false);
    }

    @Test
    public void test_onActivityCreated_nullSavedInstanceState() {
        mSearchFragment.onActivityCreated(null);
        // Should not throw null pointer exception.
    }

    @Test
    public void test_onActivityCreated_before_fragmentInflated() {
        Bundle savedInstanceState = mock(Bundle.class);
        when(savedInstanceState.getBoolean(SearchFragment.SAVED_SUGGEST_DECOR)).thenReturn(true);

        mSearchFragment.onActivityCreated(savedInstanceState);

        SearchSuggestionDecorator suggestDecor = inflateSuggestDecor();

        verify(suggestDecor).onRestoreSavedState(savedInstanceState);
    }

    @Test
    public void test_onActivityCreated_after_fragmentInflated() {
        SearchSuggestionDecorator suggestDecor = inflateSuggestDecor();

        Bundle savedInstanceState = mock(Bundle.class);
        when(savedInstanceState.getBoolean(SearchFragment.SAVED_SUGGEST_DECOR)).thenReturn(true);

        mSearchFragment.onActivityCreated(savedInstanceState);

        verify(suggestDecor).onRestoreSavedState(savedInstanceState);
    }
}

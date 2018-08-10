package com.simplexorg.searchfragment.decorator;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.decorator.SearchSuggestionDecorator.OnSuggestionClickListener;
import com.simplexorg.searchfragment.search.BaseSearchView;
import com.simplexorg.searchfragment.search.SearchSuggestionAdapter;
import com.simplexorg.searchfragment.util.Helper;
import com.simplexorg.searchfragment.view.SearchEditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchSuggestionDecoratorUnitTest {
    private SearchSuggestionDecorator mSuggestDecor;
    @Mock private BaseSearchView mSearchView;
    @Mock private SearchEditText mSearchText;
    @Mock private CardView mCardView;
    @Mock private RelativeLayout mRecyclerListParent;
    @Mock private RecyclerView mRecyclerList;
    @Mock private Helper mHelper;
    @Mock private LayoutInflater mLayoutInflater;
    @Mock private View mView;
    @Mock private RelativeLayout mRoot;
    @Mock private SearchSuggestionAdapter mAdapter;

    @Before
    public void setup() {
        initMocks(this);
        mockSuggestViews();

        when(mHelper.generateSearchSuggestionAdapter()).thenReturn(mAdapter);
        mSuggestDecor = new SearchSuggestionDecorator(mSearchView);
        when(mView.findViewById(R.id.search_id_search)).thenReturn(mSearchText);
        when(mView.findViewById(R.id.search_id_root)).thenReturn(mRoot);
        when(mView.findViewById(R.id.search_id_card_view)).thenReturn(mCardView);

        mSuggestDecor.attach(mView);
    }

    @After
    public void cleanup() {
        Helper.setHelper(null);
    }

    private void mockSuggestViews() {
        Helper.setHelper(mHelper);
        when(mHelper.generateLayoutInflater(any(Context.class))).thenReturn(mLayoutInflater);

        View suggestionView = mock(View.class);
        when(suggestionView.findViewById(R.id.search_id_list)).thenReturn(mRecyclerList);
        when(suggestionView.findViewById(R.id.search_id_suggest_root)).thenReturn(mRecyclerListParent);

        when(mLayoutInflater.inflate(R.layout.search_suggestion_list, mRoot)).thenReturn(suggestionView);
    }

    @Test
    public void test_gainFocus() {
        mSuggestDecor.gainFocus();
        verify(mSearchText).setFocusableInTouchMode(true);
    }

    @Test
    public void test_removeFocus() {
        mSuggestDecor.removeFocus();
        verify(mSearchText).setFocusableInTouchMode(false);
    }

    @Test
    public void test_initDisplay() {
        when(mCardView.getId()).thenReturn(7);

        LinearLayoutManager linearLayoutManager = mock(LinearLayoutManager.class);
        when(mHelper.generateLinearLayoutManager(any(Context.class))).thenReturn(linearLayoutManager);

        LayoutParams params = mock(LayoutParams.class);
        when(mRecyclerListParent.getLayoutParams()).thenReturn(params);

        OnSuggestionClickListener onSuggestionClickListener = mock(OnSuggestionClickListener.class);

        mSuggestDecor.setOnSuggestionClickListener(onSuggestionClickListener);
        mSuggestDecor.initDisplay();

        verify(mAdapter).setOnSuggestionClickListener(onSuggestionClickListener);
        verify(linearLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        verify(mRecyclerList).setLayoutManager(linearLayoutManager);
        verify(mRecyclerList).setAdapter(mAdapter);

        verify(params).addRule(RelativeLayout.BELOW, mCardView.getId());
        verify(mRecyclerListParent).setLayoutParams(params);
    }

    @Test
    public void test_onSuggestionObtainedWithNotAdapter() {
        mSuggestDecor.onSuggestionObtained(new ArrayList<>());
        // Should not throw null pointer exception.
    }

    @Test
    public void test_onSaveInstanceState() {
        Bundle outState = mock(Bundle.class);
        mSuggestDecor.onSaveInstanceState(outState);

        verify(mAdapter).onSaveInstanceState(outState);
    }

    @Test
    public void test_onRestoreSavedState() {
        Bundle savedInstanceState = mock(Bundle.class);
        mSuggestDecor.onRestoreSavedState(savedInstanceState);

        verify(mAdapter).onRestoreSavedState(savedInstanceState);
    }
}

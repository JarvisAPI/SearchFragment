package com.simplexorg.searchfragment.search;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchPresenterUnitTest {
    private SearchPresenter mPresenter;
    @Mock private SearchContract.View mSearchView;

    @Before
    public void setup() {
        initMocks(this);
        SearchPresenter.attach(mSearchView);
        ArgumentCaptor<SearchPresenter> argumentCaptor = ArgumentCaptor.forClass(SearchPresenter.class);
        verify(mSearchView).setPresenter(argumentCaptor.capture());
        mPresenter = argumentCaptor.getValue();
    }

    @Test
    public void test_onTextChanged() {
        mPresenter.onTextChanged("");
        verify(mSearchView).hideRemoveIcon();
        mPresenter.onTextChanged("123");
        verify(mSearchView).showRemoveIcon();
    }

    @Test
    public void test_onOutsideClicked() {
        mPresenter.onOutsideClicked();
        verify(mSearchView).removeFocus();
        verify(mSearchView).hideKeyboard();
    }

    @Test
    public void test_onSearchClicked() {
        mPresenter.onSearchClicked();
        verify(mSearchView).gainFocus();
        verify(mSearchView).showKeyboard();
    }

    @Test
    public void test_onRemoveTextClicked() {
        mPresenter.onRemoveTextClicked();
        verify(mSearchView).removeText();
        verify(mSearchView).hideRemoveIcon();
    }

    @Test
    public void test_subscribe() {
        mPresenter.subscribe();
        verify(mSearchView).removeFocus();
        verify(mSearchView).initDisplay();
    }
}

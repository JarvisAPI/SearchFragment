package com.simplexorg.searchfragment.search;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.search.SearchContract.Presenter;
import com.simplexorg.searchfragment.search.SearchFragment.OnSearchClickListener;
import com.simplexorg.searchfragment.view.SearchEditText;
import com.simplexorg.searchfragment.view.SearchEditText.OnKeyImeChangeListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchViewUnitTest {
    private SearchView mSearchView;
    @Mock private SearchEditText mSearchText;
    @Mock private Presenter mPresenter;

    @Before
    public void setup() {
        initMocks(this);
        mSearchView = new SearchView();
        mSearchView.setPresenter(mPresenter);
    }

    private void bindSearchText() {
        View view = mock(View.class);
        when(view.findViewById(R.id.search_id_search)).thenReturn(mSearchText);
        mSearchView.attach(view);
    }

    @Test
    public void test_listeners_attached_to_view() {
        bindSearchText();
        ArgumentCaptor<OnClickListener> onClickCaptor = ArgumentCaptor.forClass(OnClickListener.class);
        verify(mSearchText).setOnClickListener(onClickCaptor.capture());
        OnClickListener onClickListener = onClickCaptor.getValue();
        onClickListener.onClick(mock(View.class));
        verify(mPresenter).onSearchClicked();

        ArgumentCaptor<TextWatcher> textWatcherCaptor = ArgumentCaptor.forClass(TextWatcher.class);
        verify(mSearchText).addTextChangedListener(textWatcherCaptor.capture());
        TextWatcher textWatcher = textWatcherCaptor.getValue();
        Editable editable = mock(Editable.class);
        when(editable.toString()).thenReturn("test");
        textWatcher.afterTextChanged(editable);
        verify(mPresenter).onTextChanged("test");

        ArgumentCaptor<OnKeyImeChangeListener> keyImeListenerCaptor = ArgumentCaptor.forClass(OnKeyImeChangeListener.class);
        verify(mSearchText).setOnKeyImeChangeListener(keyImeListenerCaptor.capture());
        OnKeyImeChangeListener keyImeChangeListener = keyImeListenerCaptor.getValue();
        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getAction()).thenReturn(KeyEvent.ACTION_UP);
        keyImeChangeListener.onKeyImeChange(KeyEvent.KEYCODE_ENTER, keyEvent);
        verify(mPresenter, times(0)).onOutsideClicked();
        keyImeChangeListener.onKeyImeChange(KeyEvent.KEYCODE_BACK, keyEvent);
        verify(mPresenter).onOutsideClicked();

        verify(mSearchText).setOnEditorActionListener(mSearchView);
    }

    @Test
    public void test_onEditorAction() {
        TextView textView = mock(TextView.class);
        Editable editable = mock(Editable.class);
        when(editable.toString()).thenReturn("test");
        when(textView.getText()).thenReturn(editable);

        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getAction()).thenReturn(KeyEvent.ACTION_UP);
        when(keyEvent.getKeyCode()).thenReturn(KeyEvent.KEYCODE_SEARCH);
        // Should not throw null pointer.
        assertEquals(true, mSearchView.onEditorAction(textView, KeyEvent.KEYCODE_SEARCH, keyEvent));

        OnSearchClickListener onSearchClickListener = mock(OnSearchClickListener.class);
        mSearchView.setOnSearchClickListener(onSearchClickListener);
        assertEquals(true, mSearchView.onEditorAction(textView, KeyEvent.KEYCODE_SEARCH, keyEvent));
        verify(onSearchClickListener).onSearchClick("test");

        when(keyEvent.getKeyCode()).thenReturn(KeyEvent.KEYCODE_0);
        assertEquals(false, mSearchView.onEditorAction(textView, KeyEvent.KEYCODE_0, keyEvent));
        verifyNoMoreInteractions(onSearchClickListener);
    }

}

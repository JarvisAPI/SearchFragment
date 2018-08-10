package com.simplexorg.searchfragment.decorator;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.NavDrawerIcon;
import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.NavDrawerIcon.NavState;
import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.OnMenuClickListener;
import com.simplexorg.searchfragment.search.BaseSearchView;
import com.simplexorg.searchfragment.search.SearchContract.Presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NavDrawerSearchDecoratorUnitTest {
    private NavDrawerSearchDecorator mNavDecor;
    @Mock private BaseSearchView mSearchView;
    @Mock private Presenter mPresenter;
    @Mock private NavDrawerIcon mNavDrawerIcon;
    @Mock private View mRootView;
    @Mock private Toolbar mToolbar;

    @Before
    public void setup() {
        initMocks(this);
        when(mRootView.findViewById(R.id.search_id_toolbar)).thenReturn(mToolbar);
        mNavDecor = new NavDrawerSearchDecorator(mSearchView);
        mNavDecor.setPresenter(mPresenter);
        mNavDecor.attach(mRootView);
        mNavDecor.setNavDrawerIcon(mNavDrawerIcon);
    }

    @Test
    public void test_gainFocus() {
        mNavDecor.gainFocus();
        verify(mNavDrawerIcon).setArrow(true);
    }

    @Test
    public void test_removeFocus() {
        mNavDecor.removeFocus();
        verify(mNavDrawerIcon).setArrow(false);
    }

    @Test
    public void test_initDisplay() {
        mNavDecor.initDisplay();
        verify(mToolbar).setNavigationIcon(mNavDrawerIcon.mDrawerArrowDrawable);
        assertEquals(NavState.HAMBURGER, mNavDrawerIcon.mNavState);
    }

    @Test
    public void test_toolbarNavigationListener() {
        mNavDecor.initDisplay();
        ArgumentCaptor<OnClickListener> argumentCaptor = ArgumentCaptor.forClass(OnClickListener.class);
        verify(mToolbar).setNavigationOnClickListener(argumentCaptor.capture());

        OnClickListener onClickListener = argumentCaptor.getValue();
        onClickListener.onClick(mToolbar); // Test if throw null pointer exception.

        OnMenuClickListener onMenuClickListener = mock(OnMenuClickListener.class);
        mNavDecor.setOnMenuClickListener(onMenuClickListener);
        onClickListener.onClick(mToolbar);
        verify(onMenuClickListener).onMenuClick();

        mNavDrawerIcon.mNavState = NavState.ARROW;
        onClickListener.onClick(mToolbar);
        verify(mPresenter).onOutsideClicked();
    }
}

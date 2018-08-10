package com.simplexorg.searchfragment.decorator;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.decorator.SearchIconDecorator.IconFactory;
import com.simplexorg.searchfragment.search.BaseSearchView;
import com.simplexorg.searchfragment.view.SearchEditText;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchIconDecoratorUnitTest {
    private SearchIconDecorator mIconDecor;
    @Mock private BaseSearchView mSearchView;
    @Mock private RelativeLayout mSearchBarLayout;
    @Mock private SearchEditText mSearchText;
    @Mock private View mRootView;
    @Mock private IconFactory mIconFactory;
    @Mock private Context mContext;

    @Before
    public void setup() {
        initMocks(this);
        when(mRootView.findViewById(R.id.search_id_search_bar_layout)).thenReturn(mSearchBarLayout);
        when(mRootView.findViewById(R.id.search_id_search)).thenReturn(mSearchText);
        mIconDecor = new SearchIconDecorator(mSearchView);
        mIconDecor.attach(mRootView);
        when(mContext.getResources()).thenReturn(mock(Resources.class));
        mIconDecor.mContext = mContext;
        mIconDecor.mIconFactory = mIconFactory;
    }

    @Test
    public void test_initDisplayWithNoIcon() {
        mIconDecor.initDisplay();
        // Should not throw null pointer exception.
    }

    @Test
    public void test_initDisplayWithOneIcon() {
        int[] resourceIds = {0xFFFF};
        mIconDecor.addIcons(resourceIds);
        OnClickListener onClickListener = mock(OnClickListener.class);
        mIconDecor.attachOnClickListener(0, onClickListener);

        ImageView icon = mock(ImageView.class);
        when(icon.getId()).thenReturn(10);
        LayoutParams iconParams = mock(LayoutParams.class);
        when(icon.getLayoutParams()).thenReturn(iconParams);
        when(mIconFactory.create(resourceIds[0], onClickListener, mSearchBarLayout)).thenReturn(icon);

        LayoutParams searchTextParams = mock(LayoutParams.class);
        when(mSearchText.getLayoutParams()).thenReturn(searchTextParams);

        mIconDecor.initDisplay();
        verify(mIconFactory).create(anyInt(), any(OnClickListener.class), any(ViewGroup.class));
        assertEquals(icon, mIconDecor.mIcons[0]);
        verify(iconParams).addRule(RelativeLayout.ALIGN_PARENT_END);
        verify(iconParams).setMarginEnd(anyInt());
        verify(icon).setLayoutParams(iconParams);

        verify(searchTextParams).addRule(RelativeLayout.START_OF, icon.getId());
        verify(mSearchText).setLayoutParams(searchTextParams);
    }

    @Test
    public void test_initDisplayWithTwoIcons() {
        int[] resourceIds = {0xFFFF, 0xFFFE};
        mIconDecor.addIcons(resourceIds);
        OnClickListener[] onClickListeners = {mock(OnClickListener.class), mock(OnClickListener.class)};
        mIconDecor.attachOnClickListener(0, onClickListeners[0]);
        mIconDecor.attachOnClickListener(1, onClickListeners[1]);

        ImageView leftIcon = mock(ImageView.class);
        when(leftIcon.getId()).thenReturn(100);
        ImageView rightIcon = mock(ImageView.class);
        when(rightIcon.getId()).thenReturn(200);

        LayoutParams leftIconParams = mock(LayoutParams.class);
        LayoutParams rightIconParams = mock(LayoutParams.class);
        when(leftIcon.getLayoutParams()).thenReturn(leftIconParams);
        when(rightIcon.getLayoutParams()).thenReturn(rightIconParams);
        when(mIconFactory.create(resourceIds[0], onClickListeners[0], mSearchBarLayout)).thenReturn(leftIcon);
        when(mIconFactory.create(resourceIds[1], onClickListeners[1], mSearchBarLayout)).thenReturn(rightIcon);

        LayoutParams searchTextParams = mock(LayoutParams.class);
        when(mSearchText.getLayoutParams()).thenReturn(searchTextParams);

        mIconDecor.initDisplay();
        verify(mIconFactory, times(2)).create(anyInt(), any(OnClickListener.class), any(ViewGroup.class));
        assertEquals(rightIcon, mIconDecor.mIcons[1]);
        verify(rightIconParams).addRule(RelativeLayout.ALIGN_PARENT_END);
        verify(rightIconParams).setMarginEnd(anyInt());
        verify(rightIcon).setLayoutParams(rightIconParams);

        assertEquals(leftIcon, mIconDecor.mIcons[0]);
        verify(leftIconParams).addRule(RelativeLayout.START_OF, rightIcon.getId());
        verify(leftIcon).setLayoutParams(leftIconParams);

        verify(searchTextParams).addRule(RelativeLayout.START_OF, leftIcon.getId());
        verify(mSearchText).setLayoutParams(searchTextParams);
    }
}

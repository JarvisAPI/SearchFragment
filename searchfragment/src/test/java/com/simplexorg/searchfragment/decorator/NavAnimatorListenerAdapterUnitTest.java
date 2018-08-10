package com.simplexorg.searchfragment.decorator;

import android.animation.Animator;

import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.NavDrawerIcon;
import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.NavDrawerIcon.NavAnimatorListenerAdapter;
import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.NavDrawerIcon.NavState;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class NavAnimatorListenerAdapterUnitTest {
    private NavDrawerIcon mNavDrawerIcon;
    private NavAnimatorListenerAdapter mNavAnimatorListenerAdapter;

    @Before
    public void setup() {
        mNavDrawerIcon = new NavDrawerIcon();
        mNavAnimatorListenerAdapter = mNavDrawerIcon.new NavAnimatorListenerAdapter();
    }

    @Test
    public void test_onAnimationEnd() {
        mNavAnimatorListenerAdapter.setEndArrow(true);
        mNavAnimatorListenerAdapter.onAnimationEnd(mock(Animator.class));
        assertEquals(NavState.ARROW, mNavDrawerIcon.mNavState);

        mNavAnimatorListenerAdapter.setEndArrow(false);
        mNavAnimatorListenerAdapter.onAnimationEnd(mock(Animator.class));
        assertEquals(NavState.HAMBURGER, mNavDrawerIcon.mNavState);
    }
}

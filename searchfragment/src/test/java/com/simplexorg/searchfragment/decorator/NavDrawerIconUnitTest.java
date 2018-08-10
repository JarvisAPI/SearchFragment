package com.simplexorg.searchfragment.decorator;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.animation.DecelerateInterpolator;

import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.NavDrawerIcon;
import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.NavDrawerIcon.NavAnimatorListenerAdapter;
import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.NavDrawerIcon.NavState;
import com.simplexorg.searchfragment.decorator.NavDrawerSearchDecorator.ValueAnimatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NavDrawerIconUnitTest {
    private NavDrawerIcon mNavDrawerIcon;
    @Mock private DrawerArrowDrawable mDrawerArrowDrawable;
    @Mock private NavAnimatorListenerAdapter mAnimAdapter;
    @Mock private ValueAnimatorFactory mValueAnimFactory;
    @Mock private ValueAnimator mValueAnim;

    @Before
    public void setup() {
        initMocks(this);
        when(mValueAnimFactory.create(anyFloat(), anyFloat())).thenReturn(mValueAnim);
        mNavDrawerIcon = new NavDrawerIcon();
        mNavDrawerIcon.mDrawerArrowDrawable = mDrawerArrowDrawable;
        mNavDrawerIcon.mAnimAdapter = mAnimAdapter;
        mNavDrawerIcon.mValueAnimFactory = mValueAnimFactory;
    }

    private void verifyAnimSequence(boolean endArrow) {
        reset(mValueAnim);
        mNavDrawerIcon.setArrow(endArrow);
        verify(mValueAnim).addUpdateListener(any(AnimatorUpdateListener.class));
        mAnimAdapter.setEndArrow(endArrow);
        verify(mValueAnim).addListener(mAnimAdapter);
        verify(mValueAnim).setInterpolator(any(DecelerateInterpolator.class));
        verify(mValueAnim).setDuration(400);
        verify(mValueAnim).start();
    }

    private void verifyNoAnimSequence(boolean endArrow) {
        mNavDrawerIcon.setArrow(endArrow);
        verifyZeroInteractions(mDrawerArrowDrawable);
        verifyZeroInteractions(mAnimAdapter);
    }

    @Test
    public void test_setArrowTrue() {
        mNavDrawerIcon.mNavState = NavState.ARROW;
        verifyNoAnimSequence(true);

        mNavDrawerIcon.mNavState = NavState.HAMBURGER_TO_ARROW;
        verifyNoAnimSequence(true);

        mNavDrawerIcon.mNavState = NavState.HAMBURGER;
        verifyAnimSequence(true);

        mNavDrawerIcon.mNavState = NavState.ARROW_TO_HAMBURGER;
        verifyAnimSequence(true);
    }

    @Test
    public void test_setArrowFalse() {
        mNavDrawerIcon.mNavState = NavState.HAMBURGER;
        verifyNoAnimSequence(false);

        mNavDrawerIcon.mNavState = NavState.ARROW_TO_HAMBURGER;
        verifyNoAnimSequence(false);

        mNavDrawerIcon.mNavState = NavState.ARROW;
        verifyAnimSequence(false);

        mNavDrawerIcon.mNavState = NavState.HAMBURGER_TO_ARROW;
        verifyAnimSequence(false);
    }

    @Test
    public void test_updateListener() {
        mNavDrawerIcon.mNavState = NavState.ARROW;
        mNavDrawerIcon.setArrow(false);

        ArgumentCaptor<AnimatorUpdateListener> argumentCaptor = ArgumentCaptor.forClass(AnimatorUpdateListener.class);
        verify(mValueAnim).addUpdateListener(argumentCaptor.capture());
        AnimatorUpdateListener updateListener = argumentCaptor.getValue();

        float val = 10;
        when(mValueAnim.getAnimatedValue()).thenReturn(val);
        updateListener.onAnimationUpdate(mValueAnim);
        verify(mDrawerArrowDrawable).setProgress(val);
    }
}

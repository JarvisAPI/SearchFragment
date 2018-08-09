package com.simplexorg.searchfragment.decorator;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.search.BaseSearchView;
import com.simplexorg.searchfragment.search.SearchContract.Presenter;

public class NavDrawerSearchDecorator extends SearchDecorator {
    private static final String TAG = NavDrawerSearchDecorator.class.getSimpleName();

    public interface OnMenuClickListener {
        void onMenuClick();
    }

    private OnMenuClickListener mOnMenuClickListener;

    private DrawerArrowDrawable mNavDrawable;

    private enum NavState {
        ARROW, ARROW_TO_HAMBURGER, HAMBURGER, HAMBURGER_TO_ARROW
    }

    private NavState mNavState;
    private DecelerateInterpolator mInterpolator;
    private NavAnimatorListenerAdapter mAnimAdapter;
    private Toolbar mToolbar;
    private Presenter mPresenter;

    public NavDrawerSearchDecorator(BaseSearchView searchBar) {
        super(searchBar);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
        mSearchBar.setPresenter(presenter);
    }

    @Override
    public void gainFocus() {
        setArrow(true);
        mSearchBar.gainFocus();
    }

    @Override
    public void removeFocus() {
        setArrow(false);
        mSearchBar.removeFocus();
    }

    @Override
    public void initDisplay() {
        Log.d(TAG, "initDisplay");
        mToolbar.setNavigationIcon(mNavDrawable);
        mNavState = NavState.HAMBURGER;
        setupToolbarNavigationListener();
        mSearchBar.initDisplay();
    }

    private void setupToolbarNavigationListener() {
        mToolbar.setNavigationOnClickListener((View view) -> {
            if (mNavState == NavState.ARROW) {
                mPresenter.onOutsideClicked();
            } else if (mNavState == NavState.HAMBURGER) {
                if (mOnMenuClickListener != null) {
                    mOnMenuClickListener.onMenuClick();
                }
            }
        });
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }

    @Override
    public void attach(View view) {
        mToolbar = view.findViewById(R.id.search_id_toolbar);
        mSearchBar.attach(view);
    }

    @Override
    public void attach(Context context) {
        mNavDrawable = new DrawerArrowDrawable(context);
        mSearchBar.attach(context);
    }

    /**
     * Start off as arrow then change to hamburger or vise versa.
     *
     * @param endArrow true if on animation end should be arrow,
     *                false if on animation end should be hamburger.
     */
    private void setArrow(boolean endArrow) {
        if (endArrow) {
            if (mNavState == NavState.ARROW || mNavState == NavState.HAMBURGER_TO_ARROW) {
                return;
            }
        } else {
            if (mNavState == NavState.HAMBURGER || mNavState == NavState.ARROW_TO_HAMBURGER) {
                return;
            }
        }
        ValueAnimator anim = endArrow ? ValueAnimator.ofFloat(0, 1) : ValueAnimator.ofFloat(1, 0);
        anim.addUpdateListener((ValueAnimator valueAnimator) -> {
            float slideOffset = (Float) valueAnimator.getAnimatedValue();
            mNavDrawable.setProgress(slideOffset);
        });
        if (endArrow) {
            mNavState = NavState.HAMBURGER_TO_ARROW;
        } else {
            mNavState = NavState.ARROW_TO_HAMBURGER;
        }
        if (mAnimAdapter == null) {
            mAnimAdapter = new NavAnimatorListenerAdapter();
        }
        if (mInterpolator == null) {
            mInterpolator = new DecelerateInterpolator();
        }
        mAnimAdapter.setEndArrow(endArrow);
        anim.addListener(mAnimAdapter);
        anim.setInterpolator(mInterpolator);
        anim.setDuration(400);
        anim.start();
    }

    private class NavAnimatorListenerAdapter extends AnimatorListenerAdapter {
        private boolean mmEndArrow;

        void setEndArrow(boolean endArrow) {
            mmEndArrow = endArrow;
        }

        @Override
        public void onAnimationEnd(android.animation.Animator animation) {
            super.onAnimationEnd(animation);
            if (mmEndArrow) {
                mNavState = NavState.ARROW;
            } else {
                mNavState = NavState.HAMBURGER;
            }
        }
    }
}

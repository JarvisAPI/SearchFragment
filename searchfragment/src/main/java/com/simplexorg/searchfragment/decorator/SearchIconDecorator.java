package com.simplexorg.searchfragment.decorator;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.search.BaseSearchView;
import com.simplexorg.searchfragment.search.SearchContract.Presenter;
import com.simplexorg.searchfragment.util.Helper;
import com.simplexorg.searchfragment.view.SearchEditText;

public class SearchIconDecorator extends SearchDecorator {
    private static final String TAG = SearchIconDecorator.class.getSimpleName();
    private int[] mResourceIds;
    private OnClickListener[] mIconOnClickListeners;
    private RelativeLayout mSearchBarLayout;
    private SearchEditText mSearchText;
    @VisibleForTesting
    IconFactory mIconFactory;
    @VisibleForTesting
    Context mContext;
    @VisibleForTesting
    ImageView[] mIcons;

    public SearchIconDecorator(BaseSearchView searchView) {
        super(searchView);
        mIconFactory = new IconFactory();
    }

    public void addIcons(int[] resourceIds) {
        mIconOnClickListeners = new OnClickListener[resourceIds.length];
        mResourceIds = resourceIds;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mSearchBar.setPresenter(presenter);
    }

    @Override
    public void attach(View view) {
        mSearchBarLayout = view.findViewById(R.id.search_id_search_bar_layout);
        mSearchText = view.findViewById(R.id.search_id_search);
        mSearchBar.attach(view);
    }

    @Override
    public void attach(Context context) {
        mContext = context;
        mIconFactory.mLayoutInflater = LayoutInflater.from(context);
        mSearchBar.attach(context);
    }

    @Override
    public void initDisplay() {
        if (mResourceIds != null) {
            mIcons = new ImageView[mResourceIds.length];

            int index = mResourceIds.length - 1;
            ImageView lastIcon = mIconFactory.create(mResourceIds[index],
                    mIconOnClickListeners[index], mSearchBarLayout);
            mIcons[mResourceIds.length - 1] = lastIcon;
            LayoutParams params = (LayoutParams) lastIcon.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            int pixel = (int) mContext
                    .getResources()
                    .getDimension(R.dimen.search_icon_end_margin);
            params.setMarginEnd(pixel);
            lastIcon.setLayoutParams(params);

            for (int i = mResourceIds.length - 2; i >= 0; i--) {
                ImageView currentIcon = mIconFactory.create(mResourceIds[i],
                        mIconOnClickListeners[i], mSearchBarLayout);
                mIcons[i] = currentIcon;
                params = (LayoutParams) currentIcon.getLayoutParams();
                params.addRule(RelativeLayout.START_OF, lastIcon.getId());
                currentIcon.setLayoutParams(params);
                lastIcon = currentIcon;
            }

            params = (LayoutParams) mSearchText.getLayoutParams();
            params.addRule(RelativeLayout.START_OF, lastIcon.getId());
            mSearchText.setLayoutParams(params);
        }
        mSearchBar.initDisplay();
    }

    /**
     * Attach an on click listener to an icon button.
     * @param iconIndex the index that icon is in.
     * @param listener the listener to attach.
     */
    public void attachOnClickListener(int iconIndex, OnClickListener listener) {
        if (mIcons != null &&
                iconIndex >= 0 &&
                iconIndex < mIcons.length) {
            mIcons[iconIndex].setOnClickListener(listener);
        }
        if (mIconOnClickListeners != null) {
            mIconOnClickListeners[iconIndex] = listener;
        }
    }

    static class IconFactory {
        LayoutInflater mLayoutInflater;

        ImageView create(int resourceId, OnClickListener onClickListener, ViewGroup parent) {
            ImageView imageView = mLayoutInflater
                    .inflate(R.layout.search_icon_image_view, parent)
                    .findViewById(R.id.search_id_image);
            imageView.setImageResource(resourceId);
            imageView.setId(Helper.getInstance().generateViewId());
            imageView.setOnClickListener(onClickListener);
            return imageView;
        }
    }
}

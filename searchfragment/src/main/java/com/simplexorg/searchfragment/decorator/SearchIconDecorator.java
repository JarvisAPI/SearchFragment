package com.simplexorg.searchfragment.decorator;

import android.content.Context;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.search.BaseSearchView;
import com.simplexorg.searchfragment.search.SearchContract.Presenter;
import com.simplexorg.searchfragment.view.SearchEditText;

public class SearchIconDecorator extends SearchDecorator {
    private static final String TAG = SearchIconDecorator.class.getSimpleName();
    private int[] mResourceIds;
    private ImageView[] mIcons;
    private OnClickListener[] mIconOnClickListeners;
    private RelativeLayout mSearchBarLayout;
    private SearchEditText mSearchText;
    private Context mContext;

    public SearchIconDecorator(BaseSearchView searchView) {
        super(searchView);
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
        mSearchBar.attach(context);
    }

    @Override
    public void initDisplay() {
        if (mResourceIds != null) {
            mIcons = new ImageView[mResourceIds.length];

            ImageView lastIcon = inflateIcon(mResourceIds.length - 1);
            mIcons[mResourceIds.length - 1] = lastIcon;
            LayoutParams params = (LayoutParams) lastIcon.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            int pixel = (int) mContext.getResources().getDimension(R.dimen.search_icon_end_margin);
            MarginLayoutParamsCompat.setMarginEnd(params, pixel);
            lastIcon.setLayoutParams(params);

            for (int i = mResourceIds.length - 2; i >= 0; i--) {
                ImageView currentIcon = inflateIcon(i);
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

    private ImageView inflateIcon(int index) {
        ImageView imageView = LayoutInflater.from(mContext)
                .inflate(R.layout.search_icon_image_view, mSearchBarLayout)
                .findViewById(R.id.search_id_image);
        imageView.setImageResource(mResourceIds[index]);
        imageView.setId(View.generateViewId());
        imageView.setOnClickListener(mIconOnClickListeners[index]);
        return imageView;
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
}

package com.simplexorg.searchfragment.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.search.SearchContract.Presenter;
import com.simplexorg.searchfragment.view.SearchEditText;

public class SearchView extends BaseSearchView {
    private interface OnViewAttachListener {
        void onAttach(View view);
    }
    private static final String TAG = SearchView.class.getSimpleName();
    private OnViewAttachListener mOnViewAttachListener;
    private SearchEditText mSearchText;
    private Presenter mPresenter;
    private CharSequence mSearchHint;
    private Context mContext;

    public void attach(Context context) {
        mContext = context;
    }

    public void attach(View view) {
        bindViews(view);
        setupSearchTextListeners();
        setupSearchRemoveIconListener();
        if (mOnViewAttachListener != null) {
            mOnViewAttachListener.onAttach(view);
            mOnViewAttachListener = null;
        }
    }

    public void setSearchHint(CharSequence searchHint) {
        mSearchHint = searchHint;
    }

    public void start() {
        mPresenter.subscribe();
    }

    public void clearFocus() {
        mPresenter.onOutsideClicked();
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        if (mSearchText != null) {
            mSearchText.addTextChangedListener(watcher);
        } else {
            mOnViewAttachListener = (View view) -> mSearchText.addTextChangedListener(watcher);
        }
    }

    private void bindViews(View view) {
        mSearchText = view.findViewById(R.id.search_id_search);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showRemoveIcon() {
        mSearchText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.search_delete_circle, 0);
    }

    @Override
    public void hideRemoveIcon() {
        mSearchText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
    }

    @Override
    public void gainFocus() {
        mSearchText.setCursorVisible(true);
    }

    @Override
    public void removeFocus() {
        mSearchText.setCursorVisible(false);
    }

    @Override
    public void hideKeyboard() {
        if (mContext != null) {
            InputMethodManager in = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(mSearchText.getWindowToken(),
                        0);
            }
        }
    }

    @Override
    public void showKeyboard() {
        if (mContext != null) {
            if (mSearchText.requestFocus()) {
                InputMethodManager in = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (in != null) {
                    in.showSoftInput(mSearchText, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }
    }

    @Override
    public void removeText() {
        mSearchText.setText("");
    }

    @Override
    public void initDisplay() {
        mSearchText.setHint(mSearchHint);
        Log.d(TAG, "initDisplay: focus: " + mSearchText.isFocused());
    }


    private void setupSearchRemoveIconListener() {
        mSearchText.setOnTouchListener((View view, MotionEvent motionEvent) -> {
                final int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Drawable removeIcon = mSearchText.getCompoundDrawables()[DRAWABLE_RIGHT];
                    if (removeIcon != null && motionEvent.getRawX() >= (mSearchText.getRight() - removeIcon
                            .getBounds().width())) {
                        mPresenter.onRemoveTextClicked();
                    }
                    return view.performClick();
                }
                return false;
        });
    }

    private void setupSearchTextListeners() {
        mSearchText.setOnClickListener((View view) -> mPresenter.onSearchClicked());
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPresenter.onTextChanged(mSearchText.getText().toString());
            }
        });
        mSearchText.setOnKeyImeChangeListener(((int keyCode, KeyEvent event) -> {
            if (event != null &&
                    keyCode == KeyEvent.KEYCODE_BACK &&
                    event.getAction() == KeyEvent.ACTION_UP) {
                mPresenter.onOutsideClicked();
            }
            return false;
        }));
        mSearchText.setOnEditorActionListener((TextView textView, int keyCode, KeyEvent keyEvent) -> {
                if (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_UP &&
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                    mPresenter.search(mSearchText.getText().toString());
                    return true;
                }
                return false;
            });
    }
}

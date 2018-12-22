package com.simplexorg.searchfragment.search;

import com.simplexorg.searchfragment.BasePresenter;
import com.simplexorg.searchfragment.BaseView;

public interface SearchContract {
    interface View extends BaseView<Presenter> {
        void showRemoveIcon();

        void hideRemoveIcon();

        void gainFocus();

        void removeFocus();

        void hideKeyboard();

        void showKeyboard();

        void removeText();

        void initDisplay();

    }

    interface Presenter extends BasePresenter {
        void onTextChanged(String text);

        void onOutsideClicked();

        void onSearchClicked();

        void onRemoveTextClicked();
    }
}

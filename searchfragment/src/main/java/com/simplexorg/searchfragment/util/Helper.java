package com.simplexorg.searchfragment.util;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.simplexorg.searchfragment.search.SearchSuggestionAdapter;

/**
 * Helps with testing mainly.
 */
public class Helper {
    private static Helper mHelper;

    private Helper() {

    }

    public int generateViewId() {
        return View.generateViewId();
    }

    public LayoutInflater generateLayoutInflater(Context context) {
        return LayoutInflater.from(context);
    }

    public LinearLayoutManager generateLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    public SearchSuggestionAdapter generateSearchSuggestionAdapter() {
        return new SearchSuggestionAdapter();
    }

    public static Helper getInstance() {
        if (mHelper == null) {
            mHelper = new Helper();
        }
        return mHelper;
    }

    @VisibleForTesting
    public static void setHelper(Helper helper) {
        mHelper = helper;
    }
}

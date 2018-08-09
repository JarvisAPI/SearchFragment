package com.simplexorg.searchfragmenttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.simplexorg.searchfragment.search.SearchData;
import com.simplexorg.searchfragment.search.SearchFragment;
import com.simplexorg.searchfragment.search.SearchSuggestionSupplier.OnSuggestionObtainedListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        searchFragment.addIcons(new int[]{R.drawable.ic_shop_black_24dp, R.drawable.baseline_my_location_black_24});

        searchFragment.setOnMenuClickListener(() -> Log.d(TAG, "Menu Clicked!"));
        searchFragment.attachOnClickListener(0, (View view) -> Log.d(TAG, "Clicked ShopBlack"));
        searchFragment.attachOnClickListener(1, (View view) -> Log.d(TAG, "Clicked MyLocationIcon"));

        setupSearchSuggestion(searchFragment);
        View root = findViewById(R.id.root);
        root.setOnClickListener((View view) -> searchFragment.clearFocus());
    }

    private void setupSearchSuggestion(SearchFragment searchFragment) {
        searchFragment.setSearchSuggestionSupplier((SearchData searchData, OnSuggestionObtainedListener listener) -> {
            String query = (String) searchData.get("query");
            List<String> suggestions = new ArrayList<>();
            if (query.length() >= 1) {
                suggestions.add("suggestion 0");

            }
            if (query.length() >= 2) {
                suggestions.add("suggestion 1");
            }
            if (query.length() >= 3) {
                suggestions.add("suggestion 2");
            }
            if (query.length() == 0) {
                suggestions.clear();
            }
            listener.onSuggestionObtained(suggestions);
        });
        searchFragment.addTextChangedListener(new TextWatcher() {
            SearchData mSearchData = new SearchData();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchData.clear();
                mSearchData.put("query", editable.toString());
                searchFragment.updateSuggestion(mSearchData);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Activity resume!");
    }

}

package com.simplexorg.searchfragment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.simplexorg.searchfragment.util.SearchFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SuggestionUnitTest {
    private Suggestion mSuggestion;
    @Mock
    private SearchFactory mFactory;

    @Before
    public void setup() {
        initMocks(this);
        mSuggestion = new Suggestion("suggestion", mock(Parcelable.class), "className");
    }

    @After
    public void cleanup() {
        SearchFactory.setFactory(null);
    }

    @Test
    public void test_writeToParcel() {
        Parcel parcel = mock(Parcel.class);
        mSuggestion.writeToParcel(parcel, 0);

        verify(parcel).writeString(mSuggestion.suggestion);
        verify(parcel).writeString(mSuggestion.className);
        verify(parcel).writeParcelable(mSuggestion.data, 0);
    }

    @Test
    public void test_create_from_parcel() {
        Parcel parcel = mock(Parcel.class);
        when(parcel.readString()).thenReturn(mSuggestion.className);

        ClassLoader classLoader = mock(ClassLoader.class);
        when(mFactory.getClassLoader(mSuggestion.className)).thenReturn(classLoader);
        SearchFactory.setFactory(mFactory);

        Suggestion.CREATOR.createFromParcel(parcel);

        verify(parcel, times(2)).readString();
        verify(parcel).readParcelable(classLoader);
    }
}

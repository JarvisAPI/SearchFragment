package com.simplexorg.searchfragment.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;

import com.simplexorg.searchfragment.util.SearchFactory;

public class Suggestion implements Parcelable {
    public final String suggestion;
    public final Parcelable data;
    @VisibleForTesting
    final String className;

    public static final Creator<Suggestion> CREATOR = new Creator<Suggestion>() {
        @Override
        public Suggestion createFromParcel(Parcel parcel) {
            return new Suggestion(parcel);
        }

        @Override
        public Suggestion[] newArray(int i) {
            return new Suggestion[i];
        }
    };

    public Suggestion(String suggestion, Parcelable data, String dataClassName) {
        this.suggestion = suggestion;
        this.data = data;
        this.className = dataClassName;
    }

    private Suggestion(Parcel parcel) {
        suggestion = parcel.readString();
        className = parcel.readString();
        ClassLoader classLoader = SearchFactory.getInstance().getClassLoader(className);
        if (classLoader == null) {
            data = null;
        } else {
            data = parcel.readParcelable(classLoader);
        }
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(suggestion);
        parcel.writeString(className);
        parcel.writeParcelable(data, flags);
    }
}

package com.simplexorg.searchfragment.decorator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.simplexorg.searchfragment.R;
import com.simplexorg.searchfragment.decorator.SearchIconDecorator.IconFactory;
import com.simplexorg.searchfragment.util.Helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class IconFactoryUnitTest {
    private IconFactory mIconFactory;
    @Mock private LayoutInflater mLayoutInflater;
    @Mock private ImageView mImageView;
    @Mock private Helper mHelper;

    @Before
    public void setup() {
        initMocks(this);
        mIconFactory = new IconFactory();
        View root = mock(View.class);
        when(root.findViewById(R.id.search_id_image)).thenReturn(mImageView);
        when(mLayoutInflater.inflate(anyInt(), any(ViewGroup.class))).thenReturn(root);
        mIconFactory.mLayoutInflater = mLayoutInflater;
        Helper.setHelper(mHelper);
    }

    @After
    public void cleanup() {
        Helper.setHelper(null);
    }

    @Test
    public void test_create() {
        int resourceId = 0xFF;
        OnClickListener onClickListener = mock(OnClickListener.class);
        ViewGroup parent = mock(ViewGroup.class);
        int id = 0xFFE;
        when(mHelper.generateViewId()).thenReturn(id);
        ImageView createdImageView = mIconFactory.create(resourceId, onClickListener, parent);

        verify(mImageView).setImageResource(resourceId);
        verify(mImageView).setId(id);
        verify(mImageView).setOnClickListener(onClickListener);
        assertEquals(mImageView, createdImageView);
    }
}

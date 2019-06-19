package com.joe.preview.ui.fragment;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import com.joe.preview.constants.PreviewConstants;

public class BaseFragment extends Fragment implements PreviewConstants {

    protected Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

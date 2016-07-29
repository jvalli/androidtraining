package com.gap.androidtraining.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BaseFragment extends Fragment {

    @UiThread
    protected final void replace(@NonNull BaseFragment fragment) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.replace(fragment);
        }
    }

    @UiThread
    protected void replace(@IdRes int id, @NonNull BaseFragment fragment) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.replace(id, fragment);
        }
    }

    @UiThread
    protected final void add(@NonNull BaseFragment fragment) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.add(fragment);
        }
    }

    @UiThread
    protected void popBackStack() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.popBackStack();
        }
    }

    @UiThread
    protected void popBackStackAll() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @UiThread
    public void showProgress(boolean show) {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).showProgress(show);
        }
    }

    @UiThread
    protected void hideKeyboard(@NonNull View view) {
        InputMethodManager in = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

package com.hasawi.sears.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseFragment extends Fragment {

    protected ViewDataBinding viewDataBinding;
    private BaseActivity activity;
    private InputMethodManager inputMethodManager;

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null)
            try {
                final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    if (activity.getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    }
                }
            } catch (Exception e) {
            }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity)
            activity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false);
        View ViewRootBinding = viewDataBinding.getRoot();
        return ViewRootBinding;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
    }

    @Override
    public void onDestroyView() {
        //dispose();
        super.onDestroyView();
    }

    protected abstract int getLayoutResId();

    protected abstract void setup();

    public BaseActivity getBaseActivity() {
        return activity;
    }

    protected View findViewById(int id) {
        return getView().findViewById(id);
    }

    public void hideKeyboard() {
        if (inputMethodManager == null)
            inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

//    public void replaceFragment(int frame_layout, Fragment newFragment, Bundle args, boolean toBeAddedInStack, boolean clearAllHistory) {
//        replaceFragment(frame_layout, newFragment, args, toBeAddedInStack, clearAllHistory, R.anim.slide_in_up, R.anim.slide_out_up);
//    }

    public void replaceFragment(int frame_layout, Fragment newFragment, Bundle args, boolean toBeAddedInStack, boolean clearAllHistory, int enter, int exit) {
        hideSoftKeyboard(newFragment.getActivity());
        FragmentManager manager = getChildFragmentManager();
//        manager.popBackStack();
        if (clearAllHistory) {
            if (manager.getBackStackEntryCount() != 0) {
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        if (args != null)
            newFragment.setArguments(args);
        FragmentTransaction transaction = manager.beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
//        if (newFragment instanceof SearchPickupFragment || newFragment instanceof SearchDropFragment)
        transaction.setCustomAnimations(enter, exit, enter, exit);
        transaction.replace(frame_layout, newFragment);
        if (toBeAddedInStack)
            transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }


}
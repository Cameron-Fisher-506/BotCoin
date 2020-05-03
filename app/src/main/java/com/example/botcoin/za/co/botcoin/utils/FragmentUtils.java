package com.example.botcoin.za.co.botcoin.utils;


import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.botcoin.R;

public class FragmentUtils {
    public static void startFragment(FragmentManager fragmentManager, Fragment fragment, int containerId, ActionBar actionBar, String title, boolean isRequireReplace, boolean isRequireBackStack, boolean isRequireAnimation, String tag)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if(isRequireAnimation)
        {
            fragmentTransaction.setCustomAnimations(R.anim.push_in_from_left, R.anim.push_out_to_right, R.anim.push_in_from_right, R.anim.push_out_to_left);
            //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if(isRequireReplace)
        {
            fragmentTransaction.replace(containerId, fragment);
        }else
        {
            fragmentTransaction.add(containerId, fragment);
        }

        if(isRequireBackStack)
        {
            fragmentTransaction.addToBackStack(tag);
        }

        if(actionBar != null)
        {
            actionBar.setTitle(title);

        }

        fragmentTransaction.commit();
    }

}

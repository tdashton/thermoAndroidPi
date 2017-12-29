package com.ashtonandassociates.thermopi.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

/**
 * Created by theKernel on 14.01.2016.
 */
public class FragmentVisibilitySaver {

	public void restoreVisibilityState(FragmentManager manager, Fragment fragment, Bundle savedInstanceState) {
		if(savedInstanceState!= null && savedInstanceState.getBoolean("isHidden") == true) {
			manager.beginTransaction().hide(fragment).commit();
		}
	}
}

package com.ashtonandassociates.thermopi.ui;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.api.ApiInterface;
import com.ashtonandassociates.thermopi.api.ApiListenerInterface;
import com.ashtonandassociates.thermopi.api.ApiListenerService;
import com.ashtonandassociates.thermopi.api.annotation.ApiListener;
import com.ashtonandassociates.thermopi.api.response.ControlReadResponse;
import com.ashtonandassociates.thermopi.util.Constants;
import com.ashtonandassociates.thermopi.util.FragmentVisibilitySaver;

/**
 * Created by theKernel on 20.02.2018.
 */

public class DebugFragment extends Fragment implements ApiListenerInterface {

	public static final String TAG = DebugFragment.class.getSimpleName();

	private final FragmentVisibilitySaver visibilitySaver = new FragmentVisibilitySaver();

	protected View mControlDebugOutputGroup;
	protected TextView mControlDebugOutput;
	protected SharedPreferences sharedPrefs;
	protected boolean mInitialized = false;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPrefs = getActivity().getSharedPreferences(Constants.CONST_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_debug, null);
		visibilitySaver.restoreVisibilityState(getFragmentManager(), this, savedInstanceState);

		mControlDebugOutput = view.findViewById(R.id.debug_textview_debug);
		mControlDebugOutputGroup = view.findViewById(R.id.debug_layout_group);

		mInitialized = true;

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		ApiListenerService.getInstance().registerListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		ApiListenerService.getInstance().unregisterListener(this);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if(hidden == false) {
			((ApiInterface)getActivity()).refreshControlValues();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isHidden", isHidden());
	}

	@ApiListener(ControlReadResponse.class)
	@SuppressWarnings("unused")
	public void onApiControlReadResponse(ControlReadResponse controlReadResponse) {
		Log.d(TAG, controlReadResponse.toString());
		if(mInitialized == false) {
			return;
		}
		mControlDebugOutput.setText("source: " + controlReadResponse.source + "\n");
		for(ControlReadResponse.Result result : controlReadResponse.result) {
			mControlDebugOutput.append(result.type.toString() + ": " + result.param.toString() + "\n");
		}
	}
}

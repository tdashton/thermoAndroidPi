package com.ashtonandassociates.thermopi.ui.list;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;
import com.ashtonandassociates.thermopi.ui.ControlFragment;
import com.ashtonandassociates.thermopi.ui.list.element.ControlRecentItem;

import java.util.List;

/**
 * Created by theKernel on 30.12.2017.
 */

public class RecentLogAdapter extends ArrayAdapter<ControlRecentItem> {

	protected int resource;

	public RecentLogAdapter(Context context, @LayoutRes int resource) {
		super(context, resource);
		this.resource = resource;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(this.resource, viewGroup, false);
		TextView text = rowView.findViewById(android.R.id.text1);
		switch (this.getItem(i).type) {
			case ControlFragment.COMMAND_TEMP:
				text.setText(String.format(getContext().getString(R.string.control_list_cmd_temp), Float.valueOf(this.getItem(i).value) / 1000));
				break;

			case ControlFragment.COMMAND_TIME:
				text.setText(String.format(getContext().getString(R.string.control_list_cmd_time), Integer.valueOf(this.getItem(i).value) / 60));

				break;
		}

		return rowView;
	}
}


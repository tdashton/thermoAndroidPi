package com.ashtonandassociates.thermopi.ui.list;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ashtonandassociates.thermopi.R;
import com.ashtonandassociates.thermopi.api.shared.ApiTemperature;
import com.ashtonandassociates.thermopi.ui.ControlFragment;
import com.ashtonandassociates.thermopi.ui.list.element.ControlRecentItem;
import com.ashtonandassociates.thermopi.util.NumberUtil;

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
				ApiTemperature temp = new ApiTemperature(Double.valueOf(this.getItem(i).value), ApiTemperature.CONST_API_SCALE);
				text.setText(String.format(getContext().getString(R.string.control_list_cmd_temp), NumberUtil.formatTemperature(temp.getTemperatureDouble(ApiTemperature.CONST_DEFAULT_SCALE))));
				break;

			case ControlFragment.COMMAND_TIME:
				text.setText(String.format(getContext().getString(R.string.control_list_cmd_time), Integer.toString(Integer.valueOf(this.getItem(i).value) / 60)));

				break;
		}

		return rowView;
	}
}


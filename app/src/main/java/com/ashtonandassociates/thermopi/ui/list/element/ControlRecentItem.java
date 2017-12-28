package com.ashtonandassociates.thermopi.ui.list.element;

import android.content.Context;

import com.ashtonandassociates.thermopi.api.response.ControlLogsResponse;
import com.ashtonandassociates.thermopi.ui.ControlFragment;
import com.ashtonandassociates.thermopi.R;

/**
 * Created by theKernel on 26.12.2017.
 */

public class ControlRecentItem {

	protected String type;

	protected int value;

	protected Context context;

	public ControlRecentItem(Context ctx, String type, int value) {
		this.context = ctx;
		this.type = type;
		this.value = value;
	}

	public ControlRecentItem(Context ctx, ControlLogsResponse.Result result) {
		this.context = ctx;
		this.type = result.type;
		this.value = result.param;
	}

	public String toString() {
		String ret;
		switch (this.type) {
			case ControlFragment.COMMAND_TEMP:
				ret = String.format(this.context.getString(R.string.control_list_cmd_temp), Integer.valueOf(this.value).toString());

				break;

			default:
			case ControlFragment.COMMAND_TIME:
				ret = String.format(this.context.getString(R.string.control_list_cmd_time), Integer.valueOf(this.value).toString());

				break;
		}


		return ret;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}

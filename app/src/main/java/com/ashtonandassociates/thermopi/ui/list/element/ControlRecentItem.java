package com.ashtonandassociates.thermopi.ui.list.element;

import android.content.Context;

import com.ashtonandassociates.thermopi.api.response.ControlLogsResponse;
import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;
import com.ashtonandassociates.thermopi.ui.ControlFragment;
import com.ashtonandassociates.thermopi.R;
import com.google.common.base.Function;

/**
 * Created by theKernel on 26.12.2017.
 */

public class ControlRecentItem {

	public String type;

	public String value;

	public ControlRecentItem(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public ControlRecentItem(RecentLog result) {
		this.type = result.type;
		this.value = result.param;
	}

	public String toString() {
		String ret = Integer.valueOf(this.value).toString() + ":" + this.type;
		switch (this.type) {
			case ControlFragment.COMMAND_TEMP:
//				ret = String.format(this.context.getString(R.string.control_list_cmd_temp), Integer.valueOf(this.value).toString());

				break;

			default:
			case ControlFragment.COMMAND_TIME:
//				ret = String.format(this.context.getString(R.string.control_list_cmd_time), Integer.valueOf(this.value).toString());

				break;
		}

		return ret;
	}

	public static Function<RecentLog, ControlRecentItem> transformFromRecentLog() {
		return new Function<RecentLog, ControlRecentItem>() {
			@Override
			public ControlRecentItem apply(RecentLog input) {
				ControlRecentItem obj = new ControlRecentItem(input);

				return obj;
			}
		};
	}

	public String getType() {
		return this.type;
	}

	public String getValue() {
		return this.value;
	}
}

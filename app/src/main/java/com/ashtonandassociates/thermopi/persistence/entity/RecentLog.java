package com.ashtonandassociates.thermopi.persistence.entity;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.ashtonandassociates.thermopi.api.response.ControlLogsResponse;
import com.ashtonandassociates.thermopi.ui.list.element.ControlRecentItem;
import com.google.common.base.Function;

/**
 * Created by theKernel on 28.12.2017.
 */

@Entity(
	tableName = "recent_log",
	primaryKeys = {"type", "param"}
)
public class RecentLog
{
	@NonNull
	public String type;

	@NonNull
	public String param;

	public Integer count;

	public Boolean hide = false;

	public RecentLog() {}

	public RecentLog(ControlRecentItem input) {
		this.type = input.type;
		this.param = input.value;
	}

	public static Function<ControlRecentItem, RecentLog> transformFromControlRecentLog() {
		return new Function<ControlRecentItem, RecentLog>() {
			@Override
			public RecentLog apply(ControlRecentItem input) {
				RecentLog obj = new RecentLog(input);

				return obj;
			}
		};
	}


	/**
	 * zis ist zee anonymous function which converts the retrofit results into DAOs which can then
	 * be inserted into the database.
	 */
	public static Function<ControlLogsResponse.Result, RecentLog> transform() {
		return new Function<ControlLogsResponse.Result, RecentLog>() {
			@Override
			public RecentLog apply(ControlLogsResponse.Result input) {
				RecentLog obj = new RecentLog();
				obj.type = input.type;
				obj.param = input.param;
				obj.count = input.count;
				obj.hide = false;

				return obj;
			}
		};
	}
}

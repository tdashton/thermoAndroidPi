package com.ashtonandassociates.thermopi.persistence.entity;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.ashtonandassociates.thermopi.api.response.ControlLogsResponse;
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

	public static Function<ControlLogsResponse.Result, RecentLog> transform() {
		return new Function<ControlLogsResponse.Result, RecentLog>() {
			@Override
			public RecentLog apply(ControlLogsResponse.Result input) {
				RecentLog obj = new RecentLog();
				obj.type = input.type;
				obj.param = input.param;

				return obj;
			}
		};
	}
}

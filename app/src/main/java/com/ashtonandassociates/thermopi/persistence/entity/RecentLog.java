package com.ashtonandassociates.thermopi.persistence.entity;

import android.arch.core.util.Function;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.ashtonandassociates.thermopi.api.response.ControlLogsResponse;

/**
 * Created by theKernel on 28.12.2017.
 */

@Entity(tableName = "recent_setting")
public class RecentSetting
{
	@PrimaryKey
	public int id;

	public String type;

	public String param;

	public static Function<ControlLogsResponse.Result, RecentSetting> transform() {
		return new Function<ControlLogsResponse.Result, RecentSetting>() {
			@Override
			public RecentSetting apply(ControlLogsResponse.Result input) {
				RecentSetting obj = new RecentSetting();
				obj.type = input.type;
				obj.param = input.param;

				return obj;
			}
		};
	}
}

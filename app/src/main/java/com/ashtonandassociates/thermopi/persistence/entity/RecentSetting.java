package com.ashtonandassociates.thermopi.persistence.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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
}

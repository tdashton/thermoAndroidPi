package com.ashtonandassociates.thermopi.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ashtonandassociates.thermopi.persistence.entity.RecentSetting;

import java.util.List;

@Dao
public interface RecentSettingDao
{
	@Query("SELECT * FROM recent_setting")
	List<RecentSetting> getAll();

	@Insert
	void insertAll(RecentSetting... recentSettings);
}

package com.ashtonandassociates.thermopi.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ashtonandassociates.thermopi.persistence.dao.RecentSettingDao;
import com.ashtonandassociates.thermopi.persistence.entity.RecentSetting;

@Database(entities = {RecentSetting.class}, version = 1, exportSchema = false)
public abstract class PiDatabase extends RoomDatabase {
	public abstract RecentSettingDao recentSettingDao();
}

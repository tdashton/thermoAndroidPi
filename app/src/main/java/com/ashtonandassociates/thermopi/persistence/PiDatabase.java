package com.ashtonandassociates.thermopi.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ashtonandassociates.thermopi.persistence.dao.RecentLogDao;
import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;

@Database(entities = {RecentLog.class}, version = 1, exportSchema = false)
public abstract class PiDatabase extends RoomDatabase {
	public abstract RecentLogDao recentLogDao();
}

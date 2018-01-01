package com.ashtonandassociates.thermopi.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;

import java.util.List;

@Dao
public interface RecentLogDao
{
	@Query("SELECT * FROM recent_log WHERE type = :type order by count desc")
	List<RecentLog> getAllOfType(String type);

	@Query("SELECT * FROM recent_log")
	List<RecentLog> getAll();

	@Query("SELECT count(*) FROM recent_log")
	Double getCount();

	@Query("DELETE FROM recent_log")
	void removeAll();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(RecentLog... recentLogs);
}

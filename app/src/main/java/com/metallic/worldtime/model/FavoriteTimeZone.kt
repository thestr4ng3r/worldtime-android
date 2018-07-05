package com.metallic.worldtime.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

private const val tableName = "favorite_time_zone"

@Entity(tableName = tableName)
class FavoriteTimeZone
{
	@PrimaryKey
	@ColumnInfo(name = "time_zone_id")
	var timeZoneID: String? = null

	@ColumnInfo(name = "sort")
	var sort: Int = 0
}

@Dao
interface FavoriteTimeZoneDao
{
	@Query("SELECT * FROM $tableName ORDER BY sort, time_zone_id")
	fun getAll(): LiveData<List<FavoriteTimeZone>>

	@Insert
	fun insert(o: FavoriteTimeZone)

	@Insert
	fun insert(o: List<FavoriteTimeZone>)

	@Delete
	fun delete(o: FavoriteTimeZone)

	@Query("DELETE FROM $tableName WHERE time_zone_id = :arg0")
	fun deleteByTimeZoneID(id: String)
}
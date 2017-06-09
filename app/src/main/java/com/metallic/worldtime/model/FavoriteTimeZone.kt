package com.metallic.worldtime.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

private const val tableName = "favorite_time_zone"

@Entity(tableName = tableName)
class FavoriteTimeZone
{
	@PrimaryKey
	lateinit var timeZoneID: String

	@ColumnInfo(name = "sort")
	var sort: Int = 0
}

@Dao
interface FavoriteTimeZoneDao
{
	@Query("SELECT * FROM $tableName")
	fun getAll(): LiveData<List<FavoriteTimeZone>>

	@Insert
	fun insert(o: FavoriteTimeZone)

	@Delete
	fun delete(o: FavoriteTimeZone)

	@Query("DELETE FROM $tableName WHERE timeZoneId = :arg0")
	fun deleteByTimeZoneID(id: String)
}
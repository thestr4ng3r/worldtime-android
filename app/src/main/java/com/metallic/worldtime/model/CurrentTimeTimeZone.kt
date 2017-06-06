package com.metallic.worldtime.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

const val tableName = "current_time_time_zone"

@Entity(tableName = tableName)
class CurrentTimeTimeZone
{
	@PrimaryKey
	lateinit var timeZoneID: String

	@ColumnInfo(name = "sort")
	var sort: Int = 0
}

@Dao
interface CurrentTimeTimeZoneDao
{
	@Query("SELECT * FROM $tableName")
	fun getAll(): LiveData<List<CurrentTimeTimeZone>>

	@Insert
	fun insert(o: CurrentTimeTimeZone)

	@Delete
	fun delete(o: CurrentTimeTimeZone)

	@Query("DELETE FROM $tableName WHERE timeZoneId = :arg0")
	fun deleteByTimeZoneID(id: String)
}
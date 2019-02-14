package com.metallic.worldtime.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

private const val tableName = "event"

@Entity(tableName = tableName)
data class Event
(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	var id: Long = 0,

	@ColumnInfo(name = "time_zone")
	var timeZoneId: String,

	@ColumnInfo(name = "date")
	var date: Long,

	@ColumnInfo(name = "title")
	var title: String
)

@Dao
interface EventDao
{
	@Query("SELECT * FROM $tableName ORDER BY date DESC")
	fun getAll(): LiveData<List<Event>>

	@Query("SELECT * FROM $tableName WHERE id = :id")
	fun getById(id: Long): Event

	@Insert
	fun insert(o: Event)

	@Update
	fun update(o: Event)

	@Delete
	fun delete(o: Event)
}
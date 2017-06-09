package com.metallic.worldtime.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

private const val tableName = "event"

@Entity(tableName = tableName)
class Event
{
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	var id: Long = 0

	@ColumnInfo(name = "time_zone")
	lateinit var timeZoneId: String

	@ColumnInfo(name = "date")
	var date: Long = 0

	@ColumnInfo(name = "title")
	lateinit var title: String
}

@Dao
interface EventDao
{
	@Query("SELECT * FROM $tableName")
	fun getAll(): LiveData<List<Event>>

	@Query("SELECT * FROM $tableName WHERE id = :arg0")
	fun getById(id: Long): Event

	@Insert
	fun insert(o: Event)

	@Update
	fun update(o: Event)

	@Delete
	fun delete(o: Event)
}
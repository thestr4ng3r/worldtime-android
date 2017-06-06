package com.metallic.worldtime

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.metallic.worldtime.model.CurrentTimeTimeZone
import com.metallic.worldtime.model.CurrentTimeTimeZoneDao

@Database(entities = arrayOf(CurrentTimeTimeZone::class), version = 2)
abstract class AppDatabase: RoomDatabase()
{
	abstract fun currentTimeTimeZoneDao(): CurrentTimeTimeZoneDao

	companion object
	{
		private var instance: AppDatabase? = null
		fun getInstance(context: Context): AppDatabase
		{
			var instance = instance
			if(instance == null)
			{
				instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app-database")
						.allowMainThreadQueries()
						.build()
				this.instance = instance
			}
			return instance!!
		}
	}
}
package com.metallic.worldtime

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.metallic.worldtime.model.FavoriteTimeZone
import com.metallic.worldtime.model.FavoriteTimeZoneDao
import com.metallic.worldtime.model.Event
import com.metallic.worldtime.model.EventDao

@Database(entities = arrayOf(FavoriteTimeZone::class, Event::class), version = 7)
abstract class AppDatabase: RoomDatabase()
{
	abstract fun favoriteTimeZoneDao(): FavoriteTimeZoneDao
	abstract fun eventDao(): EventDao

	companion object
	{
		private var instance: AppDatabase? = null
		fun getInstance(context: Context): AppDatabase
		{
			var instance = instance
			if(instance == null)
			{
				instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app-database")
						.addMigrations(MIGRATION_6_7)
						.allowMainThreadQueries()
						.build()
				this.instance = instance
			}
			return instance!!
		}


		private val MIGRATION_6_7 = object : Migration(6, 7) { override fun migrate(database: SupportSQLiteDatabase?) { } }
	}
}
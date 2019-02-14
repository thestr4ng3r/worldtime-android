package com.metallic.worldtime

import android.app.Application
import android.content.Context
import com.metallic.worldtime.model.FavoriteTimeZone
import net.danlew.android.joda.JodaTimeAndroid

class App: Application()
{
	override fun onCreate()
	{
		super.onCreate()
		JodaTimeAndroid.init(this)

		val prefs = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
		val firstLaunch = prefs.getBoolean(PREFERENCE_FIRST_LAUNCH, true)

		if(firstLaunch)
		{
			val dao = AppDatabase.getInstance(this).favoriteTimeZoneDao()

			val favorites = arrayOf("America/Los_Angeles", "America/New_York", "Asia/Tokyo", "Australia/Sydney", "Europe/London")
					.map {
						FavoriteTimeZone(it)
					}

			dao.insert(favorites)

			prefs.edit().putBoolean(PREFERENCE_FIRST_LAUNCH, false).apply()
		}
	}
}
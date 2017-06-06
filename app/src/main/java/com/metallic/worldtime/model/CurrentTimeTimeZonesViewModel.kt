package com.metallic.worldtime.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.metallic.worldtime.AppDatabase


class CurrentTimeTimeZonesViewModel(application: Application) : AndroidViewModel(application)
{
	var timeZones: LiveData<List<CurrentTimeTimeZone>>? = null

	init
	{
		timeZones = AppDatabase.getInstance(application).currentTimeTimeZoneDao().getAll()
	}
}
package com.metallic.worldtime.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.metallic.worldtime.AppDatabase


class EventsViewModel(application: Application) : AndroidViewModel(application)
{
	var events: LiveData<List<Event>>? = null

	init
	{
		events = AppDatabase.getInstance(application).eventDao().getAll()
	}
}
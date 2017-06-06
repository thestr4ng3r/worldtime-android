package com.metallic.worldtime

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import com.metallic.worldtime.adapter.SelectTimeZonesAdapter
import com.metallic.worldtime.model.CurrentTimeTimeZone
import com.metallic.worldtime.model.CurrentTimeTimeZonesViewModel
import com.metallic.worldtime.utils.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.activity_select_time_zones.*
import java.util.*
import kotlin.collections.HashSet

class SelectTimeZonesActivity: LifecycleAppCompatActivity()
{
	private lateinit var viewModel: CurrentTimeTimeZonesViewModel

	private val adapter = SelectTimeZonesAdapter()

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_select_time_zones)

		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		recycler_view.adapter = adapter

		viewModel = ViewModelProviders.of(this).get(CurrentTimeTimeZonesViewModel::class.java)
		var observer: Observer<List<CurrentTimeTimeZone>>? = null
		observer = Observer<List<CurrentTimeTimeZone>> { items ->
			val selectedIds = HashSet(items?.map { a -> a.timeZoneID } )
			adapter.selectedTimeZones = selectedIds
			adapter.notifyDataSetChanged()
			viewModel.timeZones?.removeObserver(observer)
		}
		viewModel.timeZones?.observe(this, observer)

		val timeZones = TimeZone.getAvailableIDs()
				.map { id -> TimeZone.getTimeZone(id) }
				.sortedBy { timeZone -> timeZone.displayName }
		adapter.allTimeZones = timeZones
	}
}
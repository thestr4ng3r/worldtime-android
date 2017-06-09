package com.metallic.worldtime

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.MenuItem
import com.metallic.worldtime.adapter.SelectTimeZonesAdapter
import com.metallic.worldtime.model.FavoriteTimeZone
import com.metallic.worldtime.model.CurrentTimeTimeZonesViewModel
import com.metallic.worldtime.utils.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.activity_select_time_zones.*
import org.joda.time.DateTimeZone

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
		var observer: Observer<List<FavoriteTimeZone>>? = null
		observer = Observer<List<FavoriteTimeZone>> { items ->
			val selectedIds = items?.map { a -> a.timeZoneID }?.toSet()
			adapter.selectedTimeZones = selectedIds
			adapter.notifyDataSetChanged()
			viewModel.timeZones?.removeObserver(observer)
		}
		viewModel.timeZones?.observe(this, observer)

		val timeZones = DateTimeZone.getAvailableIDs()
				.sorted()
				.map { id -> DateTimeZone.forID(id) }
		adapter.allTimeZones = timeZones
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when(item.itemId)
		{
			android.R.id.home ->
			{
				onBackPressed()
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}
}
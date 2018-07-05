package com.metallic.worldtime

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.metallic.worldtime.adapter.SelectTimeZonesAdapter
import com.metallic.worldtime.model.FavoriteTimeZone
import com.metallic.worldtime.model.FavoriteTimeZonesViewModel
import com.metallic.worldtime.utils.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.activity_select_time_zones.*
import org.joda.time.DateTimeZone

class SelectTimeZonesActivity: LifecycleAppCompatActivity(), SelectTimeZonesAdapter.OnTimeZoneSelectedListener, SearchView.OnQueryTextListener
{
	companion object
	{
		val MODE_EXTRA = "select_time_zones_mode"

		val ZONE_ID_RESULT_EXTRA = "zone_id_result"
	}

	enum class Mode
	{
		EDIT_FAVORIES,
		SELECT_SINGLE
	}

	private lateinit var mode: Mode
	private lateinit var viewModel: FavoriteTimeZonesViewModel
	private lateinit var allTimeZones: List<String>
	private lateinit var adapter: SelectTimeZonesAdapter

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_select_time_zones)

		mode = (intent.getSerializableExtra(MODE_EXTRA) as? Mode) ?: Mode.EDIT_FAVORIES

		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		adapter = SelectTimeZonesAdapter(mode)
		adapter.onTimeZoneSelectedListener = this
		recycler_view.adapter = adapter

		viewModel = ViewModelProviders.of(this).get(FavoriteTimeZonesViewModel::class.java)
		var observer: Observer<List<FavoriteTimeZone>>? = null
		observer = Observer<List<FavoriteTimeZone>> { items ->
			val selectedIds = items?.map { a -> a.timeZoneID!! }?.toSet()
			adapter.selectedTimeZones = selectedIds
			adapter.notifyDataSetChanged()
			viewModel.timeZones?.removeObserver(observer)
		}
		viewModel.timeZones?.observe(this, observer)

		allTimeZones = DateTimeZone.getAvailableIDs()
				.sorted()
		adapter.allTimeZones = allTimeZones
	}


	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		menuInflater.inflate(R.menu.activity_select_time_zones, menu)
		val searchView = menu.findItem(R.id.action_search).actionView as SearchView
		searchView.setOnQueryTextListener(this)
		return true
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

	override fun onTimeZoneSelected(timeZoneId: String)
	{
		if(mode == Mode.SELECT_SINGLE)
		{
			val intent = intent
			intent.putExtra(ZONE_ID_RESULT_EXTRA, timeZoneId)
			setResult(Activity.RESULT_OK, intent)
			finish()
		}
	}


	override fun onQueryTextChange(query: String): Boolean
	{
		val searchQuery = query.trim().toLowerCase()

		if(searchQuery.isEmpty())
		{
			adapter.allTimeZones = allTimeZones
		}
		else
		{
			adapter.allTimeZones = allTimeZones.filter {
				dateTimeZone -> dateTimeZone.toLowerCase().contains(searchQuery)
			}
		}

		adapter.notifyDataSetChanged()

		return true
	}

	override fun onQueryTextSubmit(query: String) = false
}
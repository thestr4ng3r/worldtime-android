package com.metallic.worldtime

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.metallic.worldtime.model.Event
import com.metallic.worldtime.utils.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_event.*
import java.time.ZoneId
import java.time.ZonedDateTime

class EditEventActivity: LifecycleAppCompatActivity()
{
	val EVENT_ID_EXTRA = "event_id"

	private lateinit var event: Event
	private var newEvent = false

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_edit_event)

		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_24dp)
		supportActionBar?.setDisplayShowTitleEnabled(false)

		if(intent.hasExtra(EVENT_ID_EXTRA))
		{
			event = AppDatabase.getInstance(this).eventDao().getById(intent.getLongExtra(EVENT_ID_EXTRA, 0))
			newEvent = false
		}
		else
		{
			event = Event()
			newEvent = true
		}

		time_zone_row.setOnClickListener {

		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		menuInflater.inflate(R.menu.activity_edit_event_toolbar, menu)
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

			R.id.action_save ->
			{
				onBackPressed()
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}


}
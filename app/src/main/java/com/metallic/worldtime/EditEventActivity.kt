package com.metallic.worldtime

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.metallic.worldtime.model.Event
import com.metallic.worldtime.utils.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_event.*
import org.joda.time.Instant
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

			event.date = Instant.now().millis
			event.timeZoneId = "UTC"
			event.title = ""
		}

		event_title_edit_text.setText(event.title)
		event_title_edit_text.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
			override fun afterTextChanged(s: Editable)
			{
				event.title = s.toString()
			}
		})

		time_zone_text_view.text = event.timeZoneId

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
				save()
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}

	private fun save()
	{
		val dao = AppDatabase.getInstance(this).eventDao()
		if(newEvent)
			dao.insert(event)
		else
			dao.update(event)
		finish()
	}
}
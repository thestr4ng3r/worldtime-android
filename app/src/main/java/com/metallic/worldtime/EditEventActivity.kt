package com.metallic.worldtime

import android.app.*
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import com.metallic.worldtime.model.Event
import com.metallic.worldtime.utils.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_event.*
import org.joda.time.DateTimeZone
import org.joda.time.Instant
import org.joda.time.LocalDateTime

class EditEventActivity: LifecycleAppCompatActivity()
{
	companion object
	{
		val EVENT_ID_EXTRA = "event_id"

		private val PICK_TIMEZONE_REQUEST = 1
	}

	private lateinit var event: Event
	private var newEvent = false
	lateinit var currentSelectedDate: LocalDateTime

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
			event.timeZoneId = DateTimeZone.getDefault().id
			event.title = ""
		}

		currentSelectedDate = LocalDateTime(event.date, DateTimeZone.forID(event.timeZoneId))

		event_title_edit_text.setText(event.title)
		event_title_edit_text.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
			override fun afterTextChanged(s: Editable)
			{
				event.title = s.toString()
			}
		})

		updateTimeZoneId()
		updateTime()

		time_zone_row.setOnClickListener {
			val intent = Intent(this, SelectTimeZonesActivity::class.java)
			intent.putExtra(SelectTimeZonesActivity.MODE_EXTRA, SelectTimeZonesActivity.Mode.SELECT_SINGLE)
			startActivityForResult(intent, PICK_TIMEZONE_REQUEST)
		}

		date_text_view.setOnClickListener {
			DatePickerDialogFragment().show(fragmentManager, "DatePicker")
		}

		time_text_view.setOnClickListener {
			TimePickerDialogFragment().show(fragmentManager, "TimePicker")
		}
	}

	class DatePickerDialogFragment: DialogFragment(), DatePickerDialog.OnDateSetListener
	{
		override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
		{
			val activity = activity as EditEventActivity
			return DatePickerDialog(activity, this,
					activity.currentSelectedDate.year,
					activity.currentSelectedDate.monthOfYear-1,
					activity.currentSelectedDate.dayOfMonth)
		}

		override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int)
		{
			val activity = activity as EditEventActivity
			activity.currentSelectedDate = activity.currentSelectedDate
					.withYear(year)
					.withMonthOfYear(month+1)
					.withDayOfMonth(day)
			activity.updateTime()
		}
	}

	class TimePickerDialogFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener
	{
		override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
		{
			val activity = activity as EditEventActivity
			return TimePickerDialog(activity, this,
					activity.currentSelectedDate.hourOfDay,
					activity.currentSelectedDate.minuteOfHour,
					DateFormat.is24HourFormat(activity))
		}

		override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int)
		{
			val activity = activity as EditEventActivity
			activity.currentSelectedDate = activity.currentSelectedDate
					.withHourOfDay(hourOfDay)
					.withMinuteOfHour(minute)
			activity.updateTime()
		}
	}


	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
	{
		when(requestCode)
		{
			PICK_TIMEZONE_REQUEST ->
			{
				if(resultCode == Activity.RESULT_OK && data != null)
				{
					val zoneId = data.getStringExtra(SelectTimeZonesActivity.ZONE_ID_RESULT_EXTRA)
					setTimeZoneId(zoneId)
				}
			}
		}
	}

	private fun setTimeZoneId(zoneId: String)
	{
		event.timeZoneId = zoneId
		updateTimeZoneId()
	}

	private fun updateTimeZoneId()
	{
		time_zone_text_view.text = event.timeZoneId
	}

	fun updateTime()
	{
		date_text_view.text = DATE_FORMAT_DATE.print(currentSelectedDate)
		time_text_view.text = DATE_FORMAT_TIME.print(currentSelectedDate)
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
		val timeZone = DateTimeZone.forID(event.timeZoneId)
		event.date = currentSelectedDate.toDateTime(timeZone).millis

		val dao = AppDatabase.getInstance(this).eventDao()
		if(newEvent)
			dao.insert(event)
		else
			dao.update(event)
		finish()
	}
}
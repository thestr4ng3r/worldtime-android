package com.metallic.worldtime.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.metallic.worldtime.utils.inflate
import com.metallic.worldtime.R
import com.metallic.worldtime.dateFormatTime
import com.metallic.worldtime.model.Event
import kotlinx.android.synthetic.main.item_event.view.*
import org.joda.time.DateTimeZone
import org.joda.time.Instant
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat

class EventsRecyclerViewAdapter: RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder>()
{
	var events: List<Event>? = null
		set(value)
		{
			field = value
			notifyDataSetChanged()
		}

	override fun getItemCount(): Int
	{
		return events?.size ?: 0
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = parent.inflate(R.layout.item_event)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
	{
		val event = events?.get(position) ?: return

		viewHolder.nameTextView.text = DateTimeZone.getDefault().id
		val localTime = LocalDateTime(event.date, DateTimeZone.getDefault())
		viewHolder.timeTextView.text = dateFormatTime.print(localTime)

		val timeZone = DateTimeZone.forID(event.timeZoneId)
		viewHolder.name2TextView.text = timeZone.id
		val remoteTime = LocalDateTime(event.date, timeZone)
		viewHolder.time2TextView.text = dateFormatTime.print(remoteTime)

		viewHolder.eventTitleTextView.text = event.title
	}

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	{
		val nameTextView = itemView.name_text_view!!
		val timeTextView = itemView.time_text_view!!

		val name2TextView = itemView.name2_text_view!!
		val time2TextView = itemView.time2_text_view!!

		val eventTitleTextView = itemView.event_title_text_view!!
	}
}
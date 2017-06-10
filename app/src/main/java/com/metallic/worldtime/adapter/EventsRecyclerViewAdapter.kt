package com.metallic.worldtime.adapter

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.metallic.worldtime.utils.inflate
import com.metallic.worldtime.R
import com.metallic.worldtime.DATE_FORMAT_TIME
import com.metallic.worldtime.EditEventActivity
import com.metallic.worldtime.colorsForTimeZone
import com.metallic.worldtime.model.Event
import kotlinx.android.synthetic.main.item_event.view.*
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

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
		viewHolder.timeTextView.text = DATE_FORMAT_TIME.print(localTime)

		val timeZone = DateTimeZone.forID(event.timeZoneId)
		viewHolder.name2TextView.text = timeZone.id
		val remoteTime = LocalDateTime(event.date, timeZone)
		viewHolder.time2TextView.text = DATE_FORMAT_TIME.print(remoteTime)

		viewHolder.eventTitleTextView.text = event.title

		val resources = viewHolder.headerBackgroundView.resources
		val timeZoneColor = colorsForTimeZone(resources, timeZone.id)
		viewHolder.headerBackgroundView.setBackgroundColor(timeZoneColor.color)
		viewHolder.nameTextView.setTextColor(timeZoneColor.textColor)
		viewHolder.timeTextView.setTextColor(timeZoneColor.textColor)
		viewHolder.name2TextView.setTextColor(timeZoneColor.textColor)
		viewHolder.time2TextView.setTextColor(timeZoneColor.textColor)

		viewHolder.itemLayout.setOnClickListener {
			val context = viewHolder.itemLayout.context
			val intent = Intent(context, EditEventActivity::class.java)
			intent.putExtra(EditEventActivity.EVENT_ID_EXTRA, event.id)
			context.startActivity(intent)
		}
	}

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	{
		val nameTextView = itemView.name_text_view!!
		val timeTextView = itemView.time_text_view!!

		val name2TextView = itemView.name2_text_view!!
		val time2TextView = itemView.time2_text_view!!

		val eventTitleTextView = itemView.event_title_text_view!!

		val headerBackgroundView = itemView.header_background_view!!

		val itemLayout = itemView.item_layout!!
	}
}
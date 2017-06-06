package com.metallic.worldtime.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.metallic.worldtime.utils.inflate
import com.metallic.worldtime.R
import com.metallic.worldtime.model.CurrentTimeTimeZone
import kotlinx.android.synthetic.main.item_current_time_time_zone.view.*
import java.text.SimpleDateFormat
import java.util.*

class CurrentTimeTimeZonesRecyclerViewAdapter: RecyclerView.Adapter<CurrentTimeTimeZonesRecyclerViewAdapter.ViewHolder>()
{
	var timeZones: List<CurrentTimeTimeZone>? = null
		set(value)
		{
			field = value
			notifyDataSetChanged()
		}

	override fun getItemCount(): Int
	{
		return timeZones?.size ?: 0
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = parent.inflate(R.layout.item_current_time_time_zone)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
	{
		val timeZone = timeZones?.get(position) ?: return

		viewHolder.nameTextView.text = timeZone.timeZoneID
		val format = SimpleDateFormat("HH:mm", Locale.getDefault())
		format.timeZone = TimeZone.getTimeZone(timeZone.timeZoneID)
		viewHolder.timeTextView.text = format.format(Date())
		viewHolder.infoTextView.text = viewHolder.infoTextView.context.getString(R.string.hours_delta).format("+4")
	}

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	{
		val nameTextView = itemView.name_text_view!!
		val timeTextView = itemView.time_text_view!!
		val infoTextView = itemView.info_text_view!!
	}
}
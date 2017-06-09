package com.metallic.worldtime.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.metallic.worldtime.utils.inflate
import com.metallic.worldtime.R
import com.metallic.worldtime.dateFormatTime
import com.metallic.worldtime.decimalFormatOffset
import com.metallic.worldtime.model.FavoriteTimeZone
import kotlinx.android.synthetic.main.item_current_time_time_zone.view.*
import org.joda.time.DateTimeZone
import org.joda.time.Instant
import org.joda.time.LocalDateTime

class CurrentTimeTimeZonesRecyclerViewAdapter: RecyclerView.Adapter<CurrentTimeTimeZonesRecyclerViewAdapter.ViewHolder>()
{
	var timeZones: List<FavoriteTimeZone>? = null
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
		val favoriteTimeZone = timeZones?.get(position) ?: return
		val timeZone = DateTimeZone.forID(favoriteTimeZone.timeZoneID)
		val localTimeZone = DateTimeZone.getDefault()
		val timeZoneOffset = timeZone.getOffset(Instant.now()) - localTimeZone.getOffset(Instant.now())

		viewHolder.nameTextView.text = timeZone.id
		viewHolder.timeTextView.text = dateFormatTime.print(LocalDateTime(Instant.now(), timeZone))

		val offsetHours = timeZoneOffset.toDouble() / (1000.0 * 60.0 * 60.0)
		viewHolder.infoTextView.text = viewHolder.infoTextView.context.getString(R.string.hours_delta)
				.format(decimalFormatOffset.format(offsetHours))
	}

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	{
		val nameTextView = itemView.name_text_view!!
		val timeTextView = itemView.time_text_view!!
		val infoTextView = itemView.info_text_view!!
	}
}
package com.metallic.worldtime.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.metallic.worldtime.AppDatabase
import com.metallic.worldtime.utils.inflate
import com.metallic.worldtime.R
import com.metallic.worldtime.DATE_FORMAT_TIME
import com.metallic.worldtime.DECIMAL_FORMAT_OFFSET
import com.metallic.worldtime.model.FavoriteTimeZone
import kotlinx.android.synthetic.main.item_current_time_time_zone.view.*
import org.joda.time.*

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

		val remoteDateTime = LocalDateTime(Instant.now(), timeZone)
		val remoteDate = remoteDateTime.toLocalDate()
		val localDate = LocalDate.now()

		viewHolder.nameTextView.text = timeZone.id
		viewHolder.timeTextView.text = DATE_FORMAT_TIME.print(remoteDateTime)

		val daysDelta = Period(localDate, remoteDate).days
		if(daysDelta == -1)
		{
			viewHolder.dayTextView.setText(R.string.yesterday)
			viewHolder.dayTextView.visibility = View.VISIBLE
		}
		else if(daysDelta == 1)
		{
			viewHolder.dayTextView.setText(R.string.tomorrow)
			viewHolder.dayTextView.visibility = View.VISIBLE
		}
		else
		{
			viewHolder.dayTextView.visibility = View.GONE
		}

		val offsetHours = timeZoneOffset.toDouble() / (1000.0 * 60.0 * 60.0)
		viewHolder.infoTextView.text = viewHolder.infoTextView.context.getString(R.string.hours_delta)
				.format(DECIMAL_FORMAT_OFFSET.format(offsetHours))

		viewHolder.itemLayout.setOnLongClickListener {
			val context = viewHolder.itemLayout.context
			AlertDialog.Builder(context)
					.setMessage(context.getString(R.string.message_question_delete_favorite_timezone).format(timeZone.id))
					.setNegativeButton(R.string.action_cancel, { _: DialogInterface, _: Int -> })
					.setPositiveButton(R.string.action_delete, { _: DialogInterface, _: Int ->
						val dao = AppDatabase.getInstance(context).favoriteTimeZoneDao()
						dao.delete(favoriteTimeZone)
					})
					.create()
					.show()
			true
		}
	}

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	{
		val nameTextView = itemView.name_text_view!!
		val timeTextView = itemView.time_text_view!!
		val infoTextView = itemView.info_text_view!!
		val dayTextView = itemView.day_text_view!!
		val itemLayout = itemView.item_layout!!
	}
}
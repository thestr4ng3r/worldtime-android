package com.metallic.worldtime.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.metallic.worldtime.AppDatabase
import com.metallic.worldtime.utils.inflate
import com.metallic.worldtime.R
import com.metallic.worldtime.model.FavoriteTimeZone
import kotlinx.android.synthetic.main.item_select_time_zone.view.*
import org.joda.time.DateTimeZone

class SelectTimeZonesAdapter: RecyclerView.Adapter<SelectTimeZonesAdapter.ViewHolder>()
{
	var allTimeZones: List<DateTimeZone>? = null
	var selectedTimeZones: Set<String>? = null

	override fun getItemCount(): Int
	{
		return allTimeZones?.size ?: 0
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = parent.inflate(R.layout.item_select_time_zone)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
	{
		val timeZone = allTimeZones?.get(position) ?: return

		viewHolder.checkBox.text = timeZone.id //timeZone.displayName
		viewHolder.checkBox.setOnCheckedChangeListener(null)
		viewHolder.checkBox.isChecked = selectedTimeZones?.contains(timeZone.id) ?: false
		viewHolder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
			val dao = AppDatabase.getInstance(viewHolder.checkBox.context).currentTimeTimeZoneDao()
			if(!isChecked)
			{
				dao.deleteByTimeZoneID(timeZone.id)
			}
			else
			{
				val tz = FavoriteTimeZone()
				tz.timeZoneID = timeZone.id
				tz.sort = -1
				dao.insert(tz)
			}
		}
	}

	class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
	{
		val checkBox = itemView.check_box!!
	}
}

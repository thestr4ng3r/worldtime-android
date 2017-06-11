package com.metallic.worldtime.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.metallic.worldtime.AppDatabase
import com.metallic.worldtime.utils.inflate
import com.metallic.worldtime.R
import com.metallic.worldtime.SelectTimeZonesActivity
import com.metallic.worldtime.model.FavoriteTimeZone
import kotlinx.android.synthetic.main.item_select_time_zone_check.view.*
import kotlinx.android.synthetic.main.item_select_time_zone_single.view.*
import org.joda.time.DateTimeZone

class SelectTimeZonesAdapter(val mode: SelectTimeZonesActivity.Mode): RecyclerView.Adapter<SelectTimeZonesAdapter.ViewHolder>()
{
	var allTimeZones: List<String>? = null
	var selectedTimeZones: Set<String>? = null
	var onTimeZoneSelectedListener: OnTimeZoneSelectedListener? = null

	override fun getItemCount(): Int
	{
		return allTimeZones?.size ?: 0
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val layoutId = when(mode)
		{
			SelectTimeZonesActivity.Mode.EDIT_FAVORIES -> R.layout.item_select_time_zone_check
			SelectTimeZonesActivity.Mode.SELECT_SINGLE -> R.layout.item_select_time_zone_single
		}

		val view = parent.inflate(layoutId)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
	{
		val timeZone = allTimeZones?.getOrNull(position) ?: return

		if(viewHolder.checkBox != null)
		{
			viewHolder.checkBox.text = timeZone //timeZone.displayName
			viewHolder.checkBox.setOnCheckedChangeListener(null)
			viewHolder.checkBox.isChecked = selectedTimeZones?.contains(timeZone) ?: false
			viewHolder.checkBox.setOnCheckedChangeListener { _, isChecked ->
				val dao = AppDatabase.getInstance(viewHolder.checkBox.context).favoriteTimeZoneDao()
				if(!isChecked)
				{
					dao.deleteByTimeZoneID(timeZone)
				}
				else
				{
					val tz = FavoriteTimeZone()
					tz.timeZoneID = timeZone
					tz.sort = -1
					dao.insert(tz)
				}
			}
		}

		if(viewHolder.textView != null)
		{
			viewHolder.textView.text = timeZone
		}

		if(mode == SelectTimeZonesActivity.Mode.SELECT_SINGLE)
		{
			viewHolder.frame.setOnClickListener {
				onTimeZoneSelectedListener?.onTimeZoneSelected(timeZone)
			}
		}
	}

	class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
	{
		val frame: View = itemView.findViewById(R.id.item_frame)
		val checkBox: CheckBox? = itemView.check_box
		val textView: TextView? = itemView.text_view
	}

	interface OnTimeZoneSelectedListener
	{
		fun onTimeZoneSelected(timeZoneId: String)
	}
}

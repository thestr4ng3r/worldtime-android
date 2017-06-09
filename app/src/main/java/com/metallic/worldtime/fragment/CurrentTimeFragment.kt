package com.metallic.worldtime.fragment

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.metallic.worldtime.MainActivity
import com.metallic.worldtime.R
import com.metallic.worldtime.SelectTimeZonesActivity
import com.metallic.worldtime.adapter.CurrentTimeTimeZonesRecyclerViewAdapter
import com.metallic.worldtime.dateFormatTime
import com.metallic.worldtime.model.CurrentTimeTimeZonesViewModel
import kotlinx.android.synthetic.main.fragment_current_time.*
import kotlinx.android.synthetic.main.fragment_current_time.view.*
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class CurrentTimeFragment: LifecycleFragment()
{
	private var viewModel: CurrentTimeTimeZonesViewModel? = null

	private val adapter = CurrentTimeTimeZonesRecyclerViewAdapter()
	private lateinit var drawerToggle: ActionBarDrawerToggle

	private lateinit var timeTextView: TextView

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
	{
		super.onCreateView(inflater, container, savedInstanceState)
		val view = inflater.inflate(R.layout.fragment_current_time, container, false)

		val mainActivity = activity as MainActivity

		mainActivity.setSupportActionBar(view.toolbar)
		drawerToggle = ActionBarDrawerToggle(mainActivity, mainActivity.drawerLayout, view.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
		mainActivity.drawerLayout.addDrawerListener(drawerToggle)
		drawerToggle.syncState()
		mainActivity.title = getString(R.string.section_current_time)

		view.recycler_view.adapter = adapter

		timeTextView = view.time_text_view

		view.fab.setOnClickListener { view ->
			val intent = Intent(activity, SelectTimeZonesActivity::class.java)
			startActivity(intent)
		}

		return view
	}

	override fun onDestroyView()
	{
		super.onDestroyView()
		val mainActivity = activity as MainActivity
		mainActivity.drawerLayout.removeDrawerListener(drawerToggle)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?)
	{
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProviders.of(this).get(CurrentTimeTimeZonesViewModel::class.java)
		viewModel?.timeZones?.observe(this, Observer { items -> adapter.timeZones = items; })
	}

	override fun onResume()
	{
		super.onResume()
		updateTime()
		activity.registerReceiver(timeBroadcastReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
	}

	override fun onPause()
	{
		super.onPause()
		activity.unregisterReceiver(timeBroadcastReceiver)
	}

	private fun updateTime()
	{
		val title = dateFormatTime.print(LocalDateTime.now())
		timeTextView.text = title
		adapter.notifyDataSetChanged()
	}

	val timeBroadcastReceiver = TimeBroadcastReceiver()
	inner class TimeBroadcastReceiver : BroadcastReceiver()
	{
		override fun onReceive(context: Context, intent: Intent)
		{
			view?.post { updateTime() }
		}
	}

}
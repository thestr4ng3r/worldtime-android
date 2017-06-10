package com.metallic.worldtime.fragment

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.metallic.worldtime.EditEventActivity
import com.metallic.worldtime.MainActivity
import com.metallic.worldtime.R
import com.metallic.worldtime.adapter.EventsRecyclerViewAdapter
import com.metallic.worldtime.model.EventsViewModel
import kotlinx.android.synthetic.main.fragment_current_time.view.*

class EventsFragment: LifecycleFragment()
{
	private var viewModel: EventsViewModel? = null

	private val adapter = EventsRecyclerViewAdapter()
	private lateinit var drawerToggle: ActionBarDrawerToggle

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
	{
		super.onCreateView(inflater, container, savedInstanceState)
		val view = inflater.inflate(R.layout.fragment_events, container, false)

		val mainActivity = activity as MainActivity

		mainActivity.setSupportActionBar(view.toolbar)
		drawerToggle = ActionBarDrawerToggle(mainActivity, mainActivity.drawerLayout, view.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
		mainActivity.drawerLayout.addDrawerListener(drawerToggle)
		drawerToggle.syncState()
		mainActivity.title = getString(R.string.section_events)

		view.recycler_view.adapter = adapter

		view.fab.setOnClickListener { view ->
			val intent = Intent(activity, EditEventActivity::class.java)
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
		viewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
		viewModel?.events?.observe(this, Observer { items -> adapter.events = items; })
	}
}
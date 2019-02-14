package com.metallic.worldtime.fragment

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.metallic.worldtime.MainActivity
import com.metallic.worldtime.R
import kotlinx.android.synthetic.main.fragment_about.view.*
import java.io.BufferedReader
import java.io.InputStreamReader

class AboutFragment: Fragment()
{
	private lateinit var drawerToggle: ActionBarDrawerToggle

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
	{
		super.onCreateView(inflater, container, savedInstanceState)
		val view = inflater.inflate(R.layout.fragment_about, container, false)

		val mainActivity = activity as MainActivity

		mainActivity.setSupportActionBar(view.toolbar)
		drawerToggle = ActionBarDrawerToggle(mainActivity, mainActivity.drawerLayout, view.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
		mainActivity.drawerLayout.addDrawerListener(drawerToggle)
		drawerToggle.syncState()
		mainActivity.title = getString(R.string.section_about)

		val reader = BufferedReader(InputStreamReader(activity.assets.open("about.html")))
		val html = reader.readText()
		reader.close()

		view.text_view.movementMethod = LinkMovementMethod.getInstance()

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
			view.text_view.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
		else
		{
			@Suppress("DEPRECATION")
			view.text_view.text = Html.fromHtml(html)
		}

		return view
	}

	override fun onDestroyView()
	{
		super.onDestroyView()
		val mainActivity = activity as MainActivity
		mainActivity.drawerLayout.removeDrawerListener(drawerToggle)
	}
}
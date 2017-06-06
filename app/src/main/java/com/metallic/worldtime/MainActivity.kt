package com.metallic.worldtime

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import com.metallic.worldtime.fragment.CurrentTimeFragment
import com.metallic.worldtime.utils.LifecycleAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : LifecycleAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
	enum class Section { CURRENT_TIME }

	lateinit var drawerLayout: DrawerLayout

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		drawerLayout = drawer_layout

		val navigationView = nav_view
		navigationView.setNavigationItemSelectedListener(this)

		switchToSection(Section.CURRENT_TIME)
	}

	override fun onBackPressed()
	{
		if (drawerLayout.isDrawerOpen(GravityCompat.START))
			drawerLayout.closeDrawer(GravityCompat.START)
		else
			super.onBackPressed()
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			R.id.nav_current_time -> switchToSection(Section.CURRENT_TIME)
		}

		drawerLayout.closeDrawer(GravityCompat.START)
		return true
	}

	private fun switchToSection(section: Section)
	{
		val fragment = when(section)
		{
			MainActivity.Section.CURRENT_TIME -> CurrentTimeFragment()
		}

		val transaction = supportFragmentManager.beginTransaction()
		transaction.replace(R.id.content_frame, fragment)
		transaction.commit()
	}
}

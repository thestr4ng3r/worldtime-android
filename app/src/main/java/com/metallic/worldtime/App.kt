package com.metallic.worldtime

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

class App: Application()
{
	override fun onCreate()
	{
		super.onCreate()
		JodaTimeAndroid.init(this)
	}
}
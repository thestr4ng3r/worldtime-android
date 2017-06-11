package com.metallic.worldtime

import android.content.res.Resources
import org.joda.time.format.DateTimeFormat
import java.text.DecimalFormat

val DATE_FORMAT_TIME = DateTimeFormat.shortTime()
val DATE_FORMAT_DATE = DateTimeFormat.fullDate()

val DECIMAL_FORMAT_OFFSET = DecimalFormat("+#0.#;-#0.#")

val TIME_ZONE_COLORS_COUNT = 16
data class TimeZoneColor(val color: Int, val textColor: Int)
fun colorsForTimeZone(resources: Resources, timeZoneId: String): TimeZoneColor
{
	val hash = timeZoneId.hashCode()
	val colorIndex = (if(hash < 0) -hash else hash) % TIME_ZONE_COLORS_COUNT

	val colorId = resources.getIntArray(R.array.time_zone_colors)[colorIndex]
	val textColorId = resources.getIntArray(R.array.time_zone_colors_text)[colorIndex]
	return TimeZoneColor(colorId, textColorId)
}
package com.metallic.worldtime

import org.joda.time.format.DateTimeFormat
import java.text.DecimalFormat

var dateFormatTime = DateTimeFormat.forPattern("HH:mm")

var decimalFormatOffset = DecimalFormat("+#0.#;-#0.#")